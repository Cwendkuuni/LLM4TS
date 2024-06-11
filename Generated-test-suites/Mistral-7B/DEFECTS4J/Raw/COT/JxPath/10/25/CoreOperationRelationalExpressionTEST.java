import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 expression = new CoreOperationRelationalExpression(new Expression[2]) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 }

 @Test
 public void testComputeValue() {
 Object left = 5;
 Object right = 6;
 EvalContext context = new EvalContext(new Object[0]);
 assertEquals(Boolean.TRUE, expression.computeValue(context));
 left = 5.0;
 right = 6.0;
 EvalContext contextDouble = new EvalContext(new Object[0]);
 assertEquals(Boolean.TRUE, expression.computeValue(contextDouble));
 left = new InitialContext();
 right = new InitialContext();
 Iterator leftIterator = (Iterator) left;
 Iterator rightIterator = (Iterator) right;
 leftIterator.next();
 rightIterator.next();
 EvalContext contextIterator = new EvalContext(new Object[0]);
 assertEquals(Boolean.TRUE, expression.computeValue(contextIterator));
 left = 3.5;
 right = 4.5;
 EvalContext contextDouble2 = new EvalContext(new Object[0]);
 assertFalse(expression.computeValue(contextDouble2));
 }

 @Test(expected = ClassCastException.class)
 public void testComputeValueWithInvalidTypes() {
 Object left = new Object();
 Object right = new Object();
 EvalContext context = new EvalContext(new Object[0]);
 expression.computeValue(context);
 }

 @Test
 public void testGetPrecedence() {
 assertEquals(3, expression.getPrecedence());
 }

 @Test
 public void testIsSymmetric() {
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testComputeWithIterators() {
 Iterator leftIterator = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 };
 Iterator rightIterator = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 2;
 }
 };
 EvalContext context = new EvalContext(new Object[0]);
 assertTrue(expression.compute(leftIterator, rightIterator));
 leftIterator = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 2;
 }
 };
 rightIterator = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 };
 assertFalse(expression.compute(leftIterator, rightIterator));
 }

 @Test
 public void testComputeWithCollection() {
 Collection left = new HashSet<>();
 left.add(1);
 left.add(2);
 Collection right = new HashSet<>();
 right.add(2);
 right.add(1);
 EvalContext context = new EvalContext(new Object[0]);
 assertTrue(expression.compute(left.iterator(), right.iterator()));
 }

 @Test
 public void testComputeWithEmptyCollections() {
 Collection left = new HashSet<>();
 Collection right = new HashSet<>();
 EvalContext context = new EvalContext(new Object[0]);
 assertFalse(expression.compute(left.iterator(), right.iterator()));
 }

 @Test
 public void testComputeWithNaNValues() {
 Object left = Double.NaN;
 Object right = Double.NaN;
 EvalContext context = new EvalContext(new Object[0]);
 assertFalse(expression.compute(left, right));
 }

 @Test
 public void testComputeWithNullValues() {
 Object left = null;
 Object right = null;
 EvalContext context = new EvalContext(new Object[0]);
 assertFalse(expression.compute(left, right));
 }

 @Test
 public void testReduce() {
 Object o = 5;
 assertEquals(o, expression.reduce(o));
 o = new InitialContext();
 assertEquals(o, expression.reduce(o));
 o = new HashSet<>();
 o.add(1);
 assertEquals(o.iterator(), expression.reduce(o));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReduceWithIteratorNotSupportingRemove() {
 Object o = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void remove() {
 // Not supported
 }
 };
 assertEquals(o, expression.reduce(o));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsMatchWithIteratorNotSupportingRemove() {
 Object left = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void remove() {
 // Not supported
 }
 };
 Object right = 1;
 EvalContext context = new EvalContext(new Object[0]);
 assertTrue(expression.containsMatch(left, right));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFindMatchWithIteratorsNotSupportingAdd() {
 Object left = new HashSet<>();
 Object leftIterator = left.iterator();
 Iterator rightIterator = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void add(Object e) {
 // Not supported
 }
 };
 EvalContext context = new EvalContext(new Object[0]);
 assertTrue(expression.findMatch((Iterator) leftIterator, rightIterator));
 }

}