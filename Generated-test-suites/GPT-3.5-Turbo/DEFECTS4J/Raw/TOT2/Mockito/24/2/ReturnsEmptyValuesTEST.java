import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_forToString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("toString"));
        Object result = answer.answer(invocation);
        assertTrue(result instanceof String);
    }

    @Test
    public void testAnswer_forCompareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethod("compareTo"));
        Object result = answer.answer(invocation);
        assertEquals(1, result);
    }

    @Test
    public void testReturnValueFor_primitiveType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(int.class);
        assertEquals(0, (int) result);
    }

    @Test
    public void testReturnValueFor_collectionType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(Collection.class);
        assertTrue(result instanceof LinkedList);
        assertTrue(((Collection) result).isEmpty());
    }

    // Add more test cases here for other methods in ReturnsEmptyValues class

}