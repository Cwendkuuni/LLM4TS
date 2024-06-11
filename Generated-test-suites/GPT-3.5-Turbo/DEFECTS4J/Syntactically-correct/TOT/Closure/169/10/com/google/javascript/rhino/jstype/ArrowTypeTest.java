import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  // Test for ArrowType.isSubtype()
  @Test
  public void testIsSubtype() {
    // Typical use case
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));

    // Edge case: when return type of arrowType1 is a subtype of return type of arrowType2
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertTrue(arrowType1.isSubtype(arrowType2));

    // Error scenario: when return type of arrowType1 is not a subtype of return type of arrowType2
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  // Test for ArrowType.hasEqualParameters()
  @Test
  public void testHasEqualParameters() {
    // Typical use case
    ArrowType arrowType1 = new ArrowType(null, null);
    ArrowType arrowType2 = new ArrowType(null, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));

    // Edge case: when both arrow types have varArgs parameters
    arrowType1.parameters = new Node().getVarArgs();
    arrowType2.parameters = new Node().getVarArgs();
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));

    // Error scenario: when arrowType1 has required parameter but arrowType2 doesn't
    arrowType1.parameters = new Node().setOptionalArg(false);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  // Test for ArrowType.checkArrowEquivalenceHelper()
  @Test
  public void testCheckArrowEquivalenceHelper() {
    // Typical use case
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    // Edge case: when return type of arrowType1 is equivalent to return type of arrowType2
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    // Error scenario: when return type of arrowType1 is not equivalent to return type of arrowType2
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  // Test for ArrowType.hashCode()
  @Test
  public void testHashCode() {
    // Typical use case
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    // Edge case: when hash code is calculated for arrow types with non-null parameters and return types
    arrowType1.parameters = new Node();
    arrowType2.parameters = new Node();
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    // Error scenario: when hash code is different for arrow types with non-equal parameters or return types
    arrowType1.parameters = new Node();
    arrowType2.parameters = new Node();
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  // Test for ArrowType.getLeastSupertype()
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.getLeastSupertype(null);
  }

  // Test for ArrowType.getGreatestSubtype()
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.getGreatestSubtype(null);
  }

  // Test for ArrowType.testForEquality()
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.testForEquality(null);
  }

  // Test for ArrowType.visit()
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.visit(null);
  }

  // Test for ArrowType.getPossibleToBooleanOutcomes()
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  // Test for ArrowType.resolveInternal()
  @Test
  public void testResolveInternal() {
    // Typical use case
    ArrowType arrowType = new ArrowType(null, null, null);
    assertEquals(arrowType, arrowType.resolveInternal(null, null));

    // Edge case: when returnType or any parameter type is unknownType
    arrowType.returnType = new JSType();
    assertNull(arrowType.resolveInternal(null, null));

    // Error scenario: when failure occurs during resolution
    arrowType.returnType = new JSType();
    assertNull(arrowType.resolveInternal(null, null));
  }

  // Test for ArrowType.hasUnknownParamsOrReturn()
  @Test
  public void testHasUnknownParamsOrReturn() {
    // Typical use case
    ArrowType arrowType = new ArrowType(null, null, null);
    assertFalse(arrowType.hasUnknownParamsOrReturn());

    // Edge case: when returnType or any parameter type is unknownType
    arrowType.returnType = new JSType();
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    // Error scenario: when returnType or any parameter type is unknownType
    arrowType.returnType = new JSType();
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  // Test for ArrowType.toStringHelper()
  @Test
  public void testToStringHelper() {
    // Typical use case
    ArrowType arrowType = new ArrowType(null, null, null);
    String expected = "[ArrowType]";
    assertEquals(expected, arrowType.toStringHelper(false));

    // Edge case: when forAnnotations is true
    expected = "[ArrowType]";
    assertEquals(expected, arrowType.toStringHelper(true));
  }

  // Test for ArrowType.hasAnyTemplateInternal()
  @Test
  public void testHasAnyTemplateInternal() {
    // Typical use case
    ArrowType arrowType = new ArrowType(null, null, null);
    assertFalse(arrowType.hasAnyTemplateInternal());

    // Edge case: when returnType or any parameter type has a template
    arrowType.returnType = new JSType();
    assertTrue(arrowType.hasAnyTemplateInternal());

    // Error scenario: when returnType or any parameter type has a template
    arrowType.returnType = new JSType();
    assertTrue(arrowType.hasAnyTemplateInternal());
  }
}