package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private final String[] values = {"value1", "value2", "value3"};
    private final String comment = "This is a comment";
    private final long recordNumber = 1;

    @Before
    public void setup() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetEnum() {
        Assert.assertEquals("value1", csvRecord.get(EnumExample.ENUM1));
    }

    @Test
    public void testGetIndex() {
        Assert.assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(values, null, comment, recordNumber);
        csvRecordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidMapping() {
        csvRecord.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidIndex() {
        csvRecord.get("column3");
        csvRecord.get("column4");
    }

    @Test
    public void testGetComment() {
        Assert.assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        Assert.assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Assert.assertTrue(csvRecord.isMapped("column1"));
    }

    @Test
    public void testIsMappedInvalidColumn() {
        Assert.assertFalse(csvRecord.isMapped("invalidColumn"));
    }

    @Test
    public void testIsSet() {
        Assert.assertTrue(csvRecord.isSet("column1"));
    }

    @Test
    public void testIsSetInvalidColumn() {
        Assert.assertFalse(csvRecord.isSet("invalidColumn"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        expectedMap.put("column3", "value3");

        Assert.assertEquals(expectedMap, csvRecord.toMap());
    }

    @Test
    public void testToString() {
        String expected = "[value1, value2, value3]";
        Assert.assertEquals(expected, csvRecord.toString());
    }

    @Test
    public void testValues() {
        String[] expectedValues = {"value1", "value2", "value3"};
        Assert.assertArrayEquals(expectedValues, csvRecord.values());
    }

    private enum EnumExample {
        ENUM1, ENUM2, ENUM3
    }

}