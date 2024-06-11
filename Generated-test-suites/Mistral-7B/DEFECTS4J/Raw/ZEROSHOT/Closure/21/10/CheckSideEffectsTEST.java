import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private NodeTraversal traverser;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    traverser = new NodeTraversal(compiler);
  }

  @Test
  public void testProcessNoSideEffects() {
    Node root = NodeOperations.createRootNode("function test() {}");
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessSideEffects() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "var s = 'this string is '" +
            "continued on the next line but you forgot the +';" +
            "x == foo();" +
            "foo();;" +
            "}");
    checkSideEffects.process(null, root);
    assertEquals(3, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessNoWarningForJSDocComment() {
    Node root = NodeOperations.createRootNode(
        "/** @type {number} */ var x = 0;");
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessProtectedCode() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "function JSCOMPILER_PRESERVE foo() {}" +
            "JSCOMPILER_PRESERVE foo();" +
            "}");
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessProtectedCodeWithErrors() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "function JSCOMPILER_PRESERVE foo() { return 1; }" +
            "JSCOMPILER_PRESERVE foo();" +
            "}");
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessNoWarningForForStatement() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "for (var i = 0; i < 10; i++) {" +
            "  i++;" +
            "}" +
            "}");
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessNoWarningForCallWithoutParentheses() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "foo();" +
            "}");
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessWarningForSimpleOp() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "var x = 1 + 1;" +
            "}");
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessWarningForStringConcat() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "var s = 'this string is " +
            "continued on the next line but you forgot the +';" +
            "}");
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcessWarningForAssignment() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "x = foo();" +
            "}");
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testHotSwapScript() {
    Node originalRoot = NodeOperations.createRootNode("function test() {}");
    Node scriptRoot = NodeOperations.createRootNode("test();");
    checkSideEffects.hotSwapScript(originalRoot, scriptRoot);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testVisitNoOperation() {
    Node n = NodeOperations.createNode(Token.KEYWORD, Token.VAR, "var");
    checkSideEffects.visit(traverser, null, n);
  }

  @Test
  public void testVisitEmptyNode() {
    Node n = NodeOperations.createNode(Token.SEMICOLON);
    checkSideEffects.visit(traverser, null, n);
  }

  @Test
  public void testVisitCommaNode() {
    Node parent = NodeOperations.createNode(Token.COMMA);
    Node n = NodeOperations.createNode(Token.EXPR_RESULT, Token.NAME, "foo");
    parent.addChildToBack(n);
    checkSideEffects.visit(traverser, null, parent);
  }

  @Test
  public void testVisitQualifiedNameNode() {
    Node n = NodeOperations.createNode(Token.NAME, "my.package.foo");
    checkSideEffects.visit(traverser, null, n);
  }

  @Test
  public void testVisitCallNodeWithParentheses() {
    Node parent = NodeOperations.createNode(Token.EXPR_RESULT, Token.CALL, null);
    Node target = NodeOperations.createNode(Token.NAME, "foo");
    Node arg = NodeOperations.createNode(Token.NUMBER, 1);
    Node callArg = NodeOperations.createNode(Token.EXPR_RESULT, Token.NAME, "arg");
    arg.addChildToBack(callArg);
    parent.addChildToBack(target);
    parent.addChildToBack(arg);
    checkSideEffects.visit(traverser, null, parent);
  }

  @Test
  public void testVisitCallNodeWithoutParentheses() {
    Node parent = NodeOperations.createNode(Token.EXPR_RESULT, Token.CALL, null);
    Node target = NodeOperations.createNode(Token.NAME, "foo");
    parent.addChildToBack(target);
    checkSideEffects.visit(traverser, null, parent);
  }

  @Test
  public void testVisitForStatement() {
    Node parent = NodeOperations.createNode(Token.EXPR_RESULT, Token.BLOCK, null);
    Node forStatement = NodeOperations.createNode(Token.FOR, null, null);
    Node initialization = NodeOperations.createNode(Token.VAR, null, "i");
    Node condition = NodeOperations.createNode(Token.BINOP, Token.EQ, NodeOperations.createNode(Token.NAME, "i"), NodeOperations.createNode(Token.NUMBER, 10));
    Node increment = NodeOperations.createNode(Token.ASSIGN, Token.PLUS, NodeOperations.createNode(Token.NAME, "i"), NodeOperations.createNode(Token.NUMBER, 1));
    forStatement.addChildren(initialization, condition, increment);
    parent.addChildToBack(forStatement);
    Node child = NodeOperations.createNode(Token.EXPR_RESULT, Token.CALL, null);
    parent.addChildToBack(child);
    checkSideEffects.visit(traverser, null, parent);
  }

  @Test
  public void testVisitSimpleOp() {
    Node parent = NodeOperations.createNode(Token.EXPR_RESULT, Token.BLOCK, null);
    Node addition = NodeOperations.createNode(Token.BINOP, Token.PLUS, NodeOperations.createNode(Token.NUMBER, 1), NodeOperations.createNode(Token.NUMBER, 1));
    parent.addChildToBack(addition);
    Node child = NodeOperations.createNode(Token.EXPR_RESULT, Token.NAME, "result");
    parent.addChildToBack(child);
    checkSideEffects.visit(traverser, null, parent);
  }

  @Test
  public void testVisitSideEffectFreeNode() {
    Node parent = NodeOperations.createNode(Token.EXPR_RESULT, Token.BLOCK, null);
    Node child = NodeOperations.createNode(Token.EXPR_RESULT, Token.NAME, "result");
    parent.addChildToBack(child);
    checkSideEffects.visit(traverser, null, parent);
  }

  @Test
  public void testVisitStatementWithSideEffects() {
    Node parent = NodeOperations.createNode(Token.EXPR_RESULT, Token.BLOCK, null);
    Node assignment = NodeOperations.createNode(Token.ASSIGN, Token.EQ, NodeOperations.createNode(Token.NAME, "x"), NodeOperations.createNode(Token.NAME, "foo"));
    Node call = NodeOperations.createNode(Token.EXPR_RESULT, Token.CALL, null);
    Node target = NodeOperations.createNode(Token.NAME, "foo");
    Node arg = NodeOperations.createNode(Token.NUMBER, 1);
    call.addChildToBack(target);
    call.addChildToBack(arg);
    parent.addChildToBack(assignment);
    parent.addChildToBack(call);
    checkSideEffects.visit(traverser, null, parent);
  }

  @Test
  public void testProtectSideEffects() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "function JSCOMPILER_PRESERVE foo() {}" +
            "JSCOMPILER_PRESERVE foo();" +
            "}");
    checkSideEffects.protectSideEffects();
    Node externs = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
    Node foobar = externs.getFirstChild();
    assertEquals(3, foobar.getChildCount());
    assertEquals("function JSCOMPILER_PRESERVE foo() {", foobar.getChild(0).getString());
    assertEquals("}", foobar.getChild(2).getString());
  }

  @Test
  public void testStripProtection() {
    Node root = NodeOperations.createRootNode(
        "function test() {" +
            "function JSCOMPILER_PRESERVE foo() { return 1; }" +
            "JSCOMPILER_PRESERVE foo();" +
            "}");
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, false);
    checkSideEffects.protectSideEffects();
    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(null, root);
    Node foo = root.getFirstChild().getLastChild();
    assertEquals(1, foo.getChildCount());
    assertEquals("return 1;", foo.getString());
  }
}