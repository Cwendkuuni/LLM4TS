import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.csv.CSVRecord;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    // Test cases for get(int i)
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
    }

    // Test cases for get(String name)
    @Test
    public void testGetByName() {
        String[] values = {"name=value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("name=value", record.get("name"));
        Assert.assertNull(record.get("nonExistingColumn"));
    }

    @Test (expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"name=value"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("name");
    }

    // Test cases for isConsistent()
    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentIncompleteMapping() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    // Test cases for isMapped(String name)
    @Test
    public void testIsMapped() {
        String[] values = {"name=value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isMapped("name"));
        Assert.assertFalse(record.isMapped("nonExistingColumn"));
    }

    @Test
    public void testIsMappedNoMapping() {
        String[] values = {"name=value"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertFalse(record.isMapped("name"));
    }

    // Test cases for isSet(String name)
    @Test
    public void testIsSet() {
        String[] values = {"name=value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isSet("name"));
        Assert.assertFalse(record.isSet("nonExistingColumn"));
    }

    @Test
    public void testIsSetNoMapping() {
        String[] values = {"name=value"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertFalse(record.isSet("name"));
    }

    @Test
    public void testIsSetIncompleteMapping() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isSet("column1"));
    }

    // Test cases for iterator()
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        Assert.assertEquals(2, count);
    }

    // Test cases for getComment()
    @Test
    public void testGetComment() {
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 1);
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetCommentNull() {
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertNull(record.getComment());
    }

    // Test cases for getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, null, null, 10);
        Assert.assertEquals(10, record.getRecordNumber());
    }

    // Test cases for size()
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertEquals(3, record.size());
    }

    // Test cases for toString()
    @Test
    public void testToString() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertEquals("[value1, value2]", record.toString());
    }

}