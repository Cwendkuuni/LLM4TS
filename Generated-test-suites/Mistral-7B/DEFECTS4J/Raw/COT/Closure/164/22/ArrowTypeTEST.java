import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.Property.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructionWithParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    assertEquals(registry.createParametersWithVarArgs(JSType.INT, JSType.STRING), arrowType.parameters);
    assertEquals(JSType.INT, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructionWithParametersAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertEquals(registry.createParametersWithVarArgs(JSType.INT, JSType.STRING), arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructionWithOnlyReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSType.INT, false);

    assertNull(arrowType.parameters);
    assertEquals(JSType.INT, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructionWithOnlyInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructionWithInvalidParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, (int, String))");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, false);
  }

  @Test
  public void testArrowTypeIsSubtypeWithMatchingTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String)");
    Node parametersNode2 = createNode("(int, String)");
    JSType returnType1 = JSType.INT;
    JSType returnType2 = JSType.INT;

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithDifferentReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType1 = JSType.INT;
    JSType returnType2 = JSType.STRING;

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithLessSpecificParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String)");
    Node parametersNode2 = createNode("(int, Number, String)");

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithMoreSpecificParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String)");
    Node parametersNode2 = createNode("(int, String, Number)");

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String...)");
    Node parametersNode2 = createNode("(int...)");

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithNoVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String)");
    Node parametersNode2 = createNode("(int, Number)");

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = null;
    Node parametersNode2 = createNode("()");

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeWithEmptyParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("()");
    Node parametersNode2 = null;

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String)");
    Node parametersNode2 = createNode("(int, String)");
    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeDoesNotHaveEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String)");
    Node parametersNode2 = createNode("(String, int)");
    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = createNode("(int, String)");
    Node parametersNode2 = createNode("(String, int)");
    JSType returnType1 = JSType.INT;
    JSType returnType2 = JSType.STRING;

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, parametersNode1, returnType1);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testArrowTypeHashCodeWithEqualTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeHashCodeWithDifferentTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType1 = JSType.INT;
    JSType returnType2 = JSType.STRING;

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeGetLeastSupertypeUnsupported() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    try {
      arrowType.getLeastSupertype(null);
      fail("UnsupportedOperationException expected");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeGetGreatestSubtypeUnsupported() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    try {
      arrowType.getGreatestSubtype(null);
      fail("UnsupportedOperationException expected");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeTestForEqualityUnsupported() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    try {
      arrowType.testForEquality(null);
      fail("UnsupportedOperationException expected");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeVisitUnsupported() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    try {
      arrowType.visit(null);
      fail("UnsupportedOperationException expected");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    BooleanLiteralSet expected = BooleanLiteralSet.TRUE;
    assertEquals(expected, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testArrowTypeResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    JSType returnType = JSType.INT;

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> scope = new StaticScope<>();

    arrowType.resolveInternal(errorReporter, scope);

    assertEquals(JSType.INT, arrowType.returnType);
    assertNotNull(arrowType.returnType);
  }

  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = createNode("(int, String)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    Node unknownParametersNode = createNode("(int, (int, String))");
    ArrowType arrowTypeWithUnknownParameters = new ArrowType(registry, unknownParametersNode, null);

    assertTrue(arrowTypeWithUnknownParameters.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, null, null);

    assertTrue(arrowTypeWithUnknownReturn.hasUnknownParamsOrReturn());
  }

  private static Node createNode(String nodeString) {
    return new Node() {
      @Override
      public String toSource() {
        return nodeString;
      }
    };
  }
}