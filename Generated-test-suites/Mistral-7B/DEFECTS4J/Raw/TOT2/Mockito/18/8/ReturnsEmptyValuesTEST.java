import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import java.util.ArrayList;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, new Object() {});
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.toStringMethod(Object.class));

        // When
        Object result = returnsEmptyValues.answer(invocation);

        // Then
        verify(mock).toString();
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testReturnValueForPrimitiveType() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<Integer> integerType = Integer.class;

        // When
        Object result = returnsEmptyValues.returnValueFor(integerType);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForCollectionType() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<List<Integer>> listType = List.class;
        Class<Integer> integerType = Integer.class;

        // When
        Object result = returnsEmptyValues.returnValueFor(listType);

        // Then
        assertInstanceOf(LinkedList.class, result);
        assertTrue(result instanceof Collection);
        assertTrue(result.isEmpty());
    }
}