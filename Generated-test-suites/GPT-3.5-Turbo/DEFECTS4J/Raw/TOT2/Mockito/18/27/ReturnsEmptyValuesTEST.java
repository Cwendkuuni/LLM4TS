import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = { new Object() };
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswerPrimitiveReturnType() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    // Add more tests for other public methods in ReturnsEmptyValues class

}