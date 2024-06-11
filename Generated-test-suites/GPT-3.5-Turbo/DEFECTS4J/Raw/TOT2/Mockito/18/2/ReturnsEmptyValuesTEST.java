import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void answer_toString_ReturnsMockDescription() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[0]);

        Object result = answer.answer(invocation);

        assertTrue(result instanceof String);
        String expectedResult = "Mock for InvocationOnMock, hashCode: " + invocation.hashCode();
        assertEquals(expectedResult, result);
    }

    @Test
    public void answer_compareTo_ReturnsZeroIfSameReference() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[1]);
        when(invocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{invocation.getMock()});

        Object result = answer.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void answer_compareTo_ReturnsNonZeroIfDifferentReferences() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[1]);
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.getArguments()).thenReturn(new Object[]{"mock2"});

        Object result = answer.answer(invocation);

        assertNotEquals(0, result);
    }

    @Test
    public void answer_primitiveReturnType_ReturnsDefaultValue() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[2]);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        Object result = answer.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void answer_collectionReturnType_ReturnsEmptyList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[3]);
        when(invocation.getMethod().getReturnType()).thenReturn(Collection.class);

        Object result = answer.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((Collection) result).isEmpty());
    }

    @Test
    public void answer_setReturnType_ReturnsEmptyHashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[4]);
        when(invocation.getMethod().getReturnType()).thenReturn(Set.class);

        Object result = answer.answer(invocation);

        assertTrue(result instanceof HashSet);
        assertTrue(((Set) result).isEmpty());
    }

    // Add more test cases for other scenarios as needed

}