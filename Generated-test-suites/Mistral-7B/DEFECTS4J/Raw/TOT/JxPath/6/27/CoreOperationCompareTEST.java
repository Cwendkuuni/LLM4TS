import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath. ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.apache.commons.jxpath.ri.eval.SimpleContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Variable("left"), new Constant(new Integer(1)));
 JXPathContext ctx = new JXPathContext(new SimpleContext());
 assertTrue(op.compute(ctx));

 op = new CoreOperationCompare(
 new Constant(new Integer(1)), new Variable("right"));
 ctx.setVariable("right", new Integer(1));
 assertTrue(op.compute(ctx));

 op = new CoreOperationCompare(
 new Variable("left"), new Constant(new Integer(2)));
 ctx.setVariable("left", new Integer(1));
 assertFalse(op.compute(ctx));

 op = new CoreOperationCompare(
 new Constant(new Integer(1)), new Variable("right"));
 ctx.setVariable("right", new Integer(2));
 assertFalse(op.compute(ctx));
 }

 @Test
 public void testNotEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Variable("left"), new Constant(new Integer(1)));
 JXPathContext ctx = new JXPathContext(new SimpleContext());
 assertFalse(op.compute(ctx));

 op = new CoreOperationCompare(
 new Constant(new Integer(1)), new Variable("right"));
 ctx.setVariable("right", new Integer(2));
 assertFalse(op.compute(ctx));

 op = new CoreOperationCompare(
 new Variable("left"), new Variable("right"));
 ctx.setVariable("left", new Integer(1));
 ctx.setVariable("right", new Integer(2));
 assertFalse(op.compute(ctx));

 op = new CoreOperationCompare(
 new Variable("left"), new Variable("right"));
 ctx.setVariable("left", new Integer(2));
 ctx.setVariable("right", new Integer(1));
 assertFalse(op.compute(ctx));
 }

 @Test
 public void testEqualWithIterators() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorVariable("left"), new IteratorVariable("right"));
 JXPathContext ctx = new JXPathContext(new SimpleContext());
 ctx.setVariable("left", new IteratorVariableList(new Integer[] { 1, 2, 3 }));
 ctx.setVariable("right", new IteratorVariableList(new Integer[] { 1, 2, 3 }));
 assertTrue(op.compute(ctx));

 ctx.setVariable("right", new IteratorVariableList(new Integer[] { 1, 2, 4 }));
 assertFalse(op.compute(ctx));
 }

 @Test
 public void testNotEqualWithIterators() {
 CoreOperationCompare op = new CoreOperationCompare(
 new IteratorVariable("left"), new IteratorVariable("right"));
 JXPathContext ctx = new JXPathContext(new SimpleContext());
 ctx.setVariable("left", new IteratorVariableList(new Integer[] { 1, 2, 3 }));
 ctx.setVariable("right", new IteratorVariableList(new Integer[] { 1, 2, 2 }));
 assertFalse(op.compute(ctx));

 ctx.setVariable("left", new IteratorVariableList(new Integer[] { 1, 2, 2 }));
 ctx.setVariable("right", new IteratorVariableList(new Integer[] { 1, 2, 3 }));
 assertFalse(op.compute(ctx));
 }

 @Test
 public void testEqualWithNullValues() {
 CoreOperationCompare op = new CoreOperationCompare(
 new Variable("left"), new Variable("right"));
 JXPathContext ctx = new JXPathContext(new SimpleContext());
 ctx.setVariable("left", null);
 ctx.setVariable("right", null);
 assertTrue(op.compute(ctx));
 }
}