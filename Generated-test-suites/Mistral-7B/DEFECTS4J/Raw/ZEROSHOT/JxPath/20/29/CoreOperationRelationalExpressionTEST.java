import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testCoreOperationRelationalExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreNumberExpression(1), new CoreNumberExpression(2) }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 assertNotNull(expression);
 assertEquals(CoreOperation.Type.RELATIONAL, expression.getType());
 assertEquals(2, expression.getArgCount());
 }

 @Test
 public void testComputeValue() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreNumberExpression(1), new CoreNumberExpression(2) }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueFalse() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreNumberExpression(1), new CoreNumberExpression(2) }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testComputeValueDouble() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreNumberExpression(1.5), new CoreNumberExpression(2.5) }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0 ? InfoSetUtil.doubleValue(1.5) == InfoSetUtil.doubleValue(2.5) : InfoSetUtil.doubleValue(1.5) < InfoSetUtil.doubleValue(2.5) ? -1 : 1 > 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeValueDoubleEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreNumberExpression(1.5), new CoreNumberExpression(1.5) }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeValueIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreStringExpression("a"), new CoreStringExpression("a") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Iterator left = Arrays.asList("a", "b", "c").iterator();
 Iterator right = Arrays.asList("a", "b", "c").iterator();
 boolean result = expression.compute(left, right);
 assertTrue(result);
 }

 @Test
 public void testComputeValueIteratorsDifferentSize() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreStringExpression("a"), new CoreStringExpression("b") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Iterator left = Arrays.asList("a", "b", "c").iterator();
 Iterator right = Arrays.asList("a", "b").iterator();
 boolean result = expression.compute(left, right);
 assertFalse(result);
 }

 @Test
 public void testComputeValueIteratorsNullLeft() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreStringExpression("a"), null }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Iterator left = null;
 Iterator right = Arrays.asList("a", "b", "c").iterator();
 boolean result = expression.compute(left, right);
 assertFalse(result);
 }

 @Test
 public void testComputeValueIteratorsNullRight() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { new CoreStringExpression("a"), new CoreStringExpression("a") }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setCurrentNode(new SelfContext());
 Iterator left = Arrays.asList("a", "b", "c").iterator();
 Iterator right = null;
 boolean result = expression.compute(left, right);
 assertFalse(result);
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[0]);
 int precedence = expression.getPrecedence();
 assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expression = new CoreExpressionGreaterThan(new CoreExpression[0]);
 assertFalse(expression.isSymmetric());
 }
}