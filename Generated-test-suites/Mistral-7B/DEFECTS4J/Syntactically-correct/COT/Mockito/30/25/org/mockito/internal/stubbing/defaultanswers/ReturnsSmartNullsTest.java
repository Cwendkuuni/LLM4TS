import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.JUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testAnswerNullMethod() {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);

 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object getMock() {
 return mock;
 }

 @Override
 public Method getMethod() {
 return Object.class.getMethod("methodWithoutReturnValue");
 }

 @Override
 public Object[] getArguments() {
 return new Object[0];
 }
 });

 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);

 // Verify that smart null pointer exception was thrown
 verify(invocation).getMock().toString();
 }

 @Test
 public void testAnswerNotNullMethod() {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);

 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object getMock() {
 return mock;
 }

 @Override
 public Method getMethod() {
 return Object.class.getMethod("methodWithReturnValue", Integer.class);
 }

 @Override
 public Object[] getArguments() {
 return new Object[]{1};
 }
 });

 Object result = returnsSmartNulls.answer(invocation);
 assertNotNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerNullWithToStringMethod() {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);

 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object getMock() {
 return mock;
 }

 @Override
 public Method getMethod() {
 return Object.class.getMethod("toString");
 }

 @Override
 public Object[] getArguments() {
 return new Object[0];
 }
 });

 returnsSmartNulls.answer(invocation);
 }

 @Test
 public void testAnswerFinalClass() {
 final Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);

 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object getMock() {
 return mock;
 }

 @Override
 public Method getMethod() {
 return Long.class.getMethod("valueOf", long.class);
 }

 @Override
 public Object[] getArguments() {
 return new Object[]{0L};
 }
 });

 assertNull(returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testAnswerMethodWithImposterizableReturnType() {
 Object mock = mock(String.class, Answers.RETURNS_DEEP_STUBS);

 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object getMock() {
 return mock;
 }

 @Override
 public Method getMethod() {
 return String.class.getMethod("valueOf", char.class);
 }

 @Override
 public Object[] getArguments() {
 return new Object[]{'c'};
 }
 });

 Object result = returnsSmartNulls.answer(invocation);
 assertNotNull(result);
 }
}