import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  
  ArrowType arrowType;
  
  @Before
  public void setUp() {
    arrowType = new ArrowType(new JSTypeRegistry(), null, null);
  }
  
  @Test
  public void testIsSubtype() {
    // Test when 'other' is not an instance of ArrowType
    JSType other = new JSType(new JSTypeRegistry());
    assertFalse(arrowType.isSubtype(other));
    
    // Test when 'other' is an instance of ArrowType
    ArrowType that = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType.isSubtype(that));
    arrowType.returnType = new JSType(new JSTypeRegistry());
    assertTrue(arrowType.isSubtype(that));
  }
  
  @Test
  public void testHasEqualParameters() {
    // Test when 'that' has parameters but 'this' does not
    ArrowType that = new ArrowType(new JSTypeRegistry(), new Node(), null);
    assertFalse(arrowType.hasEqualParameters(that));
    
    // Test when both 'this' and 'that' have null parameters
    arrowType.parameters = null;
    that.parameters = null;
    assertTrue(arrowType.hasEqualParameters(that));
    
    // Test when 'this' and 'that' have parameters, but the parameters are not equal
    arrowType.parameters = new Node();
    that.parameters = new Node();
    assertFalse(arrowType.hasEqualParameters(that));
    
    // Test when 'this' and 'that' have parameters and the parameters are equal
    Node param = new Node();
    arrowType.parameters.addChildToFront(param);
    that.parameters.addChildToFront(param);
    assertTrue(arrowType.hasEqualParameters(that));
  }
  
  @Test
  public void testIsEquivalentTo() {
    // Test when 'other' is not an instance of ArrowType
    JSType other = new JSType(new JSTypeRegistry());
    assertFalse(arrowType.isEquivalentTo(other));
    
    // Test when 'other' is an instance of ArrowType, but the return types are not equivalent
    ArrowType that = new ArrowType(new JSTypeRegistry(), null, null);
    assertFalse(arrowType.isEquivalentTo(that));
    
    // Test when 'other' is an instance of ArrowType and the return types are equivalent,
    // but the parameter lists are not equal
    arrowType.returnType = new JSType(new JSTypeRegistry());
    assertFalse(arrowType.isEquivalentTo(that));
    
    // Test when 'other' is an instance of ArrowType and the return types and parameter lists are equivalent
    arrowType.parameters = new Node();
    that.parameters = new Node();
    assertTrue(arrowType.isEquivalentTo(that));
  }
  
  @Test
  public void testHashCode() {
    // Test when parameters and return type are null
    assertEquals(0, arrowType.hashCode());
    
    // Test when parameters and return type are not null
    arrowType.parameters = new Node();
    arrowType.returnType = new JSType(new JSTypeRegistry());
    assertNotEquals(0, arrowType.hashCode());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType that = new JSType(new JSTypeRegistry());
    arrowType.getLeastSupertype(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType that = new JSType(new JSTypeRegistry());
    arrowType.getGreatestSubtype(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSType that = new JSType(new JSTypeRegistry());
    arrowType.testForEquality(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    Visitor visitor = new Visitor();
    arrowType.visit(visitor);
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    arrowType.resolveInternal(t, scope);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    assertFalse(arrowType.hasUnknownParamsOrReturn());
    arrowType.returnType = new JSType(new JSTypeRegistry());
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }
  
  @Test
  public void testToStringHelper() {
    assertTrue(arrowType.toStringHelper(true).equals(super.toString()));
  }
}