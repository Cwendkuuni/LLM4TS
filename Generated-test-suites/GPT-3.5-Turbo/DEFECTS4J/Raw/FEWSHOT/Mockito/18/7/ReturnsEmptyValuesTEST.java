import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Test returnType for primitive types
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class), 0.001);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class), 0.001);
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(byte.class));
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(short.class));
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(char.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));

        // Test returnType for wrapper types
        assertEquals(Integer.valueOf(0), returnsEmptyValues.returnValueFor(Integer.class));
        assertEquals(Long.valueOf(0), returnsEmptyValues.returnValueFor(Long.class));
        assertEquals(Float.valueOf(0.0f), returnsEmptyValues.returnValueFor(Float.class));
        assertEquals(Double.valueOf(0.0), returnsEmptyValues.returnValueFor(Double.class));
        assertEquals(Byte.valueOf((byte) 0), returnsEmptyValues.returnValueFor(Byte.class));
        assertEquals(Short.valueOf((short) 0), returnsEmptyValues.returnValueFor(Short.class));
        assertEquals(Character.valueOf('\u0000'), returnsEmptyValues.returnValueFor(Character.class));
        assertEquals(Boolean.FALSE, returnsEmptyValues.returnValueFor(Boolean.class));

        // Test returnType for Collection types
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(Collection.class));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(Set.class));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(HashSet.class));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(SortedSet.class));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.returnValueFor(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(List.class));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(LinkedList.class));
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.returnValueFor(ArrayList.class));

        // Test returnType for Map types
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(Map.class));
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.returnValueFor(LinkedHashMap.class));
        
        // Test returnType for unknown type
        assertNull(returnsEmptyValues.returnValueFor(Object.class));
    }

}