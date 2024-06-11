import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.*;
import static org.junit.Assert.*;

public class ArrowTypeTest {
  
  @Test
  public void testSubtype() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    boolean result = arrow1.isSubtype(arrow2);
    
    assertTrue(result);
  }
  
  @Test
  public void testNotSubtype() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    boolean result = arrow1.isSubtype(arrow2);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualParameters() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    boolean result = arrow1.hasEqualParameters(arrow2, true);
    
    assertTrue(result);
  }
  
  @Test
  public void testNotEqualParameters() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    boolean result = arrow1.hasEqualParameters(arrow2, true);
    
    assertFalse(result);
  }
  
  @Test
  public void testArrowEquivalence() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    boolean result = arrow1.checkArrowEquivalenceHelper(arrow2, true);
    
    assertTrue(result);
  }
  
  @Test
  public void testNotArrowEquivalence() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    boolean result = arrow1.checkArrowEquivalenceHelper(arrow2, true);
    
    assertFalse(result);
  }
  
  @Test
  public void testHashCode() {
    ArrowType arrow = new ArrowType(null, null, null);
    
    int hashCode = arrow.hashCode();
    
    assertTrue(hashCode > 0);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    arrow1.getLeastSupertype(arrow2);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    arrow1.getGreatestSubtype(arrow2);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrow1 = new ArrowType(null, null, null);
    ArrowType arrow2 = new ArrowType(null, null, null);
    
    arrow1.testForEquality(arrow2);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrow = new ArrowType(null, null, null);
    
    arrow.visit(new Visitor<>());
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrow = new ArrowType(null, null, null);
    
    BooleanLiteralSet outcomes = arrow.getPossibleToBooleanOutcomes();
    
    assertNotNull(outcomes);
    assertEquals(BooleanLiteralSet.TRUE, outcomes);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testResolveInternal() {
    ArrowType arrow = new ArrowType(null, null, null);
    
    arrow.resolveInternal(null, null);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrow = new ArrowType(null, null, null);
    
    boolean result = arrow.hasUnknownParamsOrReturn();
    
    assertFalse(result);
  }
  
  @Test
  public void testToStringHelper() {
    ArrowType arrow = new ArrowType(null, null, null);
    
    String str = arrow.toStringHelper(false);
    
    assertNotNull(str);
    assertEquals("[ArrowType]", str);
  }
  
  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrow = new ArrowType(null, null, null);
    
    boolean result = arrow.hasAnyTemplateInternal();
    
    assertFalse(result);
  }
}