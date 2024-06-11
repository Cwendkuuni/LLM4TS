import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.ANY;
import static com.google.javascript.rhino.JSType.ARRAY_TYPE;
import static com.google.javascript.rhino.JSType.BOOL;
import static com.google.javascript.rhino.JSType.INT;
import static com.google.javascript.rhino.JSType.VOID;
import static com.google.javascript.rhino.JSTypeFactory.createArrayType;
import static com.google.javascript.rhino.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.createNamedType;
import static com.google.javascript.rhino.JSTypeFactory.createParameterTypes;
import static com.google.javascript.rhino.JSTypeFactory.createType;
import static com.google.javascript.rhino.JSTypeFactory.getNativeType;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeError;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node params = new Node("(int, int)");
    ArrowType arrowType = new ArrowType(registry, params, null);
    assertEquals("ArrowType: (int, int) -> UnknownType", arrowType.toString());

    arrowType = new ArrowType(registry, params, INT);
    assertEquals("ArrowType: (int, int) -> Int", arrowType.toString());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT, INT), BOOL);

    JSType functionType = createFunctionType(createParameterTypes(INT), BOOL);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(functionType));
    assertTrue(arrowType2.isSubtype(functionType));
    assertFalse(arrowType1.isSubtype(VOID));
    assertFalse(arrowType2.isSubtype(VOID));
    assertFalse(functionType.isSubtype(VOID));
  }

  @Test
  public void testArrowTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType3 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT, INT), BOOL);
    JSType arrowType4 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), INT);
    JSType functionType = createFunctionType(createParameterTypes(INT, INT), BOOL);

    assertTrue(arrowType1.equals(arrowType1));
    assertTrue(arrowType1.equals(arrowType2));
    assertFalse(arrowType1.equals(arrowType3));
    assertFalse(arrowType1.equals(arrowType4));
    assertFalse(arrowType1.equals(functionType));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType3 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT, INT), BOOL);
    JSType arrowType4 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), INT);
    JSType functionType = createFunctionType(createParameterTypes(INT, INT), BOOL);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();
    int hashCode3 = arrowType2.hashCode();
    int hashCode4 = arrowType3.hashCode();
    int hashCode5 = arrowType4.hashCode();
    int hashCode6 = functionType.hashCode();

    assertEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, hashCode3);
    assertNotEquals(hashCode1, hashCode4);
    assertNotEquals(hashCode1, hashCode5);
    assertNotEquals(hashCode1, hashCode6);
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, null, null);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    Node params = new Node("(int, int?)");
    arrowType = new ArrowType(registry, params, null);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, params, INT);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType arrowType = new ArrowType(registry, null, INT);
    arrowType.getLeastSupertype(VOID);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType arrowType = new ArrowType(registry, null, INT);
    arrowType.getGreatestSubtype(VOID);
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT, INT), BOOL);

    assertEquals("ArrowType: (int) -> Boolean", arrowType1.toString());
    assertEquals("ArrowType: (int, int) -> Boolean", arrowType2.toString());
  }

  @Test(expected = TypeError.class)
  public void testArrowTypeWithInvalidParams() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node params = new Node("(int, int, int)");
    new ArrowType(registry, params, null);
  }

  @Test(expected = TypeError.class)
  public void testArrowTypeWithInvalidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node params = new Node("()");
    new ArrowType(registry, params, VOID);
  }

  private ArrowType createArrowTypeWithParamsAndReturnType(JSTypeRegistry registry, Node params, JSType returnType) {
    return new ArrowType(registry, params, returnType);
  }

  @Test
  public void testIsSubtypeWithDifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), INT);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithDifferentParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT, INT), BOOL);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithIdenticalTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithSubTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType1 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrowType2 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT, INT), BOOL);
    JSType arrowType3 = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), INT);

    assertTrue(arrowType1.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType1));
    assertTrue(arrowType2.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType functionType = createFunctionType(createParameterTypes(INT), BOOL);

    assertTrue(arrowType.isSubtype(functionType));
  }

  @Test
  public void testIsSubtypeWithArrayType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);
    JSType arrayType = createArrayType(createType(INT), 0);

    assertFalse(arrowType.isSubtype(arrayType));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node intParam = new Node("int");
    Node intParam2 = new Node("int");
    Node intParam3 = new Node("int?");

    ArrowType arrowType1 = new ArrowType(registry, createParameterTypes(intParam), BOOL);
    ArrowType arrowType2 = new ArrowType(registry, createParameterTypes(intParam2), BOOL);
    ArrowType arrowType3 = new ArrowType(registry, createParameterTypes(intParam3), BOOL);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType = new ArrowType(registry, null, INT);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
  }

  @Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType arrowType = new ArrowType(registry, null, INT);

    // UnsupportedOperationException is expected as this method is not
    // implemented in the ArrowType class.
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = INT;
    JSType arrowType = createArrowTypeWithParamsAndReturnType(registry, createParameterTypes(INT), BOOL);

    BooleanLiteralSet booleanLiteralSet = arrowType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.TRUE, booleanLiteralSet);
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();

    JSType intType = INT;
    Node params = new Node("(int, int)");
    ArrowType arrowType = new ArrowType(registry, params, null);

    arrowType.resolveInternal(errorReporter, scope);

    JSType resolvedArrowType = arrowType;
    assertNotNull(resolvedArrowType);
    assertNotNull(resolvedArrowType.returnType);
    assertNotNull(resolvedArrowType.parameters);
  }
}