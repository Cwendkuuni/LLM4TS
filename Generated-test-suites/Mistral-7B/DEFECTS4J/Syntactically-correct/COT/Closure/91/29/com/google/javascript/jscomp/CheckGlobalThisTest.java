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

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionAnnotatedWithConstructorOrThisOrOverride() {
    Node functionNode = Node.newFunction("constructorFunction");
    JSDocInfo jsDoc = new JSDocInfo("constructor");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node parentNode = Node.newBlock();
    Node result = checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoParent() {
    Node functionNode = Node.newFunction("functionWithoutParent");

    Node result = checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithPrototypeProperty() {
    Node functionNode = Node.newFunction("functionWithPrototypeProperty");
    Node prototypeNode = Node.newName("prototype");

    Node parentNode = Node.newGetProp(Node.newName("Object"), prototypeNode);
    Node result = checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithPrototypeSubProperty() {
    Node functionNode = Node.newFunction("functionWithPrototypeSubProperty");
    Node prototypeNode = Node.newName("prototype");
    Node subPropertyNode = Node.newName("subProperty");

    Node parentNode = Node.newGetProp(Node.newName("Object"), prototypeNode);
    Node getPropNode = Node.newGetProp(parentNode, subPropertyNode);

    Node result = checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, getPropNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("functionWithNoLogicalPlaceForThisAnnotation");

    Node parentNode = Node.newBlock();
    Node result = checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithPrototypeMethod() {
    Node functionNode = Node.newFunction("prototypeMethod");
    JSDocInfo jsDoc = new JSDocInfo("method");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node parentNode = Node.newName("Object");
    Node prototypeNode = Node.newGetProp(parentNode, Node.newName("prototype"));
    Node result = checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, prototypeNode);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_AssignmentLeftSide() {
    Node assignNode = Node.newAssign();
    Node leftHandSideNode = Node.newThis();

    Node parentNode = Node.newBlock();
    checker.visit(NodeTraversal.EMPTY, assignNode, parentNode);
    assertTrue(checker.shouldReportThis(leftHandSideNode, parentNode));
  }

  @Test
  public void testShouldReportThis_AssignmentRightSide() {
    Node assignNode = Node.newAssign();
    Node leftHandSideNode = Node.newName("leftHandSide");
    Node rightHandSideNode = Node.newThis();

    Node parentNode = Node.newBlock();
    checker.visit(NodeTraversal.EMPTY, assignNode, parentNode);
    assertFalse(checker.shouldReportThis(rightHandSideNode, parentNode));
  }

  @Test
  public void testShouldReportThis_AssignmentRightSideWithPropertyAccess() {
    Node assignNode = Node.newAssign();
    Node leftHandSideNode = Node.newName("leftHandSide");
    Node rightHandSideNode = Node.newGetProp(Node.newThis(), Node.newName("property"));

    Node parentNode = Node.newBlock();
    checker.visit(NodeTraversal.EMPTY, assignNode, parentNode);
    assertTrue(checker.shouldReportThis(rightHandSideNode, parentNode));
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJSDoc() {
    Node functionNode = Node.newFunction("functionWithJSDoc");
    JSDocInfo jsDoc = new JSDocInfo("constructor");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);
    assertTrue(result.isConstructor());
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc() {
    Node functionNode = Node.newFunction("functionWithoutJSDoc");

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);
    assertNull(result);
  }

}