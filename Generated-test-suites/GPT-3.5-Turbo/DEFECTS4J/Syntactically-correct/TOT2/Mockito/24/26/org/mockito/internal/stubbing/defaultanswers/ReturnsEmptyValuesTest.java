import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertNotNull(emptyValues.answer(null));
    }

    @Test
    public void testReturnValueFor() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertNotNull(emptyValues.returnValueFor(Collection.class));
    }

    @Test
    public void testToString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertNotNull(emptyValues.answer(invocationToStringMethod()));
    }

    private InvocationOnMock invocationToStringMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ReturnsEmptyValues.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        return invocation;
    }

}