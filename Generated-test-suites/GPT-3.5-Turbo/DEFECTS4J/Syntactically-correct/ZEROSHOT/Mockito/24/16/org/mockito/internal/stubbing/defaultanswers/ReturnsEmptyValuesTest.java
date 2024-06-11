import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;

public class ReturnsEmptyValuesTest {

    /**
     * Test method for {@link org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues#answer(org.mockito.invocation.InvocationOnMock)}.
     */
    @Test
    public void testAnswer() {
        ReturnsEmptyValues obj = new ReturnsEmptyValues();
        InvocationOnMock invocation = createInvocationMock();
        
        // Test case for isToString method
        invocation.getMethod().setName("toString");
        assertEquals("Expected toString value", "Mock for Object, hashCode: 0", obj.answer(invocation));
        
        // Test case for isCompareToMethod method
        invocation.getMethod().setName("compareTo");
        assertEquals("Expected compareTo value", 1, obj.answer(invocation));
        
        // Test case for primitive type return
        invocation.getMethod().setName("booleanReturn");
        assertEquals("Expected boolean return value", false, obj.answer(invocation));
        
        // Test case for primitive wrapper class return
        invocation.getMethod().setName("integerReturn");
        assertEquals("Expected Integer return value", 0, obj.answer(invocation));
        
        // Test case for collection return
        invocation.getMethod().setName("collectionReturn");
        assertNotNull("Expected non-null collection", obj.answer(invocation));
        assertEquals("Expected empty collection", 0, obj.answer(invocation).size());
    }
    
    private InvocationOnMock createInvocationMock() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockName nameMock = mock(MockName.class);
        MockUtil mockUtilMock = mock(MockUtil.class);
        ObjectMethodsGuru methodsGuruMock = mock(ObjectMethodsGuru.class);
        
        when(invocation.getMock()).thenReturn(new Object());
        when(invocation.getMethod()).thenReturn(new Object().getClass().getMethod("toString"));
        when(nameMock.isDefault()).thenReturn(true);
        when(mockUtilMock.getMockSettings(any())).thenReturn(null);
        when(mockUtilMock.getMockName(any())).thenReturn(nameMock);
        when(methodsGuruMock.isToString(any())).thenReturn(true);
        
        return invocation;
    }

}