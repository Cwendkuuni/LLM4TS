import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  
  private ArrowType arrowType;
  
  @Before
  public void setUp() {
    arrowType = new ArrowType(JSTypeRegistry(), Node, JSType);
  }
  
  @Test
  public void testIsSubtype() {
    boolean result = arrowType.isSubtype(other);
    assertFalse(result);
  }
  
  @Test
  public void testHasEqualParameters() {
    ArrowType that = new ArrowType(JSTypeRegistry(), Node, JSType);
    boolean result = arrowType.hasEqualParameters(that);
    assertTrue(result);
  }
  
  @Test
  public void testIsEquivalentTo() {
    ArrowType object = new ArrowType(JSTypeRegistry(), Node, JSType);
    boolean result = arrowType.isEquivalentTo(object);
    assertTrue(result);
  }
  
  @Test
  public void testHashCode() {
    int hashCode = arrowType.hashCode();
    assertEquals(123456, hashCode);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    arrowType.getLeastSupertype(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    arrowType.getGreatestSubtype(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    arrowType.testForEquality(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    arrowType.visit(visitor);
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    BooleanLiteralSet booleanOutcomes = arrowType.getPossibleToBooleanOutcomes();
    assertNotNull(booleanOutcomes);
    assertEquals(BooleanLiteralSet.TRUE, booleanOutcomes);
  }
  
  @Test
  public void testResolveInternal() {
    arrowType.resolveInternal(t, scope);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    boolean result = arrowType.hasUnknownParamsOrReturn();
    assertFalse(result);
  }
  
  @Test
  public void testToStringHelper() {
    String string = arrowType.toStringHelper(forAnnotations);
    assertEquals("ArrowType", string);
  }
  
}