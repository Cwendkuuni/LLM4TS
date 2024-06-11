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

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_noHeaderMapping() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(new String[] { "value1" }, null, null, 1);
        csvRecordWithoutMapping.get("column1");
    }

    @Test
    public void testGetByName_mappingNotFound() {
        assertNull(csvRecord.get("column4"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_inconsistentMapping() {
        Map<String, Integer> inconsistentMapping = new HashMap<String, Integer>();
        inconsistentMapping.put("column1", 0);
        inconsistentMapping.put("column2", 1);
        assertFalse(new CSVRecord(new String[] { "value1" }, inconsistentMapping, null, 1).isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsMapped_mappingNotFound() {
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
    }

    @Test
    public void testIsSet_mappingNotFound() {
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(csvRecord.size(), count);
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertEquals(csvRecord.size(), values.length);
        for (int i = 0; i < csvRecord.size(); i++) {
            assertEquals(csvRecord.get(i), values[i]);
        }
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}