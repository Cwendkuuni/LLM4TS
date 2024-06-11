import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.util.Map;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = createMockInvocation("toString");
        Object result = returnValue.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(((String) result).contains("Mock for"));
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = createMockInvocation("compareTo");
        Object result = returnValue.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(1, ((Integer) result).intValue());
    }

    @Test
    public void testCollectionReturnType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.returnValueFor(Collection.class);
        assertNotNull(result);
        assertTrue(result instanceof Collection);
        assertTrue(((Collection) result).isEmpty());
    }

    @Test
    public void testSetReturnType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.returnValueFor(Set.class);
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testMapReturnType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Object result = returnValue.returnValueFor(Map.class);
        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertTrue(((Map) result).isEmpty());
    }

    private InvocationOnMock createMockInvocation(String methodName) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn(methodName);
        return invocation;
    }
}