import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;

 @Test
 public void testConstructor() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 assertNotNull(expression);
 }

 @Test
 public void testComputeValue() {
 Object leftValue = new Integer(5);
 Object rightValue = new Integer(10);
 EvalContext context = new EvalContext(new SelfContext(), new InitialContext());
 expression = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(leftValue), new ConstantExpression(rightValue) });
 Boolean result = (Boolean) expression.computeValue(context);
 assertFalse(result.booleanValue());
 }

 @Test
 public void testComputeValueTrue() {
 Object leftValue = new Integer(5);
 Object rightValue = new Integer(5);
 EvalContext context = new EvalContext(new SelfContext(), new InitialContext());
 expression = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(leftValue), new ConstantExpression(rightValue) });
 Boolean result = (Boolean) expression.computeValue(context);
 assertTrue(result.booleanValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueNullArgs() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 EvalContext context = new EvalContext(new SelfContext(), new InitialContext());
 expression.computeValue(context);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testGetPrecedence() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 int precedence = expression.getPrecedence();
 assertEquals(3, precedence);
 }

 @Test
 public void testIsSymmetric() {
 expression = new CoreOperationRelationalExpression(new Expression[0]);
 boolean symmetric = expression.isSymmetric();
 assertFalse(symmetric);
 }

 @Test
 public void testEvaluateCompare() {
 expression = new CoreOperationRelationalExpression(new Expression[0]) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 expression.args = new Expression[] { new ConstantExpression(5), new ConstantExpression(10) };
 assertTrue(expression.evaluateCompare(0));
 }

 @Test
 public void testReduce() {
 Object o = new Integer(5);
 Object result = expression.reduce(o);
 assertEquals(o, result);
 }

 @Test
 public void testReduceInitialContext() {
 Object o = new InitialContext();
 Object result = expression.reduce(o);
 fail("InitialContext should not be reduced");
 }

 @Test
 public void testReduceCollection() {
 Collection collection = new HashSet();
 collection.add(new Integer(5));
 Object result = expression.reduce(collection);
 assertEquals(collection.iterator().next(), result);
 }

 @Test
 public void testContainsMatch() {
 Iterator it = new java.util.ArrayList(2).iterator();
 it.next();
 it.next();
 Object value = new Integer(5);
 boolean result = expression.containsMatch(it, value);
 assertFalse(result);
 }

 @Test
 public void testContainsMatchTrue() {
 Iterator it = new java.util.ArrayList(2).iterator();
 it.next();
 it.next();
 it.next();
 Object value = new Integer(5);
 boolean result = expression.containsMatch(it, value);
 assertTrue(result);
 }

 @Test
 public void testFindMatch() {
 Iterator lit = new java.util.ArrayList(2).iterator();
 lit.next();
 lit.next();
 Iterator rit = new java.util.ArrayList(2).iterator();
 rit.next();
 rit.next();
 HashSet left = new HashSet();
 left.add(lit.next());
 boolean result = expression.findMatch(lit, rit);
 assertFalse(result);
 }

 @Test
 public void testFindMatchTrue() {
 Iterator lit = new java.util.ArrayList(3).iterator();
 Iterator rit = new java.util.ArrayList(3).iterator();
 HashSet left = new HashSet();
 while (lit.hasNext()) {
 left.add(lit.next());
 }
 while (rit.hasNext()) {
 if (expression.containsMatch(left.iterator(), rit.next())) {
 break;
 }
 }
 assertTrue(result);
 }

}