import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {
    
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockName name = mock(MockName.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[0]); // Assume first method is toString()
        when(mockUtil.getMockName(invocation.getMock())).thenReturn(name);
        when(name.isDefault()).thenReturn(true);
        when(mockUtil.getMockSettings(invocation.getMock()).getTypeToMock()).thenReturn(String.class);
        when(invocation.getMock().hashCode()).thenReturn(123);
        
        assertEquals("Mock for String, hashCode: 123", returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object[] arguments = { invocation.getMock() };
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[1]); // Assume second method is compareTo()
        when(invocation.getArguments()).thenReturn(arguments);
        
        assertEquals(0, returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testAnswerPrimitive() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethods()[2]); // Assume third method is a primitive-returning method
        
        assertEquals(0, returnsEmptyValues.answer(invocation));
    }
    
    // Add more test cases for other methods
    
}