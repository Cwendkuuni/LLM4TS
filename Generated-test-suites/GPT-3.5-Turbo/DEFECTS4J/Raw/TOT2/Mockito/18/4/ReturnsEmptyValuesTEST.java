import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        String expected = "Mock for ReturnsEmptyValues, hashCode: " + returnValue.hashCode();
        InvocationOnMock invocation = new InvocationOnMock(this, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class), null);
        assertEquals(expected, returnValue.answer(invocation));
    }

    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock(this, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class), null);
        assertEquals(0, returnValue.answer(invocation)); // CompareTo returns 0 when references are the same
    }

    @Test
    public void testAnswerPrimitiveOrWrapper() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        assertTrue(returnValue.answer(new InvocationOnMock(this, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class), null)) instanceof Integer);
    }

    @Test
    public void testAnswerCollection() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        assertTrue(returnValue.answer(new InvocationOnMock(this, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class), null)) instanceof LinkedList);
    }

    @Test
    public void testAnswerSet() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        assertTrue(returnValue.answer(new InvocationOnMock(this, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class), null)) instanceof HashSet);
    }

    @Test
    public void testAnswerMap() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        assertTrue(returnValue.answer(new InvocationOnMock(this, ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class), null)) instanceof HashMap);
    }
}