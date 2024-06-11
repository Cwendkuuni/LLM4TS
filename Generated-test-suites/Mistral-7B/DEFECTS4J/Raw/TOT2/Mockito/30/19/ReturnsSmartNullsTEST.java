import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {
    private ReturnsSmartNulls returnsSmartNulls;
    private Object mockObject;

    @BeforeEach
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
        mockObject = mock(Object.class);
    }

    @Test
    public void testAnswer() {
        when(mockObject.toString()).thenReturn("Test String");
        Answer<Object> answer = returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class));
        Object result = answer.answer(ArgumentMatchers.any(InvocationOnMock.class));
        assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
    }
}