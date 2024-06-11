import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  
  private ArrowType arrowType;
  
  @Before
  public void setUp() {
    arrowType = new ArrowType(new JSTypeRegistry(), null, null);
  }
  
  @Test
  public void testIsSubtype() {
    JSType other = new ArrowType(new JSTypeRegistry(), null, null);
    boolean result = arrowType.isSubtype(other);
    Assert.assertEquals(false, result);
  }
  
  @Test
  public void testHasEqualParameters() {
    ArrowType that = new ArrowType(new JSTypeRegistry(), null, null);
    boolean result = arrowType.hasEqualParameters(that);
    Assert.assertEquals(true, result);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType that = new ArrowType(new JSTypeRegistry(), null, null);
    arrowType.getLeastSupertype(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType that = new ArrowType(new JSTypeRegistry(), null, null);
    arrowType.getGreatestSubtype(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSType that = new ArrowType(new JSTypeRegistry(), null, null);
    arrowType.testForEquality(that);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAcceptVisitor() {
    Visitor<T> visitor = new Visitor<T>();
    arrowType.visit(visitor);
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
    Assert.assertEquals(BooleanLiteralSet.TRUE, result);
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    JSType result = arrowType.resolveInternal(t, scope);
    Assert.assertEquals(arrowType, result);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    boolean result = arrowType.hasUnknownParamsOrReturn();
    Assert.assertEquals(false, result);
  }
  
  @Test
  public void testToStringHelper() {
    String result = arrowType.toStringHelper(false);
    Assert.assertEquals(arrowType.toString(), result);
  }
}