package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private NodeTraversal nodeTraversal;
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node globalFunction = Node.newFunction("globalFunction");
    Node globalParent = Node.newName("window");
    Node globalContext = Node.newBlock();
    globalContext.addChildToEnd(globalParent);
    globalContext.addChildToEnd(globalFunction);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, globalFunction, globalContext));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorFunction = Node.newFunction("constructorFunction");
    Node constructorParent = Node.newName("MyClass");
    constructorParent.addJSDocInfo(new JSDocInfo(true, null, null, null));
    Node constructorContext = Node.newBlock();
    constructorContext.addChildToEnd(constructorParent);
    constructorContext.addChildToEnd(constructorFunction);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, constructorFunction, constructorContext));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node thisFunction = Node.newFunction("thisFunction");
    Node thisParent = Node.newName("MyClass");
    thisParent.addJSDocInfo(new JSDocInfo(null, null, "@this", null));
    Node thisContext = Node.newBlock();
    thisContext.addChildToEnd(thisParent);
    thisContext.addChildToEnd(thisFunction);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, thisFunction, thisContext));
  }

  @Test
  public void testShouldNotTraverseFunctionInNonGlobalContext() {
    Node localFunction = Node.newFunction("localFunction");
    Node localParent = Node.newName("foo");
    Node localContext = Node.newBlock();
    localContext.addChildToEnd(localParent);
    localContext.addChildToEnd(localFunction);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, localFunction, localContext));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignParent = Node.newName("assignParent");
    Node assignContext = Node.newBlock();
    assignContext.addChildToEnd(assignParent);

    Node lhs = Node.newName("lhs");
    Node rhs = Node.newName("rhs");
    Node assign = Node.newAssign(lhs, rhs);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, lhs, assignParent));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignParent = Node.newName("assignParent");
    Node assignContext = Node.newBlock();
    assignContext.addChildToEnd(assignParent);

    Node lhs = Node.newName("lhs");
    Node rhs = Node.newGetProp(Node.newName("prototype"), lhs);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, rhs, assignParent));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    Node assignParent = Node.newName("assignParent");
    Node assignContext = Node.newBlock();
    assignContext.addChildToEnd(assignParent);

    Node lhs = Node.newName("lhs");
    Node rhs = Node.newGetProp(Node.newName("prototype"), Node.newGetProp(Node.newName("prototype"), lhs));

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, rhs, assignParent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignParent = Node.newName("assignParent");
    Node assignContext = Node.newBlock();
    assignContext.addChildToEnd(assignParent);

    Node lhs = Node.newThis();
    Node rhs = Node.newName("rhs");
    Node assign = Node.newAssign(lhs, rhs);

    assertTrue(checkGlobalThis.shouldReportThis(assignParent, assign));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node assignParent = Node.newName("assignParent");
    Node assignContext = Node.newBlock();
    assignContext.addChildToEnd(assignParent);

    Node lhs = Node.newThis();
    Node prop = Node.newName("prop");
    Node getProp = Node.newGetProp(lhs, prop);

    assertTrue(checkGlobalThis.shouldReportThis(assignParent, getProp));
  }
}