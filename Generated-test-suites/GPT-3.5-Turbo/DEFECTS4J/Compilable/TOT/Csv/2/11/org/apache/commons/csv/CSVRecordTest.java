package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    //Test for get(int) method
    @Test
    public void testGetByIndex() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals("Doe", record.get(1));
        Assert.assertEquals("25", record.get(2));
    }

    //Test for get(String) method when the column name exists
    @Test
    public void testGetByNameExists() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals("Doe", record.get("Surname"));
        Assert.assertEquals("25", record.get("Age"));
    }

    //Test for get(String) method when the column name does not exist
    @Test
    public void testGetByNameNotExists() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertNull(record.get("Address"));
    }

    //Test for get(String) method when no header mapping was specified
    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        String[] values = {"John", "Doe", "25"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    //Test for isConsistent() method when the record is consistent
    @Test
    public void testIsConsistentTrue() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    //Test for isConsistent() method when the record is not consistent
    @Test
    public void testIsConsistentFalse() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    //Test for isMapped() method when the column is mapped
    @Test
    public void testIsMapped() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isMapped("Name"));
    }

    //Test for isMapped() method when the column is not mapped
    @Test
    public void testIsNotMapped() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isMapped("Address"));
    }

    //Test for isSet() method when the column is mapped and has a value
    @Test
    public void testIsSetTrue() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Surname"));
        Assert.assertTrue(record.isSet("Age"));
    }

    //Test for isSet() method when the column is mapped but does not have a value
    @Test
    public void testIsSetFalse() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isSet("Age"));
    }

    //Test for iterator() method
    @Test
    public void testIterator() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        StringBuilder sb = new StringBuilder();
        for (String value : record) {
            sb.append(value).append(" ");
        }
        Assert.assertEquals("John Doe 25 ", sb.toString());
    }

    //Test for values() method
    @Test
    public void testValues() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertArrayEquals(new String[]{"John", "Doe", "25"}, record.values());
    }

    //Test for getComment() method
    @Test
    public void testGetComment() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, "This is a comment", 1);
        Assert.assertEquals("This is a comment", record.getComment());
    }

    //Test for getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 5);
        Assert.assertEquals(5, record.getRecordNumber());
    }

    //Test for size() method
    @Test
    public void testSize() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(3, record.size());
    }

}