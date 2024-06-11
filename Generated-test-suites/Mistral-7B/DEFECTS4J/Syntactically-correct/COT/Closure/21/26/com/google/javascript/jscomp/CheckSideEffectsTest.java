package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.List;

/**
 * Test case for CheckSideEffects class
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    // Test for basic functionality
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeFactory.createRoot();
    checkSideEffects.process(null, root);

    // Test for expected warnings with useless code
    Node uselessCode = NodeFactory.createName("x");
    Node uselessCodeAssigment = NodeFactory.createAssign(uselessCode, NodeFactory.createName("foo"));
    root.addChildToBack(uselessCodeAssigment);
    compiler.reportCodeChange();
    List<String> warnings = compiler.getWarnings();
    assertTrue(warnings.size() > 0);
    assertTrue(warnings.get(0).startsWith("Suspicious code. x = "));

    // Test for expected warnings with missing '+'
    Node missingPlus = NodeFactory.createStringLiteral("this string is ");
    Node missingPlusContinued = NodeFactory.createStringLiteral("continued on the next line but you forgot the +");
    Node concatenation = NodeFactory.createBinaryOp(Token.OP_PLUS, missingPlus, missingPlusContinued);
    root.addChildToBack(concatenation);
    compiler.reportCodeChange();
    warnings = compiler.getWarnings();
    assertTrue(warnings.size() > 0);
    assertTrue(warnings.get(0).startsWith("Is there a missing '+' on the previous line?"));

    // Test for expected warnings with simple operators
    Node simpleOp = NodeFactory.createBinaryOp(Token.OP_EQ, NodeFactory.createName("x"), NodeFactory.createName("foo"));
    root.addChildToBack(simpleOp);
    compiler.reportCodeChange();
    warnings = compiler.getWarnings();
    assertTrue(warnings.size() > 0);
    assertTrue(warnings.get(0).startsWith("The result of the '==' operator is not being used."));

    // Test for exceptions
    Node emptyBlock = NodeFactory.createBlock();
    root.addChildToBack(emptyBlock);
    compiler.reportCodeChange();
    try {
      checkSideEffects.process(null, root);
    } catch (AssertionError e) {
      List<String> expectedWarnings = Lists.newArrayList(
          "Suspicious code. VOID statements are not allowed",
          "Suspicious code. This code lacks side-effects. Is there a bug?"
      );
      List<String> warnings = compiler.getWarnings();
      for (String warning : warnings) {
        assertTrue(expectedWarnings.contains(warning));
      }
    }

    // Test for edge cases: empty root node
    root = NodeFactory.createRoot();
    checkSideEffects.process(null, root);
    warnings = compiler.getWarnings();
    assertFalse(warnings.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = NodeFactory.createRoot();
    checkSideEffects.process(null, root);

    // Test for expected warnings with useless code
    Node uselessCode = NodeFactory.createName("x");
    Node uselessCodeAssigment = NodeFactory.createAssign(uselessCode, NodeFactory.createName("foo"));
    root.addChildToBack(uselessCodeAssigment);
    compiler.reportCodeChange();
    List<String> warnings = compiler.getWarnings();
    assertTrue(warnings.size() > 0);
    assertTrue(warnings.get(0).startsWith("Suspicious code. x = "));

    // Test for protected side-effect free nodes
    Node sideEffectFreeNode = NodeFactory.createName("protectedNode");
    Node protectCall = NodeFactory.createCall(NodeFactory.createName(CheckSideEffects.PROTECTOR_FN), NodeFactory.createList(sideEffectFreeNode));
    root.addChildToBack(protectCall);
    compiler.reportCodeChange();
    warnings = compiler.getWarnings();
    assertTrue(warnings.isEmpty());

    checkSideEffects.protectSideEffects();
    warnings = compiler.getWarnings();
    assertFalse(warnings.isEmpty());

    Node protectedNode = compiler.getRoot().getChildAt(0);
    assertTrue(protectedNode.isCall());
    assertTrue(protectedNode.getFirstChild().isName());
    assertTrue(protectedNode.getFirstChild().getString().equals(CheckSideEffects.PROTECTOR_FN));

    compiler.dispose();

    // Test for removing side-effect sync functions
    Compiler compiler2 = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR);
    compiler2.setOptions(options);
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler2);
    Node root2 = NodeFactory.createRoot();
    stripProtection.process(null, root2);

    // Test for empty root node
    root2 = NodeFactory.createRoot();
    stripProtection.process(null, root2);

    Node strippedNode = root2.getChildAt(0);
    assertTrue(strippedNode.isCall());
    assertTrue(strippedNode.getFirstChild().isName());
    assertTrue(strippedNode.getFirstChild().getString().equals(CheckSideEffects.PROTECTOR_FN));
    assertTrue(strippedNode.getLastChild().isString());
    assertTrue(strippedNode.getLastChild().getString().equals(CheckSideEffects.PROTECTOR_FN + "("));
  }
}