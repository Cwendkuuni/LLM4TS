import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private static final JSTypeRegistry REGISTRY = new JSTypeRegistry();
  private static final ErrorReporter ERROR_REPORTER = new ErrorReporter();

  @org.junit.BeforeClass
  public static void setUp() {
    REGISTRY.init();
  }

  @org.junit.Test
  public void testArrowTypeCreationWithNullParametersAndReturnType() {
    ArrowType arrowType =
        new ArrowType(REGISTRY, null, null);

    assertSame(UNKNOWN_TYPE, arrowType.getReturnType());
    assertNull(arrowType.getParameters());
  }

  @org.junit.Test
  public void testArrowTypeCreationWithParametersAndReturnType() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters = new Node(Node.LIST);
    parameters.appendChild(new Node("int"));
    parameters.appendChild(new Node("string"));

    ArrowType arrowType =
        new ArrowType(REGISTRY, parameters, intType);

    assertEquals(intType, arrowType.getReturnType());
    assertEquals(parameters, arrowType.getParameters());
    assertFalse(arrowType.isReturnTypeInferred());
  }

  @org.junit.Test
  public void testArrowTypeCreationWithParametersAndInferredReturnType() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters = new Node(Node.LIST);
    parameters.appendChild(new Node("int"));
    parameters.appendChild(new Node("string"));

    ArrowType arrowType =
        new ArrowType(REGISTRY, parameters, null, true);

    assertNull(arrowType.getReturnType());
    assertEquals(parameters, arrowType.getParameters());
    assertTrue(arrowType.isReturnTypeInferred());
  }

  @org.junit.Test
  public void testArrowTypeIsSubtypeOfArrowTypeWithEqualParametersAndReturnType() {
    JSType intType = REGISTRY.getIntType();
    JSType StringType = REGISTRY.getStringType();

    Node parameters = new Node(Node.LIST);
    parameters.appendChild(new Node("int"));
    parameters.appendChild(new Node("String"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters, StringType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @org.junit.Test
  public void testArrowTypeIsSubtypeOfArrowTypeWithDifferentReturnType() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters = new Node(Node.LIST);
    parameters.appendChild(new Node("int"));
    parameters.appendChild(new Node("String"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters, stringType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @org.junit.Test
  public void testArrowTypeIsSubtypeOfArrowTypeWithDifferentParameters() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();
    JSType booleanType = REGISTRY.getBooleanType();

    Node parameters1 = new Node(Node.LIST);
    parameters1.appendChild(new Node("int"));
    parameters1.appendChild(new Node("String"));

    Node parameters2 = new Node(Node.LIST);
    parameters2.appendChild(new Node("int"));
    parameters2.appendChild(new Node("boolean"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, booleanType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @org.junit.Test
  public void testArrowTypeIsSubtypeOfFunctionType() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();
    JSType functionType =
        REGISTRY.getFunctionType(intType, stringType, null);

    Node parameters = new Node(Node.LIST);
    parameters.appendChild(new Node("int"));
    parameters.appendChild(new Node("String"));

    ArrowType arrowType =
        new ArrowType(REGISTRY, parameters, intType);

    assertTrue(arrowType.isSubtype(functionType));
    assertFalse(functionType.isSubtype(arrowType));
  }

  @org.junit.Test
  public void testArrowTypeWithVarArgsIsSubtypeOfArrowTypeWithoutVarArgs() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters1 = new Node(Node.LIST);
    parameters1.appendChild(new Node("int"));
    parameters1.appendChild(new Node("String"));
    parameters1.appendChild(new Node("..."));

    Node parameters2 = new Node(Node.LIST);
    parameters2.appendChild(new Node("int"));
    parameters2.appendChild(new Node("String"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @org.junit.Test
  public void testHasEqualParameters() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters1 = new Node(Node.LIST);
    parameters1.appendChild(new Node("int"));
    parameters1.appendChild(new Node("String"));

    Node parameters2 = new Node(Node.LIST);
    parameters2.appendChild(new Node("int"));
    parameters2.appendChild(new Node("String"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @org.junit.Test
  public void testHasEqualParametersDifferentTypes() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters1 = new Node(Node.LIST);
    parameters1.appendChild(new Node("int"));
    parameters1.appendChild(new Node("String"));

    Node parameters2 = new Node(Node.LIST);
    parameters2.appendChild(new Node("Number"));
    parameters2.appendChild(new Node("String"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @org.junit.Test
  public void testHasEqualParametersDifferentNumberOfParameters() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters1 = new Node(Node.LIST);
    parameters1.appendChild(new Node("int"));
    parameters1.appendChild(new Node("String"));

    Node parameters2 = new Node(Node.LIST);
    parameters2.appendChild(new Node("int"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @org.junit.Test
  public void testIsEquivalentTo() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters1 = new Node(Node.LIST);
    parameters1.appendChild(new Node("int"));
    parameters1.appendChild(new Node("String"));

    Node parameters2 = new Node(Node.LIST);
    parameters2.appendChild(new Node("int"));
    parameters2.appendChild(new Node("String"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType);

    ArrowType arrowType3 =
        new ArrowType(REGISTRY, parameters1, stringType);

    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType3);

    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType2);
    assertEquals(arrowType1, arrowType3);

    assertNotEquals(arrowType1, null);
    assertNotEquals(arrowType1, intType);
    assertNotEquals(arrowType1, JSTypeNative.UNKNOWN_TYPE);

    assertNotEquals(arrowType1, arrowType2);
    assertNotEquals(arrowType1, arrowType3);
  }

  @org.junit.Test
  public void testHashCode() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters1 = new Node(Node.LIST);
    parameters1.appendChild(new Node("int"));
    parameters1.appendChild(new Node("String"));

    Node parameters2 = new Node(Node.LIST);
    parameters2.appendChild(new Node("int"));
    parameters2.appendChild(new Node("String"));

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();

    int hashCode3 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
    assertEquals(hashCode1, hashCode3);
  }

  @org.junit.Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @org.junit.Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @org.junit.Test
  public void testTestForEquality() {
    throw new UnsupportedOperationException();
  }

  @org.junit.Test
  public void testVisit() {
    throw new UnsupportedOperationException();
  }

  @org.junit.Test
  public void testGetPossibleToBooleanOutcomes() {
    throw new UnsupportedOperationException();
  }

  @org.junit.Test
  public void testResolveInternal() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters = new Node(Node.LIST);
    parameters.appendChild(new Node("int"));
    parameters.appendChild(new Node("String"));

    ArrowType arrowType =
        new ArrowType(REGISTRY, parameters, intType);

    assertEquals(intType, arrowType.getReturnType());
    assertSame(parameters, arrowType.getParameters());
  }

  @org.junit.Test
  public void testHasUnknownParamsOrReturn() {
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();

    Node parameters = new Node(Node.LIST);
    parameters.appendChild(new Node("int"));
    parameters.appendChild(new Node("String"));

    ArrowType arrowType =
        new ArrowType(REGISTRY, parameters, intType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownReturnType =
        new ArrowType(REGISTRY, parameters, null);

    assertTrue(arrowTypeWithUnknownReturnType.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownParameters =
        new ArrowType(REGISTRY, null, intType);

    assertTrue(arrowTypeWithUnknownParameters.hasUnknownParamsOrReturn());
  }
}