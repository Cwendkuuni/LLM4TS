import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.expr.ExpressionTest;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest extends ExpressionTest {

 @Test
 public void testEqualTrue() {
 Expression arg1 = createNumber(1);
 Expression arg2 = createNumber(1);
 CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = opEqual.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testEqualFalse() {
 Expression arg1 = createNumber(1);
 Expression arg2 = createNumber(2);
 CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = opEqual.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqualNullArg1() {
 Expression arg1 = null;
 Expression arg2 = createNumber(1);
 CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = opEqual.computeValue(context);
 Assert.assertNull(result);
 }

 @Test
 public void testEqualNullArg2() {
 Expression arg1 = createNumber(1);
 Expression arg2 = null;
 CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = opEqual.computeValue(context);
 Assert.assertNull(result);
 }

 @Test
 public void testEqualNullArg1AndArg2() {
 Expression arg1 = null;
 Expression arg2 = null;
 CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = opEqual.computeValue(context);
 Assert.assertNull(result);
 }

 @Test(expected = AssertionError.class)
 public void testUnequalTypes() {
 Expression arg1 = createNumber(1);
 Expression arg2 = createString("1");
 CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 Object result = opEqual.computeValue(context);
 }

 @Test
 public void testEqualValuesWithDifferentExpressions() {
 Expression arg1Exp = createNumber(1);
 Expression arg2Exp = createVariable("value");
 Variable variable = new Variable("value", createNumber(1));
 OpVariable opVariable = new OpVariable(variable, this);
 Expression arg1 = new ConstantExpression(arg1Exp);
 Expression arg2 = opVariable;
 CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
 EvalContext context = createContext();
 context.setVariables(new Variable[] { variable });
 Object result = opEqual.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

}