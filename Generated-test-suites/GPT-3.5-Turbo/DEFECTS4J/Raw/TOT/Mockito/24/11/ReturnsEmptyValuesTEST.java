import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    // Test answer method for toString() method
    @Test
    public void testAnswer_forToString() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), result);
    }

    // Test answer method for compareTo() method
    @Test
    public void testAnswer_forCompareTo() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(1, result);
    }

    // Test answer method for primitive-returning method
    @Test
    public void testAnswer_forPrimitiveReturn() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethods()[0]);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(0, result);
    }

    // Test answer method for primitive wrapper class return
    @Test
    public void testAnswer_forPrimitiveWrapperClassReturn() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethods()[1]);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(0, result);
    }

    // Test answer method for collection-returning method
    @Test
    public void testAnswer_forCollectionReturn() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethods()[2]);
        Object result = returnsEmptyValues.answer(invocation);
        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((LinkedList) result).size());
    }

    // Test answer method for other methods returning null
    @Test
    public void testAnswer_forOtherMethods() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethods()[3]);
        Object result = returnsEmptyValues.answer(invocation);
        assertNull(result);
    }

    // Sample class for testing
    class SampleClass {
        public int getPrimitive() {
            return 0;
        }
        public Integer getPrimitiveWrapper() {
            return 0;
        }
        public Collection<Object> getCollection() {
            return new LinkedList<Object>();
        }
        public void doSomething() {}
    }

}