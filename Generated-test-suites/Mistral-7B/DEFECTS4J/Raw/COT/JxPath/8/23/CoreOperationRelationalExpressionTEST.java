import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testComputeValue() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(1),
 new CoreOperation(2)
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = mock(EvalContext.class);
 Object leftValue = 5.0;
 Object rightValue = 10.0;
 when(context.getSingleNodePointer()).thenReturn(leftValue);
 when(context.getSingleNodePointer()).thenReturn(rightValue);

 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueWithFalseResult() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(1),
 new CoreOperation(2)
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };
 EvalContext context = mock(EvalContext.class);
 Object leftValue = 5.0;
 Object rightValue = 10.0;
 when(context.getSingleNodePointer()).thenReturn(leftValue);
 when(context.getSingleNodePointer()).thenReturn(rightValue);

 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueWithInitialContext() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(new InitialContext()),
 new CoreOperation(new InitialContext())
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = mock(EvalContext.class);
 when(context.getSingleNodePointer()).thenReturn(new InitialContext());
 when(context.getSingleNodePointer()).thenReturn(new InitialContext());

 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueWithInvalidArgumentType() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation("test"),
 new CoreOperation(2)
 });
 EvalContext context = mock(EvalContext.class);
 Object leftValue = "test";
 when(context.getSingleNodePointer()).thenReturn(leftValue);
 when(context.getSingleNodePointer()).thenReturn(2);

 try {
 expression.computeValue(context);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(1),
 new CoreOperation(2)
 });
 int precedence = expression.getPrecedence();
 Assert.assertEquals(3, precedence);
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(1),
 new CoreOperation(2)
 });
 boolean symmetric = expression.isSymmetric();
 Assert.assertEquals(false, symmetric);
 }

 @Test
 public void testEvaluateCompare() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(1),
 new CoreOperation(2)
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };
 boolean result = expression.evaluateCompare(3);
 Assert.assertEquals(false, result);

 result = expression.evaluateCompare(0);
 Assert.assertEquals(true, result);

 result = expression.evaluateCompare(-3);
 Assert.assertEquals(false, result);
 }

 @Test
 public void testReduce() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(5.0),
 new CoreOperation("test")
 });
 Object result = expression.reduce(5.0);
 Assert.assertEquals(5.0, result);

 result = expression.reduce("test");
 Assert.assertNull(result);
 }

 @Test
 public void testContainsMatch() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 }),
 new CoreOperation(2)
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
 Iterator rightIterator = Arrays.asList(2, 3, 4).iterator();
 boolean result = expression.containsMatch(leftIterator, rightIterator);
 Assert.assertEquals(false, result);

 leftIterator = Arrays.asList(1, 2, 3).iterator();
 rightIterator = Arrays.asList(1, 2, 3).iterator();
 result = expression.containsMatch(leftIterator, rightIterator);
 Assert.assertEquals(true, result);
 }

 @Test
 public void testFindMatch() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperation(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 }),
 new CoreOperation(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 })
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
 Iterator rightIterator = Arrays.asList(1, 2, 3).iterator();
 boolean result = expression.findMatch(leftIterator, rightIterator);
 Assert.assertEquals(true, result);

 leftIterator = Arrays.asList(1, 2).iterator();
 rightIterator = Arrays.asList(1, 2, 3).iterator();
 result = expression.findMatch(leftIterator, rightIterator);
 Assert.assertEquals(false, result);
 }

}