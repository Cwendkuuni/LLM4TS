import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
  
  @Test
  public void testIsSubtype_ReturnsTrue() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    boolean result = arrowType1.isSubtype(arrowType2);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype_ReturnsFalse() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    JSType jSType1 = new JSType(null);
    
    boolean result = arrowType1.isSubtype(jSType1);
    
    assertFalse(result);
  }
  
  @Test
  public void testHasEqualParameters_ReturnsTrue() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    boolean result = arrowType1.hasEqualParameters(arrowType2);
    
    assertTrue(result);
  }
  
  @Test
  public void testHasEqualParameters_ReturnsFalse() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    arrowType2.parameters = new Node();
    
    boolean result = arrowType1.hasEqualParameters(arrowType2);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsEquivalentTo_ReturnsTrue() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    boolean result = arrowType1.isEquivalentTo(arrowType2);
    
    assertTrue(result);
  }
  
  @Test
  public void testHashCode_ReturnsHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    int hashCode = arrowType.hashCode();
    
    assertEquals(hashCode, arrowType.hashCode());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype_ThrowsException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.getLeastSupertype(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype_ThrowsException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.getGreatestSubtype(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality_ThrowsException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.testForEquality(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit_ThrowsException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    arrowType.visit(null);
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes_ReturnsBooleanLiteralSet() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    BooleanLiteralSet booleanLiteralSet = arrowType.getPossibleToBooleanOutcomes();
    
    assertNotNull(booleanLiteralSet);
  }
  
  @Test
  public void testResolveInternal_ReturnsJSType() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    JSType jSType = arrowType.resolveInternal(null, null);
    
    assertNotNull(jSType);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn_ReturnsTrue() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.returnType = new JSType(null);
    
    boolean result = arrowType.hasUnknownParamsOrReturn();
    
    assertTrue(result);
  }
  
  @Test
  public void testToStringHelper_ReturnsString() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    String string = arrowType.toStringHelper(false);
    
    assertNotNull(string);
  }
}