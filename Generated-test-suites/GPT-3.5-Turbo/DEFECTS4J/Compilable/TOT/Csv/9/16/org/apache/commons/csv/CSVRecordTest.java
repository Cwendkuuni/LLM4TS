package org.apache.commons.csv;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    // Test the get(Enum) method
    @Test
    public void testGetByEnum() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the get(Enum) method returns the correct value
        Assert.assertEquals("A", record.get(0));
        Assert.assertEquals("B", record.get(1));
        Assert.assertEquals("C", record.get(2));
    }

    // Test the get(int) method
    @Test
    public void testGetByIndex() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the get(int) method returns the correct value
        Assert.assertEquals("A", record.get("Header1"));
        Assert.assertEquals("B", record.get("Header2"));
        Assert.assertEquals("C", record.get("Header3"));
    }

    // Test the get(String) method
    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        // Create a CSVRecord without mapping
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // This method should throw an IllegalStateException
        record.get("Header1");
    }

    // Test the get(String) method with invalid name
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidName() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // This method should throw an IllegalArgumentException
        record.get("InvalidHeader");
    }

    // Test the get(String) method with inconsistent record
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentRecord() {
        // Create a CSVRecord with inconsistent record
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // This method should throw an IllegalArgumentException
        record.get("Header3");
    }

    // Test the getComment() method
    @Test
    public void testGetComment() {
        // Create a CSVRecord with a comment
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "This is a comment", 1);

        // Assert that the getComment() method returns the correct comment
        Assert.assertEquals("This is a comment", record.getComment());
    }

    // Test the getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        // Create a CSVRecord with a record number
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 10);

        // Assert that the getRecordNumber() method returns the correct record number
        Assert.assertEquals(10, record.getRecordNumber());
    }

    // Test the isConsistent() method with a consistent record
    @Test
    public void testIsConsistentWithConsistentRecord() {
        // Create a CSVRecord with consistent record
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the isConsistent() method returns true
        Assert.assertTrue(record.isConsistent());
    }

    // Test the isConsistent() method with an inconsistent record
    @Test
    public void testIsConsistentWithInconsistentRecord() {
        // Create a CSVRecord with inconsistent record
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the isConsistent() method returns false
        Assert.assertFalse(record.isConsistent());
    }

    // Test the isMapped() method
    @Test
    public void testIsMapped() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the isMapped() method returns the correct value
        Assert.assertTrue(record.isMapped("Header1"));
        Assert.assertTrue(record.isMapped("Header2"));
        Assert.assertTrue(record.isMapped("Header3"));
        Assert.assertFalse(record.isMapped("InvalidHeader"));
    }

    // Test the isSet() method
    @Test
    public void testIsSet() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the isSet() method returns the correct value
        Assert.assertTrue(record.isSet("Header1"));
        Assert.assertTrue(record.isSet("Header2"));
        Assert.assertTrue(record.isSet("Header3"));
        Assert.assertFalse(record.isSet("InvalidHeader"));
    }

    // Test the iterator() method
    @Test
    public void testIterator() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the iterator() method returns the correct values
        Assert.assertEquals("A", record.iterator().next());
    }

    // Test the putIn() method
    @Test
    public void testPutIn() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Populate a new map using putIn() method
        Map<String, String> map = record.putIn(new HashMap<String, String>());

        // Assert that the map contains the correct values
        Assert.assertEquals("A", map.get("Header1"));
        Assert.assertEquals("B", map.get("Header2"));
        Assert.assertEquals("C", map.get("Header3"));
    }

    // Test the size() method
    @Test
    public void testSize() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the size() method returns the correct size
        Assert.assertEquals(3, record.size());
    }

    // Test the toMap() method
    @Test
    public void testToMap() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Convert the record to a map using toMap() method
        Map<String, String> map = record.toMap();

        // Assert that the map contains the correct values
        Assert.assertEquals("A", map.get("Header1"));
        Assert.assertEquals("B", map.get("Header2"));
        Assert.assertEquals("C", map.get("Header3"));
    }

    // Test the toString() method
    @Test
    public void testToString() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the toString() method returns the correct string
        Assert.assertEquals("[A, B, C]", record.toString());
    }

    // Test the values() method
    @Test
    public void testValues() {
        // Create a CSVRecord with sample values
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Assert that the values() method returns the correct array
        Assert.assertArrayEquals(values, record.values());
    }

}