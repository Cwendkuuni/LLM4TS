package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(values, csvRecord.values());
        assertSame(mapping, csvRecord.mapping);
        assertNull(csvRecord.getComment());
        assertTrue(csvRecord.isConsistent());
        assertTrue(csvRecord.size() == values.length);
        assertEquals(1, csvRecord.getRecordNumber());
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", csvRecord.get(0));
        assertEquals("Doe", csvRecord.get(1));
        assertEquals("30", csvRecord.get(2));
        assertNull(csvRecord.get(3));

        String name = "Name";
        assertEquals("John", csvRecord.get(name));
        assertEquals("Doe", csvRecord.get("Age"));
        assertNull(csvRecord.get("Unknown"));
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertFalse(csvRecord.isConsistent());

        mapping.put("Age", 2);
        assertTrue(csvRecord.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isMapped("Name"));
        assertTrue(csvRecord.isMapped("Age"));
        assertFalse(csvRecord.isMapped("Unknown"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isSet("Name"));
        assertTrue(csvRecord.isSet("Age"));
        assertFalse(csvRecord.isSet("Unknown"));

        String[] valuesWithNull = new String[]{"John", null, "30"};
        CSVRecord csvRecordWithNull = new CSVRecord(valuesWithNull, mapping, null, 1);
        assertFalse(csvRecordWithNull.isSet("Name"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Iterator<String> it = csvRecord.iterator();
        assertEquals("John", it.next());
        assertEquals("Doe", it.next());
        assertEquals("30", it.next());
        assertFalse(it.hasNext());
    }

    /**
     * Test values method.
     */
    @Test
    public void testValues() {
        String[] values = new String[]{"John", "Doe", "30"};

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        assertArrayEquals(values, csvRecord.values());
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, "Comment", 1);
        assertEquals("Comment", csvRecord.getComment());

        CSVRecord csvRecordWithoutComment = new CSVRecord(values, mapping, null, 1);
        assertNull(csvRecordWithoutComment.getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, csvRecord.getRecordNumber());
    }
}