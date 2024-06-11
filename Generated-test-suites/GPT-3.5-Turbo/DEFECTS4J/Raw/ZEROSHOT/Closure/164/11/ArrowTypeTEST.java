import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testIsEquivalentTo() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    arrowType1.resolveInternal(errorReporter, scope);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
    assertFalse(arrowType1.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(), null, null);
    assertNotNull(arrowType.toStringHelper(false));
  }
}