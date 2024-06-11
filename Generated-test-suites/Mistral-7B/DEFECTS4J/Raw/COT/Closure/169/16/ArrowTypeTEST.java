import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowTypeNode;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testArrowTypeConstructorWithParametersAndReturnType() {
    Node parametersNode = NodeOperations.createList(
        NodeOperations.createNameNode("x"),
        NodeOperations.createNameNode("y"));
    JSType returnType = JSType.getBooleanType(registry);
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertEquals("Expected ArrowType", ArrowType.class, arrowType.getClass());
    assertEquals("Unexpected Parameters", 2, arrowType.parameters.getChildCount());
    assertEquals("Unexpected Return Type", returnType, arrowType.returnType);
    assertFalse("Unexpected Return Type Inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithOnlyReturnType() {
    JSType returnType = JSType.getBooleanType(registry);
    ArrowType arrowType = new ArrowType(registry, null, returnType, false);

    assertEquals("Expected ArrowType", ArrowType.class, arrowType.getClass());
    assertNull("Unexpected Parameters", arrowType.parameters);
    assertEquals("Unexpected Return Type", returnType, arrowType.returnType);
    assertFalse("Unexpected Return Type Inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithOnlyReturnTypeAndInferred() {
    JSType returnType = JSType.getBooleanType(registry);
    ArrowType arrowType = new ArrowType(registry, null, returnType, true);

    assertEquals("Expected ArrowType", ArrowType.class, arrowType.getClass());
    assertNull("Unexpected Parameters", arrowType.parameters);
    assertEquals("Unexpected Return Type", returnType, arrowType.returnType);
    assertTrue("Unexpected Return Type Inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithEmptyParameterList() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    assertEquals("Expected ArrowType", ArrowType.class, arrowType.getClass());
    assertNull("Unexpected Parameters", arrowType.parameters);
    assertNull("Unexpected Return Type", arrowType.returnType);
    assertFalse("Unexpected Return Type Inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithVarargsParameter() {
    Node parametersNode = NodeOperations.createList(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null));
    JSType returnType = JSType.getBooleanType(registry);
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertEquals("Expected ArrowType", ArrowType.class, arrowType.getClass());
    assertEquals("Unexpected Parameters", 1, arrowType.parameters.getChildCount());
    Node varargsNode = arrowType.parameters.getFirstChild();
    assertEquals("Unexpected First Parameter", varargsNode.getClass(), VarArgsNode.class);
    assertEquals("Unexpected Number of Children", 1, varargsNode.getChildCount());
    assertEquals("Unexpected Variadic Parameter Type", JSType.getArrayType(UNKNOWN_TYPE), varargsNode.getJSType());
    assertEquals("Unexpected Return Type", returnType, arrowType.returnType);
    assertFalse("Unexpected Return Type Inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtypeBasic() {
    ArrowType arrowType1 = new ArrowType(registry, null, JSType.getNumberType(registry), false);
    ArrowType arrowType2 = new ArrowType(registry, null, JSType.getBooleanType(registry), false);

    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithInferredReturnType() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, true);
    ArrowType arrowType2 = new ArrowType(registry, null, JSType.getBooleanType(registry), false);

    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithDifferentNumberOfParameters() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(NodeOperations.createNameNode("x"), null), null, false);
    ArrowType arrowType2 = new ArrowType(registry, null, null, false);

    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithDifferentParameterTypes() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(NodeOperations.createNameNode("x"), null), null, false);
    ArrowType arrowType2 = new ArrowType(registry, createArrowTypeNode(NodeOperations.createNameNode("y"), null), null, false);

    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithOptionalParameters() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null),
        NodeOperations.createOptionalArgCallNode(NodeOperations.createNameNode("y"), null)),
        null, false);

    ArrowType arrowType2 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null)), null, false);

    assertTrue("Expected true", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithOptionalParametersAndInferredReturnType() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null),
        NodeOperations.createOptionalArgCallNode(NodeOperations.createNameNode("y"), null)),
        true);

    ArrowType arrowType2 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null)), null, false);

    assertTrue("Expected true", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithVarargsParameters() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null)), null, false);

    ArrowType arrowType2 = new ArrowType(registry, null, null, false);

    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithVarargsParametersAndInferredReturnType() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null)), null, true);

    ArrowType arrowType2 = new ArrowType(registry, null, null, false);

    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithInferredReturnTypeAndParameter() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, true);
    ArrowType arrowType2 = new ArrowType(registry, null, JSType.getBooleanType(registry), false);

    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithInferredReturnTypeAndParameterInferred() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, true);
    ArrowType arrowType2 = new ArrowType(registry, null, null, true);

    assertTrue("Expected true", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParametersBasic() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(NodeOperations.createNameNode("x"), null), null, false);
    ArrowType arrowType2 = new ArrowType(registry, createArrowTypeNode(NodeOperations.createNameNode("y"), null), null, false);
    ArrowType arrowType3 = new ArrowType(registry, createArrowTypeNode(NodeOperations.createNameNode("x"), null), null, false);

    assertFalse("Expected false", arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue("Expected true", arrowType1.hasEqualParameters(arrowType3, true));
  }

  @Test
  public void testHasEqualParametersWithOptionalParameters() {
    ArrowType arrowType1 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null),
        NodeOperations.createOptionalArgCallNode(NodeOperations.createNameNode("y"), null)),
        false);

    ArrowType arrowType2 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null),
        NodeOperations.createOptionalArgCallNode(NodeOperations.createNameNode("z"), null)),
        false);

    assertFalse("Expected false", arrowType1.hasEqualParameters(arrowType2, true));

    ArrowType arrowType3 = new ArrowType(registry, createArrowTypeNode(
        NodeOperations.createNameNode("x"),
        NodeOperations.createListCallNode(NodeOperations.createNameNode("..."), null),
        NodeOperations.createOptionalArgCallNode(NodeOperations.createNameNode("y"), null)),
        false);

    assertTrue("Expected true", arrowType1.hasEqualParameters(arrowType3, true));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    arrowType.getLeastSupertype(JSType.getBooleanType(registry));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    arrowType.getGreatestSubtype(JSType.getBooleanType(registry));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    arrowType.testForEquality(JSType.getBooleanType(registry));
  }

  @Test
  public void testVisit() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    Visitor visitor = mock(Visitor.class);

    arrowType.visit(visitor);

    verify(visitor).visitArrowType(arrowType);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    BooleanLiteralSet expected = BooleanLiteralSet.TRUE;
    BooleanLiteralSet actual = arrowType.getPossibleToBooleanOutcomes();

    assertSame("Expected same objects", expected, actual);
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    arrowType.resolveInternal(errorReporter, null);

    assertNotNull("Unexpected null return type", arrowType.returnType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    ArrowType arrowType2 = new ArrowType(registry, null, null, true);

    assertFalse("Expected false", arrowType1.hasUnknownParamsOrReturn());
    assertTrue("Expected true", arrowType2.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    String expected = "[ArrowType]";
    String actual = arrowType.toStringHelper(false);

    assertEquals("Unexpected toStringHelper output", expected, actual);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    assertFalse("Expected false", arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithInferredReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    assertFalse("Expected false", arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithTemplateParameter() {
    ArrowType arrowType = new ArrowType(registry, createArrowTypeNode(NodeOperations.createNameNode("T"), null), null, false);

    assertTrue("Expected true", arrowType.hasAnyTemplateInternal());
  }
}