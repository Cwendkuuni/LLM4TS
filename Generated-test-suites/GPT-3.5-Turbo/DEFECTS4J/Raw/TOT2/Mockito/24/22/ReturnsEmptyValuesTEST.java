import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_ToString() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock(null, null, ReturnsEmptyValues.class.getMethod("toString"));
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + rev.hashCode(), rev.answer(invocation));
    }

    @Test
    public void testAnswer_CompareToMethod() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock(null, null, ReturnsEmptyValues.class.getMethod("compareToMethod"));
        assertEquals(1, rev.answer(invocation));
    }

    @Test
    public void testReturnValueFor_PrimitiveType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        assertEquals(0, rev.returnValueFor(int.class));
    }

    @Test
    public void testReturnValueFor_CollectionType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        assertEquals(new LinkedList<>(), rev.returnValueFor(Collection.class));
    }

    @Test
    public void testReturnValueFor_SetType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        assertEquals(new HashSet<>(), rev.returnValueFor(Set.class));
    }

    @Test
    public void testReturnValueFor_MapType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        assertEquals(new HashMap<>(), rev.returnValueFor(Map.class));
    }

    @Test
    public void testReturnValueFor_OtherType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        assertNull(rev.returnValueFor(Object.class));
    }
}