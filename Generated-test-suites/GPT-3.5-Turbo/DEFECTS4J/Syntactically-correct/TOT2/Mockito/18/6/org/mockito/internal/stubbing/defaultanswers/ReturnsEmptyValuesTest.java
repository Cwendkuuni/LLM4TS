import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock();
        invocation.setMethod(ObjectMethodsGuru.class.getMethods()[0]);
        Object result = returnValue.answer(invocation);
        assertNotNull(result);
        assertEquals("Mock for ObjectMethodsGuru, hashCode: 0", result);
    }

    @Test
    public void testAnswer_compareToMethod() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock();
        Object mock = new Object();
        Object[] arguments = {mock};
        invocation.setArguments(arguments);
        invocation.setMock(mock);
        int result = (int) returnValue.answer(invocation);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueFor_primitiveType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.returnValueFor(int.class);
        assertNotNull(result);
        assertEquals(0, (int) result);
    }

    @Test
    public void testReturnValueFor_collectionType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.returnValueFor(Collection.class);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueFor_mapType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.returnValueFor(Map.class);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testReturnValueFor_invalidType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.returnValueFor(LinkedList.class);
        assertNull(result);
    }
}