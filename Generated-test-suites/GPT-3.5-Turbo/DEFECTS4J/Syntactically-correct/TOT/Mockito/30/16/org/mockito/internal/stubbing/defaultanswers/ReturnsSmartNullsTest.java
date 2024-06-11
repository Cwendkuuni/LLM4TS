import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    
    private ReturnsSmartNulls returnsSmartNulls;
    
    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }
    
    @Test
    public void testAnswer_withInvocationOnMock() throws Throwable {
        // Arrange
        final InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        
        // Act
        Object result = returnsSmartNulls.answer(invocationMock);
        
        // Assert
        assertThat(result).isNull();
        verify(invocationMock, times(1)).getMethod();
        verifyNoMoreInteractions(invocationMock);
    }
    
    @Test
    public void testAnswer_withInvocationOnMockAndNonNullDefaultReturnValue() throws Throwable {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithNonNullDefaultReturnValue();
        
        // Act
        Object result = returnsSmartNulls.answer(invocationMock);
        
        // Assert
        assertThat(result).isNotNull();
        verify(invocationMock, times(1)).getMethod();
        verifyNoMoreInteractions(invocationMock);
    }
    
    @Test
    public void testAnswer_withInvocationOnMockAndMockableReturnType() throws Throwable {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithMockableReturnType();
        final ReturnsSmartNulls.ThrowingInterceptor throwingInterceptorMock = mock(ReturnsSmartNulls.ThrowingInterceptor.class);
        
        // Act
        Object result = returnsSmartNulls.answer(invocationMock);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CGLibProxy.class);
        verify(throwingInterceptorMock, times(1)).intercept(notNull(), any(Method.class), any(Object[].class), any(MethodProxy.class));
        verifyNoMoreInteractions(throwingInterceptorMock);
    }
    
    @Test
    public void testAnswer_withInvocationOnMockAndNonMockableReturnType() throws Throwable {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithNonMockableReturnType();
        final ReturnsSmartNulls.ThrowingInterceptor throwingInterceptorMock = mock(ReturnsSmartNulls.ThrowingInterceptor.class);
        
        // Act
        Object result = returnsSmartNulls.answer(invocationMock);
        
        // Assert
        assertThat(result).isNull();
        verify(throwingInterceptorMock, times(1)).intercept(notNull(), any(Method.class), any(Object[].class), any(MethodProxy.class));
        verifyNoMoreInteractions(throwingInterceptorMock);
    }
    
    @Test
    public void testFormatMethodCall_withValidArguments() throws Exception {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithNonNullMethod();
        final String expectedResult = "testMethod(arg1, arg2, arg3)";
        
        // Act
        String result = returnsSmartNulls.new ThrowingInterceptor(invocationMock).formatMethodCall();
        
        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }
    
    @Test
    public void testFormatMethodCall_withEmptyArguments() throws Exception {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithNoArguments();
        final String expectedResult = "testMethod()";
        
        // Act
        String result = returnsSmartNulls.new ThrowingInterceptor(invocationMock).formatMethodCall();
        
        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }
    
    @Test
    public void testFormatMethodCall_withSingleStringArgument() throws Exception {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithSingleStringArgument();
        final String expectedResult = "testMethod(\"arg1\")";
        
        // Act
        String result = returnsSmartNulls.new ThrowingInterceptor(invocationMock).formatMethodCall();
        
        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }
    
    @Test
    public void testFormatMethodCall_withSingleIntegerArgument() throws Exception {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithSingleIntegerArgument();
        final String expectedResult = "testMethod(1)";
        
        // Act
        String result = returnsSmartNulls.new ThrowingInterceptor(invocationMock).formatMethodCall();
        
        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }
    
    @Test
    public void testFormatMethodCall_withNullArguments() throws Exception {
        // Arrange
        final InvocationOnMock invocationMock = getInvocationOnMockWithNullArguments();
        final String expectedResult = "testMethod(null, null, null)";
        
        // Act
        String result = returnsSmartNulls.new ThrowingInterceptor(invocationMock).formatMethodCall();
        
        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }
    
    private InvocationOnMock getInvocationOnMockWithNonNullDefaultReturnValue() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        Answer<Object> delegateAnswer = returnsSmartNulls.new ReturnsMoreEmptyValues();
        when(delegateAnswer.answer(invocationMock)).thenReturn(new Object());
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationOnMockWithMockableReturnType() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        Answer<Object> delegateAnswer = returnsSmartNulls.new ReturnsMoreEmptyValues();
        when(delegateAnswer.answer(invocationMock)).thenReturn(null);
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationOnMockWithNonMockableReturnType() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        Answer<Object> delegateAnswer = returnsSmartNulls.new ReturnsMoreEmptyValues();
        when(delegateAnswer.answer(invocationMock)).thenReturn(null);
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationOnMockWithNonNullMethod() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getArguments()).thenReturn(new Object[] { "arg1", "arg2", "arg3" });
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationOnMockWithNoArguments() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getArguments()).thenReturn(new Object[] {});
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationOnMockWithSingleStringArgument() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getArguments()).thenReturn(new Object[] { "arg1" });
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationOnMockWithSingleIntegerArgument() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getArguments()).thenReturn(new Object[] { 1 });
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        return invocationMock;
    }
    
    private InvocationOnMock getInvocationOnMockWithNullArguments() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(getMockedMethod());
        when(invocationMock.getArguments()).thenReturn(new Object[] { null, null, null });
        when(invocationMock.getMock()).thenReturn(getDummyObject());
        return invocationMock;
    }
    
    private Method getMockedMethod() throws NoSuchMethodException {
        return ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
    }
    
    private Object getDummyObject() {
        return new Object();
    }
}