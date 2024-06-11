import static org.junit.Assert.*;
import org.junit.*;

public class ArrowTypeTest {
  
  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    boolean result = arrowType1.isSubtype(arrowType2);
    
    assertTrue(result);
  }
  
  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    boolean result = arrowType1.hasEqualParameters(arrowType2, false);
    
    assertTrue(result);
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    boolean result = arrowType1.checkArrowEquivalenceHelper(arrowType2, false);
    
    assertTrue(result);
  }
  
  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    int result = arrowType.hashCode();
    
    assertEquals(0, result);
  }
  
  @Test(expected=UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.getLeastSupertype(null);
  }
  
  @Test(expected=UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.getGreatestSubtype(null);
  }
  
  @Test(expected=UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.testForEquality(null);
  }
  
  @Test(expected=UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.visit(null);
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
    
    assertEquals(BooleanLiteralSet.TRUE, result);
  }
  
  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    JSType result = arrowType.resolveInternal(null, null);
    
    assertEquals(arrowType, result);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    boolean result = arrowType.hasUnknownParamsOrReturn();
    
    assertFalse(result);
  }
  
  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    String result = arrowType.toStringHelper(false);
    
    assertEquals("[ArrowType]", result);
  }
  
  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    boolean result = arrowType.hasAnyTemplateInternal();
    
    assertFalse(result);
  }
}