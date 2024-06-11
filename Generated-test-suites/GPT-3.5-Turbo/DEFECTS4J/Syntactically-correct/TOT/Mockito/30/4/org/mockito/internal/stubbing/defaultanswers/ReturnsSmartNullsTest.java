
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private Answer<Object> delegate;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
        delegate = new ReturnsMoreEmptyValues();
    }

    @Test
    public void testAnswer_OrdinaryReturn() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn("mockValue");

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertEquals("mockValue", result);
    }

    @Test
    public void testAnswer_SmartNullReturn() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn(null);
        MethodProxy proxy = mock(MethodProxy.class);
        ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
        when(objectMethodsGuru.isToString(any())).thenReturn(true);
        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(classImposterizer.canImposterise(any(Class.class))).thenReturn(true);
        ThrowingInterceptor throwingInterceptor = mock(ThrowingInterceptor.class);
        whenNew(ThrowingInterceptor.class).withArguments(invocation).thenReturn(throwingInterceptor);
        when(classImposterizer.imposterise(throwingInterceptor, Object.class)).thenReturn("SmartNull");

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertEquals("SmartNull", result);
        verify(objectMethodsGuru).isToString(any());
        verify(classImposterizer).imposterise(throwingInterceptor, Object.class);
    }

    @Test
    public void testAnswer_NullReturn() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn(null);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        Class<?> returnType = Object.class;
        when(method.getReturnType()).thenReturn(returnType);
        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(classImposterizer.canImposterise(Object.class)).thenReturn(false);

        // Act
        Object result = returnsSmartNulls.answer(invocation);

        // Assert
        assertNull(result);
        verify(classImposterizer).canImposterise(Object.class);
    }
}
