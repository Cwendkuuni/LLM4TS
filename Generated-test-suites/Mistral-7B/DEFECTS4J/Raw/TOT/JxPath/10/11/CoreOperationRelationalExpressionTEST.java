import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg1;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg2;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(
 new CoreOperationRelationalExpressionarg1(new NumberValue(5)),
 new CoreOperationRelationalExpressionarg2(new NumberValue(3))
 );

 Iterator left = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(6);
 }
 };

 Iterator right = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(4);
 }
 };

 EvalContext context = new EvalContext();
 context.setCurrentNode(new SelfContext());
 context.setCurrentNode(left.next());

 assertTrue(exp.computeValue(context));

 context.setCurrentNode(left.next());
 assertFalse(exp.computeValue(context));

 context.setCurrentNode(right.next());
 assertFalse(exp.computeValue(context));
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(
 new CoreOperationRelationalExpressionarg1(new NumberValue(5)),
 new CoreOperationRelationalExpressionarg2(new NumberValue(5))
 );

 Iterator left = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(6);
 }
 };

 Iterator right = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(5);
 }
 };

 EvalContext context = new EvalContext();
 context.setCurrentNode(new SelfContext());
 context.setCurrentNode(left.next());

 assertTrue(exp.computeValue(context));

 context.setCurrentNode(left.next());
 assertTrue(exp.computeValue(context));

 context.setCurrentNode(right.next());
 assertTrue(exp.computeValue(context));
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(
 new CoreOperationRelationalExpressionarg1(new NumberValue(3)),
 new CoreOperationRelationalExpressionarg2(new NumberValue(5))
 );

 Iterator left = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(3);
 }
 };

 Iterator right = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(6);
 }
 };

 EvalContext context = new EvalContext();
 context.setCurrentNode(new SelfContext());
 context.setCurrentNode(left.next());

 assertFalse(exp.computeValue(context));

 context.setCurrentNode(left.next());
 assertFalse(exp.computeValue(context));

 context.setCurrentNode(right.next());
 assertFalse(exp.computeValue(context));
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(
 new CoreOperationRelationalExpressionarg1(new NumberValue(3)),
 new CoreOperationRelationalExpressionarg2(new NumberValue(5))
 );

 Iterator left = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(3);
 }
 };

 Iterator right = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NumberValue(5);
 }
 };

 EvalContext context = new EvalContext();
 context.setCurrentNode(new SelfContext());
 context.setCurrentNode(left.next());

 assertFalse(exp.computeValue(context));

 context.setCurrentNode(left.next());
 assertFalse(exp.computeValue(context));

 context.setCurrentNode(right.next());
 assertTrue(exp.computeValue(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLeft() {
 CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(
 new CoreOperationRelationalExpressionarg1(null),
 new CoreOperationRelationalExpressionarg2(new NumberValue(5))
 );
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRight() {
 CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(
 new CoreOperationRelationalExpressionarg1(new NumberValue(5)),
 new CoreOperationRelationalExpressionarg2(null)
 );
 }
}