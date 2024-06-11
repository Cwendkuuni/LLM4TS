import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CoreOperationNotEqualTest {

  @Test
  public void testComputeValue_EqualStrings() {
    CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression("abc"), new Expression("abc"));
    assertFalse(coreOperationNotEqual.computeValue(null));
  }

  @Test
  public void testComputeValue_NotEqualStrings() {
    CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression("abc"), new Expression("def"));
    assertTrue(coreOperationNotEqual.computeValue(null));
  }

  @Test
  public void testComputeValue_EqualIntegers() {
    CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(5), new Expression(5));
    assertFalse(coreOperationNotEqual.computeValue(null));
  }

  @Test
  public void testComputeValue_NotEqualIntegers() {
    CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(5), new Expression(10));
    assertTrue(coreOperationNotEqual.computeValue(null));
  }

  @Test
  public void testComputeValue_NullOperands() {
    CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(null), new Expression(null));
    assertFalse(coreOperationNotEqual.computeValue(null));
  }

  @Test
  public void testComputeValue_OneOperandNull() {
    CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression("abc"), new Expression(null));
    assertTrue(coreOperationNotEqual.computeValue(null));
  }

  @Test
  public void testComputeValue_Error() {
    // Add a test case for any error scenarios or exceptional cases that need to be handled
    // ...

    // Uncomment the following assertion if errors are handled gracefully
    // assertTrue(coreOperationNotEqual.computeValue(null));
  }

  @Test
  public void testGetSymbol() {
    CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, null);
    assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
  }

}