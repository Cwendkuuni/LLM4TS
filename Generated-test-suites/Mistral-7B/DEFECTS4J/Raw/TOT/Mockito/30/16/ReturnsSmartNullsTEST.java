import org.junit.Test;
import org.junit.internal.runners.MethodRoles;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answerstubbing.ReturnsSmartNulls;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private static final String SMART_NULL_MESSAGE = "SmartNull returned by unstubbed method on mock";

 @Mock
 private StubbedClass stubbedClass;

 @Captor
 private ArgumentCaptor<String> stringArgumentCaptor;

 @Test
 public void testSmartNull_whenStringMethodCalled_returnSmartNull() {
     when(stubbedClass.toString()).thenAnswer(new ReturnsSmartNulls());

     String result = stubbedClass.toString();
     assertEquals(SMART_NULL_MESSAGE, result.toString());
     verify(stubbedClass).toString();
 }

 @Test(expected = NullPointerException.class)
 public void testNull_whenUnstubbedMethodCalled_throwsNPE() {
     String result = stubbedClass.someUnstubbedMethod();
     fail("Expected NullPointerException, but got: " + result);
 }

 @Test
 public void testGetReturnType_whenTypeIsNotMockable_returnNull() {
     when(stubbedClass.someUnstubbedMethod(anyInt())).thenAnswer(new ReturnsSmartNulls());

     Integer result = stubbedClass.someUnstubbedMethod(1);
     assertNull(result);
 }

 @Test
 public void testToString_whenToStringIsCalledOnNonMockObject_returnSmartNull() {
     String result = new Object().toString();
     assertEquals(SMART_NULL_MESSAGE, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testThrowingInterceptor_whenInvokedWithNonMockObject_throwsIllegalStateException() {
     Object obj = new Object();
     MethodInvocation mockInvocation = mock(MethodInvocation.class);
     Method method = mock(Method.class);
     Object[] args = new Object[0];
     MethodProxy methodProxy = mock(MethodProxy.class);

     new ReturnsSmartNulls().new ThrowingInterceptor(mockInvocation).intercept(obj, method, args, methodProxy);

     fail("Expected IllegalStateException, but got: " + obj);
 }
}