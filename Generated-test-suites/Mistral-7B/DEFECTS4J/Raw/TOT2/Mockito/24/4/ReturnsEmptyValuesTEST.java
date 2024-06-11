import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = Mockito.mock(String.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(ReturnsEmptyValues.class, "answer", Mockito.type(String.class)));
        Mockito.when(invocation.getMock()).thenReturn(mock);

        String result = (String) new ReturnsEmptyValues().answer(invocation);

        Assertions.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        Object result = new ReturnsEmptyValues().returnValueFor(int.class);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void testReturnValueForCollectionTypes() {
        Object result = new ReturnsEmptyValues().returnValueFor(ArrayList.class);
        Assertions.assertInstanceOf(ArrayList.class, result);
        Assertions.assertTrue(result instanceof Collection);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerCompareToMethodNull() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(ReturnsEmptyValues.class, "answer", Mockito.type(Object[].class)));
        Mockito.when(invocation.getMock()).thenReturn(mock);

        Assertions.assertThrows(NullPointerException.class, () -> new ReturnsEmptyValues().answer(invocation));
    }

    @Test
    public void testAnswerCompareToMethodArray() {
        Object[] mockArray = new Object[2];
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(ReturnsEmptyValues.class, "answer", Mockito.type(Object[].class)));
        Mockito.when(invocation.getMock()).thenReturn(mockArray);

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new ReturnsEmptyValues().answer(invocation));
    }

    @Test
    public void testAnswerCompareToMethodNullArgument() {
        Object mock = Mockito.mock(Comparable.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(ReturnsEmptyValues.class, "answer", Mockito.type(Comparable.class)));
        Mockito.when(invocation.getMock()).thenReturn(mock);

        Assertions.assertThrows(NullPointerException.class, () -> new ReturnsEmptyValues().answer(invocation));
    }

    // ...
}