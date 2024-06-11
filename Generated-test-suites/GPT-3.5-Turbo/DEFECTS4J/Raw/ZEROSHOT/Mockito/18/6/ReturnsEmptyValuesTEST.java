import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_ToString() {
        String actual = (String) returnsEmptyValues.answer(null);
        assertTrue(actual.startsWith("Mock for "));
        assertTrue(actual.endsWith(", hashCode: "));
    }

    @Test
    public void testAnswer_CompareTo() {
        int actual1 = (int) returnsEmptyValues.answer(null);
        assertEquals(0, actual1);

        Integer actual2 = (Integer) returnsEmptyValues.answer(null);
        assertEquals(0, actual2.intValue());
    }

    @Test
    public void testAnswer_Primitive() {
        int actual1 = (int) returnsEmptyValues.answer(null);
        assertEquals(0, actual1);

        Integer actual2 = (Integer) returnsEmptyValues.answer(null);
        assertEquals(0, actual2.intValue());
    }

    @Test
    public void testAnswer_Collection() {
        Collection<Object> actual = (Collection<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
        assertTrue(actual instanceof LinkedList);
    }

    @Test
    public void testAnswer_Set() {
        Set<Object> actual = (Set<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
        assertTrue(actual instanceof HashSet);
    }

    @Test
    public void testAnswer_HashSet() {
        HashSet<Object> actual = (HashSet<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_SortedSet() {
        SortedSet<Object> actual = (SortedSet<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
        assertTrue(actual instanceof TreeSet);
    }

    @Test
    public void testAnswer_TreeSet() {
        TreeSet<Object> actual = (TreeSet<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_LinkedHashSet() {
        LinkedHashSet<Object> actual = (LinkedHashSet<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_List() {
        List<Object> actual = (List<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
        assertTrue(actual instanceof LinkedList);
    }

    @Test
    public void testAnswer_LinkedList() {
        LinkedList<Object> actual = (LinkedList<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_ArrayList() {
        ArrayList<Object> actual = (ArrayList<Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_Map() {
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
        assertTrue(actual instanceof HashMap);
    }

    @Test
    public void testAnswer_HashMap() {
        HashMap<Object, Object> actual = (HashMap<Object, Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_SortedMap() {
        SortedMap<Object, Object> actual = (SortedMap<Object, Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
        assertTrue(actual instanceof TreeMap);
    }

    @Test
    public void testAnswer_TreeMap() {
        TreeMap<Object, Object> actual = (TreeMap<Object, Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_LinkedHashMap() {
        LinkedHashMap<Object, Object> actual = (LinkedHashMap<Object, Object>) returnsEmptyValues.answer(null);
        assertNotNull(actual);
    }

    @Test
    public void testAnswer_Default() {
        Object actual = returnsEmptyValues.answer(null);
        assertNull(actual);
    }
}