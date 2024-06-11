import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.NodeInfoImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(
 new CoreExpressionLiteral(new NodeInfoImpl(1)),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterOrEqualExpression(
 new CoreExpressionLiteral(new NodeInfoImpl(1)),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(2)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessExpression(
 new CoreExpressionLiteral(new NodeInfoImpl(1)),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(0)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessOrEqualExpression(
 new CoreExpressionLiteral(new NodeInfoImpl(1)),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(3)));
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testGreaterExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(
 new CoreExpressionIterator(new Iterate()),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 context.setCurrentNode(new NodeInfoImpl(1));
 Iterator it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(i++);
 }
 }.iterator();
 context.setIterator(it);
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(2);
 }
 }.iterator();
 context.setIterator(it);
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterOrEqualExpression(
 new CoreExpressionIterator(new Iterate()),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 context.setCurrentNode(new NodeInfoImpl(1));
 Iterator it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(i++);
 }
 }.iterator();
 context.setIterator(it);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(2);
 }
 }.iterator();
 context.setIterator(it);
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(3);
 }
 }.iterator();
 context.setIterator(it);
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testLessExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationLessExpression(
 new CoreExpressionIterator(new Iterate()),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 context.setCurrentNode(new NodeInfoImpl(1));
 Iterator it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(i++);
 }
 }.iterator();
 context.setIterator(it);
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(0);
 }
 }.iterator();
 context.setIterator(it);
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationLessOrEqualExpression(
 new CoreExpressionIterator(new Iterate()),
 new CoreExpressionLiteral(new NodeInfoImpl(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1));
 context.setCurrentNode(new NodeInfoImpl(1));
 Iterator it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(i++);
 }
 }.iterator();
 context.setIterator(it);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(2);
 }
 }.iterator();
 context.setIterator(it);
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 it = new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new NodeInfoImpl(3);
 }
 }.iterator();
 context.setIterator(it);
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterExpressionWithDoubles() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterExpression(
 new CoreExpressionLiteral(new Double(1.5)),
 new CoreExpressionLiteral(new Double(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1.5));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(2.5)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterOrEqualExpressionWithDoubles() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterOrEqualExpression(
 new CoreExpressionLiteral(new Double(1.5)),
 new CoreExpressionLiteral(new Double(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1.5));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(2)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(2.5)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessExpressionWithDoubles() {
 CoreOperationRelationalExpression expression = new CoreOperationLessExpression(
 new CoreExpressionLiteral(new Double(1.5)),
 new CoreExpressionLiteral(new Double(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1.5));
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(0.5)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessOrEqualExpressionWithDoubles() {
 CoreOperationRelationalExpression expression = new CoreOperationLessOrEqualExpression(
 new CoreExpressionLiteral(new Double(1.5)),
 new CoreExpressionLiteral(new Double(2))
 );
 EvalContext context = new EvalContext(new Iterate(), new NodeInfoImpl(1.5));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(2)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(2.5)));
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 result = expression.computeValue(new EvalContext(new Iterate(), new NodeInfoImpl(3)));
 assertFalse(result instanceof Boolean);
 }

}