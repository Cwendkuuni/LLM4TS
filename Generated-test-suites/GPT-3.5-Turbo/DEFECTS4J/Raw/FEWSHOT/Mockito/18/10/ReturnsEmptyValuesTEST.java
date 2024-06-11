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
        
        // Test for toString() method
        Object mock = new Object();
        String expectedToString = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expectedToString, rev.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }
            
            @Override
            public java.lang.reflect.Method getMethod() {
                return null;
            }
            
            @Override
            public Object[] getArguments() {
                return null;
            }
        }));
        
        // Test for compareTo() method
        Object object = new Object();
        assertEquals(0, rev.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return object;
            }
            
            @Override
            public java.lang.reflect.Method getMethod() {
                return null;
            }
            
            @Override
            public Object[] getArguments() {
                return new Object[] {object};
            }
        }));
        assertTrue(rev.returnValueFor(Object.class) != 0);
        
        // Test for primitive and wrapper class methods
        assertEquals(0, rev.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }
            
            @Override
            public java.lang.reflect.Method getMethod() {
                return null;
            }
            
            @Override
            public Object[] getArguments() {
                return null;
            }
        }));
        
        // Test for collection and map methods
        assertTrue(rev.returnValueFor(Collection.class) instanceof LinkedList<?>);
        assertTrue(rev.returnValueFor(Set.class) instanceof HashSet<?>);
        assertTrue(rev.returnValueFor(HashSet.class) instanceof HashSet<?>);
        assertTrue(rev.returnValueFor(SortedSet.class) instanceof TreeSet<?>);
        assertTrue(rev.returnValueFor(TreeSet.class) instanceof TreeSet<?>);
        assertTrue(rev.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet<?>);
        assertTrue(rev.returnValueFor(List.class) instanceof LinkedList<?>);
        assertTrue(rev.returnValueFor(LinkedList.class) instanceof LinkedList<?>);
        assertTrue(rev.returnValueFor(ArrayList.class) instanceof ArrayList<?>);
        assertTrue(rev.returnValueFor(Map.class) instanceof HashMap<?, ?>);
        assertTrue(rev.returnValueFor(HashMap.class) instanceof HashMap<?, ?>);
        assertTrue(rev.returnValueFor(SortedMap.class) instanceof TreeMap<?, ?>);
        assertTrue(rev.returnValueFor(TreeMap.class) instanceof TreeMap<?, ?>);
        assertTrue(rev.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap<?, ?>);
    }
}