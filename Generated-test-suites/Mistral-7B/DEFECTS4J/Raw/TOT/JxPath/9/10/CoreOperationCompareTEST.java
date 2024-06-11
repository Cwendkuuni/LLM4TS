import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEquals() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(1), new Constant(1)
 );
 JXPathContext ctx = new JXPathContext();
 assertTrue(op.evaluate(ctx).booleanValue());

 op = new CoreOperationCompare(
 new Constant(1), new Constant(2)
 );
 assertFalse(op.evaluate(ctx).booleanValue());
 }

 @Test
 public void testEqualsWithNulls() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(null), new Constant(null)
 );
 assertTrue(op.evaluate(ctx).booleanValue());

 op = new CoreOperationCompare(
 new Constant(null), new Constant(1)
 );
 assertFalse(op.evaluate(ctx).booleanValue());
 }

 @Test(expected = EvaluationException.class)
 public void testEqualsWithDifferentTypes() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(1), new Constant("one")
 );
 }

 @Test
 public void testEqualsWithCollections() {
 Collection<Integer> collection1 = new HashSet<>();
 collection1.add(1);
 collection1.add(2);
 Collection<Integer> collection2 = new HashSet<>();
 collection2.add(1);
 collection2.add(3);
 CoreOperationCompare op = new CoreOperationCompare(
 new Variable("collection1"), new Variable("collection2")
 );
 JXPathContext ctx = new JXPathContext();
 ctx.setVariables(new Variables(new Variables.Binding[] {
 new Variables.Binding("collection1", collection1),
 new Variables.Binding("collection2", collection2)
 }));
 assertTrue(op.evaluate(ctx).booleanValue());

 collection1 = new HashSet<>();
 collection1.add(1);
 collection2 = new HashSet<>();
 collection2.add(2);
 assertFalse(op.evaluate(ctx).booleanValue());
 }

 @Test
 public void testNotEquals() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(1), new Constant(1)
 );
 assertFalse(op.negate().evaluate(ctx).booleanValue());

 op = new CoreOperationCompare(
 new Constant(1), new Constant(2)
 );
 assertTrue(op.negate().evaluate(ctx).booleanValue());
 }

 @Test
 public void testNotEqualsWithNulls() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(1), new Constant(null)
 );
 assertTrue(op.negate().evaluate(ctx).booleanValue());

 op = new CoreOperationCompare(
 new Constant(null), new Constant(1)
 );
 assertTrue(op.evaluate(ctx).booleanValue());
 }

 @Test(expected = EvaluationException.class)
 public void testNotEqualsWithDifferentTypes() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Constant(1), new Constant("one")
 );
 op = op.negate();
 }

 @Test
 public void testNotEqualsWithCollections() {
 Collection<Integer> collection1 = new HashSet<>();
 collection1.add(1);
 collection1.add(2);
 Collection<Integer> collection2 = new HashSet<>();
 collection2.add(1);
 CoreOperationCompare op = new CoreOperationCompare(
 new Variable("collection1"), new Variable("collection2")
 );
 JXPathContext ctx = new JXPathContext();
 ctx.setVariables(new Variables(new Variables.Binding[] {
 new Variables.Binding("collection1", collection1),
 new Variables.Binding("collection2", collection2)
 }));
 assertFalse(op.evaluate(ctx).booleanValue());

 collection1 = new HashSet<>();
 collection1.add(1);
 collection2 = new HashSet<>();
 collection2.add(2);
 op = new CoreOperationCompare(
 new Variable("collection1"), new Variable("collection2")
 );
 assertTrue(op.negate().evaluate(ctx).booleanValue());
 }
}