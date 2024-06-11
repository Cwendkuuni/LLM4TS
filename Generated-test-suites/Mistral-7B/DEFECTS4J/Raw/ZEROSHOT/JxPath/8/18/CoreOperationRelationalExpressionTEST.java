import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateResult;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperation expression = new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperationConstant(1), new CoreOperationConstant(2) }, ">") {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 JXPathContext context = new JXPathContext();
 context.setNodeInfo(new NodeInfo("root"));
 Path path = context.makePath("/root");
 EvalContext evalContext = new EvalContext(path, context);
 Object result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperation expression = new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperationConstant(1), new CoreOperationConstant(2) }, ">=") {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };

 JXPathContext context = new JXPathContext();
 context.setNodeInfo(new NodeInfo("root"));
 Path path = context.makePath("/root");
 EvalContext evalContext = new EvalContext(path, context);
 Object result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.TRUE);

 context = new JXPathContext();
 context.setNodeInfo(new NodeInfo("root"));
 path = context.makePath("/root");
 evalContext = new EvalContext(path, context);
 NodeInfo node1 = new NodeInfo("child1", null, context.makeNodeIterator(new Object[] { new Integer(1) }), null, null, null);
 evalContext.push(node1);
 Object left = node1.getValue();
 NodeInfo node2 = new NodeInfo("child2", null, context.makeNodeIterator(new Object[] { new Integer(2) }), null, null, null);
 evalContext.push(node2);
 Object right = node2.getValue();
 evalContext.pop();
 evalContext.pop();
 evalContext.push(new EvalContext(null, context));
 result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.FALSE);
 }

 @Test
 public void testLessThanExpression() {
 CoreOperation expression = new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperationConstant(2), new CoreOperationConstant(1) }, "<") {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };

 JXPathContext context = new JXPathContext();
 context.setNodeInfo(new NodeInfo("root"));
 Path path = context.makePath("/root");
 EvalContext evalContext = new EvalContext(path, context);
 Object result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.FALSE);
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperation expression = new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperationConstant(2), new CoreOperationConstant(1) }, "<=") {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };

 JXPathContext context = new JXPathContext();
 context.setNodeInfo(new NodeInfo("root"));
 Path path = context.makePath("/root");
 EvalContext evalContext = new EvalContext(path, context);
 Object result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.TRUE);

 context = new JXPathContext();
 context.setNodeInfo(new NodeInfo("root"));
 path = context.makePath("/root");
 evalContext = new EvalContext(path, context);
 NodeInfo node1 = new NodeInfo("child1", null, context.makeNodeIterator(new Object[] { new Integer(1) }), null, null, null);
 evalContext.push(node1);
 Object left = node1.getValue();
 NodeInfo node2 = new NodeInfo("child2", null, context.makeNodeIterator(new Object[] { new Integer(2) }), null, null, null);
 evalContext.push(node2);
 Object right = node2.getValue();
 evalContext.pop();
 evalContext.pop();
 evalContext.push(new EvalContext(null, context));
 result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testIteratorsComparison() {
 CoreOperation expression = new CoreOperationRelationalExpression(new CoreOperation[] { Iterators.EMPTY, Iterators.SEQUENCE(new IterateResult[] { new IterateResult(1), new IterateResult(2), new IterateResult(3) }) }, ">") {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 JXPathContext context = new JXPathContext();
 Path path = context.makePath("/");
 EvalContext evalContext = new EvalContext(path, context);
 Object result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.FALSE);
 }

 @Test
 public void testIteratorsContainsMatch() {
 CoreOperation expression = new CoreOperationRelationalExpression(new CoreOperation[] { Iterators.SEQUENCE(new IterateResult[] { new IterateResult(1), new IterateResult(2), new IterateResult(3) }), new CoreOperationConstant(2) }, "contains") {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };

 JXPathContext context = new JXPathContext();
 Path path = context.makePath("/");
 EvalContext evalContext = new EvalContext(path, context);
 Object result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.TRUE);
 }

 @Test
 public void testIteratorsFindMatch() {
 CoreOperation expression = new CoreOperationRelationalExpression(new CoreOperation[] { Iterators.SEQUENCE(new IterateResult[] { new IterateResult("child1"), new IterateResult("child2"), new IterateResult("child3") }), Iterators.SEQUENCE(new IterateResult[] { new IterateResult("child2"), new IterateResult("child3"), new IterateResult("child4") }) }, "contains") {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };

 JXPathContext context = new JXPathContext();
 Path path = context.makePath("/");
 EvalContext evalContext = new EvalContext(path, context);
 Object result = expression.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue(result == Boolean.TRUE);
 }
}