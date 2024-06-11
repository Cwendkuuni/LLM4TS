import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_for_ToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[0]);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals("Mock for ObjectMethodsGuru, hashCode: " + invocation.getMock().hashCode(), result);
    }

    @Test
    public void testAnswer_for_CompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[1]);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(1, result);
    }

    @Test
    public void testReturnValueFor_PrimitiveType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> type = int.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueFor_CollectionType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> type = Collection.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testReturnValueFor_SetType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> type = Set.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        assertEquals(new HashSet<Object>(), result);
    }

    // Add more test cases for other methods if necessary

}