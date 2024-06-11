import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerWithMethodInterceptor() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        Mockito.doAnswer(Mockito.isA(InvocationOnMock.class)).when(returnsSmartNulls).answer(any(InvocationOnMock.class));

        Object result = returnsSmartNulls.answer(Mockito.mock(Object.class));
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullInput() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.answer(null);
    }

    @Test(expected = RuntimeException.class)
    public void testAnswerWithMethodInterceptorCreationFailure() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Mockito.doThrow(new RuntimeException()).when(returnsSmartNulls).answer(any(InvocationOnMock.class));

        ReturnsSmartNulls throwingInterceptor = Mockito.mock(ReturnsSmartNulls.ThrowingInterceptor.class);
        try {
            Mockito.when(returnsSmartNulls.answer(any(InvocationOnMock.class))).then(throwingInterceptor);
            Object result = returnsSmartNulls.answer(Mockito.mock(Object.class));
        } catch (Throwable e) {
            // expected exception
        }
    }

    @Test
    public void testAnswerWithToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        Mockito.doAnswer(Mockito.isA(InvocationOnMock.class)).when(returnsSmartNulls).answer(any(InvocationOnMock.class));

        String result = (String) returnsSmartNulls.answer(Mockito.mock(String.class));
        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }

    @Test
    public void testgetClass() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        assertEquals(ReturnsSmartNulls.class, returnsSmartNulls.getClass());
    }

    @Test
    public void testHashCode() {
        ReturnsSmartNulls returnsSmartNulls1 = new ReturnsSmartNulls();
        ReturnsSmartNulls returnsSmartNulls2 = new ReturnsSmartNulls();
        assertEquals(returnsSmartNulls1.hashCode(), returnsSmartNulls2.hashCode());
    }

    @Test
    public void testEquals() {
        ReturnsSmartNulls returnsSmartNulls1 = new ReturnsSmartNulls();
        ReturnsSmartNulls returnsSmartNulls2 = new ReturnsSmartNulls();
        assertTrue(returnsSmartNulls1.equals(returnsSmartNulls2));
    }

    @Test
    public void testToString() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        assertEquals("ReturnsSmartNulls@" + Integer.toHexString(hashCode()), returnsSmartNulls.toString());
    }
}