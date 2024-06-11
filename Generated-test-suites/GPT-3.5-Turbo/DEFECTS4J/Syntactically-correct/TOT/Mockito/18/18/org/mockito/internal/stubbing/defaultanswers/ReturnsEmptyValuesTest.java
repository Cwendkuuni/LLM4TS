import static org.mockito.Mockito.*;
import org.junit.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringDefaultName() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]); // toString() method
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswerToStringCustomName() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]); // toString() method
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        MockName name = mock(MockName.class);
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("CustomName");
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(mock)).thenReturn(name);
        answer.mockUtil = mockUtil;

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertEquals("CustomName", result);
    }

    @Test
    public void testAnswerCompareToSameReference() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]); // compareTo() method - first argument is same reference as the mock itself
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        Object[] arguments = new Object[]{mock};
        when(invocation.getArguments()).thenReturn(arguments);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAnswerCompareToDifferentReference() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]); // compareTo() method - first argument is different reference
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        Object[] arguments = new Object[]{new Object()};
        when(invocation.getArguments()).thenReturn(arguments);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertEquals(1, result);
    }

    @Test
    public void testAnswerReturnValueForPrimitiveType() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[0]); // non-void method
        Class<?> returnType = int.class;
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAnswerReturnValueForCollectionType() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[0]); // non-void method
        Class<?> returnType = List.class;
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List<?>) result).isEmpty());
    }

    @Test
    public void testAnswerReturnValueForMapType() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[0]); // non-void method
        Class<?> returnType = Map.class;
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Map);
        Assert.assertTrue(((Map<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswerReturnValueForOtherType() {
        // Arrange
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[0]); // non-void method
        Class<?> returnType = ReturnsEmptyValues.class;
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);

        // Act
        Object result = answer.answer(invocation);

        // Assert
        Assert.assertNull(result);
    }
}