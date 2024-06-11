package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    traversal = new NodeTraversal(nodeFactory, compiler);
  }

  @Test
  public void testShouldTraverse_function() {
    Node functionNode = Node.newFunctionNode(null, null, null, null, null, false, null, null, null, null, null, null, null);
    Node functionParent = Node.newNameNode("someName");
    Node functionGrandparent = Node.newBlockNode(null);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.FLAG_CHECK_GLOBAL_THIS, false));

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldTraverse(traversal, functionNode, functionParent);
    assertThat(result).isFalse();

    result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldTraverse(traversal, functionNode, functionGrandparent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_prototypePropertyAccess() {
    Node functionNode = Node.newFunctionNode(null, null, null, null, null, false, null, null, null, null, null, null, null);
    Node propertyAccessNode = Node.newGetPropNode("prototype", Node.newNameNode("someName"));
    Node propertyAccessParent = Node.newNameNode("someFunction");

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.FLAG_CHECK_GLOBAL_THIS, false));

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldTraverse(traversal, propertyAccessNode, propertyAccessParent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithJsDocInfo() {
    Node functionNode = Node.newFunctionNode(null, null, null, null, null, false, null, null, null, null, null, null, null);
    Node functionParent = Node.newNameNode("someName");

    JSDocInfo jsDocInfo = Node.newJSDocInfo(null, null, null, null, null, null, null, null, null, null);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.FLAG_CHECK_GLOBAL_THIS, false));

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldTraverse(traversal, functionNode, functionParent);
    assertThat(result).isFalse();

    jsDocInfo = Node.newJSDocInfo(null, null, null, null, null, null, null, null, new String[]{"constructor"}, null);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldTraverse(traversal, functionNode, functionParent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode(null, null, null, null, null, false, null, null, null, null, null, null, null);
    Node functionParent = Node.newNameNode("someName");
    Node thisAnnotationNode = Node.newIdentifierNode("this");
    Node thisAnnotationParent = Node.newNameNode("someFunction");

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.FLAG_CHECK_GLOBAL_THIS, false));

    functionNode.addChildToEnd(thisAnnotationNode);
    thisAnnotationNode.setParent(functionNode);
    functionNode.setParent(thisAnnotationParent);
    thisAnnotationParent.setParent(functionParent);

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldTraverse(traversal, functionNode, functionParent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunctionNode(null, null, null, null, null, false, null, null, null, null, null, null, null);
    Node functionParent = Node.newNameNode("someName");

    when(compiler.getOptions()).thenReturn(new CheckLevel(CheckLevel.FLAG_CHECK_GLOBAL_THIS, false));

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldTraverse(traversal, functionNode, functionParent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignment() {
    Node assignmentNode = Node.newAssignNode(Node.newThisNode(), Node.newIdentifierNode("someName"));
    Node assignmentParent = Node.newNameNode("someName");

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldReportThis(assignmentNode, assignmentParent);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThis_rightSideOfAssignment() {
    Node assignmentNode = Node.newAssignNode(Node.newIdentifierNode("someName"), Node.newThisNode());
    Node assignmentParent = Node.newNameNode("someName");

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldReportThis(assignmentNode, assignmentParent);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThis_propertyAccess() {
    Node propertyAccessNode = Node.newGetPropNode("prototype", Node.newThisNode());
    Node propertyAccessParent = Node.newNameNode("someName");

    boolean result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).shouldReportThis(propertyAccessNode, propertyAccessParent);
    assertThat(result).isTrue();
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node functionNode = Node.newFunctionNode(null, null, null, null, null, false, null, null, null, null, null, null, null);
    Node parentNull = null;
    Node parentName = Node.newNameNode("someName");
    Node parentAssign = Node.newAssignNode(Node.newIdentifierNode("someName"), Node.newFunctionNode(null, null, null, null, null, false, null, null, null, null, null, null, null));

    JSDocInfo result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).getFunctionJsDocInfo(functionNode);
    assertNull(result);

    result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).getFunctionJsDocInfo(functionNode, parentNull);
    assertNull(result);

    result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).getFunctionJsDocInfo(functionNode, parentName);
    assertNull(result);

    result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).getFunctionJsDocInfo(functionNode, parentAssign);
    assertNull(result);

    functionNode.setJSDocInfo(Node.newJSDocInfo(null, null, null, null, null, null, null, null, null, null));

    JSDocInfo jsDocInfo = Node.newJSDocInfo(null, null, null, null, null, null, null, null, null, null);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    result = new CheckGlobalThis(compiler, CheckLevel.FLAG_CHECK_GLOBAL_THIS).getFunctionJsDocInfo(functionNode, parentName);
    assertThat(result).isEqualTo(jsDocInfo);
  }
}