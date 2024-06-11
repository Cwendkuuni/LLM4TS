import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

 private Expression arg1, arg2;
 private CoreOperationNotEqual notEqual;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 arg1 = new ConstantExpression(1);
 arg2 = new ConstantExpression(2);
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 }

 @Test
 public void testComputeValueWithBooleanFalseInputs() {
 assertEquals(Boolean.FALSE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testComputeValueWithBooleanTrueInputs() {
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNullInputs() {
 notEqual = new CoreOperationNotEqual(null, arg2);
 notEqual.computeValue(new EvalContext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNullArg1Input() {
 notEqual = new CoreOperationNotEqual(null, arg2);
 notEqual.computeValue(new EvalContext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNullArg2Input() {
 notEqual = new CoreOperationNotEqual(arg1, null);
 notEqual.computeValue(new EvalContext());
 }

 @Test
 public void testComputeValueWithDifferentTypesInput() {
 arg2 = new ConstantExpression("Hello");
 assertFalse(notEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithSameTypeButDifferentValuesInput() {
 arg2 = new ConstantExpression(3);
 assertFalse(notEqual.computeValue(new EvalContext()).equals(Boolean.TRUE));
 }

 @Test
 public void testComputeValueWithSameTypeAndSameValueInput() {
 arg2 = arg1;
 assertTrue(notEqual.computeValue(new EvalContext()).equals(Boolean.FALSE));
 }

 @Test
 public void testGetSymbol() {
 assertEquals("!=", notEqual.getSymbol());
 }
}