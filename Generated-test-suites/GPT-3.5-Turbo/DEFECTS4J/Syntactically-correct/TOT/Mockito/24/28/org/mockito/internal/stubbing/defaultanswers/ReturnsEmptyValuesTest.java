import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_toStringMethod_DefaultName() {
        // Arrange
        Method toStringMethod = null;
        try {
            toStringMethod = returnsEmptyValues.getClass().getMethod("answer", InvocationOnMock.class);
        } catch (NoSuchMethodException e) {
            fail("No such method exception: " + e.getMessage());
        }

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(toStringMethod);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof String);
        assertTrue(result.toString().startsWith("Mock for ReturnsEmptyValues"));
        assertTrue(result.toString().contains("hashCode: "));
    }

    @Test
    public void testAnswer_toStringMethod_NamedMock() {
        // Arrange
        Method toStringMethod = null;
        try {
            toStringMethod = returnsEmptyValues.getClass().getMethod("answer", InvocationOnMock.class);
        } catch (NoSuchMethodException e) {
            fail("No such method exception: " + e.getMessage());
        }

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockName name = mock(MockName.class);
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("MockName");
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(any())).thenReturn(name);
        when(mockUtil.getMockSettings(any())).thenReturn(null);
        when(invocation.getMethod()).thenReturn(toStringMethod);
        when(invocation.getMock()).thenReturn(returnsEmptyValues);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof String);
        assertEquals("MockName", result.toString());
    }

    @Test
    public void testAnswer_compareToMethod() {
        // Arrange
        Method compareToMethod = null;
        try {
            compareToMethod = returnsEmptyValues.getClass().getMethod("answer", InvocationOnMock.class);
        } catch (NoSuchMethodException e) {
            fail("No such method exception: " + e.getMessage());
        }

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(compareToMethod);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof Integer);
        assertEquals(1, result);
    }

    @Test
    public void testAnswer_primitiveMethod() {
        // Arrange
        Method primitiveMethod = null;
        try {
            primitiveMethod = returnsEmptyValues.getClass().getMethod("answer", InvocationOnMock.class);
        } catch (NoSuchMethodException e) {
            fail("No such method exception: " + e.getMessage());
        }

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(primitiveMethod);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_collectionMethod() {
        // Arrange
        Method collectionMethod = null;
        try {
            collectionMethod = returnsEmptyValues.getClass().getMethod("answer", InvocationOnMock.class);
        } catch (NoSuchMethodException e) {
            fail("No such method exception: " + e.getMessage());
        }

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(collectionMethod);
        when(invocation.getMethod().getReturnType()).thenReturn(Set.class);
        
        // Act
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof Set);
        assertTrue(((Set<?>) result).isEmpty());
    }
}