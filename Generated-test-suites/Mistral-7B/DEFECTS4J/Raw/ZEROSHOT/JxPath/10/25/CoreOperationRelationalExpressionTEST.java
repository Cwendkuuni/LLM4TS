import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.util.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util. Collection;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CoreOperationRelationalExpression.class})
public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;
 private EvalContext context;
 private CoreOperation leftArg, rightArg;

 @Before
 public void setUp() throws Exception {
 MockitoAnnotations.initMocks(this);
 context = mock(EvalContext.class);
 leftArg = mock(CoreOperation.class);
 rightArg = mock(CoreOperation.class);
 expression = new CoreOperationRelationalExpression(new CoreOperation[]{leftArg, rightArg});
 }

 @Test
 public void testComputeValue() throws Exception {
 // Test with boolean values
 when(leftArg.computeValue(context)).thenReturn(Boolean.TRUE);
 when(rightArg.computeValue(context)).thenReturn(Boolean.TRUE);
 when(expression.evaluateCompare(0)).thenReturn(true);

 Object result = expression.computeValue(context);
 assertThat(result, is(Boolean.TRUE));

 // Test with double values
 when(leftArg.computeValue(context)).thenReturn(1.0);
 when(rightArg.computeValue(context)).thenReturn(2.0);
 when(expression.evaluateCompare(-1)).thenReturn(true);

 result = expression.computeValue(context);
 assertThat(result, is(Boolean.TRUE));

 // Test with null values
 when(leftArg.computeValue(context)).thenReturn(nullValue());
 when(rightArg.computeValue(context)).thenReturn(nullValue());
 when(expression.evaluateCompare(0)).thenReturn(false);

 result = expression.computeValue(context);
 assertThat(result, is(Boolean.FALSE));
 }

 @Test
 public void testGetPrecedence() throws Exception {
 assertThat(expression.getPrecedence(), is(3));
 }

 @Test
 public void testIsSymmetric() throws Exception {
 assertThat(expression.isSymmetric(), is(false));
 }

 @Test
 public void testComputeWithInitialContexts() throws Exception {
 when(leftArg.computeValue(context)).thenReturn(new InitialContext());
 when(rightArg.computeValue(context)).thenReturn(new InitialContext());

 Object result = expression.computeValue(context);
 assertThat(result, is(Boolean.FALSE));
 }

 @Test
 public void testComputeWithIterators() throws Exception {
 Collection leftCollection = new ArrayList<>();
 Collection rightCollection = new ArrayList<>();
 when(leftArg.computeValue(context)).thenReturn(leftCollection.iterator());
 when(rightArg.computeValue(context)).thenReturn(rightCollection.iterator());

 PowerMockito.whenNew(HashSet.class).withArguments().thenReturn(new HashSet<>());
 when(expression.findMatch(any(Iterator.class), any(Object.class))).thenReturn(true);

 Iterator leftIterator = PowerMockito.mock(Iterator.class);
 Iterator rightIterator = PowerMockito.mock(Iterator.class);
 when(leftArg.computeValue(context)).thenReturn(leftIterator);
 when(rightArg.computeValue(context)).thenReturn(rightIterator);

 PowerMockito.whenNew(HashSet.class).withArguments().thenReturn(new HashSet<>());
 when(expression.containsMatch(any(Iterator.class), any(Object.class))).thenReturn(true);

 Object result = expression.computeValue(context);
 assertThat(result, is(Boolean.TRUE));
 }

 @Test
 public void testReduce() throws Exception {
 Object left = new SelfContext();
 Object right = new SelfContext();

 Object result = expression.reduce(left);
 assertThat(result, is(left));

 result = expression.reduce(right);
 assertThat(result, is(right));

 result = expression.reduce(new ArrayList<>());
 assertThat(result, is(new ArrayList<>().iterator()));
 }

}