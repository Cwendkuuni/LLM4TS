package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.TernaryValue;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;

  @Before
  public void setup() {
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, Object... args) {}
    };

    registry = new JSTypeRegistry();
  }

  @Test
  public void testIsSubtype() {
    // Test cases for covariant return type.
    JSType type1 = registry.createArrowType(null, null, JSTypeNative.INT);
    JSType type2 = registry.createArrowType(null, null, JSTypeNative.DOUBLE);
    JSType type3 = registry.createArrowType(null, null, JSTypeNative.STRING);
    JSType type4 = registry.createArrowType(null, null, JSTypeNative.BOOLEAN);

    ArrowType arrowType1 = (ArrowType) type1;
    ArrowType arrowType2 = (ArrowType) type2;
    ArrowType arrowType3 = (ArrowType) type3;
    ArrowType arrowType4 = (ArrowType) type4;

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType4));

    // Test cases for contravariant parameter types.
    Node paramNode1 = registry.createNodeFromString("x:number");
    Node paramNode2 = registry.createNodeFromString("x:string");

    JSType arrowType5 =
        registry.createArrowType(null, paramNode1, JSTypeNative.INT);
    JSType arrowType6 =
        registry.createArrowType(null, paramNode2, JSTypeNative.INT);

    ArrowType arrowType5_1 = (ArrowType) arrowType5;
    ArrowType arrowType6_1 = (ArrowType) arrowType6;

    assertTrue(arrowType5_1.isSubtype(arrowType6_1));
    assertFalse(arrowType6_1.isSubtype(arrowType5_1));

    // Test cases for null and unknown types.
    JSType arrowType7 = registry.createArrowType(null, null, null);
    JSType arrowType8 = registry.createArrowType(null, null, UNKNOWN_TYPE);

    ArrowType arrowType7_1 = (ArrowType) arrowType7;
    ArrowType arrowType8_1 = (ArrowType) arrowType8;

    assertFalse(arrowType7_1.isSubtype(arrowType8_1));
    assertFalse(arrowType8_1.isSubtype(arrowType7_1));

    // Test cases for FunctionType.
    JSType functionType1 = registry.createFunctionType(null, null, null);
    JSType arrowType9 = registry.createArrowType(null, null, functionType1);

    ArrowType arrowType9_1 = (ArrowType) arrowType9;
    FunctionType functionType1_1 = (FunctionType) functionType1;

    assertFalse(arrowType9_1.isSubtype(functionType1_1));
    assertFalse(functionType1_1.isSubtype(arrowType9_1));

    // Test cases for ArrowType with varargs.
    Node varargsNode = registry.createNodeFromString("...");
    JSType arrowType10 =
        registry.createArrowType(null, null, JSTypeNative.INT, varargsNode);
    JSType arrowType11 =
        registry.createArrowType(null, null, JSTypeNative.INT);

    ArrowType arrowType10_1 = (ArrowType) arrowType10;
    ArrowType arrowType11_1 = (ArrowType) arrowType11;

    assertFalse(arrowType10_1.isSubtype(arrowType11_1));
    assertFalse(arrowType11_1.isSubtype(arrowType10_1));
  }

  @Test
  public void testHasEqualParameters() {
    // Test cases for equal parameter lists.
    Node paramNode1 = registry.createNodeFromString("x:number");
    Node paramNode2 = registry.createNodeFromString("x:number");

    ArrowType arrowType1 =
        registry.createArrowType(null, paramNode1, JSTypeNative.INT);
    ArrowType arrowType2 =
        registry.createArrowType(null, paramNode2, JSTypeNative.INT);

    ArrowType arrowType1_1 = (ArrowType) arrowType1;
    ArrowType arrowType2_1 = (ArrowType) arrowType2;

    assertTrue(arrowType1_1.hasEqualParameters(arrowType2_1));

    // Test cases for unequal parameter lists.
    Node paramNode3 = registry.createNodeFromString("x:number");
    Node paramNode4 = registry.createNodeFromString("y:number");

    ArrowType arrowType3 =
        registry.createArrowType(null, paramNode1, JSTypeNative.INT);
    ArrowType arrowType4 =
        registry.createArrowType(null, paramNode3, JSTypeNative.INT);

    ArrowType arrowType3_1 = (ArrowType) arrowType3;
    ArrowType arrowType4_1 = (ArrowType) arrowType4;

    assertFalse(arrowType3_1.hasEqualParameters(arrowType4_1));

    // Test cases for varargs.
    Node varargsNode = registry.createNodeFromString("...");

    ArrowType arrowType5 =
        registry.createArrowType(null, null, JSTypeNative.INT, varargsNode);
    ArrowType arrowType6 =
        registry.createArrowType(null, null, JSTypeNative.STRING, varargsNode);

    ArrowType arrowType5_1 = (ArrowType) arrowType5;
    ArrowType arrowType6_1 = (ArrowType) arrowType6;

    assertFalse(arrowType5_1.hasEqualParameters(arrowType6_1));
  }
}