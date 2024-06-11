import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for String, hashCode: 1", rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return "String";
            }
            public Object[] getArguments() {
                return null;
            }
            public Object callRealMethod() throws Throwable {
                return null;
            }
            public boolean isRealMethod() {
                return false;
            }
            public Object getMockitoInterceptor() {
                return null;
            }
            public void setMockitoInterceptor(Object mockitoInterceptor) {
                
            }
        }));
        
        // Test compareTo() method
        assertEquals(0, rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return "Object";
            }
            public Object[] getArguments() {
                return new Object[] {"Object"};
            }
            public Object callRealMethod() throws Throwable {
                return null;
            }
            public boolean isRealMethod() {
                return false;
            }
            public Object getMockitoInterceptor() {
                return null;
            }
            public void setMockitoInterceptor(Object mockitoInterceptor) {
                
            }
        }));
        
        // Test all other methods
        assertNull(rev.returnValueFor(null));
        assertTrue(rev.returnValueFor(Boolean.class) instanceof Boolean);
        assertTrue(rev.returnValueFor(Byte.class) instanceof Byte);
        assertTrue(rev.returnValueFor(Character.class) instanceof Character);
        assertTrue(rev.returnValueFor(Double.class) instanceof Double);
        assertTrue(rev.returnValueFor(Float.class) instanceof Float);
        assertTrue(rev.returnValueFor(Integer.class) instanceof Integer);
        assertTrue(rev.returnValueFor(Long.class) instanceof Long);
        assertTrue(rev.returnValueFor(Short.class) instanceof Short);
        assertTrue(rev.returnValueFor(Void.class) instanceof Void);
        
        assertTrue(rev.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(rev.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(rev.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(rev.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(rev.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(rev.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(rev.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(rev.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(rev.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(rev.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(rev.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(rev.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(rev.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(rev.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
        
        assertNull(rev.returnValueFor(Object.class));
    }
}