import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.StringValue;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperation gt = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 Object left = new NumberValue(1.0);
 Object right = new NumberValue(2.0);
 EvalContext context = new EvalContext();
 assertFalse(gt.computeValue(context));

 left = new NumberValue(2.0);
 right = new NumberValue(1.0);
 context.setSingleNodePointer(left);
 assertTrue(gt.computeValue(context));
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperation gte = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };

 Object left = new NumberValue(1.0);
 Object right = new NumberValue(1.0);
 EvalContext context = new EvalContext();
 assertTrue(gte.computeValue(context));

 left = new NumberValue(1.0);
 right = new NumberValue(2.0);
 context.setSingleNodePointer(left);
 assertFalse(gte.computeValue(context));
 }

 @Test
 public void testLessThan() {
 CoreOperation lt = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };

 Object left = new NumberValue(1.0);
 Object right = new NumberValue(2.0);
 EvalContext context = new EvalContext();
 assertFalse(lt.computeValue(context));

 left = new NumberValue(2.0);
 right = new NumberValue(1.0);
 context.setSingleNodePointer(left);
 assertTrue(lt.computeValue(context));
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperation lte = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };

 Object left = new NumberValue(1.0);
 Object right = new NumberValue(1.0);
 EvalContext context = new EvalContext();
 assertTrue(lte.computeValue(context));

 left = new NumberValue(1.0);
 right = new NumberValue(2.0);
 context.setSingleNodePointer(left);
 assertTrue(lte.computeValue(context));
 }

 @Test
 public void testComparingIterators() {
 CoreOperation gt = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 Object left = new ArrayIter(new Object[] { 1, 2, 3, 4 });
 Object right = new ArrayIter(new Object[] { 1, 2, 3, 5 });
 EvalContext context = new EvalContext();
 context.setSingleNodePointer(left.iterator().next());
 assertFalse(gt.computeValue(context));

 left = new ArrayIter(new Object[] { 1, 2, 3, 4 });
 right = new ArrayIter(new Object[] { 1, 2, 3, 4 });
 context.setSingleNodePointer(left.iterator().next());
 assertTrue(gt.computeValue(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComparingNonComparable() {
 CoreOperation gt = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") });
 Object left = new StringValue("1");
 Object right = new NumberValue(1);
 EvalContext context = new EvalContext();
 gt.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComparingDifferentTypes() {
 CoreOperation gt = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") });
 Object left = new StringValue("1");
 Object right = new StringValue("2");
 EvalContext context = new EvalContext();
 gt.computeValue(context);
 }

 @Test
 public void testComparingSelfContexts() {
 CoreOperation gt = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 Object left = new SelfContext();
 Object right = new SelfContext();
 EvalContext context = new EvalContext();
 context.setSingleNodePointer(left);
 assertFalse(gt.computeValue(context));

 left = new SelfContext();
 right = new SelfContext();
 context.setSingleNodePointer(right);
 assertTrue(gt.computeValue(context));
 }

 @Test
 public void testComparingSingleNodeIterators() {
 CoreOperation gt = new CoreOperationRelationalExpression(new CoreOperation[] {
 new Variable("left"), new Variable("right") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 Object left = new SingleNodeIterator(new Object[] { new Object() {}});
 Object right = new SingleNodeIterator(new Object[] { new Object() {}});
 EvalContext context = new EvalContext();
 context.setSingleNodePointer(left.iterator().next());
 assertFalse(gt.computeValue(context));

 left = new SingleNodeIterator(new Object[] { new Object() {}});
 right = new SingleNodeIterator(new Object[] { new Object() {}});
 context.setSingleNodePointer(right.iterator().next());
 assertTrue(gt.computeValue(context));
 }
}