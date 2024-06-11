package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    // Test cases for isSubtype method

    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(double.class), false);
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), false);
    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), true);
    ArrowType arrowType6 = new ArrowType(registry, null, null, false);
    ArrowType arrowType7 = new ArrowType(registry, null, null, true);

    ArrowType arrowType8 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType9 = new ArrowType(registry, new Node("number"), getNativeType(String.class), false);
    ArrowType arrowType10 = new ArrowType(registry, new Node("number"), getNativeType(double.class), false);

    ArrowType arrowType11 = new ArrowType(registry, new Node("number"), getNativeType(int.class), true);
    ArrowType arrowType12 = new ArrowType(registry, new Node("number"), getNativeType(String.class), true);
    ArrowType arrowType13 = new ArrowType(registry, new Node("number"), getNativeType(double.class), true);

    ArrowType arrowType14 = new ArrowType(registry, new Node("number"), null, false);
    ArrowType arrowType15 = new ArrowType(registry, new Node("number"), null, true);

    ArrowType arrowType16 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType17 = new ArrowType(registry, null, getNativeType(int[].class), false);
    ArrowType arrowType18 = new ArrowType(registry, null, getNativeType(int[].class), true);

    ArrowType arrowType19 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType20 = new ArrowType(registry, new Node("number, number"), getNativeType(int.class), false);
    ArrowType arrowType21 = new ArrowType(registry, new Node("number, number"), getNativeType(int[].class), false);

    ArrowType arrowType22 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType23 = new ArrowType(registry, new Node("number, number"), getNativeType(int.class), false);
    ArrowType arrowType24 = new ArrowType(registry, new Node("number, number"), getNativeType(int[].class), false);

    ArrowType arrowType25 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType26 = new ArrowType(registry, null, getNativeType(int[].class), false);

    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int lineNumber, String sourceName) {
        fail("Error: " + message);
      }

      @Override
      public void warning(String message, int lineNumber, String sourceName) {
        // Ignore warnings
      }
    };

    StaticScope<JSType> scope = new StaticScope<>(registry, errorReporter);

    // Basic test cases
    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType1.isSubtype(arrowType6));
    assertFalse(arrowType1.isSubtype(arrowType7));

    // Test cases with unknown types
    assertTrue(arrowType4.isSubtype(arrowType4));
    assertTrue(arrowType4.isSubtype(arrowType5));
    assertFalse(arrowType4.isSubtype(arrowType1));

    // Test cases with null return types
    assertTrue(arrowType6.isSubtype(arrowType6));
    assertTrue(arrowType6.isSubtype(arrowType7));
    assertFalse(arrowType1.isSubtype(arrowType6));
    assertFalse(arrowType1.isSubtype(arrowType7));

    // Test cases with different parameter lists
    assertFalse(arrowType8.isSubtype(arrowType9));
    assertFalse(arrowType9.isSubtype(arrowType8));

    // Test cases with inferred return types
    assertEquals(arrowType11, arrowType11.isSubtype(arrowType11));
    assertEquals(arrowType11, arrowType11.isSubtype(arrowType13));
    assertFalse(arrowType11.isSubtype(arrowType12));

    // Test cases with varargs
    assertTrue(arrowType14.isSubtype(arrowType15));
    assertTrue(arrowType14.isSubtype(arrowType16));
    assertTrue(arrowType14.isSubtype(arrowType17));

    // Test cases with different arities
    assertTrue(arrowType20.isSubtype(arrowType21));
    assertTrue(arrowType22.isSubtype(arrowType23));
    assertTrue(arrowType22.isSubtype(arrowType24));

    // Test cases with different return types
    assertFalse(arrowType16.isSubtype(arrowType17));
    assertFalse(arrowType17.isSubtype(arrowType16));

    // Test cases with different and null return types
    assertFalse(arrowType16.isSubtype(arrowType18));
    assertFalse(arrowType18.isSubtype(arrowType16));

    // Test cases with different and inferred return types
    assertFalse(arrowType16.isSubtype(arrowType25));
    assertFalse(arrowType25.isSubtype(arrowType16));

    // Test cases with different and null inferred return types
    assertFalse(arrowType16.isSubtype(arrowType26));
    assertFalse(arrowType26.isSubtype(arrowType16));
  }

  @Test
  public void testHasEqualParameters() {
    // Test cases for hasEqualParameters method

    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(int[].class), false);
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(int[].class), true);

    ArrowType arrowType5 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType6 = new ArrowType(registry, new Node("number, number"), getNativeType(int.class), false);

    ArrowType arrowType7 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType8 = new ArrowType(registry, new Node("number, number"), getNativeType(int.class), false);

    Node paramNode1 = new Node("number");
    Node paramNode2 = new Node("number");
    JSType paramType1 = new JSTypeNative(registry, int.class);
    JSType paramType2 = new JSTypeNative(registry, int[].class);

    ArrowType arrowType9 = new ArrowType(registry, paramNode1, paramType1, false);
    ArrowType arrowType10 = new ArrowType(registry, paramNode2, paramType1, false);
    ArrowType arrowType11 = new ArrowType(registry, paramNode1, paramType2, false);

    // Basic test cases
    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));

    // Test cases with single parameters
    assertTrue(arrowType5.hasEqualParameters(arrowType5));
    assertTrue(arrowType5.hasEqualParameters(arrowType7));
    assertFalse(arrowType5.hasEqualParameters(arrowType6));

    // Test cases with multiple parameters
    assertTrue(arrowType6.hasEqualParameters(arrowType6));
    assertTrue(arrowType6.hasEqualParameters(arrowType8));
    assertFalse(arrowType6.hasEqualParameters(arrowType5));

    // Test cases with varargs
    assertTrue(arrowType9.hasEqualParameters(arrowType10));
    assertTrue(arrowType9.hasEqualParameters(arrowType11));
    assertFalse(arrowType9.hasEqualParameters(arrowType5));
  }

  @Test
  public void testEqualsAndHashCode() {
    // Test cases for equals and hashCode methods

    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(int[].class), false);

    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(int[].class), true);
    ArrowType arrowType5 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType6 = new ArrowType(registry, new Node("number, number"), getNativeType(int.class), false);

    ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType8 = new ArrowType(registry, null, getNativeType(int[].class), false);

    Node paramNode1 = new Node("number");
    Node paramNode2 = new Node("number");
    JSType paramType1 = new JSTypeNative(registry, int.class);
    JSType paramType2 = new JSTypeNative(registry, int[].class);

    ArrowType arrowType9 = new ArrowType(registry, paramNode1, paramType1, false);
    ArrowType arrowType10 = new ArrowType(registry, paramNode2, paramType1, false);
    ArrowType arrowType11 = new ArrowType(registry, paramNode1, paramType2, false);

    // Basic test cases
    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType3);
    assertNotEquals(arrowType1, arrowType2);
    assertNotEquals(arrowType1, arrowType4);
    assertNotEquals(arrowType1, arrowType5);
    assertNotEquals(arrowType1, arrowType6);

    // Test cases with single parameters
    assertEquals(arrowType5, arrowType5);
    assertEquals(arrowType5, arrowType7);
    assertNotEquals(arrowType5, arrowType6);

    // Test cases with multiple parameters
    assertEquals(arrowType6, arrowType6);
    assertEquals(arrowType6, arrowType8);
    assertNotEquals(arrowType6, arrowType5);

    // Test cases with varargs
    assertEquals(arrowType9, arrowType10);
    assertEquals(arrowType9, arrowType11);
    assertNotEquals(arrowType9, arrowType5);

    // Test cases with different return types
    assertNotEquals(arrowType1, arrowType3);

    // Test cases with inferred return types
    ArrowType arrowType12 = new ArrowType(registry, null, null, false);
    ArrowType arrowType13 = new ArrowType(registry, null, null, true);

    assertEquals(arrowType12, arrowType12);
    assertEquals(arrowType12, arrowType13);
    assertNotEquals(arrowType12, arrowType1);

    // Test cases with null return types
    ArrowType arrowType14 = new ArrowType(registry, null, null, false);
    ArrowType arrowType15 = new ArrowType(registry, null, null, true);

    assertEquals(arrowType14, arrowType14);
    assertEquals(arrowType14, arrowType15);
    assertNotEquals(arrowType14, arrowType1);

    // Test cases with different and null return types
    assertNotEquals(arrowType1, arrowType14);
    assertNotEquals(arrowType14, arrowType1);

    // Test cases with different and inferred return types
    assertNotEquals(arrowType1, arrowType12);
    assertNotEquals(arrowType12, arrowType1);

    // Test cases with different and null inferred return types
    assertNotEquals(arrowType1, arrowType15);
    assertNotEquals(arrowType15, arrowType1);
  }

  @Test
  public void testToString() {
    // Test cases for toString method

    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(int[].class), false);

    ArrowType arrowType3 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType4 = new ArrowType(registry, new Node("number, number"), getNativeType(int.class), false);

    ArrowType arrowType5 = new ArrowType(registry, null, null, false);
    ArrowType arrowType6 = new ArrowType(registry, null, null, true);

    ArrowType arrowType7 = new ArrowType(registry, new Node("number"), getNativeType(int.class), false);
    ArrowType arrowType8 = new ArrowType(registry, new Node("number, number"), getNativeType(int.class), false);

    Node paramNode1 = new Node("number");
    Node paramNode2 = new Node("number");
    JSType paramType1 = new JSTypeNative(registry, int.class);
    JSType paramType2 = new JSTypeNative(registry, int[].class);

    ArrowType arrowType9 = new ArrowType(registry, paramNode1, paramType1, false);
    ArrowType arrowType10 = new ArrowType(registry, paramNode2, paramType1, false);
    ArrowType arrowType11 = new ArrowType(registry, paramNode1, paramType2, false);

    assertEquals("function(int)", arrowType1.toString());
    assertEquals("function(int)", arrowType3.toString());
    assertEquals("function(int)", arrowType7.toString());

    assertEquals("function(int, int)", arrowType4.toString());
    assertEquals("function(int, int)", arrowType8.toString());

    assertEquals("function()", arrowType5.toString());
    assertEquals("function()", arrowType6.toString());

    assertEquals("function(int)", arrowType9.toString());
    assertEquals("function(int)", arrowType10.toString());
    assertEquals("function(int)", arrowType11.toString());
  }

  private Node getNativeType(Class<?> clazz) {
    return new Node("number"); // Replace with the actual node for the given class
  }
}