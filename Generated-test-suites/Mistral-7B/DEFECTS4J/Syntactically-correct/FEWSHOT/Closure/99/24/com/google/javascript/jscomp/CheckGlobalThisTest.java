package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(checker);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = new Node(Token.SCRIPT, null);
    Node functionNode = new Node(Token.FUNCTION, null, root);
    Node functionBody = new Node(Token.BLOCK, null, functionNode);

    Node globalThisNode = new Node(Token.THIS, null, functionBody);

    when(compiler.report(Mockito.any(NodeTraversal.ErrorReport.class))).thenReturn(true);

    Node functionCallNode = new Node(Token.GETPROP, Token.STRING_LITERAL, functionNode);
    functionCallNode.getLastChild().setString("call");

    Node functionAssignmentNode = new Node(Token.ASSIGN, null, functionNode);
    Node functionAssignmentLhs = new Node(Token.NAME, null, functionAssignmentNode);
    functionAssignmentLhs.setString("func");

    Node variableAssignmentNode = new Node(Token.ASSIGN, null, root);
    Node variableAssignmentLhs = new Node(Token.NAME, null, variableAssignmentNode);
    variableAssignmentLhs.setString("globalThis");

    Node prototypePropertyAccessNode = new Node(Token.GETPROP, Token.STRING_LITERAL, null);
    prototypePropertyAccessNode.getLastChild().setString("prototype");

    Node assignToPrototypePropertyAccessNode = new Node(Token.ASSIGN, null, null);
    assignToPrototypePropertyAccessNode.setFirstChild(prototypePropertyAccessNode);
    Node assignToPrototypePropertyAccessLhs = new Node(Token.GETPROP, Token.STRING_LITERAL, null);
    assignToPrototypePropertyAccessLhs.getLastChild().setString("prototype");
    assignToPrototypePropertyAccessLhs.setParent(assignToPrototypePropertyAccessNode);

    functionNode.setFirstChild(functionBody);
    functionBody.setFirstChild(globalThisNode);
    root.setFirstChild(functionCallNode);
    functionCallNode.setFirstChild(functionNode);
    functionNode.setParent(functionCallNode);
    functionCallNode.setNext(functionAssignmentNode);
    functionAssignmentNode.setFirstChild(functionAssignmentLhs);
    functionAssignmentLhs.setNext(functionNode);
    functionNode.setNext(variableAssignmentNode);
    variableAssignmentNode.setFirstChild(variableAssignmentLhs);
    variableAssignmentLhs.setNext(globalThisNode);
    root.setNext(assignToPrototypePropertyAccessNode);
    assignToPrototypePropertyAccessNode.setFirstChild(assignToPrototypePropertyAccessLhs);
    assignToPrototypePropertyAccessLhs.setNext(prototypePropertyAccessNode);
    prototypePropertyAccessNode.setNext(globalThisNode);

    traversal.traverse(root);

    assertThat(compiler.report).wasCalledWith(
        new NodeTraversal.ErrorReport(
            globalThisNode, CheckLevel.WARNING, DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorFunctionNode = new Node(Token.FUNCTION, null, null);
    constructorFunctionNode.setJSDocInfo(new JSDocInfo(null, null, null, null, null, true, null, null, null, null));

    Node constructorFunctionCallNode = new Node(Token.GETPROP, Token.STRING_LITERAL, null);
    constructorFunctionCallNode.getLastChild().setString("call");
    constructorFunctionCallNode.setParent(constructorFunctionNode);

    Node constructorFunctionBody = new Node(Token.BLOCK, null, constructorFunctionNode);

    Node constructorThisNode = new Node(Token.THIS, null, constructorFunctionBody);

    traversal.traverse(constructorFunctionCallNode);

    assertNull(compiler.report);
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION, null, null);
    functionNode.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, "this", null, null, null));

    Node functionBody = new Node(Token.BLOCK, null, functionNode);

    Node thisNode = new Node(Token.THIS, null, functionBody);

    traversal.traverse(functionNode);

    assertNull(compiler.report);
  }

  @Test
  public void testShouldNotTraverseFunctionWithLogicalPlaceForThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node body = new Node(Token.BLOCK, null, functionNode);
    Node assignNode = new Node(Token.ASSIGN, null, body);
    Node lhsNode = new Node(Token.NAME, null, assignNode);
    lhsNode.setString("a");

    Node thisNode = new Node(Token.THIS, null, body);

    Node rhsNode = new Node(Token.NAME, null, functionNode);
    rhsNode.setString("this");
    assignNode.setFirstChild(lhsNode);
    lhsNode.setNext(rhsNode);

    traversal.traverse(functionNode);

    assertNull(compiler.report);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node lhsNode = new Node(Token.NAME, null, assignmentNode);
    lhsNode.setString("a");

    Node thisNode = new Node(Token.THIS, null, null);

    Node rhsNode = new Node(Token.NAME, null, assignmentNode);
    rhsNode.setString("this");
    assignmentNode.setFirstChild(lhsNode);
    lhsNode.setNext(rhsNode);

    Node root = new Node(Token.SCRIPT, null);
    root.setFirstChild(assignmentNode);

    traversal.traverse(root);

    assertThat(compiler.report).wasCalledWith(
        new NodeTraversal.ErrorReport(
            thisNode, CheckLevel.WARNING, DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = new Node(Token.GETPROP, null, null);
    Node propertyNameNode = new Node(Token.NAME, null, propertyAccessNode);
    propertyNameNode.setString("a");

    Node thisNode = new Node(Token.THIS, null, null);

    propertyAccessNode.setFirstChild(propertyNameNode);
    propertyNameNode.setNext(thisNode);

    Node root = new Node(Token.SCRIPT, null);
    root.setFirstChild(propertyAccessNode);

    traversal.traverse(root);

    assertThat(compiler.report).wasCalledWith(
        new NodeTraversal.ErrorReport(
            thisNode, CheckLevel.WARNING, DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentToPrototypeProperty() {
    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node lhsNode = new Node(Token.GETPROP, Token.STRING_LITERAL, null);
    lhsNode.getLastChild().setString("prototype");

    Node thisNode = new Node(Token.THIS, null, null);

    Node rhsNode = new Node(Token.NAME, null, assignmentNode);
    rhsNode.setString("this");
    assignmentNode.setFirstChild(lhsNode);
    lhsNode.setNext(rhsNode);

    Node root = new Node(Token.SCRIPT, null);
    root.setFirstChild(assignmentNode);

    traversal.traverse(root);

    assertNull(compiler.report);
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentToPrototypePropertyWithMissingPrototypeCheck() {
    CheckGlobalThis checkerWithoutPrototypeCheck = new CheckGlobalThis(compiler, CheckLevel.WARNING) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }
    };

    Node assignmentNode = new Node(Token.ASSIGN, null, null);
    Node lhsNode = new Node(Token.GETPROP, Token.STRING_LITERAL, null);
    lhsNode.getLastChild().setString("prototype");

    Node thisNode = new Node(Token.THIS, null, null);

    Node rhsNode = new Node(Token.NAME, null, assignmentNode);
    rhsNode.setString("this");
    assignmentNode.setFirstChild(lhsNode);
    lhsNode.setNext(rhsNode);

    Node root = new Node(Token.SCRIPT, null);
    root.setFirstChild(assignmentNode);

    NodeTraversal traversalWithPrototypeCheckDisabled = NodeTraversal.create(checkerWithoutPrototypeCheck);

    traversalWithPrototypeCheckDisabled.traverse(root);

    assertNull(compiler.report);
  }
}