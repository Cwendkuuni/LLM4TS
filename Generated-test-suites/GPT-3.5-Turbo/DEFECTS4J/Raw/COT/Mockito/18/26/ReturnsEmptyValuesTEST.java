
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + rev.hashCode(), rev.answer(null));
        
        // Test compareTo() method
        assertEquals(0, rev.answer(createInvocation(rev, rev)));
        assertEquals(1, rev.answer(createInvocation(rev, new ReturnsEmptyValues())));
        
        // Test primitive and wrapper type return values
        assertEquals(0, rev.answer(createInvocation(rev, int.class)));
        assertEquals(0, rev.answer(createInvocation(rev, Integer.class)));
        assertEquals(false, rev.answer(createInvocation(rev, boolean.class)));
        assertEquals(false, rev.answer(createInvocation(rev, Boolean.class)));
        assertEquals((char)0, rev.answer(createInvocation(rev, char.class)));
        assertEquals((char)0, rev.answer(createInvocation(rev, Character.class)));
        assertEquals((byte)0, rev.answer(createInvocation(rev, byte.class)));
        assertEquals((byte)0, rev.answer(createInvocation(rev, Byte.class)));
        assertEquals((short)0, rev.answer(createInvocation(rev, short.class)));
        assertEquals((short)0, rev.answer(createInvocation(rev, Short.class)));
        assertEquals(0L, rev.answer(createInvocation(rev, long.class)));
        assertEquals(0L, rev.answer(createInvocation(rev, Long.class)));
        assertEquals(0.0f, rev.answer(createInvocation(rev, float.class)));
        assertEquals(0.0f, rev.answer(createInvocation(rev, Float.class)));
        assertEquals(0.0, rev.answer(createInvocation(rev, double.class)));
        assertEquals(0.0, rev.answer(createInvocation(rev, Double.class)));
        
        // Test collection return values
        assertTrue(rev.answer(createInvocation(rev, Collection.class)) instanceof LinkedList<?>);
        assertTrue(rev.answer(createInvocation(rev, Set.class)) instanceof HashSet<?>);
        assertTrue(rev.answer(createInvocation(rev, HashSet.class)) instanceof HashSet<?>);
        assertTrue(rev.answer(createInvocation(rev, SortedSet.class)) instanceof TreeSet<?>);
        assertTrue(rev.answer(createInvocation(rev, TreeSet.class)) instanceof TreeSet<?>);
        assertTrue(rev.answer(createInvocation(rev, LinkedHashSet.class)) instanceof LinkedHashSet<?>);
        assertTrue(rev.answer(createInvocation(rev, List.class)) instanceof LinkedList<?>);
        assertTrue(rev.answer(createInvocation(rev, LinkedList.class)) instanceof LinkedList<?>);
        assertTrue(rev.answer(createInvocation(rev, ArrayList.class)) instanceof ArrayList<?>);
        assertTrue(rev.answer(createInvocation(rev, Map.class)) instanceof HashMap<?, ?>);
        assertTrue(rev.answer(createInvocation(rev, HashMap.class)) instanceof HashMap<?, ?>);
        assertTrue(rev.answer(createInvocation(rev, SortedMap.class)) instanceof TreeMap<?, ?>);
        assertTrue(rev.answer(createInvocation(rev, TreeMap.class)) instanceof TreeMap<?, ?>);
        assertTrue(rev.answer(createInvocation(rev, LinkedHashMap.class)) instanceof LinkedHashMap<?, ?>);
    }
    
    private InvocationOnMock createInvocation(Object mock, Class<?> returnType) {
        return new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }
            
            @Override
            public Object[] getArguments() {
                return new Object[0];
            }
            
            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }
            
            @Override
            public boolean isToString() {
                return false;
            }
            
            @Override
            public boolean isCompareToMethod() {
                return false;
            }
            
            @Override
            public java.lang.reflect.Method getMethod() {
                try {
                    return mock.getClass().getMethod("method", returnType);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }
            
            @Override
            public boolean equals(Object obj) {
                return false;
            }
            
            @Override
            public int hashCode() {
                return 0;
            }
        };
    }
    
    private InvocationOnMock createInvocation(Object mock1, Object mock2) {
        return new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock1;
            }
            
            @Override
            public Object[] getArguments() {
                return new Object[] { mock2 };
            }
            
            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }
            
            @Override
            public boolean isToString() {
                return false;
            }
            
            @Override
            public boolean isCompareToMethod() {
                return true;
            }
            
            @Override
            public java.lang.reflect.Method getMethod() {
                try {
                    return mock1.getClass().getMethod("compareTo", Object.class);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }
            
            @Override
            public boolean equals(Object obj) {
                return false;
            }
            
            @Override
            public int hashCode() {
                return 0;
            }
        };
    }
    
}
