package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private Node dummyNode;
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    dummyNode = mock(Node.class);
    errorReporter = mock(ErrorReporter.class);
    registry = mock(JSTypeRegistry.class);
    scope = mock(StaticScope.class);
  }

  @Test
  public void testIsSubtype_sameType() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(UNKNOWN_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(UNKNOWN_TYPE), false);

    assertTrue(aType.isSubtype(bType));
  }

  @Test
  public void testIsSubtype_differentReturnTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(STRING_TYPE), false);

    assertFalse(aType.isSubtype(bType));
  }

  @Test
  public void testIsSubtype_differentParameterTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), null);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(STRING_TYPE), null);

    assertFalse(aType.isSubtype(bType));
  }

  @Test
  public void testIsSubtype_sameReturnTypeWithOptionalParameter() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), true);

    assertTrue(aType.isSubtype(bType));
  }

  @Test
  public void testIsSubtype_differentReturnTypeWithOptionalParameter() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(STRING_TYPE), true);

    assertFalse(aType.isSubtype(bType));
  }

  @Test
  public void testIsSubtype_varArgs() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, null, true);

    assertTrue(aType.isSubtype(bType));
  }

  @Test
  public void testIsSubtype_differentVarArgs() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(ARRAY_TYPE), true);

    assertFalse(aType.isSubtype(bType));
  }

  @Test
  public void testIsSubtype_inferredReturnType() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), true);

    assertTrue(aType.isSubtype(bType));
  }

  @Test
  public void testHasEqualParameters_sameTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), null);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), null);

    assertTrue(aType.hasEqualParameters(
        (ArrowType) bType, false));
  }

  @Test
  public void testHasEqualParameters_differentTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), null);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(STRING_TYPE), null);

    assertFalse(aType.hasEqualParameters(
        (ArrowType) bType, false));
  }

  @Test
  public void testHasEqualParameters_unknownTypes() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, null, true);

    assertTrue(aType.hasEqualParameters(
        (ArrowType) bType, true));
  }

  @Test
  public void testHasEqualParameters_optionalParameters() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), true);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), true);

    assertTrue(aType.hasEqualParameters(
        (ArrowType) bType, false));
  }

  @Test
  public void testHasEqualParameters_differentNumberOfParameters() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(ARRAY_TYPE), false);

    assertFalse(aType.hasEqualParameters(
        (ArrowType) bType, false));
  }

  @Test
  public void testHasEqualParameters_varArgs() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, null, true);

    assertTrue(aType.hasEqualParameters(
        (ArrowType) bType, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_sameTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);

    assertTrue(aType.checkArrowEquivalenceHelper(
        (ArrowType) bType, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_differentTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(STRING_TYPE), false);

    assertFalse(aType.checkArrowEquivalenceHelper(
        (ArrowType) bType, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_unknownTypes() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, null, true);

    assertTrue(aType.checkArrowEquivalenceHelper(
        (ArrowType) bType, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_optionalParameters() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), true);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), true);

    assertTrue(aType.checkArrowEquivalenceHelper(
        (ArrowType) bType, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_differentNumberOfParameters() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(ARRAY_TYPE), false);

    assertFalse(aType.checkArrowEquivalenceHelper(
        (ArrowType) bType, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_varArgs() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, null, true);

    assertTrue(aType.checkArrowEquivalenceHelper(
        (ArrowType) bType, false));
  }

  @Test
  public void testHashCode_sameTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);

    assertEquals(aType.hashCode(), bType.hashCode());
  }

  @Test
  public void testHashCode_differentTypes() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);
    JSType bType = createArrowType(registry, dummyNode, getNativeType(STRING_TYPE), false);

    assertNotEquals(aType.hashCode(), bType.hashCode());
  }

  @Test
  public void testHashCode_unknownTypes() {
    JSType aType = createArrowType(registry, dummyNode, null, false);
    JSType bType = createArrowType(registry, dummyNode, null, true);

    assertEquals(aType.hashCode(), bType.hashCode());
  }

  @Test
  public void testGetLeastSupertype_throwsException() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);

    assertThrows(UnsupportedOperationException.class, () -> aType.getLeastSupertype(getNativeType(STRING_TYPE)));
  }

  @Test
  public void testGetGreatestSubtype_throwsException() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);

    assertThrows(UnsupportedOperationException.class, () -> aType.getGreatestSubtype(getNativeType(STRING_TYPE)));
  }

  @Test
  public void testToStringHelper() {
    JSType aType = createArrowType(registry, dummyNode, getNativeType(INT_TYPE), false);

    assertEquals("[ArrowType]", aType.toStringHelper(false));
  }

  private JSType createArrowType(JSTypeRegistry registry, Node node, JSType returnType, boolean returnTypeInferred) {
    when(registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE)))
        .thenReturn(JSType.createNull());

    return new ArrowType(registry, node, returnType, returnTypeInferred);
  }

  private Node getDummyNode() {
    return dummyNode;
  }

  private JSType getNativeType(int type) {
    return JSTypeNative.getInstance().getType(type);
  }

  private static boolean assertTrue(boolean condition) {
    if (!condition) {
      throw new RuntimeException("Expected true but got false");
    }
    return true;
  }

  private static boolean assertFalse(boolean condition) {
    if (condition) {
      throw new RuntimeException("Expected false but got true");
    }
    return true;
  }

  private static void assertNull(JSType type) {
    if (type != null) {
      throw new RuntimeException("Expected null but got " + type);
    }
  }

  private static void assertThrows(Class<? extends Throwable> exceptionClass, Runnable runnable) {
    try {
      runnable.run();
    } catch (Throwable t) {
      if (exceptionClass.isInstance(t)) {
        return;
      }
      throw new RuntimeException("Expected " + exceptionClass + " but got " + t);
    }

    throw new RuntimeException("Expected " + exceptionClass + " but did not get it");
  }
}