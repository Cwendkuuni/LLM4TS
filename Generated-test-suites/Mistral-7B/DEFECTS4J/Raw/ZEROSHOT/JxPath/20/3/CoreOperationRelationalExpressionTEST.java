import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionargs;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.iter.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;
 private EvalContext context;

 @Before
 public void setup() {
 context = Mockito.spy(EvalContext.class);
 expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionargs(
 Mockito.mock(Expression.class), Mockito.mock(Expression.class)
 ));
 }

 @Test
 public void testComputeValue() {
 Object left = 5;
 Object right = 10;
 Mockito.when(context.getSingleNodePointer()).thenReturn(left);
 Mockito.when(context.getSingleNodePointer()).thenReturn(right);

 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testComputeValue_false() {
 Object left = 5;
 Object right = 10;
 Mockito.when(context.getSingleNodePointer()).thenReturn(left);
 Mockito.when(context.getSingleNodePointer()).thenReturn(right);

 Mockito.doReturn(Iterators.EmptyIterator.instance()).when(context).getIterator();

 assertFalse(expression.computeValue(context));
 }

 @Test
 public void testComputeValue_double() {
 double left = 5.0;
 double right = 10.0;

 Mockito.when(context.getSingleNodePointer()).thenReturn(left);
 Mockito.when(context.getSingleNodePointer()).thenReturn(right);

 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testComputeValue_double_false() {
 double left = 5.0;
 double right = 10.0;

 Mockito.when(context.getSingleNodePointer()).thenReturn(left);
 Mockito.when(context.getSingleNodePointer()).thenReturn(right);

 assertFalse(expression.computeValue(context));
 }

 @Test
 public void testComputeValue_collection() {
 Object left = Iterators.emptyIterator();
 Object right = 10;

 Mockito.when(context.getSingleNodePointer()).thenReturn(left);
 Mockito.when(context.getSingleNodePointer()).thenReturn(right);

 Mockito.doReturn(Iterators.singletonIterator(5)).when(context).getIterator();

 assertFalse(expression.computeValue(context));
 }

 @Test
 public void testComputeValue_collection_true() {
 Object left = Iterators.singletonIterator(5);
 Object right = Iterators.singletonIterator(10);

 Mockito.when(context.getSingleNodePointer()).thenReturn(left);
 Mockito.when(context.getSingleNodePointer()).thenReturn(right);

 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testGetPrecedence() {
 int precedence = expression.getPrecedence();

 assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
 }
}