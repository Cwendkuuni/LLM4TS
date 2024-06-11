import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {
    @Test
    public void testToStringMethod() {
        Answer answer = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, "toString()", new Object[0]);
        String result = (String) answer.answer(invocation);
        verify(mock).toString();
        assertNotNull(result);
        assertEquals("Mock for" + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock = mock(Comparable.class);
        Answer answer = new ReturnsEmptyValues();
        Object arg = mock(Comparable.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, "compareTo(T other)", new Object[]{arg});
        int result = (Integer) answer.answer(invocation);
        assertEquals(1, result);
        verify(mock).compareTo(arg);
    }
}