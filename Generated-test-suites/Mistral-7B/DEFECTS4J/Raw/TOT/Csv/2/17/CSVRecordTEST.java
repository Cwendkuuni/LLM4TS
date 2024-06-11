package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(Arrays.asList("Name", "Age", "City"), Arrays.asList(record.values()));
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.comment);
        Assert.assertEquals(Long.parseLong("1"), record.getRecordNumber());
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordGetMethod() {
        String[] values = new String[]{"Name: John Doe", "Age: 30", "City: New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertEquals("30", record.get(1));
        Assert.assertEquals("New York", record.get(2));

        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));

        Assert.assertNull(record.get(3));
        Assert.assertNull(record.get("Unknown"));
    }

    @Test
    public void testCSVRecordIsConsistent() {
        String[] values = new String[]{"Name: John Doe", "Age: 30", "City: New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isConsistent());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);

        Assert.assertTrue(emptyRecord.isConsistent());

        String[] wrongValues = new String[]{"Name: John Doe", "Age: 30", null};
        CSVRecord wrongRecord = new CSVRecord(wrongValues, mapping, null, 1);

        Assert.assertFalse(wrongRecord.isConsistent());
    }

    @Test
    public void testCSVRecordIsMapped() {
        String[] values = new String[]{"Name: John Doe", "Age: 30", "City: New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));

        Assert.assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String[] values = new String[]{"Name: John Doe", "Age: 30", "City: New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));

        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetWithNullMapping() {
        String[] values = new String[]{"Name: John Doe", "Age: 30", "City: New York"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordGetWithNoHeaderMapping() {
        String[] values = new String[]{"Name: John Doe", "Age: 30", "City: New York"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetInvalidIndex() {
        String[] values = new String[]{"Name: John Doe", "Age: 30", "City: New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(values.length + 1);
    }
}