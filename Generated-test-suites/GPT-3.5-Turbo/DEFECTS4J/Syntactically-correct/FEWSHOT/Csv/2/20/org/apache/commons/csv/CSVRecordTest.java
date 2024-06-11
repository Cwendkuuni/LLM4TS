package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        record = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[] { "value1" }, null, "comment", 1);
        recordWithoutMapping.get("name1");
    }

    @Test
    public void testIsConsistent() {
        assertEquals(true, record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("name1", 0);
        inconsistentMapping.put("name2", 1);
        CSVRecord inconsistentRecord = new CSVRecord(new String[] { "value1" }, inconsistentMapping, "comment", 1);
        assertEquals(false, inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertEquals(true, record.isMapped("name1"));
        assertEquals(true, record.isMapped("name2"));
        assertEquals(true, record.isMapped("name3"));
    }

    @Test
    public void testIsNotMapped() {
        assertEquals(false, record.isMapped("name4"));
    }

    @Test
    public void testIsSet() {
        assertEquals(true, record.isSet("name1"));
        assertEquals(true, record.isSet("name2"));
        assertEquals(true, record.isSet("name3"));
    }

    @Test
    public void testIsNotSet() {
        assertEquals(false, record.isSet("name4"));
    }

    @Test
    public void testIterator() {
        StringBuilder sb = new StringBuilder();
        for (String value : record) {
            sb.append(value);
        }
        assertEquals("value1value2value3", sb.toString());
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }
}