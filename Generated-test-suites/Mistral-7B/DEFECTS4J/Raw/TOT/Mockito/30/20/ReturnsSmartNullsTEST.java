import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private static final String EXPECTED_SMART_NULL_MESSAGE = "SmartNull returned by unstubbed method on mock";

 @Test
 public void testMethodWithToStringReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
     String result = (String) returnsSmartNulls.answer(new InvocationOnMock(null, new MethodStub(), new Object[0], new Class[0]));
     verify(returnsSmartNulls).answer(new InvocationOnMock(null, new MethodStub(), new Object[0], new Class[0]));
     assertNotNull(result);
     assertEquals(EXPECTED_SMART_NULL_MESSAGE, result.toString());
 }

 @Test(expected = org.mockito.exceptions.misc.SmartNullPointerException.class)
 public void testMethodWithNullReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
     Object result = returnsSmartNulls.answer(new InvocationOnMock(null, new MethodStub(), new Object[0], new Class[0]));
     assertNull(result);
 }

 @Test
 public void testMethodWithNonMockableReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
     Integer result = (Integer) returnsSmartNulls.answer(new InvocationOnMock(null, new MethodStub(), new Object[0], new Class[0]));
     assertNull(result);
 }

 @Test
 public void testMethodWithStubbedReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
     when(returnsSmartNulls.answer(any(InvocationOnMock.class))).thenReturn("stubbed value");
     String result = (String) returnsSmartNulls.answer(new InvocationOnMock(null, new MethodStub(), new Object[0], new Class[0]));
     assertEquals("stubbed value", result);
 }

 @Test
 public void testMethodWithThrowingInterceptor() throws Exception {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
     when(returnsSmartNulls.answer(any(InvocationOnMock.class))).thenAnswer(new Answer<Object>() {
         @Override
         public Object answer(InvocationOnMock invocation) throws Throwable {
             throw new RuntimeException();
         }
     });
     Throwable exception = null;
     try {
         returnsSmartNulls.answer(new InvocationOnMock(null, new MethodStub(), new Object[0], new Class[0]));
     } catch (Throwable e) {
         exception = e;
     }
     assertNotNull(exception);
 }

 private static class MethodStub implements Method {
     @Override
     public Class<?>[] getParameterTypes() {
         return new Class[0];
     }

     @Override
     public int getModifiers() {
         return 0;
     }

     @Override
     public String getName() {
         return "getName";
     }

     @Override
     public Class<?> getReturnType() {
         return String.class;
     }
 }
}