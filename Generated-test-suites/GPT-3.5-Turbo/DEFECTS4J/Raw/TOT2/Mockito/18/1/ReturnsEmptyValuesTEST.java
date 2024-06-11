import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswerMethodToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[0]);
        assertNotNull(returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerMethodCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getDeclaredMethods()[1]);
        when(invocation.getArguments()).thenReturn(new Object[]{new Object()});
        assertNotNull(returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerMethodPrimitiveReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Primitives.class.getDeclaredMethods()[0]);
        assertNotNull(returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerMethodCollectionReturn() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getDeclaredMethods()[1]);
        assertNotNull(returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForCollection() {
        assertNotNull(returnsEmptyValues.returnValueFor(Collection.class));
    }

    @Test
    public void testReturnValueForSet() {
        assertNotNull(returnsEmptyValues.returnValueFor(Set.class));
    }

    @Test
    public void testReturnValueForHashSet() {
        assertNotNull(returnsEmptyValues.returnValueFor(HashSet.class));
    }

    @Test
    public void testReturnValueForList() {
        assertNotNull(returnsEmptyValues.returnValueFor(List.class));
    }

    @Test
    public void testReturnValueForLinkedList() {
        assertNotNull(returnsEmptyValues.returnValueFor(LinkedList.class));
    }

}