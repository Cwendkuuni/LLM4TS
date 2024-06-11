import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    assert(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    assert(arrowType1.hasEqualParameters(arrowType2, false));
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    assert(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }
  
  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    assert(arrowType.hashCode() == 0);
  }
  
  @Test
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    try {
      arrowType.getLeastSupertype(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    try {
      arrowType.getGreatestSubtype(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    try {
      arrowType.testForEquality(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testVisit() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    try {
      arrowType.visit(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    BooleanLiteralSet expectedOutcome = BooleanLiteralSet.TRUE;
    assert(arrowType.getPossibleToBooleanOutcomes() == expectedOutcome);
  }
  
  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    try {
      arrowType.resolveInternal(null, null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    assert(arrowType.hasUnknownParamsOrReturn());
  }
  
  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    assert(!arrowType.hasAnyTemplateInternal());
  }
  
}