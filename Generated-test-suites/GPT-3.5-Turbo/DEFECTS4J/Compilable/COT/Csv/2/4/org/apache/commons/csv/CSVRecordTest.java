package org.apache.commons.csv;
// Generated JUnit test cases for class CSVRecord
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        Assert.assertEquals("value1", record.get("column1"));
        Assert.assertEquals("value2", record.get("column2"));
        Assert.assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        recordWithoutMapping.get("column1");
    }

    @Test
    public void testGetByNameNotFound() {
        Assert.assertNull(record.get("column4"));
    }

    @Test
    public void testIsConsistent() {
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingMismatch() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("column1", 0);
        inconsistentMapping.put("column2", 1);
        inconsistentMapping.put("column3", 3);
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, inconsistentMapping, null, 1);
        Assert.assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Assert.assertTrue(record.isMapped("column1"));
        Assert.assertTrue(record.isMapped("column2"));
        Assert.assertTrue(record.isMapped("column3"));
    }

    @Test
    public void testIsMappedNotFound() {
        Assert.assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        Assert.assertTrue(record.isSet("column1"));
        Assert.assertTrue(record.isSet("column2"));
        Assert.assertTrue(record.isSet("column3"));
    }

    @Test
    public void testIsSetNotFound() {
        Assert.assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
        Assert.assertEquals("value3", values[2]);
    }

    @Test
    public void testGetComment() {
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }
}