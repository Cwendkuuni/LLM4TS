import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();

        // Create InvocationOnMock and set a method to be invoked
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("equals", Object.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        // Test answer method
        Object result = rs.answer(invocation);

        // Verify that default return value is null
        Assert.assertNull(result);
        // Verfiy that delegate's answer method is called
        Mockito.verify(rs.delegate).answer(invocation);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();

        // Create InvocationOnMock and set a method to be invoked
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Mockito.when(invocation.getMethod()).thenReturn(method);

        // Create ThrowingInterceptor
        ReturnsSmartNulls.ThrowingInterceptor interceptor = rs.new ThrowingInterceptor(invocation);

        // Test intercept method
        Object result = interceptor.intercept(null, method, null, null);

        // Verify that the correct message is returned
        String expected = "SmartNull returned by unstubbed toString() method on mock";
        Assert.assertEquals(expected, result);
        // Verify that the smartNullPointerException is thrown
        Mockito.verify(new Reporter()).smartNullPointerException(any(Location.class));
    }

    @Test
    public void testFormatMethodCall() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();

        // Create InvocationOnMock and set a method to be invoked
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Mockito.when(invocation.getMethod()).thenReturn(method);

        // Test formatMethodCall method
        String result = rs.new ThrowingInterceptor(invocation).formatMethodCall();

        // Verify that the correct method call is formatted
        String expected = "toString()";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswerWithNonNullDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();

        // Create InvocationOnMock and set a return value from delegate's answer
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(rs.delegate.answer(invocation)).thenReturn("ReturnValue");

        // Test answer method
        Object result = rs.answer(invocation);

        // Verify that default return value is the value returned by delegate's answer
        Assert.assertEquals("ReturnValue", result);
    }

    @Test
    public void testAnswerWithMockableReturnType() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();

        // Create InvocationOnMock and set a method to be invoked
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("equals", Object.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        // Create ClassImposterizer and set canImposterise method to return true
        ClassImposterizer mockImposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(mockImposterizer.canImposterise(any(Class.class))).thenReturn(true);

        // Set ClassImposterizer's INSTANCE field to mockImposterizer
        Field field = ClassImposterizer.class.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, mockImposterizer);

        // Test answer method
        Object result = rs.answer(invocation);

        // Verify that ClassImposterizer's imposterise method is called
        Mockito.verify(mockImposterizer).imposterise(any(MethodInterceptor.class), any(Class.class));
    }

}