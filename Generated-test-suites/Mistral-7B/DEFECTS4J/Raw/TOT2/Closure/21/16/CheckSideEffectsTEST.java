import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  private static CheckSideEffects createChecker(CheckLevel level, boolean protectSideEffectFreeCode) {
    AbstractCompiler compiler = new TestCompiler();
    return new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
  }

  @org.junit.Test
  public void testEmptyFunctionCall() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("x"), null));
    input.setRootNode(node);
    checker.process(input, null);

    assertTrue(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testVoidExpression() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    node.addChildToBack(NodeFactory.createCall(null, Token.VOID, null));
    input.setRootNode(node);
    checker.process(input, null);

    assertTrue(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testStatementWithSideEffect() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("console.log"), null));
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testExpressionWithSideEffect() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node nameNode = NodeFactory.createQualifiedName("Math", "random");
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("x"), null, nameNode));
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testSimpleOperatorWithSideEffect() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    node.addChildToBack(NodeFactory.createBinaryOp(Token.ADD, NodeFactory.createName("x"), NodeFactory.createNumber(1)));
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProtectedExpression() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, true);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName(CheckSideEffects.PROTECTOR_FN), null));
    input.setRootNode(node);
    checker.process(input, null);

    assertTrue(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testError_UnexpectedCallToProtectorFunction() {
    CheckSideEffects checker = createChecker(CheckLevel.ERROR, true);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node nameNode = NodeFactory.createQualifiedName("CheckSideEffects", "PROTECTOR_FN");
    node.addChildToBack(NodeFactory.createCall(null, nameNode, null));
    input.setRootNode(node);

    assertThrows(RuntimeException.class, () -> checker.process(input, null));
  }

  @org.junit.Test
  public void testEdgeCase_ForLoop() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node forNode = NodeFactory.createFor(null, NodeFactory.createName("i"), NodeFactory.createNumber(0), NodeFactory.createNumber(1), NodeFactory.createBlock());
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("console.log"), null, NodeFactory.createName("i")));
    forNode.setParent(node);
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testEdgeCase_IfStatement() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node ifNode = NodeFactory.createIf(NodeFactory.createBinaryOp(Token.EQUALS, NodeFactory.createName("x"), NodeFactory.createNumber(1)), NodeFactory.createBlock(), null);
    node.addChildToBack(ifNode);
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProtectedCodeIsPreserved() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, true);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node nameNode = NodeFactory.createQualifiedName("Math", "random");
    Node protectedNode = NodeFactory.createCall(NodeFactory.createName(CheckSideEffects.PROTECTOR_FN), null);
    node.addChildToBack(protectedNode);
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("x"), null, nameNode));
    input.setRootNode(node);
    checker.process(input, null);

    assertTrue(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProtectedCodeIsRemoved() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);
    StripProtection stripper = new StripProtection(checker.compiler);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node nameNode = NodeFactory.createQualifiedName("Math", "random");
    Node protectedNode = NodeFactory.createCall(NodeFactory.createName(CheckSideEffects.PROTECTOR_FN), null);
    node.addChildToBack(protectedNode);
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("x"), null, nameNode));
    input.setRootNode(node);
    checker.process(input, null);
    stripper.process(null, input.getAstRoot(checker.compiler), null);

    assertTrue(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testMissingProtectedCodeIsReported() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, true);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("x"), null, NodeFactory.createQualifiedName("Math", "random")));
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProtectedCodeIsNotReportedIfNotRequested() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    node.addChildToBack(NodeFactory.createCall(null, NodeFactory.createName("x"), null, NodeFactory.createQualifiedName("Math", "random")));
    input.setRootNode(node);
    checker.process(input, null);

    assertTrue(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testEmptyIfStatement() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node ifNode = NodeFactory.createIf(NodeFactory.createBinaryOp(Token.EQUALS, NodeFactory.createName("x"), NodeFactory.createNumber(1)), NodeFactory.createBlock(), null);
    ifNode.setBody(NodeFactory.createEmptyStatement());
    node.addChildToBack(ifNode);
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testEmptyForStatement() {
    CheckSideEffects checker = createChecker(CheckLevel.WARNING, false);

    CompilerInput input = new CompilerInput();
    Node node = NodeFactory.createBlock();
    Node forNode = NodeFactory.createFor(null, NodeFactory.createName("i"), NodeFactory.createNumber(0), NodeFactory.createNumber(1), NodeFactory.createBlock());
    forNode.setBody(NodeFactory.createEmptyStatement());
    node.addChildToBack(forNode);
    input.setRootNode(node);
    checker.process(input, null);

    assertFalse(checker.problemNodes.isEmpty());
  }

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public CompilerOptions getOptions() {
      return new CompilerOptions();
    }

    @Override
    public boolean isCompilingForECMAScript5_1() {
      return false;
    }
  }
}