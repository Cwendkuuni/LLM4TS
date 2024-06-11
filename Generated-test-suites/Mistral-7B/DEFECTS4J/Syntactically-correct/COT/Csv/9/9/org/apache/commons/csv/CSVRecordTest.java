package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"Alice", "25", "female"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertArrayEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.comment);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"Alice", "25", "female"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test get(Enum)
        Assert.assertEquals("Alice", record.get(Enum.valueOf("Name")));
        // Test get(int)
        Assert.assertEquals("Alice", record.get(0));
        // Test get(String)
        Assert.assertEquals("25", record.get("Age"));
        // Test getComment()
        Assert.assertNull(record.getComment());
        // Test getRecordNumber()
        Assert.assertEquals(1, record.getRecordNumber());
        // Test isConsistent()
        Assert.assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidName() {
        String[] values = new String[]{"Alice", "25", "female"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("InvalidName");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethodWithoutMapping() {
        String[] values = new String[]{"Alice", "25", "female"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test
    public void testIsMappedMethod() {
        String[] values = new String[]{"Alice", "25", "female"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("Gender"));
        Assert.assertFalse(record.isMapped("InvalidName"));
    }

    @Test
    public void testIsSetMethod() {
        String[] values = new String[]{"Alice", "25", "female"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("Gender"));
        Assert.assertFalse(record.isSet("InvalidName"));
    }

    @Test
    public void testIteratorMethod() {
        String[] values = new String[]{"Alice", "25", "female"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = record.iterator();
        List<String> expectedList = Arrays.asList("Alice", "25", "female");
        Assert.assertEquals(expectedList, Arrays.asList(iterator.next(), iterator.next(), iterator.next()));
    }

    @Test
    public void testPutInMethod() {
        String[] values = new String[]{"Alice", "25", "female"};
        Map<String, String> result = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        record.putIn(result);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "Alice");
        expectedMap.put("Age", "25");
        expectedMap.put("Gender", "female");

        Assert.assertEquals(expectedMap, result);
    }

    @Test
    public void testSizeMethod() {
        String[] values = new String[]{"Alice", "25", "female"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToStringMethod() {
        String[] values = new String[]{"Alice", "25", "female"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals("[Alice, 25, female]", record.toString());
    }
}