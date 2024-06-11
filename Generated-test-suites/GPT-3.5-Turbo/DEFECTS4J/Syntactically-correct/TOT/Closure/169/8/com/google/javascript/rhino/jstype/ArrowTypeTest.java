import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype_ReturnTypesDifferent_ReturnFalse() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(parameters, otherReturnType);

    assertFalse(arrowType.isSubtype(otherArrowType));
  }

  @Test
  public void testIsSubtype_ParametersDifferent_ReturnFalse() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(otherParameters, returnType);

    assertFalse(arrowType.isSubtype(otherArrowType));
  }

  @Test
  public void testIsSubtype_ParametersAndReturnTypesDifferent_ReturnFalse() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(otherParameters, otherReturnType);

    assertFalse(arrowType.isSubtype(otherArrowType));
  }

  @Test
  public void testHasEqualParameters_EqualParameters_ReturnTrue() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(parameters, returnType);

    assertTrue(arrowType.hasEqualParameters(otherArrowType, true));
  }

  @Test
  public void testHasEqualParameters_DifferentParameters_ReturnFalse() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(otherParameters, returnType);

    assertFalse(arrowType.hasEqualParameters(otherArrowType, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_EqualArrowTypes_ReturnTrue() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(parameters, returnType);

    assertTrue(arrowType.checkArrowEquivalenceHelper(otherArrowType, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_DifferentReturnTypes_ReturnFalse() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(parameters, otherReturnType);

    assertFalse(arrowType.checkArrowEquivalenceHelper(otherArrowType, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_DifferentParameters_ReturnFalse() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(otherParameters, returnType);

    assertFalse(arrowType.checkArrowEquivalenceHelper(otherArrowType, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_DifferentParametersAndReturnTypes_ReturnFalse() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(otherParameters, otherReturnType);

    assertFalse(arrowType.checkArrowEquivalenceHelper(otherArrowType, true));
  }

  @Test
  public void testHashCode_EqualArrowTypes_ReturnEqualHashCodes() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(parameters, returnType);

    assertEquals(arrowType.hashCode(), otherArrowType.hashCode());
  }

  @Test
  public void testHashCode_DifferentReturnTypes_ReturnDifferentHashCodes() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(parameters, otherReturnType);

    assertNotEquals(arrowType.hashCode(), otherArrowType.hashCode());
  }

  @Test
  public void testHashCode_DifferentParameters_ReturnDifferentHashCodes() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(otherParameters, returnType);

    assertNotEquals(arrowType.hashCode(), otherArrowType.hashCode());
  }

  @Test
  public void testHashCode_DifferentParametersAndReturnTypes_ReturnDifferentHashCodes() {
    ArrowType arrowType = new ArrowType(parameters, returnType);
    ArrowType otherArrowType = new ArrowType(otherParameters, otherReturnType);

    assertNotEquals(arrowType.hashCode(), otherArrowType.hashCode());
  }

  @Test
  public void testGetLeastSupertype_UnsupportedOperationExceptionThrown() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    try {
      arrowType.getLeastSupertype(otherArrowType);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testGetGreatestSubtype_UnsupportedOperationExceptionThrown() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    try {
      arrowType.getGreatestSubtype(otherArrowType);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testTestForEquality_UnsupportedOperationExceptionThrown() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    try {
      arrowType.testForEquality(otherArrowType);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testVisit_UnsupportedOperationExceptionThrown() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    try {
      arrowType.visit(visitor);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes_ReturnTrue() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal_ReturnsResolvedType() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    JSType resolvedType = arrowType.resolveInternal(errorReporter, scope);

    assertNotNull(resolvedType);
  }

  @Test
  public void testHasUnknownParamsOrReturn_TrueIfParametersOrReturnTypesUnknown() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_FalseIfParametersAndReturnTypesKnown() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasAnyTemplateInternal_TrueIfReturnTypeHasTemplate() {
    JSType returnType = new JSType(/*templatedType*/true);
    ArrowType arrowType = new ArrowType(parameters, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_TrueIfParametersHaveTemplate() {
    JSType parameterType = new JSType(/*templatedType*/true);
    Node parameters = new Node(/*params*/).addJSType(parameterType);
    ArrowType arrowType = new ArrowType(parameters, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_FalseIfNoTemplates() {
    ArrowType arrowType = new ArrowType(parameters, returnType);

    assertFalse(arrowType.hasAnyTemplateInternal());
  }
}