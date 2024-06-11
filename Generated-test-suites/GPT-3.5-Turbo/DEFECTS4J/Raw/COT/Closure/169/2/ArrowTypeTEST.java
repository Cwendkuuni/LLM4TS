import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    
    // Test when the return types are different
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertFalse(arrowType1.isSubtype(arrowType2));
    
    // Test when the parameter types are different
    arrowType1.parameters = new Node();
    arrowType1.parameters.setFirstChild(new Node());
    arrowType2.parameters = new Node();
    arrowType2.parameters.setFirstChild(new Node());
    assertFalse(arrowType1.isSubtype(arrowType2));
    
    // Test when the parameter lists have different length
    arrowType1.parameters = new Node();
    arrowType1.parameters.setFirstChild(new Node());
    arrowType2.parameters = new Node();
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    
    // Test when the parameter types are different
    arrowType1.parameters = new Node();
    arrowType1.parameters.setFirstChild(new Node());
    arrowType2.parameters = new Node();
    arrowType2.parameters.setFirstChild(new Node());
    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));
    
    // Test when the parameter lists have different length
    arrowType1.parameters = new Node();
    arrowType1.parameters.setFirstChild(new Node());
    arrowType2.parameters = new Node();
    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    
    // Test when the return types are different
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    
    // Test when the parameter types are different
    arrowType1.parameters = new Node();
    arrowType1.parameters.setFirstChild(new Node());
    arrowType2.parameters = new Node();
    arrowType2.parameters.setFirstChild(new Node());
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    
    // Test when the parameter lists have different length
    arrowType1.parameters = new Node();
    arrowType1.parameters.setFirstChild(new Node());
    arrowType2.parameters = new Node();
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType();
    assertNotNull(arrowType.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.getLeastSupertype(arrowType2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.getGreatestSubtype(arrowType2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.testForEquality(arrowType2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrowType = new ArrowType();
    arrowType.visit(new Visitor<T>());
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType();
    assertNotNull(arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    assertNotNull(arrowType.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType();
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType();
    assertNotNull(arrowType.toStringHelper(true));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType();
    assertFalse(arrowType.hasAnyTemplateInternal());
  }
}