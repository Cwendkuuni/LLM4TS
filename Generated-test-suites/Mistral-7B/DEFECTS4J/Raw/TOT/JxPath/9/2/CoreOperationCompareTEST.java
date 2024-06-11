import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(5), new Constant(5)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { 5, 5 });
 EvalContext evalContext = new EvalContext(ctx);
 assertTrue(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testNotEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(5), new Constant(6)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { 5, 6 });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testEqualCollections() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(new Integer[] { 1, 2, 3 }),
 new Constant(new Integer[] { 1, 2, 3 })
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 3 } });
 EvalContext evalContext = new EvalContext(ctx);
 assertTrue(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testNotEqualCollections() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(new Integer[] { 1, 2, 3 }),
 new Constant(new Integer[] { 4, 5, 6 })
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new Integer[] { 1, 2, 3 }, new Integer[] { 4, 5, 6 } });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testEqualIteratorAndValue() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(new Object[] { 1, 2, 3 }),
 new Constant(2)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new Object[] { 1, 2, 3 }, 2 });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testEqualIteratorAndIterator() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(new Object[] { 1, 2, 3 }),
 new Constant(new Object[] { 1, 2, 3 })
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new Object[] { 1, 2, 3 }, new Object[] { 1, 2, 3 } });
 EvalContext evalContext = new EvalContext(ctx);
 assertTrue(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testNotEqualIteratorAndValue() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(new Object[] { 1, 2, 3 }),
 new Constant(4)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new Object[] { 1, 2, 3 }, 4 });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testEqualIteratorAndIteratorDifferentOrder() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(new Object[] { 1, 2, 3 }),
 new Constant(new Object[] { 3, 2, 1 })
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new Object[] { 1, 2, 3 }, new Object[] { 3, 2, 1 } });
 EvalContext evalContext = new EvalContext(ctx);
 assertTrue(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testNullLeft() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null),
 new Constant(5)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { null, 5 });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testNullRight() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(5),
 new Constant(null)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { 5, null });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testNullBoth() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null),
 new Constant(null)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { null, null });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testSelfContextLeft() {
 CoreOperationCompare op = new CoreOperationCompare(
 new SelfContext(),
 new Constant(5)
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new SelfContext() });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }

 @Test
 public void testSelfContextRight() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(5),
 new SelfContext()
 );
 JXPathContext ctx = new JXPathContext(new Object[] { new SelfContext() });
 EvalContext evalContext = new EvalContext(ctx);
 assertFalse(op.evaluateToBoolean(evalContext));
 }
}