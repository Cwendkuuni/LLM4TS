import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest extends CompilerTest {

 private CoreOperationRelationalExpression testee = null;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 testee = (CoreOperationRelationalExpression) parseExpression("$a > $b");
 }

 @Test
 public void testComputeValue() {
 Object left = new Integer(5);
 Object right = new Integer(3);
 EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
 assertTrue(testee.computeValue(context));
 left = new Double(3.1);
 right = new Double(3.1);
 assertTrue(testee.computeValue(context));
 left = new String("hello");
 right = new String("world");
 assertFalse(testee.computeValue(context));
 }

 @Test
 public void testGetPrecedence() {
 assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, testee.getPrecedence());
 }

 @Test
 public void testIsSymmetric() {
 assertFalse(testee.isSymmetric());
 }

 @Test
 public void testEvaluateCompare() {
 testee = (CoreOperationRelationalExpression) parseExpression("$a >= $b");
 testee = (CoreOperationRelationalExpression) parseExpression("$a < $b");
 testee = (CoreOperationRelationalExpression) parseExpression("$a <= $b");
 testee = (CoreOperationRelationalExpression) parseExpression("$a != $b");
 testee = (CoreOperationRelationalExpression) parseExpression("$a == $b");
 assertNotNull(testee);
 }

 @Test
 public void testComputeWithNumbers() {
 Object left = new Integer(5);
 Object right = new Integer(3);
 assertTrue(testee.evaluateCompare(0));
 left = new Integer(5);
 right = new Integer(5);
 assertTrue(testee.evaluateCompare(0));
 left = new Integer(5);
 right = new Integer(6);
 assertFalse(testee.evaluateCompare(0));
 left = new Integer(3);
 right = new Integer(5);
 assertFalse(testee.evaluateCompare(0));
 }

 @Test
 public void testComputeWithStrings() {
 Object left = new String("apple");
 Object right = new String("banana");
 assertFalse(testee.evaluateCompare(0));
 left = new String("apple");
 right = new String("apple");
 assertTrue(testee.evaluateCompare(0));
 }

 @Test
 public void testComputeWithIterators() {
 Object left = new IteratorTestSet();
 Object right = new IteratorTestSet();
 EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
 assertTrue(testee.computeValue(context));
 left = new IteratorTestSet();
 right = new IteratorTestSet(1, 2, 3, 4, 5);
 assertFalse(testee.computeValue(context));
 }

 @Test
 public void testComputeWithSelfContexts() {
 Object left = new SelfContext();
 Object right = new SelfContext();
 EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
 assertTrue(testee.computeValue(context));
 left = new SelfContext();
 right = new SelfContext();
 context.setCurrentNode(context.getCurrentNode().getPrevious());
 assertFalse(testee.computeValue(context));
 }

 @Test
 public void testComputeWithInitialContexts() {
 Object left = new InitialContext();
 Object right = new InitialContext();
 EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
 assertFalse(testee.computeValue(context));
 }

 @Test
 public void testReduce() {
 Object left = new SelfContext();
 Object right = new Integer(3);
 Object result = testee.reduce(left);
 assertNotNull(result);
 assertEquals(left.getClass(), result.getClass());
 left = new IteratorTestSet();
 result = testee.reduce(left);
 assertNotNull(result);
 assertEquals(Iterator.class, result.getClass());
 }

 @Test
 public void testFindMatch() {
 Object left = new IteratorTestSet(1, 2, 3, 4, 5);
 Object right = new IteratorTestSet(2, 3, 4, 5);
 HashSet set = new HashSet();
 while (left.hasNext()) {
 set.add(left.next());
 }
 Iterator it = set.iterator();
 Object result = testee.findMatch(it, right);
 assertFalse(result);
 left = new IteratorTestSet(1, 2, 3, 4, 5);
 right = new IteratorTestSet(1, 2, 3, 4, 5);
 it = left.iterator();
 while (it.hasNext()) {
 if (testee.compute(it.next(), right)) {
 result = it.next();
 break;
 }
 }
 assertTrue(result != null);
 }

 @Test
 public void testContainsMatch() {
 Object left = new IteratorTestSet(1, 2, 3, 4, 5);
 Object right = new Integer(3);
 Iterator it = left.iterator();
 assertTrue(testee.containsMatch(it, right));
 }
}

class IteratorTestSet implements Iterator {
 private int current = 0;
 private int[] elements = null;

 public IteratorTestSet() {
 this(new int[0]);
 }

 public IteratorTestSet(int... elements) {
 this.elements = elements;
 }

 @Override
 public boolean hasNext() {
 return current < elements.length;
 }

 @Override
 public Object next() {
 return elements[current++];
 }
}