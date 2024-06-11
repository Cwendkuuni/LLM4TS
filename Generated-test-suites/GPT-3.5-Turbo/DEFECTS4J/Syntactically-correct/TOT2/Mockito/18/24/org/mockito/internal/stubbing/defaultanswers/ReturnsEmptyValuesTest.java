import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_ReturnsEmptyCollectionForCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        Object returnValue = returnsEmptyValues.returnValueFor(Collection.class);
        
        assertTrue(returnValue instanceof LinkedList);
        assertTrue(((LinkedList<?>)returnValue).isEmpty());
    }

    @Test
    public void testAnswer_ReturnsZeroIfReferencesAreEqualForCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        Object argument = new Object();
        InvocationOnMock invocation = new InvocationOnMock(mock(Object.class), null, "compareTo", new Class[]{Object.class}, new Object[]{argument});
        
        Object returnValue = returnsEmptyValues.answer(invocation);
        
        assertEquals(0, returnValue);
    }

    @Test
    public void testAnswer_ReturnsDescriptionOfMockForToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        MockName mockName = new MockName("Mock", "name", false);
        MockSettings mockSettings = new MockSettings<Object>().typeToMock(Object.class);
        Object mock = mock(Object.class);
        when(mockUtil.getMockName(mock)).thenReturn(mockName);
        when(mockUtil.getMockSettings(mock)).thenReturn(mockSettings);
        
        InvocationOnMock invocation = new InvocationOnMock(mock, null, "toString", null, null);
        
        Object returnValue = returnsEmptyValues.answer(invocation);
        
        assertEquals("name", returnValue);
    }

}