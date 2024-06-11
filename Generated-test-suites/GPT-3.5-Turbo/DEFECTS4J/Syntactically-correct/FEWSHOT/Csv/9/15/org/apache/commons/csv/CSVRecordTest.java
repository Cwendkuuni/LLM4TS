package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGet_Enum() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGet_Index() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected=IllegalStateException.class)
    public void testGet_NoHeaderMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("column1");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGet_InvalidName() {
        record.get("column4");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGet_IndexOutOfBounds() {
        record.get("column1");
    }

    @Test
    public void testGetComment() {
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
        mapping.remove("column1");
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        assertEquals(values[0], record.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<String, String>();
        record.putIn(map);
        assertEquals(values[0], map.get("column1"));
        assertEquals(values[1], map.get("column2"));
        assertEquals(values[2], map.get("column3"));
    }

    @Test
    public void testSize() {
        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        assertEquals(values[0], record.toList().get(0));
        assertEquals(values[1], record.toList().get(1));
        assertEquals(values[2], record.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals(values[0], map.get("column1"));
        assertEquals(values[1], map.get("column2"));
        assertEquals(values[2], map.get("column3"));
    }

    @Test
    public void testValues() {
        assertEquals(values, record.values());
    }
}