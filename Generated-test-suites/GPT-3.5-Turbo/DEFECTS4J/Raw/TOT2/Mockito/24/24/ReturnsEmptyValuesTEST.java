import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.*;

public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testToStringMethod() {
        InvocationOnMock invocation = new InvocationOnMock(new Object(), getToStringMethod(), new Object[0]);
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testCompareToMethod() {
        InvocationOnMock invocation = new InvocationOnMock(new Object(), getCompareToMethod(), new Object[0]);
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertEquals(1, result);
    }

    @Test
    public void testReturnValueForPrimitive() {
        Class<?> returnType = int.class;
        Object result = returnsEmptyValues.returnValueFor(returnType);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForCollection() {
        Class<?> returnType = Collection.class;
        Object result = returnsEmptyValues.returnValueFor(returnType);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((Collection) result).isEmpty());
    }

    @Test
    public void testReturnValueForSet() {
        Class<?> returnType = Set.class;
        Object result = returnsEmptyValues.returnValueFor(returnType);
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testReturnValueForMap() {
        Class<?> returnType = Map.class;
        Object result = returnsEmptyValues.returnValueFor(returnType);
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((Map) result).isEmpty());
    }

    // Helper methods for creating InvocationOnMock objects

    private Method getToStringMethod() {
        try {
            return Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getCompareToMethod() {
        try {
            return Comparable.class.getMethod("compareTo", Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}