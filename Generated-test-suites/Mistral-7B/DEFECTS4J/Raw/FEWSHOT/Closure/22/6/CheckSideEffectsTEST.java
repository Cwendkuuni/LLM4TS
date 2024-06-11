package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test cases for CheckSideEffects class.
 */
@RunWith(PowerMockRunner.class)
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = PowerMockito.mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test empty cases
    Node root = NodeFactory.createRoot();
    checkSideEffects.process(null, root);

    // Test visit method with simple cases
    Node node1 = NodeFactory.createStringLiteral("foo");
    Node node2 = NodeFactory.createName("bar");
    Node parent = NodeFactory.createBlock(Lists.newArrayList(node1, NodeFactory.createAssign(NodeFactory.createName("x"), NodeFactory.createCall(NodeFactory.createName("foo"), Lists.newArrayList(node2)))));
    checkSideEffects.visit(NodeTraversal.create(compiler), parent, null);

    // Test visit method with assignment statements
    Node assignmentNode = NodeFactory.createAssign(NodeFactory.createName("x"), NodeFactory.createCall(NodeFactory.createName("foo"), Lists.newArrayList()));
    Node assignmentParent = NodeFactory.createBlock(Lists.newArrayList(assignmentNode));
    checkSideEffects.visit(NodeTraversal.create(compiler), assignmentParent, null);

    // Test visit method with void statements
    Node voidNode = NodeFactory.createEmpty();
    Node voidParent = NodeFactory.createBlock(Lists.newArrayList(voidNode));
    checkSideEffects.visit(NodeTraversal.create(compiler), voidParent, null);

    // Test visit method with expressions that don't have side effects
    Node expressionNode1 = NodeFactory.createCall(NodeFactory.createName("+"), Lists.newArrayList(NodeFactory.createStringLiteral("foo"), NodeFactory.createStringLiteral("bar")));
    Node expressionParent1 = NodeFactory.createBlock(Lists.newArrayList(expressionNode1));
    checkSideEffects.visit(NodeTraversal.create(compiler), expressionParent1, null);

    Node expressionNode2 = NodeFactory.createCall(NodeFactory.createName("+"), Lists.newArrayList(NodeFactory.createStringLiteral("foo"), NodeFactory.createName("bar")));
    Node expressionParent2 = NodeFactory.createBlock(Lists.newArrayList(expressionNode2));
    checkSideEffects.visit(NodeTraversal.create(compiler), expressionParent2, null);

    // Test visit method with simple operators
    Node operatorNode1 = NodeFactory.createCall(NodeFactory.createName("+"), Lists.newArrayList(NodeFactory.createNumber(1), NodeFactory.createNumber(1)));
    Node operatorParent1 = NodeFactory.createBlock(Lists.newArrayList(operatorNode1));
    checkSideEffects.visit(NodeTraversal.create(compiler), operatorParent1, null);

    Node operatorNode2 = NodeFactory.createCall(NodeFactory.createName("-"), Lists.newArrayList(NodeFactory.createNumber(1), NodeFactory.createNumber(1)));
    Node operatorParent2 = NodeFactory.createBlock(Lists.newArrayList(operatorNode2));
    checkSideEffects.visit(NodeTraversal.create(compiler), operatorParent2, null);

    // Test visit method with expressions that have side effects
    Node sideEffectNode1 = NodeFactory.createCall(NodeFactory.createName("console.log"), Lists.newArrayList(NodeFactory.createNumber(1)));
    Node sideEffectParent1 = NodeFactory.createBlock(Lists.newArrayList(sideEffectNode1));
    checkSideEffects.visit(NodeTraversal.create(compiler), sideEffectParent1, null); // Should generate warning

    Node sideEffectNode2 = NodeFactory.createCall(NodeFactory.createName("console.log"), Lists.newArrayList());
    Node sideEffectParent2 = NodeFactory.createBlock(Lists.newArrayList(sideEffectNode2));
    checkSideEffects.visit(NodeTraversal.create(compiler), sideEffectParent2, null); // Should generate warning

    Node sideEffectNode3 = NodeFactory.createCall(NodeFactory.createName("alert"), Lists.newArrayList(NodeFactory.createStringLiteral("Hello World")));
    Node sideEffectParent3 = NodeFactory.createBlock(Lists.newArrayList(sideEffectNode3));
    checkSideEffects.visit(NodeTraversal.create(compiler), sideEffectParent3, null); // Should generate warning

    // Test protectSideEffects method
    checkSideEffects.protectSideEffects();
    CompilerInput input = compiler.getSynthesizedExternsInput();
    Node protectorFn = input.getAstRoot(compiler).getChild(input.getAstRoot(compiler).getChildCount() - 1);
    assertTrue(protectorFn.isFunctionDeclaration());
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = PowerMockito.mock(AbstractCompiler.class);
    StripProtection stripProtection = new StripProtection(compiler);

    // Test empty cases
    Node root = NodeFactory.createRoot();
    stripProtection.process(null, root);

    // Test visit method with simple cases
    Node node1 = NodeFactory.createCall(NodeFactory.createName("JSCOMPILER_PROTECT"), Lists.newArrayList());
    Node parent = NodeFactory.createBlock(Lists.newArrayList(node1));
    stripProtection.visit(NodeTraversal.create(compiler), parent, null);
    assertTrue(parent.getChildCount() == 0);

    // Test visit method with calls to JSCOMPILER_PROTECT
    Node callNode1 = NodeFactory.createCall(NodeFactory.createName("JSCOMPILER_PROTECT"), Lists.newArrayList());
    Node callParent1 = NodeFactory.createBlock(Lists.newArrayList(callNode1));
    stripProtection.visit(NodeTraversal.create(compiler), callParent1, null);
    assertTrue(callParent1.getChildCount() == 0);

    Node callNode2 = NodeFactory.createCall(NodeFactory.createName("JSCOMPILER_PROTECT"), Lists.newArrayList(NodeFactory.createStringLiteral("foo")));
    Node callParent2 = NodeFactory.createBlock(Lists.newArrayList(callNode2));
    stripProtection.visit(NodeTraversal.create(compiler), callParent2, null);
    assertTrue(callParent2.getChildCount() == 0);

    Node callNode3 = NodeFactory.createCall(NodeFactory.createName("JSCOMPILER_PROTECT"), Lists.newArrayList(NodeFactory.createName("foo"), NodeFactory.createNumber(1)));
    Node callParent3 = NodeFactory.createBlock(Lists.newArrayList(callNode3));
    stripProtection.visit(NodeTraversal.create(compiler), callParent3, null);
    assertTrue(callParent3.getChildCount() == 0);

    // Test visit method with calls to JSCOMPILER_PROTECT that have arguments
    Node callNode4 = NodeFactory.createCall(NodeFactory.createName("JSCOMPILER_PROTECT"), Lists.newArrayList(NodeFactory.createName("foo"), NodeFactory.createName("bar")));
    Node callParent4 = NodeFactory.createBlock(Lists.newArrayList(callNode4));
    stripProtection.visit(NodeTraversal.create(compiler), callParent4, null);
    assertTrue(callParent4.getChildCount() == 0);

    Node callNode5 = NodeFactory.createCall(NodeFactory.createName("JSCOMPILER_PROTECT"), Lists.newArrayList(NodeFactory.createName("foo"), NodeFactory.createName("bar"), NodeFactory.createNumber(1)));
    Node callParent5 = NodeFactory.createBlock(Lists.newArrayList(callNode5));
    stripProtection.visit(NodeTraversal.create(compiler), callParent5, null);
    assertTrue(callParent5.getChildCount() == 0);

    // Test visit method with calls to other functions that are not JSCOMPILER_PROTECT
    Node otherFunctionNode = NodeFactory.createCall(NodeFactory.createName("console.log"), Lists.newArrayList(NodeFactory.createNumber(1)));
    Node otherFunctionParent = NodeFactory.createBlock(Lists.newArrayList(otherFunctionNode));
    stripProtection.visit(NodeTraversal.create(compiler), otherFunctionParent, null);
    assertFalse(otherFunctionParent.getChildCount() == 0);
  }
}