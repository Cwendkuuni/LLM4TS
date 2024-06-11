import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg1;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg2;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterableUtils;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("b"))));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanFalse() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGreaterThanIterators() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("2"))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("2"), new Node("3"), new Node("4")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanIteratorsFalse() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("1"), new Node("1"), new Node("3")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGreaterThanDouble() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Double.class, 3.0)),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Double.class, 2.0)));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanEqual() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanEqualFalse() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationAdd(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationNumber(Double.class, 1.0))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGreaterThanEqualIterators() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("2"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("2"), new Node("2"), new Node("3")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanEqualIteratorsFalse() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("1"), new Node("1"), new Node("1")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGreaterThanEqualDouble() {
 CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Double.class, 3.0)),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Double.class, 2.0)));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression op = new CoreOperationLessThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationRelationalExpressionarg2(new CoreOperationNumber(Integer.class, 1)));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanTrue() {
 CoreOperationRelationalExpression op = new CoreOperationLessThan(
 new CoreOperationRelationalExpressionarg1(new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationSubtract(
 new CoreOperationNumber(Integer.class, 1),
 new CoreOperationNumber(Integer.class, 1))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanIterators() {
 CoreOperationRelationalExpression op = new CoreOperationLessThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("2"))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("2"), new Node("3"), new Node("4")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanIteratorsTrue() {
 CoreOperationRelationalExpression op = new CoreOperationLessThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationRelationalExpressionarg2(new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("2")),
 new CoreOperationAdd(
 new CoreOperationNumber(Integer.class, -1),
 new CoreOperationNumber(Integer.class, 1))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("2")))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("2"), new Node("3"), new Node("4")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanDouble() {
 CoreOperationRelationalExpression op = new CoreOperationLessThan(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Double.class, 3.0)),
 new CoreOperationRelationalExpressionarg2(new CoreOperationNumber(Double.class, 4.0)));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualFalse() {
 CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))),
 new CoreOperationSubtract(
 new CoreOperationNumber(Integer.class, 1),
 new CoreOperationNumber(Integer.class, 1))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("a"))));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualIterators() {
 CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("2"))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("2"), new Node("3"), new Node("4")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualIteratorsTrue() {
 CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("1"))),
 new CoreOperationRelationalExpressionarg2(new CoreOperationRelationalExpressionarg1(new ConstantExpression(Node.ELEMENT_NODE, new Node("2")),
 new CoreOperationAdd(
 new CoreOperationNumber(Integer.class, -1),
 new CoreOperationNumber(Integer.class, 1))),
 new CoreOperationRelationalExpressionarg2(new ConstantExpression(Node.ELEMENT_NODE, new Node("2")))));
 EvalContext context = new EvalContext(new InitialContext());
 Node[] left = {new Node("1"), new Node("2"), new Node("3")};
 Node[] right = {new Node("2"), new Node("3"), new Node("4")};
 Iterator itLeft = IterableUtils.iterator(left);
 Iterator itRight = IterableUtils.iterator(right);
 op.setIterators(itLeft, itRight);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualDouble() {
 CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(
 new CoreOperationRelationalExpressionarg1(new ConstantExpression(Double.class, 3.0)),
 new CoreOperationRelationalExpressionarg2(new CoreOperationNumber(Double.class, 4.0)));
 EvalContext context = new EvalContext(new InitialContext());
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

}