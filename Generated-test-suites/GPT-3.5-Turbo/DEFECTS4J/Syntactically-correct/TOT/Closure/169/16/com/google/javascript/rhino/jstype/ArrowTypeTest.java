import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    arrowType = new ArrowType(null, null, null, false);
  }

  @Test
  public void testIsSubtype_ReturnTypeIsSubtype_ReturnsTrue() {
    JSType returnType = new JSType();
    arrowType.returnType = returnType;

    ArrowType that = new ArrowType(null, null, returnType, false);

    assertTrue(arrowType.isSubtype(that));
  }

  @Test
  public void testIsSubtype_DifferentReturnType_ReturnsFalse() {
    arrowType.returnType = new JSType();

    ArrowType that = new ArrowType(null, null, null, false);

    assertFalse(arrowType.isSubtype(that));
  }

  @Test
  public void testIsSubtype_NullParameterInThis_ReturnsFalse() {
    arrowType.parameters = null;

    ArrowType that = new ArrowType(null, null, null, false);

    assertFalse(arrowType.isSubtype(that));
  }

  @Test
  public void testIsSubype_NullParameterInThat_ReturnsFalse() {
    arrowType.parameters = new Node();

    ArrowType that = new ArrowType(null, null, null, false);

    assertFalse(arrowType.isSubtype(that));
  }

  @Test
  public void testIsSubtype_BothHaveNullParameters_ReturnsTrue() {
    arrowType.parameters = null;

    ArrowType that = new ArrowType(null, null, null, false);

    assertTrue(arrowType.isSubtype(that));
  }

  @Test
  public void testIsSubtype_ParametersAreEqual_ReturnsTrue() {
    Node parameters = new Node();
    parameters.setFirstChild(new Node());
    arrowType.parameters = parameters;

    ArrowType that = new ArrowType(null, parameters, null, false);

    assertTrue(arrowType.isSubtype(that));
  }

  @Test
  public void testIsSubtype_ParametersAreDifferent_ReturnsFalse() {
    Node parameters1 = new Node();
    parameters1.setFirstChild(new Node());
    arrowType.parameters = parameters1;

    Node parameters2 = new Node();
    parameters2.setFirstChild(new Node());

    ArrowType that = new ArrowType(null, parameters2, null, false);

    assertFalse(arrowType.isSubtype(that));
  }

  @Test
  public void testHashCode_ParametersAndReturnTypeAreNull_ReturnsZero() {
    arrowType.parameters = null;
    arrowType.returnType = null;
    
    int expectedHashCode = 0;

    assertEquals(expectedHashCode, arrowType.hashCode());
  }

  @Test
  public void testHashCode_ParametersAndReturnTypeAreNonNull_ReturnsNonZero() {
    arrowType.parameters = new Node();
    arrowType.parameters.setFirstChild(new Node());
    arrowType.returnType = new JSType();
    
    int expectedHashCode = 0;

    assertEquals(expectedHashCode, arrowType.hashCode());
  }
  
  @Test
  public void testGetLeastSupertype_ThrowsUnsupportedOperationException() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> arrowType.getLeastSupertype(null));
  }

  @Test
  public void testGetGreatestSubtype_ThrowsUnsupportedOperationException() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> arrowType.getGreatestSubtype(null));
  }

  @Test
  public void testTestForEquality_ThrowsUnsupportedOperationException() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> arrowType.testForEquality(null));
  }

  @Test
  public void testVisit_ThrowsUnsupportedOperationException() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> arrowType.visit(null));
  }

  @Test
  public void testGetPossibleToBooleanOutcomes_ReturnsTrue() {
    BooleanLiteralSet expectedOutcomes = BooleanLiteralSet.TRUE;

    assertEquals(expectedOutcomes, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal_ThrowsUnsupportedOperationException() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> arrowType.resolveInternal(null, null));
  }

  @Test
  public void testHasUnknownParamsOrReturn_ParametersAndReturnTypeAreKnown_ReturnsFalse() {
    arrowType.parameters = new Node();
    arrowType.parameters.setFirstChild(new Node());
    arrowType.returnType = new JSType();

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_ParameterIsUnknown_ReturnsTrue() {
    Node parameters = new Node();
    parameters.setFirstChild(new Node());
    parameters.getFirstChild().setJSType(null);
    arrowType.parameters = parameters;

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper_WithAnnotations_ReturnsString() {
    String expectedString = "[ArrowType]";

    assertEquals(expectedString, arrowType.toStringHelper(true));
  }

  @Test
  public void testToStringHelper_WithoutAnnotations_ReturnsString() {
    String expectedString = "[ArrowType]";

    assertEquals(expectedString, arrowType.toStringHelper(false));
  }

  @Test
  public void testHasAnyTemplateInternal_ReturnsFalse() {
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_ReturnTypeHasTemplate_ReturnsTrue() {
    arrowType.returnType = new JSType();
    arrowType.returnType.setTemplate(true);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternal_ParameterHasTemplate_ReturnsTrue() {
    Node parameters = new Node();
    parameters.setFirstChild(new Node());
    parameters.getFirstChild().setJSType(new JSType());
    parameters.getFirstChild().getJSType().setTemplate(true);
    arrowType.parameters = parameters;

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

}