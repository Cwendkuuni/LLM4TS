import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswerToStringMethod() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        String result = (String) ReturnsEmptyValues.answer(invocation);

        Mockito.verify(mock, Mockito.times(1)).toString();
        Mockito.verifyNoMoreInteractions(mock);

        assertNotNull(result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("compareTo", Object.class));

        int result = (int) ReturnsEmptyValues.answer(invocation);

        Mockito.verify(mock1, Mockito.times(1)).compareTo(mock2);
        Mockito.verifyNoMoreInteractions(mock1);
        Mockito.verifyNoInteractions(mock2);

        assertEquals(1, result);
    }

    @Test
    public void testAnswerForInvalidMethod() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("someInvalidMethod"));

        assertNull(ReturnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForCollection() {
        Class<?> collectionType = Collection.class;

        List<Object> expectedResult = new ArrayList<>();

        assertEquals(expectedResult, ReturnsEmptyValues.returnValueFor(collectionType));
    }

    @Test
    public void testReturnValueForSet() {
        Class<?> setType = Set.class;

        Set<Object> expectedResult = new HashSet<>();

        assertEquals(expectedResult, ReturnsEmptyValues.returnValueFor(setType));
    }

    @Test
    public void testReturnValueForMap() {
        Class<?> mapType = Map.class;

        Map<Object, Object> expectedResult = new HashMap<>();

        assertEquals(expectedResult, ReturnsEmptyValues.returnValueFor(mapType));
    }

    @Test
    public void testReturnValueForInvalidType() {
        assertNull(ReturnsEmptyValues.returnValueFor(null));
    }
}