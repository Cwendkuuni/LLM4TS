package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeVisitor;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype_SameType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    Node returnNode = registry.createName("Number");
    ArrowType arrowType1 = new ArrowType(registry, paramsNode, returnNode);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, returnNode);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    Node returnNode1 = registry.createName("Number");
    Node returnNode2 = registry.createName("String");
    ArrowType arrowType1 = new ArrowType(registry, paramsNode, returnNode1);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, returnNode2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode1 = registry.createParametersWithVarArgs(
        registry.createParameters( registry.createName("Number"),
            registry.createName("String") ) );
    Node paramsNode2 = registry.createParametersWithVarArgs(
        registry.createParameters( registry.createName("String"),
            registry.createName("Number") ) );
    Node returnNode = registry.createName("Number");

    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, returnNode);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, returnNode);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_SingleParameter() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode1 = registry.createParametersWithVarArgs(null);
    Node paramsNode2 = registry.createParameters(registry.createName("Number"));
    Node returnNode = registry.createName("Number");

    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, returnNode);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, returnNode);

    // ArrowType with variable arguments is a supertype of ArrowType with a fixed parameter
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_SameParametersDifferentReturnInferred() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(null);
    Node returnNode1 = registry.createName("Number");
    Node returnNode2 = registry.createName("void");

    ArrowType arrowType1 = new ArrowType(registry, paramsNode, returnNode1, false);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, returnNode2, true);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentParametersDifferentReturnInferred() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode1 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("Number"),
            registry.createName("String")) );
    Node paramsNode2 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("String"),
            registry.createName("Number")) );
    Node returnNode1 = registry.createName("Number");
    Node returnNode2 = registry.createName("void");

    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, returnNode1, false);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, returnNode2, true);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode1 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("Number"),
            registry.createName("String")) );
    Node paramsNode2 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("String"),
            registry.createName("Number")) );
    Node paramsNode3 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("Number"),
            registry.createName("Number")) );
    Node paramsNode4 = registry.createParametersWithVarArgs(null);

    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType3 = new ArrowType(registry, paramsNode3, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType4 = new ArrowType(registry, paramsNode4, getNativeType(UNKNOWN_TYPE));

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertTrue(arrowType1.hasEqualParameters(arrowType4));
    assertTrue(arrowType2.hasEqualParameters(arrowType4));
  }

  @Test
  public void testEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode1 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("Number"),
            registry.createName("String")) );
    Node paramsNode2 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("String"),
            registry.createName("Number")) );
    Node returnNode1 = registry.createName("Number");
    Node returnNode2 = registry.createName("void");

    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, returnNode1, false);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, returnNode1, false);
    ArrowType arrowType3 = new ArrowType(registry, paramsNode1, returnNode2, false);
    ArrowType arrowType4 = new ArrowType(registry, paramsNode2, returnNode2, false);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
    assertFalse(arrowType1.isEquivalentTo(arrowType4));
    assertFalse(arrowType2.isEquivalentTo(arrowType3));
    assertFalse(arrowType2.isEquivalentTo(arrowType4));
    assertFalse(arrowType3.isEquivalentTo(arrowType4));

    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType6 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));

    assertTrue(arrowType5.isEquivalentTo(arrowType6));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode1 = registry.createParametersWithVarArgs(null);
    Node returnNode1 = registry.createName("Number");
    Node paramsNode2 = registry.createParametersWithVarArgs(
        registry.createParameters(registry.createName("Number"),
            registry.createName("Number")) );
    Node returnNode2 = registry.createName("String");

    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, returnNode1);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, returnNode1);
    ArrowType arrowType3 = new ArrowType(registry, paramsNode1, returnNode2);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();

    assertEquals(hashCode1, hashCode2);

    int hashCode3 = arrowType2.hashCode();
    int hashCode4 = arrowType3.hashCode();

    assertNotEquals(hashCode1, hashCode3);
    assertNotEquals(hashCode1, hashCode4);
    assertNotEquals(hashCode2, hashCode3);
    assertNotEquals(hashCode2, hashCode4);
    assertNotEquals(hashCode3, hashCode4);
  }

  @Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(null);
    Node returnNode = registry.createName("Number");

    ArrowType arrowType = new ArrowType(registry, paramsNode, returnNode);

    JSTypeVisitor visitor = new JSTypeVisitor() {
      @Override
      public FunctionType visitFunctionType(FunctionType type) {
        fail("Should not visit FunctionType");
      }

      @Override
      public ArrowType visitArrowType(ArrowType type) {
        assertEquals(arrowType.returnType, type.returnType);
        assertEquals(arrowType.returnTypeInferred, type.returnTypeInferred);
        assertEquals(arrowType.parameters, type.parameters);
        return null;
      }

      @Override
      public Object visitUnknownType(JSTypeNative unknownType) {
        fail("Should not visit UnknownType");
      }
    };

    arrowType.visit(visitor);
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(null);
    Node returnNode = registry.createName("Number");

    ArrowType arrowType = new ArrowType(registry, paramsNode, returnNode);

    assertEquals("(function (...args) { return Number; })", arrowType.toString());
  }

  @Test
  public void testToString_WithAnnotations() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(null);
    Node returnNode = registry.createName("Number");

    ArrowType arrowType = new ArrowType(registry, paramsNode, returnNode);

    assertEquals("(function (...args) { @annotation1 return Number; })",
        arrowType.toStringHelper(true).toString());
  }

  @Test
  public void testResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(null);
    Node returnNode = registry.createName("Number");

    ArrowType arrowType = new ArrowType(registry, paramsNode, returnNode);

    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int lineNumber, Node node) {
        fail("Error should not be thrown");
      }

      @Override
      public void warning(String message, int line, int lineNumber, Node node) {
        fail("Warning should not be thrown");
      }
    };

    JSType resolvedType = arrowType.resolve(errorReporter, null);

    assertEquals(arrowType, resolvedType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node paramsNode = registry.createParametersWithVarArgs(null);
    Node returnNode1 = registry.createName("Number");
    Node returnNode2 = registry.createName(JSTypeNative.UNKNOWN_TYPE.getName());

    ArrowType arrowType1 = new ArrowType(registry, paramsNode, returnNode1);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, returnNode2);

    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertFalse(arrowType2.hasUnknownParamsOrReturn());
  }
}