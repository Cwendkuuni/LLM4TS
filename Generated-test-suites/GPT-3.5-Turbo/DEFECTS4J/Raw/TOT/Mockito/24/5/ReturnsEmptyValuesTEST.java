import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
        invocation = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer_toStringMethod_ReturnsDescriptionOfMock() {
        when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(Mockito.mockUtil.getMockName(mock)).thenReturn(MockName.defaultMockName(mock));
        when(Mockito.mockUtil.getMockSettings(mock).getTypeToMock()).thenReturn(Object.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswer_compareToMethod_ReturnsOne() {
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(1, result);
    }

    @Test
    public void testAnswer_primitiveReturnType_ReturnsAppropriatePrimitiveValue() throws NoSuchMethodException {
        when(invocation.getMethod()).thenReturn(MyClass.class.getMethod("getPrimitiveInt"));

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void testAnswer_wrapperClassReturnType_ReturnsCorrespondingDefaultValue() throws NoSuchMethodException {
        when(invocation.getMethod()).thenReturn(MyClass.class.getMethod("getWrapperInt"));

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(0, result);
    }

    @Test
    public void testAnswer_collectionReturnType_ReturnsEmptyLinkedList() throws NoSuchMethodException {
        when(invocation.getMethod()).thenReturn(MyClass.class.getMethod("getCollection"));

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }
    
    // Add more test cases for other methods
    
    
    private class MyClass {
        public int getPrimitiveInt() {
            return 0;
        }

        public Integer getWrapperInt() {
            return 0;
        }

        public Collection<Object> getCollection() {
            return new LinkedList<>();
        }

        // Add more methods to test
    }

}