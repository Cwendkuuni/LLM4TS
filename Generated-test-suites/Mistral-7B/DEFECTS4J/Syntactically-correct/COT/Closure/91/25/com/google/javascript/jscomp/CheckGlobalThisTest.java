import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.mockito.ArgumentCaptor;

/**
 * Tests for the {@code CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.DEFAULT);
  }

  @Test
  public void testShouldTraverseFunction() {
    NodeFunction nodeFunction = Node.newFunction("function foo() {}");
    assertEquals(true, checker.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    NodeFunction nodeFunction = Node.newFunction("function MyConstructor() {}");
    JSDocInfo jsDocInfo = Node.newJSDocInfo("constructor");
    when(nodeFunction.getJSDocInfo()).thenReturn(jsDocInfo);
    assertEquals(false, checker.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    NodeFunction nodeFunction = Node.newFunction("@this function foo() {}");
    assertEquals(false, checker.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    NodeFunction nodeFunction = Node.newFunction("@override function foo() {}");
    assertEquals(false, checker.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, null));
  }

  @Test
  public void testShouldNotTraverseFunctionInsideObjectLiteral() {
    NodeObject nodeObject = Node.newObject("{}");
    NodeFunction nodeFunction = Node.newFunction("function foo() {}");
    Node propertyName = Node.newIdentifier("foo");
    Node assignmentNode = Node.newAssignment(propertyName, nodeFunction);
    nodeObject.addChild(assignmentNode);
    assertEquals(false, checker.shouldTraverse(NodeTraversal.EMPTY, nodeFunction, nodeObject));
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypeProperty() {
    NodeObject nodeObject = Node.newObject("{}");
    Node functionNode = Node.newFunction("function foo() {}");
    Node prototypeNode = Node.newObject("{prototype: {foo: function() {}}}");
    Node propertyName = Node.newIdentifier("prototype");
    Node propertyAccess = Node.newGetProperty(nodeObject, propertyName);
    Node prototypeProperty = propertyAccess.getLastChild();
    Node prototypeFunction = prototypeProperty.getFirstChild();
    prototypeFunction.setParent(prototypeNode);
    nodeObject.addChild(propertyAccess);
    assertEquals(false, checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, nodeObject));
  }

  @Test
  public void testShouldNotTraverseFunctionInsideNestedPrototypeProperty() {
    NodeObject nodeObject = Node.newObject("{}");
    Node functionNode = Node.newFunction("function foo() {}");
    Node prototypeNode1 = Node.newObject("{prototype: {}}");
    Node propertyName1 = Node.newIdentifier("prototype");
    Node propertyAccess1 = Node.newGetProperty(nodeObject, propertyName1);
    Node prototypeProperty1 = propertyAccess1.getLastChild();
    Node prototypeNode2 = Node.newObject("{prototype: {foo: function() {}}}");
    Node propertyName2 = Node.newIdentifier("prototype");
    Node propertyAccess2 = Node.newGetProperty(prototypeNode1, propertyName2);
    Node prototypeProperty2 = propertyAccess2.getLastChild();
    Node prototypeFunction = prototypeProperty2.getFirstChild();
    prototypeFunction.setParent(prototypeNode2);
    prototypeProperty1.setLastChild(propertyAccess2);
    nodeObject.addChild(propertyAccess1);
    assertEquals(false, checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, nodeObject));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssignment(Node.newIdentifier("a"), Node.newThis());
    assertEquals(true, checker.shouldTraverse(NodeTraversal.EMPTY, Node.newThis(), assignNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    Node assignNode = Node.newAssignment(Node.newIdentifier("a"), Node.newThis());
    Node leftSide = assignNode.getFirstChild();
    Node rightSide = assignNode.getLastChild();
    Node nonPrototypeProperty = Node.newGetProperty(Node.newIdentifier("b"), Node.newIdentifier("c"));
    Node functionNode = Node.newFunction("function foo() {}");
    Node functionLiteral = Node.newExpressionStatement(functionNode);
    Node assignmentNode = Node.newAssignment(rightSide, Node.newSequence(functionLiteral, nonPrototypeProperty));
    leftSide.setLastChild(assignmentNode);
    assertEquals(false, checker.shouldTraverse(NodeTraversal.EMPTY, Node.newThis(), assignNode));
  }

  @Test
  public void testVisitThisKeyword() {
    NodeFunction nodeFunction = Node.newFunction("function foo() {}", new JSDocInfo("", null));
    Node nodeThis = Node.newThis();
    Node parentNode = Node.newIdentifier("a");
    Node traverser = mock(NodeTraversal.class);

    ArgumentCaptor<DiagnosticType> diagnosticCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    when(compiler.report(eq(traverser), eq(checker.level), any(DiagnosticType.class)))
        .then(invocation -> System.out.println(invocation.getArguments()[2]));

    checker.visit(NodeTraversal.EMPTY, nodeThis, parentNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    checker.visit(NodeTraversal.EMPTY, nodeFunction, null);

    checker.visit(traverser, nodeThis, parentNode);

    verify(compiler).report(eq(traverser), eq(checker.level), diagnosticCaptor.capture());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticCaptor.getValue().getName());
  }

  @Test
  public void testVisitThisKeywordInsidePrototypeMethod() {
    NodeObject nodeObject = Node.newObject("{prototype: {}}");
    NodeFunction prototypeMethod = Node.newFunction("function foo() {}", new JSDocInfo("", null));
    Node prototypeProperty = Node.newProperty(new NodeIdentifier("foo"), prototypeMethod);
    nodeObject.setPrototype(prototypeProperty);
    Node nodeThis = Node.newThis();
    Node parentNode = Node.newIdentifier("a");
    Node traverser = mock(NodeTraversal.class);

    ArgumentCaptor<DiagnosticType> diagnosticCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    when(compiler.report(eq(traverser), eq(checker.level), any(DiagnosticType.class)))
        .then(invocation -> System.out.println(invocation.getArguments()[2]));

    checker.visit(NodeTraversal.EMPTY, nodeThis, parentNode);

    checker.visit(traverser, nodeThis, nodeObject);

    verify(compiler, never()).report(eq(traverser), eq(checker.level), diagnosticCaptor.capture());
  }
}