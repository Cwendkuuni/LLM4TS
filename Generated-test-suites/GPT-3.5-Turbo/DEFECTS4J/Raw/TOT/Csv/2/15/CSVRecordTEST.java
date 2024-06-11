import org.junit.Test;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    // Test case for get(int i) method
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        String[] expectedValues = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        for (int i = 0; i < values.length; i++) {
            Assert.assertEquals(expectedValues[i], record.get(i));
        }
    }

    // Test case for get(String name) method with header mapping
    @Test
    public void testGetByNameWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        String columnName = "column2";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Assert.assertEquals("value2", record.get(columnName));
    }

    // Test case for get(String name) method without header mapping
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        String columnName = "column2";

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        record.get(columnName);
    }

    // Test case for isConsistent() method with consistent record
    @Test
    public void testIsConsistentWithConsistentRecord() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Assert.assertTrue(record.isConsistent());
    }

    // Test case for isConsistent() method with inconsistent record
    @Test
    public void testIsConsistentWithInconsistentRecord() {
        String[] values = {"value1", "value2"};
        long recordNumber = 1;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Assert.assertFalse(record.isConsistent());
    }

    // Test case for isMapped(String name) method with mapped column
    @Test
    public void testIsMappedWithMappedColumn() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        String columnName = "column2";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Assert.assertTrue(record.isMapped(columnName));
    }

    // Test case for isMapped(String name) method with unmapped column
    @Test
    public void testIsMappedWithUnmappedColumn() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        String columnName = "column4";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Assert.assertFalse(record.isMapped(columnName));
    }

    // Test case for isSet(String name) method with set column
    @Test
    public void testIsSetWithSetColumn() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        String columnName = "column2";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Assert.assertTrue(record.isSet(columnName));
    }

    // Test case for isSet(String name) method with unset column
    @Test
    public void testIsSetWithUnsetColumn() {
        String[] values = {"value1", "value2"};
        long recordNumber = 1;
        String columnName = "column3";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Assert.assertFalse(record.isSet(columnName));
    }

    // Test case for iterator() method
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        int count = 0;
        for (String value : record) {
            count++;
        }
        Assert.assertEquals(values.length, count);
    }

    // Test case for values() method
    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        Assert.assertArrayEquals(values, record.values());
    }

    // Test case for getComment() method with comment
    @Test
    public void testGetCommentWithComment() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;
        String comment = "This is a comment";

        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);

        Assert.assertEquals(comment, record.getComment());
    }

    // Test case for getComment() method without comment
    @Test
    public void testGetCommentWithoutComment() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        Assert.assertNull(record.getComment());
    }

    // Test case for getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        Assert.assertEquals(recordNumber, record.getRecordNumber());
    }

    // Test case for size() method
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        Assert.assertEquals(values.length, record.size());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, null, null, recordNumber);

        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }
}