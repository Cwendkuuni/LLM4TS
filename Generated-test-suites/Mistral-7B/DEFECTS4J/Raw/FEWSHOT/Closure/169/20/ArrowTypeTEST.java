package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.testtype.TernaryValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrowTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private ArrowType arrowType1;
  private ArrowType arrowType2;
  private ArrowType arrowType3;
  private ArrowType arrowType4;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    registry = new JSTypeRegistry();

    arrowType1 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    arrowType2 = new ArrowType(registry, null, JSTypeNative.getNumberType(), false);
    arrowType3 = new ArrowType(registry, null, JSTypeNative.getStringType(), false);
    arrowType4 = new ArrowType(registry, null, null, false);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    // Test subtyping ArrowType with the same parameters and return type
    ArrowType arrowType5 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    assertTrue(arrowType1.isSubtype(arrowType5));

    // Test subtyping ArrowType with the same parameters but different return type
    ArrowType arrowType6 = new ArrowType(registry, null, JSTypeNative.getNumberType(), false);
    assertTrue(arrowType1.isSubtype(arrowType6));

    // Test subtyping ArrowType with different parameters but the same return type
    Node param1 = registry.createNamedParam("p1", JSTypeNative.getNumberType());
    Node param2 = registry.createNamedParam("p2", JSTypeNative.getStringType());
    ArrowType arrowType7 = new ArrowType(registry, param1, param2, false);
    JSType param1Type = param1.getJSType();
    JSType param2Type = param2.getJSType();

    assertTrue(arrowType1.isSubtype(arrowType7));
    assertTrue(arrowType7.isSubtype(arrowType1));

    // Test subtyping ArrowType with different parameters and different return type
    ArrowType arrowType8 = new ArrowType(registry, param1, JSTypeNative.getBooleanType(), false);
    assertFalse(arrowType1.isSubtype(arrowType8));

    // Test ArrowType that cannot be subtyped because it has a required parameter that the other does not have
    Node param1Required = registry.createNamedParam("p1", JSTypeNative.getNumberType(), true);
    ArrowType arrowType9 = new ArrowType(registry, param1Required, JSTypeNative.getBooleanType(), false);
    assertFalse(arrowType1.isSubtype(arrowType9));

    // Test ArrowType that cannot be subtyped because it has a vararg parameter that the other does not have
    Node param1Varargs = registry.createNamedParam("p1", JSTypeNative.getNumberType(), false, true);
    ArrowType arrowType10 = new ArrowType(registry, param1Varargs, JSTypeNative.getBooleanType(), false);
    assertFalse(arrowType1.isSubtype(arrowType10));
  }

  @Test
  public void testHasEqualParameters() {
    Node param1 = registry.createNamedParam("p1", JSTypeNative.getNumberType());
    Node param2 = registry.createNamedParam("p2", JSTypeNative.getStringType());

    ArrowType arrowType11 = new ArrowType(registry, param1, param2, false);
    ArrowType arrowType12 = new ArrowType(registry, param2, param1, false);
    ArrowType arrowType13 = new ArrowType(registry, param1, null, false);

    // Two ArrowTypes with the same parameters should have equal parameters
    assertTrue(arrowType11.hasEqualParameters(arrowType11, true));
    assertTrue(arrowType11.hasEqualParameters(arrowType11, false));

    // Two ArrowTypes with the same parameters in reverse order should have equal parameters
    assertTrue(arrowType11.hasEqualParameters(arrowType12, true));
    assertTrue(arrowType11.hasEqualParameters(arrowType12, false));

    // Two ArrowTypes with different parameters should not have equal parameters
    ArrowType arrowType14 = new ArrowType(registry, param1, JSTypeNative.getBooleanType(), false);
    assertFalse(arrowType11.hasEqualParameters(arrowType14, true));
    assertFalse(arrowType11.hasEqualParameters(arrowType14, false));

    // An ArrowType with no parameters should have equal parameters with itself
    assertTrue(arrowType4.hasEqualParameters(arrowType4, true));
    assertTrue(arrowType4.hasEqualParameters(arrowType4, false));

    // An ArrowType with no parameters should have equal parameters with an ArrowType with parameters
    assertTrue(arrowType4.hasEqualParameters(arrowType11, true));
    assertTrue(arrowType4.hasEqualParameters(arrowType11, false));

    // An ArrowType with parameters should not have equal parameters with an ArrowType with no parameters
    assertFalse(arrowType11.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType11.hasEqualParameters(arrowType4, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    Node param1 = registry.createNamedParam("p1", JSTypeNative.getNumberType());
    Node param2 = registry.createNamedParam("p2", JSTypeNative.getStringType());

    ArrowType arrowType15 = new ArrowType(registry, param1, param2, false);
    ArrowType arrowType16 = new ArrowType(registry, param2, param1, false);

    // Two ArrowTypes with the same parameters and return type should have equal arrow type
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType15, true));
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType15, false));

    // Two ArrowTypes with the same parameters and return type but in reverse order should have equal arrow type
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType16, true));
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType16, false));

    // Two ArrowTypes with the same parameters but different return types should not have equal arrow type
    ArrowType arrowType17 = new ArrowType(registry, param1, JSTypeNative.getBooleanType(), false);
    assertFalse(arrowType15.checkArrowEquivalenceHelper(arrowType17, true));
    assertFalse(arrowType15.checkArrowEquivalenceHelper(arrowType17, false));

    // Two ArrowTypes with different parameters but the same return type should not have equal arrow type
    ArrowType arrowType18 = new ArrowType(registry, new Node(JSTypeNative.createUnknownType()), param2, false);
    assertFalse(arrowType15.checkArrowEquivalenceHelper(arrowType18, true));
    assertFalse(arrowType15.checkArrowEquivalenceHelper(arrowType18, false));

    // Two ArrowTypes with the same parameters and null return type should have equal arrow type
    ArrowType arrowType19 = new ArrowType(registry, param1, null, false);
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType19, true));
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType19, false));

    // Two ArrowTypes with the same parameters and inferred return type should have equal arrow type
    ArrowType arrowType20 = new ArrowType(registry, param1, null, true);
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType20, true));
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType20, false));
  }

  @Test
  public void testToStringHelper() {
    Node param1 = registry.createNamedParam("p1", JSTypeNative.getNumberType());
    Node param2 = registry.createNamedParam("p2", JSTypeNative.getStringType());

    ArrowType arrowType21 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    ArrowType arrowType22 = new ArrowType(registry, param1, param2, false);

    String arrowTypeString1 = arrowType1.toStringHelper(false);
    String arrowTypeString2 = arrowType11.toStringHelper(false);

    assertEquals("[ArrowType]", arrowTypeString1);
    assertEquals("[ArrowType]", arrowTypeString2);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    Node param1 = registry.createNamedParam("p1", JSTypeNative.getNumberType());
    Node param2 = registry.createNamedParam("p2", JSTypeNative.getStringType());
    Node templateParam = registry.createNamedParam("templateParam", JSTypeNative.getNumberType());

    ArrowType arrowType23 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    ArrowType arrowType24 = new ArrowType(registry, param1, param2, false);
    ArrowType arrowType25 = new ArrowType(registry, param1, templateParam, false);

    assertFalse(arrowType1.hasAnyTemplateInternal());
    assertTrue(arrowType24.hasAnyTemplateInternal());
    assertTrue(arrowType25.hasAnyTemplateInternal());
  }

  @Test
  public void testResolveInternal() {
    JSType returnType = JSTypeNative.getBooleanType();
    Node param1 = registry.createNamedParam("p1", JSTypeNative.getNumberType());
    Node param2 = registry.createNamedParam("p2", JSTypeNative.getStringType());

    ArrowType arrowType26 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowType27 = new ArrowType(registry, param1, param2, false);

    // Test ArrowType with no parameters and a resolved return type
    JSType resolvedReturnType = JSTypeNative.getBooleanType();
    arrowType1.resolveInternal(errorReporter, registry.getDefaultScope());
    assertEquals(resolvedReturnType, arrowType1.returnType);

    // Test ArrowType with parameters and a resolved return type
    JSType resolvedParam1 = JSTypeNative.getNumberType();
    JSType resolvedParam2 = JSTypeNative.getStringType();
    arrowType27.resolveInternal(errorReporter, registry.getDefaultScope());
    assertEquals(resolvedParam1, arrowType27.parameters.getFirstChild().getJSType());
    assertEquals(resolvedParam2, arrowType27.parameters.getFirstChild(1).getJSType());
    assertEquals(resolvedReturnType, arrowType27.returnType);
  }

  @Test
  public void testHashCode() {
    Node param1 = registry.createNamedParam("p1", JSTypeNative.getNumberType());
    Node param2 = registry.createNamedParam("p2", JSTypeNative.getStringType());

    ArrowType arrowType28 = new ArrowType(registry, null, null, false);
    ArrowType arrowType29 = new ArrowType(registry, param1, param2, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType29.hashCode();

    assertEquals(hashCode1, arrowType1.hashCode());

    // ArrowType with the same parameters and return type should have the same hashcode
    ArrowType arrowType30 = new ArrowType(registry, param1, param2, false);
    assertEquals(hashCode2, arrowType30.hashCode());

    // ArrowType with different parameters and return type should have different hashcodes
    ArrowType arrowType31 = new ArrowType(registry, param1, JSTypeNative.getBooleanType(), false);
    assertNotEquals(hashCode2, arrowType31.hashCode());

    // ArrowType with different parameters but the same return type should have the same hashcode
    ArrowType arrowType32 = new ArrowType(registry, param2, param1, false);
    assertEquals(hashCode2, arrowType32.hashCode());
  }
}