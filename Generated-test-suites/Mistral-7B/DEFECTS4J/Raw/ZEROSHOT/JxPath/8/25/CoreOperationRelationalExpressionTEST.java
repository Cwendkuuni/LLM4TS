import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.NumberConstant;
import org.apache.commons.jxpath.ri.compiler.Sequence;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.apache.commons.jxpath.ri.iter.IteratorIter;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.apache.commons.jxpath.ri.util.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] {
 new Variable("x"), new Constant(new NumberConstant(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext();
 context.setNode(new InitialContext(null));
 Object left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 Object right = new Constant(new NumberConstant(4));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 right = new Constant(new NumberConstant(6));
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] {
 new Variable("x"), new Constant(new NumberConstant(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };
 EvalContext context = new EvalContext();
 context.setNode(new InitialContext(null));
 Object left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 Object right = new Constant(new NumberConstant(5));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 right = new Constant(new NumberConstant(4));
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 right = new Constant(new NumberConstant(5));
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] {
 new Variable("x"), new Constant(new NumberConstant(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };
 EvalContext context = new EvalContext();
 context.setNode(new InitialContext(null));
 Object left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 Object right = new Constant(new NumberConstant(6));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);

 left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 right = new Constant(new NumberConstant(4));
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] {
 new Variable("x"), new Constant(new NumberConstant(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };
 EvalContext context = new EvalContext();
 context.setNode(new InitialContext(null));
 Object left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 Object right = new Constant(new NumberConstant(5));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 right = new Constant(new NumberConstant(6));
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);

 left = new SingletonIterator(new Object[] { new SelfContext(context, 0) });
 right = new Constant(new NumberConstant(5));
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComparisonWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] {
 new Variable("x"), new Variable("y")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext();
 context.setNode(new InitialContext(null));
 Iterator x = new IteratorIter(new Object[] { new SelfContext(context, 0), new SelfContext(context, 1) });
 Iterator y = new IteratorIter(new Object[] { new SelfContext(context, 1), new SelfContext(context, 2) });
 Object left = new Sequence(new CoreExpression[] { new Variable("x"), new Variable("y") });
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 x = new IteratorIter(new Object[] { new SelfContext(context, 0), new SelfContext(context, 1) });
 y = new IteratorIter(new Object[] { new SelfContext(context, 1), new SelfContext(context, 2) });
 left = new Sequence(new CoreExpression[] { new Variable("x"), new Variable("y") });
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 Iterator i = x.iterator();
 while (i.hasNext()) {
 Object obj = i.next();
 if (expression.compute(obj, y.next())) {
 return;
 }
 }
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComparisonWithNumbersAndIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] {
 new Variable("x"), new Variable("y")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext();
 context.setNode(new InitialContext(null));
 Object left = new Sequence(new CoreExpression[] { new Variable("x"), new Constant(new NumberConstant(5)) });
 Iterator y = new IteratorIter(new Object[] { new SelfContext(context, 0), new SelfContext(context, 1) });
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 y = new IteratorIter(new Object[] { new SelfContext(context, 0), new SelfContext(context, 1) });
 left = new Sequence(new CoreExpression[] { new Variable("x"), new Constant(new NumberConstant(5)) });
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 Iterator i = y.iterator();
 while (i.hasNext()) {
 Object obj = i.next();
 if (expression.compute(left, obj)) {
 return;
 }
 }
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComparisonWithNumbersAndNumbers() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] {
 new Variable("x"), new Variable("y")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext();
 context.setNode(new InitialContext(null));
 Object left = new Sequence(new CoreExpression[] { new Variable("x"), new Constant(new NumberConstant(5)) });
 Object right = new Sequence(new CoreExpression[] { new Variable("y"), new Constant(new NumberConstant(4)) });
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);

 left = new Sequence(new CoreExpression[] { new Variable("x"), new Constant(new NumberConstant(5)) });
 right = new Sequence(new CoreExpression[] { new Variable("y"), new Constant(new NumberConstant(5)) });
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 left = new Sequence(new CoreExpression[] { new Variable("x"), new Constant(new NumberConstant(4)) });
 right = new Sequence(new CoreExpression[] { new Variable("y"), new Constant(new NumberConstant(5)) });
 context = new EvalContext();
 context.setNode(new InitialContext(null));
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

}