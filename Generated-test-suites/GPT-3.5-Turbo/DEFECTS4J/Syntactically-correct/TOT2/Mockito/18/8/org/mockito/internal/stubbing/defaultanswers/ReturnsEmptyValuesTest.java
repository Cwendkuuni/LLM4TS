import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = new InvocationOnMock();
        assertEquals("Mock for String, hashCode: 0", returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = new InvocationOnMock();
        Object mock = new Object();
        invocation.setArguments(new Object[]{mock});
        assertEquals(1, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerPrimitiveReturnType() {
        InvocationOnMock invocation = new InvocationOnMock();
        invocation.setMethod(Integer.class.getMethod("intValue"));
        assertEquals(0, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForCollection() {
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(Collection.class));
    }

    @Test
    public void testReturnValueForSet() {
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(Set.class));
    }

    @Test
    public void testReturnValueForMap() {
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(Map.class));
    }

    // Add more tests for other methods and edge cases if needed

}