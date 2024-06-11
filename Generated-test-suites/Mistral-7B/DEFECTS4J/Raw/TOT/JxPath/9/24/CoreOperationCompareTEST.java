import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri. expressions.Constant;
import org.apache.commons.jxpath.ri.expressions.Variable;
import org.apache.commons.jxpath.ri.iter.AxisIterator;
import org.apache.commons.jxpath.ri.iter.SingletonAxisIterator;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

 private CoreOperationCompare compare;
 private Expression arg1;
 private Expression arg2;
 private EvaluationException exception;

 @BeforeClass
 public static void setupTestClass() {
 // Initialize the class under test
 try {
 compare = new CoreOperationCompare(new Variable("arg1"), new Variable("arg2"));
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Before
 public void setupTest() {
 // Prepare test data
 arg1 = new Constant(1);
 arg2 = new Constant(1);
 }

 @Test
 public void testEqual() {
 try {
 // Test equal values
 JXPathContext context = JXPathContext.newContext(new Object[] { new Integer(1) });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertTrue((Boolean) context.getValue(compare.getName()));

 // Test equal pointers
 Pointer p1 = new Pointer("node1");
 Pointer p2 = new Pointer("node1");
 context = JXPathContext.newContext(new Object[] { p1, p2 });
 compare.setArguments(new Expression[] { p1, p2 });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertTrue((Boolean) context.getValue(compare.getName()));

 // Test equal iterators
 AxisIterator iter1 = new SingletonAxisIterator(new Pointer("node1"));
 AxisIterator iter2 = new SingletonAxisIterator(new Pointer("node1"));
 context = JXPathContext.newContext(new Object[] { iter1, iter2 });
 compare.setArguments(new Expression[] { iter1, iter2 });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertTrue((Boolean) context.getValue(compare.getName()));

 // Test equal strings
 context = JXPathContext.newContext(new Object[] { "test", "test" });
 compare.setArguments(new Expression[] { new Constant("test"), new Constant("test") });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertTrue((Boolean) context.getValue(compare.getName()));

 // Test equal collections
 List<Object> coll1 = Arrays.asList(1, 2, 3);
 List<Object> coll2 = Arrays.asList(1, 2, 3);
 context = JXPathContext.newContext(new Object[] { coll1.iterator(), coll2.iterator() });
 compare.setArguments(new Expression[] { coll1.iterator(), coll2.iterator() });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertTrue((Boolean) context.getValue(compare.getName()));
 } catch (Exception e) {
 exception = e;
 } finally {
 // Verify no exceptions were thrown
 assertNull(exception);
 }
 }

 @Test
 public void testNotEqual() {
 try {
 // Test not equal values
 context = JXPathContext.newContext(new Object[] { new Integer(1), new Integer(2) });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertFalse((Boolean) context.getValue(compare.getName()));

 // Test not equal pointers
 p1 = new Pointer("node1");
 p2 = new Pointer("node2");
 context = JXPathContext.newContext(new Object[] { p1, p2 });
 compare.setArguments(new Expression[] { p1, p2 });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertFalse((Boolean) context.getValue(compare.getName()));

 // Test not equal iterators
 iter1 = new SingletonAxisIterator(new Pointer("node1"));
 iter2 = new SingletonAxisIterator(new Pointer("node2"));
 context = JXPathContext.newContext(new Object[] { iter1, iter2 });
 compare.setArguments(new Expression[] { iter1, iter2 });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertFalse((Boolean) context.getValue(compare.getName()));

 // Test not equal strings
 context = JXPathContext.newContext(new Object[] { "test", "test2" });
 compare.setArguments(new Expression[] { new Constant("test"), new Constant("test2") });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertFalse((Boolean) context.getValue(compare.getName()));

 // Test not equal collections
 coll1 = Arrays.asList(1, 2, 3);
 coll2 = Arrays.asList(1, 2, 4);
 context = JXPathContext.newContext(new Object[] { coll1.iterator(), coll2.iterator() });
 compare.setArguments(new Expression[] { coll1.iterator(), coll2.iterator() });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertFalse((Boolean) context.getValue(compare.getName()));
 } catch (Exception e) {
 exception = e;
 } finally {
 // Verify no exceptions were thrown
 assertNull(exception);
 }
 }

 @Test
 public void testEqualNaN() {
 try {
 // Test equal NaN
 context = JXPathContext.newContext(new Object[] { Double.NaN, Double.NaN });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertTrue((Boolean) context.getValue(compare.getName()));
 } catch (Exception e) {
 exception = e;
 } finally {
 // Verify no exceptions were thrown
 assertNull(exception);
 }
 }

 @Test
 public void testNotEqualNaN() {
 try {
 // Test not equal NaN
 context = JXPathContext.newContext(new Object[] { Double.NaN, 0 });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertFalse((Boolean) context.getValue(compare.getName()));
 } catch (Exception e) {
 exception = e;
 } finally {
 // Verify no exceptions were thrown
 assertNull(exception);
 }
 }

 @Test
 public void testEqualNull() {
 try {
 // Test equal null
 context = JXPathContext.newContext(new Object[] { null, null });
 compare.evaluate(context);
 assertTrue(context.getValue(compare.getName()) instanceof Boolean);
 assertTrue((Boolean) context.getValue(compare.getName()));
 } catch (Exception e) {
 exception = e;
 } finally {
 // Verify no exceptions were thrown
 assertNull(exception);
 }
 }

 @Test(expected = EvaluationException.class)
 public void testEqualDifferentTypes() {
 try {
 // Test equal different types
 context = JXPathContext.newContext(new Object[] { new Integer(1), new String("test") });
 compare.evaluate(context);
 } catch (EvaluationException e) {
 exception = e;
 } finally {
 // Verify an exception was thrown
 assertNotNull(exception);
 }
 }

 @Test(expected = EvaluationException.class)
 public void testNotEqualDifferentTypes() {
 try {
 // Test not equal different types
 context = JXPathContext.newContext(new Object[] { new Integer(1), new String("test") });
 compare.evaluate(context);
 compare.negate();
 } catch (EvaluationException e) {
 exception = e;
 } finally {
 // Verify an exception was thrown
 assertNotNull(exception);
 }
 }
}