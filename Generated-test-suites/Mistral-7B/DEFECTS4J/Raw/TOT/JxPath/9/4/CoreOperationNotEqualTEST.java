import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqualExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualWithBooleanValues() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreNumberLiteralExpression(true), new CoreNumberLiteralExpression(false));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualWithStringValues() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreStringLiteralExpression("hello"), new CoreStringLiteralExpression("world"));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualWithNullAndNonNullValues() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreNullExpression(), new CoreNumberLiteralExpression(1));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 CoreOperationNotEqualExpression notEqualExpression2 = new CoreOperationNotEqualExpression(new CoreNumberLiteralExpression(1), new CoreNullExpression());
 Object result2 = new CoreOperationNotEqual(new EvalContext(), notEqualExpression2).computeValue(new EvalContext());
 assertTrue(result2 instanceof Boolean);
 assertFalse((Boolean) result2);
 }

 @Test
 public void testNotEqualWithSameValues() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreNumberLiteralExpression(1), new CoreNumberLiteralExpression(1));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualWithNaNAndFiniteNumber() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreNumberLiteralExpression(Double.NaN), new CoreNumberLiteralExpression(1.0));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualWithEmptyListAndListWithElements() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreListLiteralExpression(), new CoreListLiteralExpression(new CoreNumberLiteralExpression(1)));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqualWithNullExpression() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(null, new CoreNumberLiteralExpression(1));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertNull(result);
 }

 @Test
 public void testNotEqualWithNonBooleanResult() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreNumberLiteralExpression(1), new CoreNumberLiteralExpression(1).add(new CoreNumberLiteralExpression(1)));
 Object result = new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext());
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithWrongNumberOfArguments() {
 CoreOperationNotEqualExpression notEqualExpression = new CoreOperationNotEqualExpression(new CoreNumberLiteralExpression(1), new CoreNumberLiteralExpression(1), new CoreNumberLiteralExpression(1));
 assertFalse(new CoreOperationNotEqual(new EvalContext(), notEqualExpression).computeValue(new EvalContext()) instanceof Boolean);
 }
}