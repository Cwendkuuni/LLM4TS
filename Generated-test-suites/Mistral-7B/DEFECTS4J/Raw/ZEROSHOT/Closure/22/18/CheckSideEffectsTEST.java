package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JavaScriptCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  private static Node createCheckSideEffectsCode(AbstractCompiler compiler) {
    CompilerOptions options = new CompilerOptions();
    String code = "function foo() { return 42; }" +
                  "function bar() { return 42; }" +
                  "function baz() { return 42; }" +
                  "var a = 1;" +
                  "var b = 2;" +
                  "var unused = 3;" +
                  "a = 5;" + // side effect
                  "b = foo() + bar() + baz();" + // no side effect
                  "unused = 6;" + // side effect
                  "var x = 7;" +
                  "x = x + 1;" + // side effect
                  "var y = 8;" +
                  "y = y + x;"; // side effect
    Node ast = JavaScriptCompiler.parseTree(ParserUtil.parseCompilationUnit(code), options, null, null);
    Node root = ast.getFirstChild();
    return root;
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler(new CheckerFactory());
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevelCode(CheckLevel.WARNING, "JSC_USELESS_CODE");
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = createCheckSideEffectsCode(compiler);
    checkSideEffects.process(null, root);

    // There should be no warning for side-effect statements.
    List<Node> warnings = compiler.getDiagnostics().asList();
    assertTrue(warnings.isEmpty());

    // There should be warnings for non-side-effect statements.
    Node unusedNode = ParserUtil.parseCompilationUnit("function foo() { return 42; }" +
                                                      "var x = 7;")
        .getFirstChild()
        .getLastChild();
    AbstractCompiler tempCompiler = new Compiler(new CheckerFactory());
    CheckSideEffects tempCheckSideEffects = new CheckSideEffects(tempCompiler, CheckLevel.WARNING, true);
    tempCompiler.process(null, unusedNode);
    List<Node> tempWarnings = tempCompiler.getDiagnostics().asList();
    assertFalse(tempWarnings.isEmpty());
    tempCompiler.dispose();

    Node unusedSideEffectNode = root.getFirstChild().getNext();
    Node unusedSideEffectLastChild = unusedSideEffectNode.getLastChild();
    AbstractCompiler tempCompilerSideEffect = new Compiler(new CheckerFactory());
    CheckSideEffects tempCheckSideEffectsSideEffect = new CheckSideEffects(tempCompilerSideEffect, CheckLevel.WARNING, true);
    tempCompilerSideEffect.process(null, unusedSideEffectNode);
    List<Node> tempWarningsSideEffect = tempCompilerSideEffect.getDiagnostics().asList();
    assertFalse(tempWarningsSideEffect.isEmpty());
    tempCompilerSideEffect.dispose();

    Node unusedAssignNode = root.getFirstChild().getNext().getNext();
    Node unusedAssignLastChild = unusedAssignNode.getLastChild();
    AbstractCompiler tempCompilerAssign = new Compiler(new CheckerFactory());
    CheckSideEffects tempCheckSideEffectsAssign = new CheckSideEffects(tempCompilerAssign, CheckLevel.WARNING, true);
    tempCompilerAssign.process(null, unusedAssignNode);
    List<Node> tempWarningsAssign = tempCompilerAssign.getDiagnostics().asList();
    assertFalse(tempWarningsAssign.isEmpty());
    tempCompilerAssign.dispose();

    // Protect side-effect free nodes.
    checkSideEffects.protectSideEffects();
    List<Node> protectedNodes = checkSideEffects.problemNodes;
    assertFalse(protectedNodes.isEmpty());
    for (Node protectedNode : protectedNodes) {
      Node protectedNodeParent = protectedNode.getParent();
      Node protectedNodeLastChild = protectedNode.getLastChild();
      Node protectedNodeName = IR.name(CheckSideEffects.PROTECTOR_FN).srcref(protectedNode);
      Node protectedNodeCall = IR.call(protectedNodeName).srcref(protectedNode);
      Node protectedNodeCallParent = protectedNodeCall.getParent();
      Node protectedNodeCallLastChild = protectedNodeCall.getLastChild();
      assertFalse(protectedNodeLastChild.isName());
      assertFalse(protectedNodeLastChild.isCall());
      assertTrue(protectedNodeName.isName());
      assertTrue(protectedNodeName.getString().equals(CheckSideEffects.PROTECTOR_FN));
      assertTrue(protectedNodeCall.isCall());
      assertTrue(protectedNodeCall.getFirstChild().isName());
      assertTrue(protectedNodeCall.getLastChild().isExpressionStatement());
      assertTrue(protectedNodeCallParent.getParent().getParent().isBlock());
      assertTrue(protectedNodeCallParent.isCall());
      Node protectedNodeCallInvoker = protectedNodeCallParent.getFirstChild();
      assertFalse(protectedNodeCallInvoker.isName());
      assertFalse(protectedNodeCallInvoker.isCall());
      assertTrue(protectedNodeParent.getChildCount() == 2);
      assertTrue(protectedNodeParent.getChild(0) == protectedNode);
      assertTrue(protectedNodeParent.getChild(1) == protectedNodeCall);
      assertTrue(protectedNodeCallParent.getChildCount() == 2);
      assertTrue(protectedNodeCallParent.getChild(0) == protectedNodeParent);
      assertTrue(protectedNodeCallParent.getChild(1) == protectedNodeCallInvoker);
    }

    // Remove protected side-effect free nodes.
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    checkSideEffects.process(null, root);
    checkSideEffects.dispose();
    List<Node> strippedNodes = compiler.getDiagnostics().asList();
    assertTrue(strippedNodes.isEmpty());
    List<Node> protectedNodesAfterStripping = checkSideEffects.problemNodes;
    assertTrue(protectedNodesAfterStripping.isEmpty());
  }
}