import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String result = (String) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("toString")));
        verify(mock).toString();
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock = mock(Comparable.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object arg = mock(Comparable.class);
        int result = (Integer) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("compareTo")));
        verify(mock).compareTo(arg);
        assertEquals(0, result); // mocks should return 0 if references are the same
    }

    @Test
    public void testAnswerOtherMethod() {
        Object mock = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> returnType = Object.class;
        Object result = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(returnType)));
        verifyZeroInteractions(mock);
        assertNull(result);
    }
}