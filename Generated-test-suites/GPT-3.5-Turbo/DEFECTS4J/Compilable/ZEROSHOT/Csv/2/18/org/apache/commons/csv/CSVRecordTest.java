package org.apache.commons.csv;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.csv.CSVRecord;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
        Assert.assertNull(record.get(3));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("value1", record.get("col1"));
        Assert.assertEquals("value2", record.get("col2"));
        Assert.assertEquals("value3", record.get("col3"));
        Assert.assertNull(record.get("col4"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("col1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record1.isConsistent());

        String[] values2 = {"value1", "value2"};
        CSVRecord record2 = new CSVRecord(values2, mapping, null, 2);
        Assert.assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isMapped("col1"));
        Assert.assertTrue(record.isMapped("col2"));
        Assert.assertTrue(record.isMapped("col3"));
        Assert.assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record1.isSet("col1"));
        Assert.assertTrue(record1.isSet("col3"));
        Assert.assertFalse(record1.isSet("col4"));

        CSVRecord record2 = new CSVRecord(values, mapping, null, 2);
        Assert.assertFalse(record2.isSet("col1"));
        Assert.assertFalse(record2.isSet("col2"));
        Assert.assertFalse(record2.isSet("col3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Iterator<String> iterator = record.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value1", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value2", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value3", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String[] expectedValues = {"value1", "value2", "value3"};
        Assert.assertArrayEquals(expectedValues, record.values());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "This is a comment", 1);
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 10);
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(3, record.size());
    }
}