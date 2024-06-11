package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, Object data) {
        throw new RuntimeException(message);
      }
    };
  }

  @Test
  public void testNewArrowType() {
    Node node = new Node("function(int, int) { }");
    JSType params = registry.createParametersWithVarArgs(node.getType("int"));
    ArrowType arrowType = new ArrowType(registry, params, JSTypeNative.INT);

    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(params, arrowType.parameters);
    assertEquals(JSTypeNative.INT, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testNewArrowTypeWithInferredReturnType() {
    Node node = new Node("function(int, int) { }");
    Node returnNode = new Node("int");
    Node params = new Node("function(int, int) { }").getParameters();
    ArrowType arrowType = new ArrowType(registry, params, null, true);

    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(params, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype() {
    Node node1 = new Node("function(int, int) { }");
    Node node2 = new Node("function(int) { }");
    Node varArgsNode = new Node("function(int, int...) { }");
    Node noReturnNode = new Node("function() { }");

    JSType params1 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node1.getTypeAt(0)));
    JSType params2 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node2.getTypeAt(0)));
    JSType paramsVarArgs = registry.createParametersWithVarArgs(
        registry.getTypeForNode(varArgsNode.getTypeAt(0)));
    ArrowType arrowType1 = new ArrowType(registry, params1, JSTypeNative.INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, null);
    ArrowType arrowTypeVarArgs = new ArrowType(registry, paramsVarArgs, null);
    ArrowType arrowTypeNoReturn = new ArrowType(registry, null, null);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    // contravariant, function(int, int) <= function(int)
    assertTrue(arrowType2.isSubtype(arrowType1));
    // contravariant, function(int, int...) <= function(int, int)
    assertTrue(arrowTypeVarArgs.isSubtype(arrowType1));
    // contravariant, function() <= function(int, int)
    assertTrue(arrowTypeNoReturn.isSubtype(arrowType1));
    // function(int, int) != function(int, int...)
    assertFalse(arrowType1.isSubtype(arrowTypeVarArgs));
    // function(int, int) != function()
    assertFalse(arrowType1.isSubtype(arrowTypeNoReturn));
  }

  @Test
  public void testHasEqualParameters() {
    Node node1 = new Node("function(int, int) { }");
    Node node2 = new Node("function(int, int) { }");
    Node node3 = new Node("function(int, int, int) { }");
    Node node4 = new Node("function(int, int...) { }");
    Node node5 = new Node("function() { }");

    JSType params1 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node1.getTypeAt(0)));
    JSType params2 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node2.getTypeAt(0)));
    JSType params3 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node3.getTypeAt(0)));
    JSType params4 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node4.getTypeAt(0)));
    JSType params5 = new JSTypeRegistry().getTypeForNode(node5.getTypeAt(0));

    ArrowType arrowType1 = new ArrowType(registry, params1, JSTypeNative.INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, JSTypeNative.INT);
    ArrowType arrowType3 = new ArrowType(registry, params3, JSTypeNative.INT);
    ArrowType arrowType4 = new ArrowType(registry, params4, JSTypeNative.INT);
    ArrowType arrowType5 = new ArrowType(registry, params5, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));
    assertFalse(arrowType1.hasEqualParameters(arrowType5));
  }

  @Test
  public void testIsEquivalentTo() {
    Node node1 = new Node("function(int, int) { }");
    Node node2 = new Node("function(int, int) { }");
    Node node3 = new Node("function(int, int, int) { }");
    Node node4 = new Node("function(int, int...) { }");
    Node node5 = new Node("function() { }");

    JSType params1 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node1.getTypeAt(0)));
    JSType params2 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node2.getTypeAt(0)));
    JSType params3 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node3.getTypeAt(0)));
    JSType params4 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node4.getTypeAt(0)));
    JSType params5 = new JSTypeRegistry().getTypeForNode(node5.getTypeAt(0));

    ArrowType arrowType1 = new ArrowType(registry, params1, JSTypeNative.INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, JSTypeNative.INT);
    ArrowType arrowType3 = new ArrowType(registry, params3, JSTypeNative.INT);
    ArrowType arrowType4 = new ArrowType(registry, params4, JSTypeNative.INT);
    ArrowType arrowType5 = new ArrowType(registry, params5, null);

    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType2);
    assertNotEquals(arrowType1, arrowType3);
    assertNotEquals(arrowType1, arrowType4);
    assertNotEquals(arrowType1, arrowType5);
    assertEquals(arrowType2, arrowType2);
    assertEquals(arrowType2, arrowType1);
    assertNotEquals(arrowType2, arrowType3);
    assertNotEquals(arrowType2, arrowType4);
    assertNotEquals(arrowType2, arrowType5);
    assertEquals(arrowType3, arrowType3);
    assertEquals(arrowType3, arrowType3);
    assertNotEquals(arrowType3, arrowType1);
    assertNotEquals(arrowType3, arrowType2);
    assertNotEquals(arrowType3, arrowType4);
    assertNotEquals(arrowType3, arrowType5);
    assertEquals(arrowType4, arrowType4);
    assertEquals(arrowType4, arrowType4);
    assertNotEquals(arrowType4, arrowType1);
    assertNotEquals(arrowType4, arrowType2);
    assertNotEquals(arrowType4, arrowType3);
    assertNotEquals(arrowType4, arrowType5);
    assertEquals(arrowType5, arrowType5);
    assertEquals(arrowType5, arrowType5);
    assertNotEquals(arrowType5, arrowType1);
    assertNotEquals(arrowType5, arrowType2);
    assertNotEquals(arrowType5, arrowType3);
    assertNotEquals(arrowType5, arrowType4);
  }

  @Test
  public void testHashCode() {
    Node node1 = new Node("function(int, int) { }");
    Node node2 = new Node("function(int, int) { }");
    Node node3 = new Node("function(int, int, int) { }");
    Node node4 = new Node("function(int, int...) { }");
    Node node5 = new Node("function() { }");

    JSType params1 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node1.getTypeAt(0)));
    JSType params2 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node2.getTypeAt(0)));
    JSType params3 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node3.getTypeAt(0)));
    JSType params4 = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node4.getTypeAt(0)));
    JSType params5 = new JSTypeRegistry().getTypeForNode(node5.getTypeAt(0));

    ArrowType arrowType1 = new ArrowType(registry, params1, JSTypeNative.INT);
    ArrowType arrowType2 = new ArrowType(registry, params2, JSTypeNative.INT);
    ArrowType arrowType3 = new ArrowType(registry, params3, JSTypeNative.INT);
    ArrowType arrowType4 = new ArrowType(registry, params4, JSTypeNative.INT);
    ArrowType arrowType5 = new ArrowType(registry, params5, null);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();
    int hashCode3 = arrowType2.hashCode();
    int hashCode4 = arrowType3.hashCode();
    int hashCode5 = arrowType4.hashCode();
    int hashCode6 = arrowType5.hashCode();

    assertEquals(hashCode1, hashCode1);
    assertEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, hashCode3);
    assertNotEquals(hashCode1, hashCode4);
    assertNotEquals(hashCode1, hashCode5);
    assertNotEquals(hashCode1, hashCode6);
    assertEquals(hashCode2, hashCode1);
    assertEquals(hashCode2, hashCode2);
    assertEquals(hashCode3, hashCode3);
    assertEquals(hashCode3, hashCode3);
    assertNotEquals(hashCode3, hashCode1);
    assertNotEquals(hashCode3, hashCode2);
    assertNotEquals(hashCode3, hashCode4);
    assertNotEquals(hashCode3, hashCode5);
    assertNotEquals(hashCode3, hashCode6);
    assertEquals(hashCode4, hashCode4);
    assertEquals(hashCode4, hashCode4);
    assertNotEquals(hashCode4, hashCode1);
    assertNotEquals(hashCode4, hashCode2);
    assertNotEquals(hashCode4, hashCode3);
    assertNotEquals(hashCode4, hashCode5);
    assertNotEquals(hashCode4, hashCode6);
    assertEquals(hashCode5, hashCode5);
    assertEquals(hashCode5, hashCode5);
    assertNotEquals(hashCode5, hashCode1);
    assertNotEquals(hashCode5, hashCode2);
    assertNotEquals(hashCode5, hashCode3);
    assertNotEquals(hashCode5, hashCode4);
    assertNotEquals(hashCode5, hashCode6);
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Test
  public void testTestForEquality() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Test
  public void testVisit() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Test
  public void testResolveInternal() {
    Node node = new Node("function(int, int) { }");
    JSType params = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node.getTypeAt(0)));
    ArrowType arrowType = new ArrowType(registry, params, JSTypeNative.INT);
    StaticScope<JSType> scope = new StaticScope<JSType>();

    // Test with no errors
    arrowType.resolveInternal(errorReporter, scope);
    assertEquals(JSTypeNative.INT, arrowType.returnType);

    // Test with error
    JSType badType = new Object();
    arrowType.returnType = badType;
    try {
      arrowType.resolveInternal(errorReporter, scope);
      fail("Expected error.");
    } catch (RuntimeException e) {
      // Expected
    }
    assertSame(badType, arrowType.returnType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node node = new Node("function(int, int) { }");
    JSType params = registry.createParametersWithVarArgs(
        registry.getTypeForNode(node.getTypeAt(0)));
    ArrowType arrowType = new ArrowType(registry, params, JSTypeNative.INT);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    arrowType.parameters = null;
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType.returnType = null;
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType.returnType = JSTypeNative.OBJECT;
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType.returnType = JSTypeNative.UNDEFINED;
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType.parameters = new JSTypeRegistry().createParametersWithVarArgs(
        JSTypeNative.ARRAY);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }
}