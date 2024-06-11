import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IterateIterable;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.axes.AxisCollection;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(3));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Node node = new Node("root", null, null, null, null, null, null, null, null, null);
 infoSet.addItem(node);
 IterateIterable iterable = Iterators.iterate(node);
 axis = axes.getAxis("self::node()");
 context.setCurrentNode(node);
 context.setCurrentAxis(axis);
 context.setCurrentIterate(iterable);
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testGreaterThanWithDoubleValue() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(3.5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testGreaterThanWithNullValues() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(null));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Node node = new Node("root", null, null, null, null, null, null, null, null, null);
 infoSet.addItem(node);
 IterateIterable iterable = Iterators.iterate(node);
 axis = axes.getAxis("self::node()");
 context.setCurrentNode(node);
 context.setCurrentAxis(axis);
 context.setCurrentIterate(iterable);
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testGreaterThanOrEqualWithDoubleValue() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(5.5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testGreaterThanOrEqualWithNullValues() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(null));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(3), new CoreConstantExpression(5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Node node = new Node("root", null, null, null, null, null, null, null, null, null);
 infoSet.addItem(node);
 IterateIterable iterable = Iterators.iterate(node);
 axis = axes.getAxis("self::node()");
 context.setCurrentNode(node);
 context.setCurrentAxis(axis);
 context.setCurrentIterate(iterable);
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((boolean) result);
 }

 @Test
 public void testLessThanWithDoubleValue() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(2.5), new CoreConstantExpression(3.5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5), new CoreConstantExpression(5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Node node = new Node("root", null, null, null, null, null, null, null, null, null);
 infoSet.addItem(node);
 IterateIterable iterable = Iterators.iterate(node);
 axis = axes.getAxis("self::node()");
 context.setCurrentNode(node);
 context.setCurrentAxis(axis);
 context.setCurrentIterate(iterable);
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithDoubleValue() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(5.5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithNullValues() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreConstantExpression(null), new CoreConstantExpression(5));
 Compiler compiler = new Compiler();
 InfoSet infoSet = new InfoSet();
 AxisCollection axes = new AxisCollection();
 EvalContext context = new EvalContext(new Object[] { infoSet, axes });
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }
}