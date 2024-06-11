import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationInteger;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression createExpression(Class<? extends CoreOperationRelationalExpression> clazz, Object left, Object right) {
 try {
 return clazz.getDeclaredConstructor(Expression[].class).newInstance(new Expression[]{new CoreOperationInteger(left), new CoreOperationInteger(right)});
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testComputeValue() {
 CoreOperationRelationalExpression expression1 = createExpression(CoreOperationGT.class, 5, 3);
 EvalContext context = new EvalContext(new Object[0]);
 Object result = expression1.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 CoreOperationRelationalExpression expression2 = createExpression(CoreOperationGE.class, 5, 3);
 result = expression2.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 CoreOperationRelationalExpression expression3 = createExpression(CoreOperationLT.class, 5, 3);
 result = expression3.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);

 CoreOperationRelationalExpression expression4 = createExpression(CoreOperationLE.class, 5, 3);
 result = expression4.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithNullArguments() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationGT.class, null, 3);
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationGT.class, 5, 3);
 int precedence = expression.getPrecedence();
 assertEquals(3, precedence);
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationGT.class, 5, 3);
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testEvaluateCompare() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationGT.class, 5, 3);
 assertTrue(expression.evaluateCompare(1));
 assertFalse(expression.evaluateCompare(0));
 assertFalse(expression.evaluateCompare(-1));
 }

 @Test
 public void testEvaluateCompareWithSameValue() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationEQ.class, 5, 5);
 assertTrue(expression.evaluateCompare(0));
 }

 @Test
 public void testEvaluateCompareWithDifferentValueTypes() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationNE.class, new CoreOperationInteger(5), new CoreOperationString("5"));
 assertTrue(expression.evaluateCompare(-1));
 }

 @Test
 public void testReduce() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationNE.class, new CoreOperationInteger(5), new CoreOperationInteger(5));
 Object left = expression.reduce(new CoreOperationInteger(5));
 assertEquals(5, InfoSetUtil.intValue(left));

 expression = createExpression(CoreOperationGT.class, new CoreOperationInteger(5), new SelfContext());
 left = expression.reduce(new SelfContext());
 assertNull(left);

 expression = createExpression(CoreOperationGT.class, new SelfContext(), new CoreOperationInteger(5));
 left = expression.reduce(new SelfContext());
 assertNull(left);

 expression = createExpression(CoreOperationNE.class, new CoreOperationInteger(5), new CoreOperationString("5"));
 left = expression.reduce(new CoreOperationInteger(5));
 assertNull(left);
 }

 @Test
 public void testContainsMatch() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationNE.class, new CoreOperationInteger(5), new CoreOperationInteger(5));
 Iterator it = Arrays.asList(new Integer[]{1, 2, 3, 5, 6, 7}).iterator();
 assertFalse(expression.containsMatch(it, new Integer(5)));

 expression = createExpression(CoreOperationGT.class, new CoreOperationInteger(5), new CoreOperationInteger(3));
 it = Arrays.asList(new Integer[]{1, 2, 3, 5, 6, 7}).iterator();
 assertFalse(expression.containsMatch(it, new Integer(5)));

 it = Arrays.asList(new Integer[]{1, 2, 3, 5, 6, 7}).iterator();
 assertFalse(expression.containsMatch(it, new Integer(3)));

 it = Arrays.asList(new Integer[]{5, 6, 7, 8, 9, 10}).iterator();
 assertTrue(expression.containsMatch(it, new Integer(5)));

 it = Arrays.asList(new Integer[]{5, 5, 5, 5, 5, 5}).iterator();
 assertTrue(expression.containsMatch(it, new Integer(5)));
 }

 @Test
 public void testFindMatch() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationNE.class, new HashSet<>(Arrays.asList(new Integer[]{1, 2, 3, 5, 6, 7})), new CoreOperationInteger(5));
 Iterator it1 = Arrays.asList(new Integer[]{1, 2, 3, 5, 6, 7}).iterator();
 Iterator it2 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6}).iterator();
 assertFalse(expression.findMatch(it1, it2));

 it1 = Arrays.asList(new Integer[]{1, 2, 3, 5, 6, 7}).iterator();
 it2 = Arrays.asList(new Integer[]{1, 2, 3, 5, 6, 7, 5}).iterator();
 assertTrue(expression.findMatch(it1, it2));

 it1 = Arrays.asList(new Integer[]{1, 2, 3, 5, 5, 6, 7}).iterator();
 it2 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7}).iterator();
 assertFalse(expression.findMatch(it1, it2));

 it1 = Arrays.asList(new Integer[]{1, 2, 5, 5, 6, 7, 5}).iterator();
 it2 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7}).iterator();
 assertFalse(expression.findMatch(it1, it2));

 it1 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 5, 6, 7}).iterator();
 it2 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7}).iterator();
 assertFalse(expression.findMatch(it1, it2));

 it1 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 5, 5, 6, 7}).iterator();
 it2 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 5, 5, 5, 6, 7}).iterator();
 assertTrue(expression.findMatch(it1, it2));
 }

}