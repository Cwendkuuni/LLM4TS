import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Object mock = mock(String.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(String.class, "toString"));
        Mockito.doReturn(returnsEmptyValues).when(returnsEmptyValues).answer(any(InvocationOnMock.class));

        String result = (String) returnsEmptyValues.answer(invocation);
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(String.class, "compareTo", String.class));
        Mockito.doReturn(returnsEmptyValues).when(returnsEmptyValues).answer(any(InvocationOnMock.class));

        int result = (int) returnsEmptyValues.answer(invocation);
        assertEquals(1, result);

        verify(returnsEmptyValues).answer(invocation);
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> intType = int.class;
        Object result = returnsEmptyValues.returnValueFor(intType);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForWrapperTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> intWrapperType = Integer.class;
        Object result = returnsEmptyValues.returnValueFor(intWrapperType);
        assertEquals(0, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReturnValueForCollectionTypeWithModifiableCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> mutableList = new ArrayList<>();
        Class<?> listType = List.class;
        List<?> result = (List<?>) returnsEmptyValues.returnValueFor(listType);
        result.add("Test");
    }

    @Test
    public void testReturnValueForCollectionTypeWithEmptyList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> listType = List.class;
        List<?> result = (List<?>) returnsEmptyValues.returnValueFor(listType);
        assertEquals(0, result.size());
    }

    // Add test cases for other types: Set, Map, etc.

}