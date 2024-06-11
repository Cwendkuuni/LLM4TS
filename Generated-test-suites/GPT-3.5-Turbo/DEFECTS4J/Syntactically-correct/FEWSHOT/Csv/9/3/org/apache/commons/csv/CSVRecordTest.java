
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    private CSVRecord record;
    private final String[] values = {"value1", "value2", "value3"};
    private final Map<String, Integer> mapping = new HashMap<>();

    @Before
    public void setUp() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(Enum1.VALUE1));
        assertEquals("value2", record.get(Enum1.VALUE2));
        assertEquals("value3", record.get(Enum1.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);
        recordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNonExistentColumn() {
        record.get("column4");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        record.get(3);
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());

        CSVRecord recordWithComment = new CSVRecord(values, mapping, "comment", 1);
        assertEquals("comment", recordWithComment.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());

        CSVRecord inconsistentRecord = new CSVRecord(new String[] {"value1"}, mapping, null, 2);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        for (String value : record) {
            assertNotNull(value);
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);

        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        assertNotNull(record.toList());
        assertEquals("value1", record.toList().get(0));
        assertEquals("value2", record.toList().get(1));
        assertEquals("value3", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();

        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

    private enum Enum1 {
        VALUE1, VALUE2, VALUE3;
    }
}
