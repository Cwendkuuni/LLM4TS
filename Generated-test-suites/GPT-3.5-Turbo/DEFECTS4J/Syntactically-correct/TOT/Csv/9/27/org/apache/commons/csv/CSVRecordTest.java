import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    // Test case for get(Enum<?> e) method
    @Test
    public void testGetEnum() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertEquals("value1", record.get(TestEnum.VALUE1));

        // Edge case: Enum value not present in CSV record
        Assert.assertNull(record.get(TestEnum.NON_EXISTING));
    }

    // Test case for get(int i) method
    @Test
    public void testGetIndex() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertEquals("value3", record.get(2));

        // Edge case: Index out of bounds
        try {
            record.get(5);
            Assert.fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test case for get(String name) method
    @Test
    public void testGetName() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertEquals("value2", record.get("header2"));

        // Edge case: Null mapping
        try {
            record.get("non_existing_header");
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }

        // Edge case: Mapping present but header not found
        try {
            record.get("non_existing_header");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Edge case: Mapping present but index out of bounds
        try {
            record.get("header1");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test case for getComment() method
    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertEquals("This is a comment", record.getComment());
    }

    // Test case for getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertEquals(1, record.getRecordNumber());
    }

    // Test case for isConsistent() method
    @Test
    public void testIsConsistent() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertTrue(record.isConsistent());

        // Edge case: Inconsistent record (header size is different from values size)
        CSVRecord inconsistentRecord = createInconsistentCSVRecord();
        Assert.assertFalse(inconsistentRecord.isConsistent());
    }

    // Test case for isMapped(String name) method
    @Test
    public void testIsMapped() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertTrue(record.isMapped("header1"));

        // Edge case: Null mapping
        Assert.assertFalse(record.isMapped("non_existing_header"));
    }

    // Test case for isSet(String name) method
    @Test
    public void testIsSet() {
        CSVRecord record = createCSVRecord();

        // Typical use case
        Assert.assertTrue(record.isSet("header1"));

        // Edge case: Mapping present but index out of bounds
        Assert.assertFalse(record.isSet("header3"));
    }

    // Helper method to create a CSVRecord for testing
    private CSVRecord createCSVRecord() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        return new CSVRecord(values, mapping, "This is a comment", 1);
    }

    // Helper method to create an inconsistent CSVRecord for testing
    private CSVRecord createInconsistentCSVRecord() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        return new CSVRecord(values, mapping, "This is a comment", 1);
    }

}