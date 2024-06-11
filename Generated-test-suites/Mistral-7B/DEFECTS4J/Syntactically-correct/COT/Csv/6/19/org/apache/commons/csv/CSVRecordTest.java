package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVRecord.
 *
 * @author <a href="mailto:john.leidegren@gmail.com">John Leidegren</a>
 */
public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = new String[]{"Alice", "35", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name", 0);
            put("age", 1);
            put("email", 2);
        }};
        record = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetEnum() {
        assertSame(record.get(Enum.NAME), "Alice");
        assertSame(record.get(Enum.AGE), "35");
        assertSame(record.get(Enum.EMAIL), "alice@example.com");
    }

    @Test
    public void testGetInt() {
        assertSame(record.get(0), "Alice");
        assertSame(record.get(1), "35");
        assertSame(record.get(2), "alice@example.com");
    }

    @Test
    public void testGetString() {
        assertSame(record.get("name"), "Alice");
        assertSame(record.get("age"), "35");
        assertSame(record.get("email"), "alice@example.com");

        assertNull(record.get("nonexistent"));

        String comment = "Comment for record 1";
        CSVRecord commentRecord = new CSVRecord(EMPTY_STRING_ARRAY, null, comment, 1);
        assertSame(commentRecord.getComment(), comment);
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertSame(record.getRecordNumber(), 1);
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());

        Map<String, Integer> inconsistentMapping = new HashMap<String, Integer>() {{
            put("name", 0);
            put("age", 2);
        }};
        CSVRecord inconsistentRecord = new CSVRecord(values, inconsistentMapping, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("nonexistent"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("nonexistent"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoMapping() {
        CSVRecord noMappingRecord = new CSVRecord(values, null, null, 1);
        noMappingRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutOfBounds() {
        CSVRecord outOfBoundsRecord = new CSVRecord(values, mapping, null, 1);
        outOfBoundsRecord.get(values.length + 1);
    }

    @Test
    public void testIterator() {
        List<String> expectedList = Arrays.asList("Alice", "35", "alice@example.com");
        Iterator<String> iterator = record.iterator();
        List<String> actualList = Arrays.asList(iterator.next(), iterator.next(), iterator.next());
        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.putAll(record.putIn(map));
        List<Map.Entry<String, String>> entries = new ArrayList<>(map.entrySet());
        List<String> values = Arrays.asList("Alice", "35", "alice@example.com");
        List<String> keys = Arrays.asList("name", "age", "email");
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, String> entry = entries.get(i);
            String key = entry.getKey();
            String value = entry.getValue();
            assertTrue(values.contains(value));
            assertTrue(keys.contains(key));
            assertEquals(key, record.get(key));
        }
    }

    @Test
    public void testSize() {
        assertEquals(values.length, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[Alice, 35, alice@example.com]", record.toString());
    }

    @Test
    public void testValues() {
        assertSame(values, record.values());
    }

}