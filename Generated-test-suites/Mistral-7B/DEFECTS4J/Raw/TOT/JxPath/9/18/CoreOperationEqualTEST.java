import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ExpressionFactory;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

 @Test
 public void testEqualIntegerValue() {
 Expression exp1 = ExpressionFactory.createNumber(1);
 Expression exp2 = ExpressionFactory.createNumber(1);
 CoreOperationEqual equal = new CoreOperationEqual(exp1, exp2);
 assertEquals(Boolean.TRUE, equal.computeValue(new EvalContext()));
 }

 @Test
 public void testEqualStringValue() {
 Expression exp1 = ExpressionFactory.createString("test");
 Expression exp2 = ExpressionFactory.createString("test");
 CoreOperationEqual equal = new CoreOperationEqual(exp1, exp2);
 assertEquals(Boolean.TRUE, equal.computeValue(new EvalContext()));
 }

 @Test
 public void testEqualBooleanValue() {
 Expression exp1 = ExpressionFactory.createBoolean(true);
 Expression exp2 = ExpressionFactory.createBoolean(true);
 CoreOperationEqual equal = new CoreOperationEqual(exp1, exp2);
 assertEquals(Boolean.TRUE, equal.computeValue(new EvalContext()));
 }

 @Test
 public void testEqualNullValue() {
 Expression exp1 = ExpressionFactory.createNull();
 Expression exp2 = ExpressionFactory.createNull();
 CoreOperationEqual equal = new CoreOperationEqual(exp1, exp2);
 assertEquals(Boolean.TRUE, equal.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualIntegerValue() {
 Expression exp1 = ExpressionFactory.createNumber(1);
 Expression exp2 = ExpressionFactory.createNumber(2);
 CoreOperationEqual notEqual = new CoreOperationEqual(exp1, exp2);
 assertFalse(notEqual.computeValue(new EvalContext()).booleanValue());
 }

 @Test
 public void testNotEqualStringValue() {
 Expression exp1 = ExpressionFactory.createString("test");
 Expression exp2 = ExpressionFactory.createString("anotherTest");
 CoreOperationEqual notEqual = new CoreOperationEqual(exp1, exp2);
 assertFalse(notEqual.computeValue(new EvalContext()).booleanValue());
 }

 @Test
 public void testNotEqualBooleanValue() {
 Expression exp1 = ExpressionFactory.createBoolean(true);
 Expression exp2 = ExpressionFactory.createBoolean(false);
 CoreOperationEqual notEqual = new CoreOperationEqual(exp1, exp2);
 assertFalse(notEqual.computeValue(new EvalContext()).booleanValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWrongNumberOfArguments() {
 CoreOperationEqual equal = new CoreOperationEqual(ExpressionFactory.createNumber(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArguments() {
 CoreOperationEqual equal = new CoreOperationEqual(null, ExpressionFactory.createNumber(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMismatchedTypes() {
 Expression exp1 = ExpressionFactory.createNumber(1);
 Expression exp2 = ExpressionFactory.createString("test");
 CoreOperationEqual equal = new CoreOperationEqual(exp1, exp2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMismatchedNullTypes() {
 CoreOperationEqual equal = new CoreOperationEqual(ExpressionFactory.createNull(), ExpressionUtil.createStringConst("test"));
 }
}