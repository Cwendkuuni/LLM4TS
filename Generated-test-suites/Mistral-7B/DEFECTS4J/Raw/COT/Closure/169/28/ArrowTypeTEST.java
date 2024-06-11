import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createNumberNode;
import static com.google.javascript.rhino.NodeFactory.createStringNode;
import static com.google.javascript.rhino.NodeFactory.createTypeError;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType.BooleanLiteralSet;
import com.google.javascript.rhino.JSType.TernaryValue;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    errorReporter = new ErrorReporter() {};
  }

  @Test
  public void testArrowTypeBasicConstructor() {
    Node parameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    JSType expectedReturnType = createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, expectedReturnType);
    assertEquals("Unexpected return type", expectedReturnType, arrowType.returnType);
    assertEquals("Unexpected parameter types", 2, arrowType.parameters.getChildCount());
  }

  @Test
  public void testArrowTypeInferredReturnTypeConstructor() {
    Node parameters = createNode(createVarArgNode());
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertEquals("Unexpected return type", JSTypeNative.VOID_TYPE, arrowType.returnType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeNullParameters() {
    new ArrowType(registry, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeNullReturnType() {
    Node parameters = createNode(createNumberNode(1));
    new ArrowType(registry, parameters, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeInvalidParameterType() {
    Node parameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createTypeError("error"));
    new ArrowType(registry, parameters, createNumberType());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, createNumberType());
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, createNumberType());
    assertTrue("Unexpected subtype result", thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testArrowTypeNotSubtypeDifferentReturnTypes() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, createNumberType());
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, createBooleanType());
    assertFalse("Unexpected subtype result", thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testArrowTypeNotSubtypeDifferentParameters() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(
        createNumberNode(2),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, createNumberType());
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, createNumberType());
    assertFalse("Unexpected subtype result", thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType arrowType = new ArrowType(registry, thisParameters, createNumberType());
    assertTrue("Unexpected equal parameter result", arrowType.hasEqualParameters(arrowType, true));
  }

  @Test
  public void testArrowTypeUnequalParameters() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str1"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(
        createNumberNode(1),
        createStringNode("str2"),
        createNode(createVarArgNode()));
    ArrowType arrowType = new ArrowType(registry, thisParameters, createNumberType());
    assertFalse("Unexpected unequal parameter result", arrowType.hasEqualParameters(arrowType, true));
  }

  @Test
  public void testArrowTypeSubtypeWithUnknownReturnType() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, null);
    ArrowType thatArrowType = new ArrowType(registry, createNode(createVarArgNode()), createNumberType());
    assertTrue("Unexpected subtype result", thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testArrowTypeSubtypeWithUnknownParameters() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(null);
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, createNumberType());
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, null);
    assertTrue("Unexpected subtype result", thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testArrowTypeSubtypeWithUnknownParametersAndReturnType() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(null);
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, null);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, null);
    assertTrue("Unexpected subtype result", thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node thisParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, createNumberType());
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, createNumberType());
    assertEquals("Unexpected hashcode result", thisArrowType.hashCode(), thatArrowType.hashCode());
  }

  @Test
  public void testArrowTypeToString() {
    Node parameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType arrowType = new ArrowType(registry, parameters, createNumberType());
    assertEquals("Unexpected toString result", "[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testArrowTypeGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testArrowTypeGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testArrowTypeTestForEquality() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testArrowTypeVisit() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testArrowTypeGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertEquals("Unexpected boolean set result", BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testArrowTypeResolveInternal() {
    Node parameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType arrowType = new ArrowType(registry, parameters, createNumberType());
    arrowType.resolveInternal(errorReporter, createEmptyScope());
  }

  @Test
  public void testArrowTypeHasAnyTemplateInternal() {
    Node parameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    ArrowType arrowType = new ArrowType(registry, parameters, null);
    assertFalse("Unexpected hasAnyTemplate result", arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testArrowTypeHasAnyTemplateWithTemplatedReturnType() {
    Node parameters = createNode(
        createNumberNode(1),
        createStringNode("str"),
        createNode(createVarArgNode()));
    JSType templatedReturnType = createTemplateType(createNumberType(), "T");
    ArrowType arrowType = new ArrowType(registry, parameters, templatedReturnType);
    assertTrue("Unexpected hasAnyTemplate result", arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testArrowTypeHasAnyTemplateWithTemplatedParameterType() {
    Node parameters = createNode(
        createTemplateType(createNumberType(), "T"),
        createStringNode("str"),
        createNode(createVarArgNode()));
    JSType templatedReturnType = createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, templatedReturnType);
    assertTrue("Unexpected hasAnyTemplate result", arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testArrowTypeHasAnyTemplateWithBothTemplatedTypes() {
    Node thisParameters = createNode(
        createTemplateType(createNumberType(), "T1"),
        createStringNode("str"),
        createNode(createVarArgNode()));
    Node thatParameters = createNode(
        createTemplateType(createNumberType(), "T2"),
        createStringNode("str"),
        createNode(createVarArgNode()));
    JSType templatedReturnType = createTemplateType(createNumberType(), "T3");
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, templatedReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, templatedReturnType);
    assertTrue("Unexpected hasAnyTemplate result", thisArrowType.hasAnyTemplateInternal());
    assertTrue("Unexpected hasAnyTemplate result", thatArrowType.hasAnyTemplateInternal());
  }

  private Node createVarArgNode() {
    return createNode(createVarArgSymbolNode(), createArrayType(createNumberType(), "..."));
  }

  private Node createVarArgSymbolNode() {
    return createNode(createIdentifierNode("..."), createIdentifierNode("args"));
  }

  private JSType createNumberType() {
    return registry.createNumberType();
  }

  private JSType createBooleanType() {
    return registry.createBooleanType();
  }

  private Node createTemplateType(JSType baseType, String templateVariableName) {
    return createTemplateType(registry, baseType, templateVariableName, null);
  }

  private Node createTemplateType(
      JSTypeRegistry registry, JSType baseType, String templateVariableName,
      Node[] templateArguments) {
    return registry.createTemplateType(baseType, templateVariableName,
        templateArguments);
  }

  private Node createArrayType(JSType elementType, Node... dimensions) {
    return registry.createArrayType(elementType, dimensions);
  }

  private Node createEmptyScope() {
    return registry.getEmptyScope();
  }
}