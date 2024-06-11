import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeVisitor;
import com.google.javascript.rhino.testing.Assertions;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.BeforeClass
  public static void setUpBeforeClass() {
    ErrorReporter.setDefaultErrorReporter(new ErrorReporter() {
      @Override
      public void error(Node node, String msg, Object data) {
        fail("Error in test: " + msg);
      }

      @Override
      public void warning(Node node, String msg, Object data) {
        // Ignore
      }
    });
  }

  @org.junit.Test
  public void testArrowTypeCreation() {
    Node paramsNode = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    JSType returnType = registry.createStringType();

    ArrowType arrowType1 = new ArrowType(registry, paramsNode, returnType);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, returnType, true);

    assertEquals(arrowType1.getJSType(), arrowType1.getJSType());
    assertEquals(arrowType1.getJSType(), arrowType2.getJSType());
  }

  @org.junit.Test
  public void testArrowTypeIsSubtype() {
    JSType returnType1 = registry.createStringType();
    JSType returnType2 = registry.createNumberType();

    Node paramsNode1 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    Node paramsNode2 = registry.createParametersWithVarArgs(registry.createNamedType("String"));

    ArrowType arrowType11 = new ArrowType(registry, paramsNode1, returnType1);
    ArrowType arrowType12 = new ArrowType(registry, paramsNode2, returnType1);
    ArrowType arrowType21 = new ArrowType(registry, paramsNode1, returnType2);
    ArrowType arrowType22 = new ArrowType(registry, paramsNode2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, null, UNKNOWN_TYPE);

    // ArrowType with same parameters and return type is subtype of itself
    assertTrue(arrowType11.isSubtype(arrowType11));

    // ArrowType with same parameters and different return type is not subtype of itself
    assertFalse(arrowType11.isSubtype(arrowType12));

    // ArrowType with different parameters but same return type is not subtype of itself
    assertFalse(arrowType11.isSubtype(arrowType21));

    // ArrowType with same parameters and inferred return type is subtype of itself
    assertTrue(arrowType12.isSubtype(arrowType12));

    // ArrowType with different parameters and inferred return type is not subtype of itself
    assertFalse(arrowType12.isSubtype(arrowType22));

    // ArrowType with any parameters and UNKNOWN_TYPE return type is a subtype of any other ArrowType
    assertTrue(arrowType3.isSubtype(arrowType11));
    assertTrue(arrowType3.isSubtype(arrowType12));
    assertTrue(arrowType3.isSubtype(arrowType21));
    assertTrue(arrowType3.isSubtype(arrowType22));

    // ArrowType with UNKNOWN_TYPE return type is not a subtype of ArrowType with a specific return type
    assertFalse(arrowType11.isSubtype(arrowType3));
    assertFalse(arrowType12.isSubtype(arrowType3));
    assertFalse(arrowType21.isSubtype(arrowType3));
    assertFalse(arrowType22.isSubtype(arrowType3));

    // ArrowType with inferred return type is a subtype of ArrowType with a specific return type
    assertTrue(arrowType12.isSubtype(arrowType11));
    assertTrue(arrowType22.isSubtype(arrowType21));

    // ArrowType with a specific return type is not a subtype of ArrowType with inferred return type
    assertFalse(arrowType11.isSubtype(arrowType12));
    assertFalse(arrowType21.isSubtype(arrowType22));
  }

  @org.junit.Test
  public void testArrowTypeHasEqualParameters() {
    Node paramsNode1 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    Node paramsNode2 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    Node paramsNode3 = registry.createParametersWithVarArgs(registry.createNamedType("String"));

    ArrowType arrowType11 = new ArrowType(registry, paramsNode1, registry.createStringType());
    ArrowType arrowType12 = new ArrowType(registry, paramsNode1, registry.createNumberType());
    ArrowType arrowType21 = new ArrowType(registry, paramsNode2, registry.createStringType());
    ArrowType arrowType22 = new ArrowType(registry, paramsNode3, registry.createStringType());

    assertTrue(arrowType11.hasEqualParameters(arrowType11));
    assertFalse(arrowType11.hasEqualParameters(arrowType12));
    assertFalse(arrowType11.hasEqualParameters(arrowType21));
    assertFalse(arrowType11.hasEqualParameters(arrowType22));
    assertTrue(arrowType12.hasEqualParameters(arrowType12));
    assertFalse(arrowType12.hasEqualParameters(arrowType11));
    assertFalse(arrowType12.hasEqualParameters(arrowType21));
    assertFalse(arrowType12.hasEqualParameters(arrowType22));
    assertTrue(arrowType21.hasEqualParameters(arrowType21));
    assertFalse(arrowType21.hasEqualParameters(arrowType11));
    assertFalse(arrowType21.hasEqualParameters(arrowType12));
    assertFalse(arrowType21.hasEqualParameters(arrowType22));
    assertTrue(arrowType22.hasEqualParameters(arrowType22));
    assertFalse(arrowType22.hasEqualParameters(arrowType11));
    assertFalse(arrowType22.hasEqualParameters(arrowType12));
    assertFalse(arrowType22.hasEqualParameters(arrowType21));
  }

  @org.junit.Test
  public void testArrowTypeEquivalence() {
    Node paramsNode1 = registry.createParametersWithVarArgs(registry.createNamedType("Number"));
    Node paramsNode2 = registry.createParametersWithVarArgs(registry.createNamedType("String"));

    ArrowType arrowType11 = new ArrowType(registry, paramsNode1, registry.createStringType());
    ArrowType arrowType12 = new ArrowType(registry, paramsNode1, registry.createNumberType());
    ArrowType arrowType21 = new ArrowType(registry, paramsNode2, registry.createStringType());
    ArrowType arrowType22 = new ArrowType(registry, paramsNode2, registry.createNumberType());

    assertEquals(arrowType11, arrowType11);
    assertEquals(arrowType11.hashCode(), arrowType11.hashCode());
    assertNotEquals(arrowType11, arrowType12);
    assertNotEquals(arrowType11.hashCode(), arrowType12.hashCode());
    assertNotEquals(arrowType11, arrowType21);
    assertNotEquals(arrowType11.hashCode(), arrowType21.hashCode());
    assertNotEquals(arrowType11, arrowType22);
    assertNotEquals(arrowType11.hashCode(), arrowType22.hashCode());
    assertEquals(arrowType12, arrowType12);
    assertEquals(arrowType12.hashCode(), arrowType12.hashCode());
    assertNotEquals(arrowType12, arrowType11);
    assertNotEquals(arrowType12.hashCode(), arrowType11.hashCode());
    assertNotEquals(arrowType12, arrowType21);
    assertNotEquals(arrowType12.hashCode(), arrowType21.hashCode());
    assertNotEquals(arrowType12, arrowType22);
    assertNotEquals(arrowType12.hashCode(), arrowType22.hashCode());
    assertEquals(arrowType21, arrowType21);
    assertEquals(arrowType21.hashCode(), arrowType21.hashCode());
    assertNotEquals(arrowType21, arrowType11);
    assertNotEquals(arrowType21.hashCode(), arrowType11.hashCode());
    assertNotEquals(arrowType21, arrowType12);
    assertNotEquals(arrowType21.hashCode(), arrowType12.hashCode());
    assertNotEquals(arrowType21, arrowType22);
    assertNotEquals(arrowType21.hashCode(), arrowType22.hashCode());
    assertEquals(arrowType22, arrowType22);
    assertEquals(arrowType22.hashCode(), arrowType22.hashCode());
    assertNotEquals(arrowType22, arrowType11);
    assertNotEquals(arrowType22.hashCode(), arrowType11.hashCode());
    assertNotEquals(arrowType22, arrowType12);
    assertNotEquals(arrowType22.hashCode(), arrowType12.hashCode());
    assertNotEquals(arrowType22, arrowType21);
    assertNotEquals(arrowType22.hashCode(), arrowType21.hashCode());
  }

  @org.junit.Test
  public void testArrowTypeToString() {
    Node paramsNode = registry.createParametersWithVarArgs(registry.createNamedType("Number"));

    ArrowType arrowType = new ArrowType(registry, paramsNode, registry.createStringType());

    String arrowTypeString = arrowType.toString();
    assertEquals("function(Number) => String", arrowTypeString);
  }

  @org.junit.Test
  public void testArrowTypeResolve() {
    Node paramsNode = registry.createParametersWithVarArgs(registry.createNamedType("Number"));

    JSType returnType = registry.createStringType();

    ArrowType arrowType1 = new ArrowType(registry, paramsNode, returnType);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, returnType, true);

    JSType resolvedArrowType1 = arrowType1.resolve(null, null);
    JSType resolvedArrowType2 = arrowType2.resolve(null, null);

    assertEquals(resolvedArrowType1, resolvedArrowType1);
    assertEquals(resolvedArrowType1.getJSType(), arrowType1.getJSType());
    assertEquals(resolvedArrowType1.getJSType().getClassName(), "ArrowType");
    assertEquals(resolvedArrowType1.toString(), arrowType1.toString());

    assertEquals(resolvedArrowType2, resolvedArrowType2);
    assertEquals(resolvedArrowType2.getJSType(), arrowType2.getJSType());
    assertEquals(resolvedArrowType2.getJSType().getClassName(), "ArrowType");
    assertEquals(resolvedArrowType2.toString(), arrowType2.toString());

    // Test with unknown types
    ArrowType arrowType3 = new ArrowType(registry, null, UNKNOWN_TYPE);

    JSType resolvedArrowType3 = arrowType3.resolve(null, null);

    assertNull(resolvedArrowType3);
    assertNull(resolvedArrowType3.getJSType());
    assertNull(resolvedArrowType3.toString());
  }

  @org.junit.Test
  public void testArrowTypeUnknownParamsOrReturn() {
    Node paramsNode = registry.createParametersWithVarArgs(registry.createNamedType("Number"));

    ArrowType arrowType = new ArrowType(registry, paramsNode, registry.createStringType());

    boolean hasUnknownParamsOrReturn = arrowType.hasUnknownParamsOrReturn();

    assertTrue(hasUnknownParamsOrReturn);

    ArrowType arrowTypeWithKnownReturn = new ArrowType(registry, paramsNode, registry.createNumberType());

    boolean hasUnknownParamsOrReturn2 = arrowTypeWithKnownReturn.hasUnknownParamsOrReturn();

    assertFalse(hasUnknownParamsOrReturn2);

    ArrowType arrowTypeWithUnknownParams =
        new ArrowType(registry, registry.createParameters(registry.createNamedType("String")), registry.createStringType());

    boolean hasUnknownParamsOrReturn3 = arrowTypeWithUnknownParams.hasUnknownParamsOrReturn();

    assertTrue(hasUnknownParamsOrReturn3);
  }

  @org.junit.Test
  public void testArrowTypeToStringForAnnotations() {
    Node paramsNode = registry.createParametersWithVarArgs(registry.createNamedType("Number"));

    ArrowType arrowType = new ArrowType(registry, paramsNode, registry.createStringType());

    String arrowTypeStringForAnnotations = arrowType.toStringForAnnotations();

    assertEquals("function(Number) => String", arrowTypeStringForAnnotations);
  }

  @org.junit.Test
  public void testArrowTypeVisit() {
    Node paramsNode = registry.createParametersWithVarArgs(registry.createNamedType("Number"));

    ArrowType arrowType = new ArrowType(registry, paramsNode, registry.createStringType());

    JSTypeVisitor visitor = new JSTypeVisitor() {
      @Override
      public void visitArrowType(ArrowType arrowType) {
        assertEquals("ArrowType", arrowType.getClass().getSimpleName());
        assertEquals(paramsNode, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertEquals(arrowType.returnTypeInferred, arrowType.returnTypeInferred);
      }
    };

    arrowType.accept(visitor);
  }
}