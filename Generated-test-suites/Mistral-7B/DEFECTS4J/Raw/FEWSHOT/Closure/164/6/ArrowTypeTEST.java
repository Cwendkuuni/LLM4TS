package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Override
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testIsSubtype_SameType() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> int"), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypes() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> boolean"), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentParameters() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(boolean) -> int"), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_WildcardReturnType() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> *"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> int"), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_VarargsParameter() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int...) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int, int) -> int"), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_VarargsInSubType() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int, int...) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int...) -> int"), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_InferredReturnType() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) ->"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> int"), true);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_InferredReturnType_Wildcard() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) ->"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> *"), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    Node paramList1 = createNode("(int, int, float)");
    Node paramList2 = createNode("(int, int, float)");

    ArrowType arrowType1 = createArrowType(registry, paramList1, null);
    ArrowType arrowType2 = createArrowType(registry, paramList2, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testHasEqualParameters_DifferentNumberOfParameters() {
    Node paramList1 = createNode("(int, int, float)");
    Node paramList2 = createNode("(int, float)");

    ArrowType arrowType1 = createArrowType(registry, paramList1, null);
    ArrowType arrowType2 = createArrowType(registry, paramList2, null);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testHasEqualParameters_DifferentTypes() {
    Node paramList1 = createNode("(int, int, int)");
    Node paramList2 = createNode("(boolean, int, int)");

    ArrowType arrowType1 = createArrowType(registry, paramList1, null);
    ArrowType arrowType2 = createArrowType(registry, paramList2, null);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testEquality() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> int"), false);

    assertTrue(arrowType1.equals(arrowType2));
    assertTrue(arrowType2.equals(arrowType1));
  }

  @Test
  public void testEquality_DifferentReturnTypes() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> boolean"), false);

    assertFalse(arrowType1.equals(arrowType2));
    assertFalse(arrowType2.equals(arrowType1));
  }

  @Test
  public void testEquality_DifferentParameters() {
    Node paramList1 = createNode("(int, int, float)");
    Node paramList2 = createNode("(boolean, int, float)");

    ArrowType arrowType1 = createArrowType(registry, paramList1, null);
    ArrowType arrowType2 = createArrowType(registry, paramList2, null);

    assertFalse(arrowType1.equals(arrowType2));
    assertFalse(arrowType2.equals(arrowType1));
  }

  @Test
  public void testHashCode_SameType() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> int"), false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCode_DifferentTypes() {
    ArrowType arrowType1 = createArrowType(registry, createNode("(int) -> int"), false);
    ArrowType arrowType2 = createArrowType(registry, createNode("(int) -> boolean"), false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCode_DifferentParameters() {
    Node paramList1 = createNode("(int, int, float)");
    Node paramList2 = createNode("(boolean, int, float)");

    ArrowType arrowType1 = createArrowType(registry, paramList1, null);
    ArrowType arrowType2 = createArrowType(registry, paramList2, null);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testGetLeastSupertype() {
    throw new RuntimeException("UnsupportedOperationException");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new RuntimeException("UnsupportedOperationException");
  }

  @Test
  public void testToString() {
    ArrowType arrowType = createArrowType(registry, createNode("(int) -> int"), false);

    String toString = arrowType.toString();
    assertEquals("(int) -> int", toString);
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = createArrowType(registry, createNode("(int) -> int"), false);

    arrowType.resolveInternal(errorReporter, registry.createEmptyScope());

    JSType resolvedReturnType = arrowType.returnType;
    JSType expectedReturnType = registry.getTypeRegistry().getType(JSTypeNative.INT);

    assertTrue(resolvedReturnType.isEquivalentTo(expectedReturnType));
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = createArrowType(registry, createNode("(int) ->"), false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_NoUnknown() {
    ArrowType arrowType = createArrowType(registry, createNode("(int) -> int"), false);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  private Node createNode(String nodeText) {
    return new Node() {
      @Override
      public String getSourceName() {
        return "source_name";
      }

      @Override
      public int getBeginPosition() {
        return 0;
      }

      @Override
      public int getEndPosition() {
        return nodeText.length();
      }

      @Override
      public String getText() {
        return nodeText;
      }

      @Override
      public JSType getJSType() {
        return null;
      }
    };
  }

  private ArrowType createArrowType(JSTypeRegistry registry, Node node, boolean returnTypeInferred) {
    return new ArrowType(registry, node, null, returnTypeInferred);
  }
}