import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, "toString", new Object[0]);
        Object result = answer.answer(invocation);
        verify(mock).toString();
        verifyNoMoreInteractions(mock);
        Assert.assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
    }
}