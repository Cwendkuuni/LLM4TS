package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, NodeOperations.createList(), UNKNOWN_TYPE);
    ArrowType arrowType3 = new ArrowType(registry, NodeOperations.createList(
        NodeOperations.createList(JSTypeNative.INT_TYPE),
        NodeOperations.createList(JSTypeNative.STRING_TYPE)),
        JSTypeNative.STRING_TYPE, true);

    assertSame(arrowType1.getClass(), ArrowType.class);
    assertSame(arrowType2.getClass(), ArrowType.class);
    assertSame(arrowType3.getClass(), ArrowType.class);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();
    JSType voidType = registry.getVoidType();

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, null, stringType);
    ArrowType arrowType3 = new ArrowType(registry, null, voidType);
    ArrowType arrowType4 = new ArrowType(registry, NodeOperations.createList(intType), stringType);
    ArrowType arrowType5 = new ArrowType(registry, NodeOperations.createList(intType, intType), voidType);
    ArrowType arrowType6 = new ArrowType(registry, NodeOperations.createList(), intType);

    assertTrue(arrowType1.isSubtype(arrowType4));
    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType5));
    assertTrue(arrowType1.isSubtype(arrowType6));

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, NodeOperations.createList(intType), intType);
    ArrowType arrowType3 = new ArrowType(registry, NodeOperations.createList(intType), stringType);
    ArrowType arrowType4 = new ArrowType(registry, NodeOperations.createList(intType), null);

    assertTrue(arrowType1.hasEqualParameters(arrowType1, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType4, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, NodeOperations.createList(intType), intType);
    ArrowType arrowType3 = new ArrowType(registry, NodeOperations.createList(intType), stringType);
    ArrowType arrowType4 = new ArrowType(registry, NodeOperations.createList(intType), null);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType1, true));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, true));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType4, true));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, NodeOperations.createList(intType), intType);
    ArrowType arrowType3 = new ArrowType(registry, NodeOperations.createList(intType), stringType);
    ArrowType arrowType4 = new ArrowType(registry, NodeOperations.createList(intType), null);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();
    int hashCode4 = arrowType4.hashCode();

    ArrowType arrowType5 = new ArrowType(registry, NodeOperations.createList(intType), intType);
    int hashCode5 = arrowType5.hashCode();

    assertEquals(hashCode1, arrowType1.hashCode());
    assertEquals(hashCode2, arrowType2.hashCode());
    assertEquals(hashCode3, arrowType3.hashCode());
    assertEquals(hashCode4, arrowType4.hashCode());
    assertEquals(hashCode1, arrowType5.hashCode());
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();
    JSType voidType = registry.getVoidType();

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, NodeOperations.createList(intType), intType);
    ArrowType arrowType3 = new ArrowType(registry, NodeOperations.createList(intType), stringType);
    ArrowType arrowType4 = new ArrowType(registry, NodeOperations.createList(), intType);

    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int pos) {
        throw new RuntimeException(message);
      }

      @Override
      public void warning(String message, int line, int pos) {
        // Ignored
      }
    };

    StaticScope<JSType> scope = new StaticScope<>(registry);

    arrowType1.resolveInternal(errorReporter, scope);
    arrowType2.resolveInternal(errorReporter, scope);
    arrowType3.resolveInternal(errorReporter, scope);
    arrowType4.resolveInternal(errorReporter, scope);

    assertNull(arrowType1.returnType);
    assertEquals(intType, arrowType1.parameters.getFirstChild().getJSType());
    assertEquals(intType, arrowType2.returnType);
    assertEquals(intType, arrowType2.parameters.getFirstChild().getJSType());
    assertNull(arrowType3.returnType);
    assertEquals(intType, arrowType3.parameters.getFirstChild().getJSType());
    assertEquals(stringType, arrowType3.parameters.get(1).getJSType());
    assertNull(arrowType4.returnType);
    assertEquals(intType, arrowType4.parameters.getFirstChild().getJSType());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();
    JSType voidType = registry.getVoidType();

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, NodeOperations.createList(intType), intType);
    ArrowType arrowType3 = new ArrowType(registry, NodeOperations.createList(intType), stringType);
    ArrowType arrowType4 = new ArrowType(registry, NodeOperations.createList(intType), null);

    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertFalse(arrowType2.hasUnknownParamsOrReturn());
    assertTrue(arrowType3.hasUnknownParamsOrReturn());
    assertFalse(arrowType4.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();

    ArrowType arrowType1 = new ArrowType(registry, null, intType);
    ArrowType arrowType2 = new ArrowType(registry, NodeOperations.createList(intType), intType);
    ArrowType arrowType3 = new ArrowType(registry, NodeOperations.createList(intType), stringType);

    assertEquals("[ArrowType]", arrowType1.toStringHelper(false));
    assertEquals("[ArrowType]", arrowType2.toStringHelper(false));
    assertEquals("[ArrowType]", arrowType3.toStringHelper(false));
  }
}