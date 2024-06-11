import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.CompilerTestSupport;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest extends CompilerTestSupport {

 private CoreOperationCompare op;

 @Before
 public void setUp() throws Exception {
 Pojo p1 = new Pojo("one", 1);
 Pojo p2 = new Pojo("two", 2);
 Pojo p3 = new Pojo("three", 3);
 JXPathContext context = createContext(new Object[] { p1, p2, p3 });
 op = new CoreOperationCompare(
 new Constant(context, "p1"),
 new Constant(context, "p1")
 );
 }

 @Test
 public void testEqualEqual() {
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualDifferent() {
 Expression right = new Variable("$context:self::node()");
 op = new CoreOperationCompare(
 newConstant(context, "p1"),
 right
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualNull() {
 op = new CoreOperationCompare(
 new Constant(context, null),
 new Constant(context, "p1")
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualEmpty() {
 op = new CoreOperationCompare(
 new Constant(context, ""),
 new Constant(context, "")
 );
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualString() {
 op = new CoreOperationCompare(
 new Constant(context, "one"),
 new Constant(context, "one")
 );
 assertTrue(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, "one"),
 new Constant(context, "two")
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualBoolean() {
 op = new CoreOperationCompare(
 new Constant(context, true),
 new Constant(context, true)
 );
 assertTrue(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, true),
 new Constant(context, false)
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualNumber() {
 op = new CoreOperationCompare(
 new Constant(context, 1.0),
 new Constant(context, 1.0)
 );
 assertTrue(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, 1.0),
 new Constant(context, 2.0)
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualNode() {
 op = new CoreOperationCompare(
 new Variable("$context:self::node()"),
 new Variable("$context:self::node()")
 );
 assertTrue(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Variable("$context:self::node()"),
 new Constant(context, p1)
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualCollection() {
 Collection coll = new HashSet();
 coll.add(p1);
 op = new CoreOperationCompare(
 new Constant(context, coll),
 new Constant(context, coll)
 );
 assertTrue(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, coll),
 new Constant(context, p2)
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualIterator() {
 op = new CoreOperationCompare(
 new Constant(context, new Iterator[] { p1.iterator() }),
 new Constant(context, new Iterator[] { p1.iterator() })
 );
 assertTrue(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, new Iterator[] { p1.iterator() }),
 new Constant(context, new Iterator[] { p2.iterator() })
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testEqualMixed() {
 op = new CoreOperationCompare(
 new Constant(context, "one"),
 new Constant(context, 1)
 );
 assertFalse(op.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualEqual() {
 assertFalse(op.negate().evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualDifferent() {
 assertTrue(op.negate().evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualNull() {
 op = new CoreOperationCompare(
 new Constant(context, null),
 new Constant(context, "p1")
 );
 assertFalse(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, "p1"),
 new Constant(context, null)
 );
 assertTrue(op.negate().evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualEmpty() {
 op = new CoreOperationCompare(
 new Constant(context, ""),
 new Constant(context, "")
 );
 assertFalse(op.evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, ""),
 new Constant(context, "p1")
 );
 assertTrue(op.negate().evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualString() {
 op = new CoreOperationCompare(
 new Constant(context, "one"),
 new Constant(context, "one")
 );
 assertFalse(op.negate().evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, "one"),
 new Constant(context, "two")
 );
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualBoolean() {
 op = new CoreOperationCompare(
 new Constant(context, true),
 new Constant(context, true)
 );
 assertFalse(op.negate().evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, true),
 new Constant(context, false)
 );
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualNumber() {
 op = new CoreOperationCompare(
 new Constant(context, 1.0),
 new Constant(context, 1.0)
 );
 assertFalse(op.negate().evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, 1.0),
 new Constant(context, 2.0)
 );
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualNode() {
 op = new CoreOperationCompare(
 new Variable("$context:self::node()"),
 new Variable("$context:self::node()")
 );
 assertFalse(op.negate().evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Variable("$context:self::node()"),
 new Constant(context, p1)
 );
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualCollection() {
 Collection coll = new HashSet();
 coll.add(p1);
 op = new CoreOperationCompare(
 new Constant(context, coll),
 new Constant(context, coll)
 );
 assertFalse(op.negate().evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, coll),
 new Constant(context, p2)
 );
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualIterator() {
 op = new CoreOperationCompare(
 new Constant(context, new Iterator[] { p1.iterator() }),
 new Constant(context, new Iterator[] { p1.iterator() })
 );
 assertFalse(op.negate().evaluateToBoolean(context));
 op = new CoreOperationCompare(
 new Constant(context, new Iterator[] { p1.iterator() }),
 new Constant(context, new Iterator[] { p2.iterator() })
 );
 assertTrue(op.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualMixed() {
 op = new CoreOperationCompare(
 new Constant(context, "one"),
 new Constant(context, 1)
 );
 assertTrue(op.negate().evaluateToBoolean(context));
 }

}