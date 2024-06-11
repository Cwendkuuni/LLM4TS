package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeVisitor;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TernaryValue;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private NodeFactory nodeFactory;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    nodeFactory = registry.getNodeFactory();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testIsSubtype_SameArrowType() {
    ArrowType type1 = new ArrowType(registry, null, null, false);
    ArrowType type2 = new ArrowType(registry, null, null, false);
    assertTrue(type1.isSubtype(type2));
    assertTrue(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtype_DifferentArrowTypes() {
    ArrowType type1 = new ArrowType(registry, null, registry.createInt(), false);
    ArrowType type2 = new ArrowType(registry, null, registry.createString(), false);
    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtype_SameArrowTypeWithDifferentReturnTypes() {
    ArrowType type1 = new ArrowType(registry, null, registry.createInt(), false);
    ArrowType type2 = new ArrowType(registry, null, registry.createString(), false);
    ArrowType subtype1 = new ArrowType(registry, null, registry.createString(), false);
    ArrowType subtype2 = new ArrowType(registry, null, registry.createInt(), false);

    assertTrue(type1.isSubtype(subtype1));
    assertFalse(type1.isSubtype(subtype2));

    assertTrue(subtype1.isSubtype(type1));
    assertTrue(subtype2.isSubtype(type1));

    assertFalse(subtype1.isSubtype(subtype2));
    assertFalse(subtype2.isSubtype(subtype1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithDifferentParameters() {
    Node param1 = nodeFactory.createIdentifier("param1");
    Node param2 = nodeFactory.createIdentifier("param2");

    ArrowType type1 = new ArrowType(registry, param1, null, false);
    ArrowType type2 = new ArrowType(registry, param2, null, false);

    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithSameParametersButDifferentReturnTypes() {
    Node param = nodeFactory.createIdentifier("param");

    ArrowType type1 = new ArrowType(registry, param, registry.createInt(), false);
    ArrowType type2 = new ArrowType(registry, param, registry.createString(), false);

    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithDifferentParametersButSameReturnType() {
    Node param1 = nodeFactory.createIdentifier("param1");
    Node param2 = nodeFactory.createIdentifier("param2");

    ArrowType type1 = new ArrowType(registry, param1, registry.createInt(), false);
    ArrowType type2 = new ArrowType(registry, param2, registry.createInt(), false);

    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithSameParametersButInferredReturnType() {
    Node param = nodeFactory.createIdentifier("param");

    ArrowType type1 = new ArrowType(registry, param, null, true);
    ArrowType type2 = new ArrowType(registry, param, registry.createInt(), false);

    assertTrue(type1.isSubtype(type2));
    assertTrue(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithInferredReturnTypeButDifferentParameters() {
    Node param1 = nodeFactory.createIdentifier("param1");
    Node param2 = nodeFactory.createIdentifier("param2");

    ArrowType type1 = new ArrowType(registry, param1, null, true);
    ArrowType type2 = new ArrowType(registry, param2, null, false);

    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithDifferentParametersAndDifferentReturnTypes() {
    Node param1 = nodeFactory.createIdentifier("param1");
    Node param2 = nodeFactory.createIdentifier("param2");

    ArrowType type1 = new ArrowType(registry, param1, registry.createInt(), false);
    ArrowType type2 = new ArrowType(registry, param2, registry.createString(), false);

    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testHasEqualParameters() {
    Node param1 = nodeFactory.createIdentifier("param1");
    Node param2 = nodeFactory.createIdentifier("param2");

    ArrowType type1 = new ArrowType(registry, param1, null, false);
    ArrowType type2 = new ArrowType(registry, param2, null, false);
    ArrowType type3 = new ArrowType(registry, param1, null, false);
    ArrowType type4 = new ArrowType(registry, param2, null, false);
    ArrowType type5 = new ArrowType(registry, param1, null, false);
    ArrowType type6 = new ArrowType(registry, null, null, false);

    assertFalse(type1.hasEqualParameters(type2));
    assertFalse(type2.hasEqualParameters(type1));

    assertTrue(type1.hasEqualParameters(type3));
    assertTrue(type3.hasEqualParameters(type1));

    assertFalse(type1.hasEqualParameters(type4));
    assertFalse(type4.hasEqualParameters(type1));

    assertTrue(type1.hasEqualParameters(type5));
    assertTrue(type5.hasEqualParameters(type1));

    assertFalse(type1.hasEqualParameters(type6));
    assertFalse(type6.hasEqualParameters(type1));
  }

  @Test
  public void testToString() {
    Node param = nodeFactory.createIdentifier("param");

    ArrowType type1 = new ArrowType(registry, param, null, false);
    ArrowType type2 = new ArrowType(registry, param, registry.createInt(), false);

    String type1String = type1.toStringHelper(false);
    String type2String = type2.toStringHelper(false);

    assertTrue(type1String.contains("ArrowType"));
    assertTrue(type1String.contains("FunctionType"));
    assertTrue(type1String.contains("parameters"));
    assertTrue(type1String.contains("returnType"));
    assertTrue(type1String.contains("returnTypeInferred"));

    assertTrue(type2String.contains("ArrowType"));
    assertTrue(type2String.contains("FunctionType"));
    assertTrue(type2String.contains("parameters"));
    assertTrue(type2String.contains("returnType"));
    assertTrue(type2String.contains("returnTypeInferred"));
    assertTrue(type2String.contains("int"));
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testVisit() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testResolveInternal() {
    Node param = nodeFactory.createIdentifier("param");
    ArrowType type = new ArrowType(registry, param, null, false);

    JSType resolvedType = type.resolveInternal(errorReporter, new StaticScope<JSType>(registry));

    if (resolvedType instanceof ArrowType) {
      ArrowType resolvedArrowType = (ArrowType) resolvedType;
      if (resolvedArrowType.parameters != null) {
        assertEquals(param, resolvedArrowType.parameters.getFirstChild());
      }
      assertEquals(UNKNOWN_TYPE, resolvedArrowType.returnType);
    } else {
      fail("Expected ArrowType, but got " + resolvedType.getClass().getName());
    }
  }

  @Test
  public void testResolveInternal_WithKnownTypes() {
    Node param = nodeFactory.createIdentifier("param");
    Node intType = registry.createInt();
    Node stringType = registry.createString();

    ArrowType intTypeArrow = new ArrowType(registry, param, intType, false);
    ArrowType stringTypeArrow = new ArrowType(registry, param, stringType, false);

    JSType intTypeResolved = intTypeArrow.resolveInternal(errorReporter, new StaticScope<JSType>(registry));
    JSType stringTypeResolved = stringTypeArrow.resolveInternal(errorReporter, new StaticScope<JSType>(registry));

    if (intTypeResolved instanceof ArrowType) {
      ArrowType intTypeArrowResolved = (ArrowType) intTypeResolved;
      assertEquals(intType, intTypeArrowResolved.returnType);
    } else {
      fail("Expected ArrowType, but got " + intTypeResolved.getClass().getName());
    }

    if (stringTypeResolved instanceof ArrowType) {
      ArrowType stringTypeArrowResolved = (ArrowType) stringTypeResolved;
      assertEquals(stringType, stringTypeArrowResolved.returnType);
    } else {
      fail("Expected ArrowType, but got " + stringTypeResolved.getClass().getName());
    }
  }

  @Test
  public void testResolveInternal_WithUnknownTypes() {
    Node param = nodeFactory.createIdentifier("param");

    ArrowType unknownTypeArrow = new ArrowType(registry, param, null, false);

    JSType unknownTypeResolved = unknownTypeArrow.resolveInternal(errorReporter, new StaticScope<JSType>(registry));

    if (unknownTypeResolved instanceof ArrowType) {
      ArrowType unknownTypeArrowResolved = (ArrowType) unknownTypeResolved;
      assertEquals(UNKNOWN_TYPE, unknownTypeArrowResolved.returnType);
    } else {
      fail("Expected ArrowType, but got " + unknownTypeResolved.getClass().getName());
    }
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node param = nodeFactory.createIdentifier("param");

    ArrowType unknownReturnTypeArrow = new ArrowType(registry, null, null, false);
    ArrowType unknownParamsAndReturnArrow = new ArrowType(registry, null, null, false);
    ArrowType knownParamsAndUnknownReturnArrow = new ArrowType(registry, param, null, false);
    ArrowType knownParamsAndKnownReturnArrow = new ArrowType(registry, param, registry.createInt(), false);

    assertTrue(unknownReturnTypeArrow.hasUnknownParamsOrReturn());
    assertTrue(unknownParamsAndReturnArrow.hasUnknownParamsOrReturn());
    assertFalse(knownParamsAndUnknownReturnArrow.hasUnknownParamsOrReturn());
    assertFalse(knownParamsAndKnownReturnArrow.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToString() {
    Node param = nodeFactory.createIdentifier("param");

    ArrowType type1 = new ArrowType(registry, param, null, false);
    ArrowType type2 = new ArrowType(registry, param, registry.createInt(), false);

    String type1String = type1.toStringHelper(false);
    String type2String = type2.toStringHelper(false);

    assertTrue(type1String.contains("ArrowType"));
    assertTrue(type1String.contains("FunctionType"));
    assertTrue(type1String.contains("parameters"));
    assertTrue(type1String.contains("returnType"));
    assertTrue(type1String.contains("returnTypeInferred"));

    assertTrue(type2String.contains("ArrowType"));
    assertTrue(type2String.contains("FunctionType"));
    assertTrue(type2String.contains("parameters"));
    assertTrue(type2String.contains("returnType"));
    assertTrue(type2String.contains("returnTypeInferred"));
    assertTrue(type2String.contains("int"));
  }

  @Test
  public void testEquals() {
    Node param1 = nodeFactory.createIdentifier("param1");
    Node param2 = nodeFactory.createIdentifier("param2");

    ArrowType type1 = new ArrowType(registry, param1, null, false);
    ArrowType type2 = new ArrowType(registry, param2, null, false);
    ArrowType type3 = new ArrowType(registry, param1, null, false);

    assertFalse(type1.equals(null));
    assertFalse(type1.equals(""));
    assertFalse(type1.equals(type2));
    assertTrue(type1.equals(type3));
  }

  @Test
  public void testHashCode() {
    Node param = nodeFactory.createIdentifier("param");
    Node intType = registry.createInt();

    ArrowType type1 = new ArrowType(registry, param, intType, false);
    ArrowType type2 = new ArrowType(registry, param, intType, false);
    ArrowType type3 = new ArrowType(registry, null, intType, false);

    int hashCode1 = type1.hashCode();
    int hashCode2 = type2.hashCode();
    int hashCode3 = type3.hashCode();

    assertEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, hashCode3);
  }

  @Test
  public void testVisit_UsesGenericType() {
    Node param = nodeFactory.createIdentifier("param");
    JSTypeVisitor visitor = new JSTypeVisitor() {
      @Override
      public <T> T visitArrowType(ArrowType arrowType, JSTypeVisitor<T> childVisitor) {
        assertEquals(param, arrowType.parameters.getFirstChild());
        assertEquals(registry.createInt(), arrowType.returnType);
        return null;
      }
    };

    ArrowType arrowType = new ArrowType(registry, param, null, false);
    visitor.visit(arrowType);
  }
}