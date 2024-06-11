import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression createTestExpression(Class<? extends CoreOperationRelationalExpression> expressionClass, Object... args) {
 return newInstance(expressionClass, args);
 }

 @Test
 public void testConstructor() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class, new CoreOperationIdentifier("a"), new CoreOperationIdentifier("b"));
 assertNotNull(expression);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullArg() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class, null, new CoreOperationIdentifier("b"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorTwoNullArgs() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class, null, null);
 }

 @Test
 public void testComputeValue() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class, new CoreOperationNumber(1.0), new CoreOperationNumber(2.0));
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 expression = createTestExpression(CoreOperationLtExpression.class, new CoreOperationNumber(2.0), new CoreOperationNumber(1.0));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 expression = createTestExpression(CoreOperationGtExpression.class, new CoreOperationNumber(2.0), new CoreOperationNumber(2.0));
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class);
 int precedence = expression.getPrecedence();
 assertEquals(3, precedence);
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class);
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testEvaluateCompare() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class);
 assertTrue(expression.evaluateCompare(1));
 assertFalse(expression.evaluateCompare(0));
 assertFalse(expression.evaluateCompare(-1));
 }

 @Test
 public void testComputeWithInitialContexts() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class, new InitialContext(), new InitialContext());
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testComputeWithIterators() {
 Collection<Object> left = new ArrayList<>();
 left.add(1);
 left.add(2);
 Iterator<Object> leftIterator = left.iterator();

 Collection<Object> right = new ArrayList<>();
 right.add(1);
 right.add(2);
 right.add(3);
 Iterator<Object> rightIterator = right.iterator();

 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class, leftIterator, rightIterator);
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 left.add(3);
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeWithDoubles() {
 CoreOperationRelationalExpression expression = createTestExpression(CoreOperationGtExpression.class, new CoreOperationNumber(1.5), new CoreOperationNumber(2.5));
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 expression = createTestExpression(CoreOperationGtExpression.class, new CoreOperationNumber(2.5), new CoreOperationNumber(1.5));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testReduce() {
 Object object = new CoreOperationIdentifier("a");
 Object reducedObject = new CoreOperationRelationalExpression(new CoreOperationIdentifier("a"))
 .reduce(object);
 assertSame(object, reducedObject);

 object = new SelfContext();
 reducedObject = new CoreOperationRelationalExpression(new CoreOperationIdentifier("a"))
 .reduce(object);
 assertNotNull(reducedObject);
 }

 @Test
 public void testContainsMatch() {
 Collection<Object> collection = new ArrayList<>();
 collection.add(1);
 collection.add(2);
 collection.add(3);
 Iterator<Object> iterator = collection.iterator();

 CoreOperationRelationalExpression expression = new CoreOperationGtExpression(iterator, new CoreOperationNumber(2));
 assertTrue(expression.containsMatch(iterator, new CoreOperationNumber(3)));
 assertFalse(expression.containsMatch(iterator, new CoreOperationNumber(1)));
 }

 @Test
 public void testFindMatch() {
 Collection<Object> left = new ArrayList<>();
 left.add(1);
 left.add(2);
 left.add(3);
 Collection<Object> right = new ArrayList<>();
 right.add(1);
 right.add(2);
 right.add(3);
right.add(4);
 Iterator<Object> leftIterator = left.iterator();
 Iterator<Object> rightIterator = right.iterator();

 CoreOperationRelationalExpression expression = new CoreOperationGtExpression(leftIterator, rightIterator);
 Iterator<Object> resultIterator = expression.findMatch(leftIterator, rightIterator);
 assertFalse(resultIterator.hasNext());

 left.add(4);
 resultIterator = expression.findMatch(leftIterator, rightIterator);
 assertTrue(resultIterator.hasNext());
 assertTrue(resultIterator.next() instanceof Integer);
 assertEquals(4, (int) resultIterator.next());
 }

}