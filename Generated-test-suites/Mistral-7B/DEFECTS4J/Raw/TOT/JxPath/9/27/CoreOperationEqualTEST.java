import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.junit.Test;

public class CoreOperationEqualTest extends CoreOperationEqualTestCase {

 @Test
 public void testEqual() {
 Expression arg1 = text("5");
 Expression arg2 = text("5");
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.TRUE", Boolean.TRUE, result);
 }

 @Test
 public void testNotEqual() {
 Expression arg1 = text("5");
 Expression arg2 = text("6");
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.FALSE", Boolean.FALSE, result);
 }

 @Test
 public void testNullLeftOperand() {
 Expression arg1 = text(""); // Empty string is used as a proxy for null
 Expression arg2 = text("5");
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 try {
 operation.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNullRightOperand() {
 Expression arg1 = text("5");
 Expression arg2 = text(""); // Empty string is used as a proxy for null
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 try {
 operation.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBothNull() {
 Expression arg1 = text(""); // Empty string is used as a proxy for null
 Expression arg2 = text(""); // Empty string is used as a proxy for null
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 }

 @Test
 public void testStringEqual() {
 Expression arg1 = text("Hello");
 Expression arg2 = text("Hello");
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.TRUE", Boolean.TRUE, result);
 }

 @Test
 public void testStringNotEqual() {
 Expression arg1 = text("Hello");
 Expression arg2 = text("World");
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.FALSE", Boolean.FALSE, result);
 }

 @Test
 public void testNumberEqual() {
 Expression arg1 = number(5);
 Expression arg2 = number(5);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.TRUE", Boolean.TRUE, result);
 }

 @Test
 public void testNumberNotEqual() {
 Expression arg1 = number(5);
 Expression arg2 = number(6);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.FALSE", Boolean.FALSE, result);
 }

 @Test
 public void testBooleanEqual() {
 Expression arg1 = booleanValue(true);
 Expression arg2 = booleanValue(true);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.TRUE", Boolean.TRUE, result);
 }

 @Test
 public void testBooleanNotEqual() {
 Expression arg1 = booleanValue(true);
 Expression arg2 = booleanValue(false);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = operation.computeValue(context);
 assertEquals("Returned expected value: Boolean.FALSE", Boolean.FALSE, result);
 }
}