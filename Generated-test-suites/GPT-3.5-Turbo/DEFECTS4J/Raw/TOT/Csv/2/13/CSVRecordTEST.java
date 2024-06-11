import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsValue() {
        String expected = "value1";
        String actual = csvRecord.get(0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetByIndex_InvalidIndex_ReturnsValue() {
        try {
            csvRecord.get(3);
            Assert.fail("Expected IndexOutOfBoundsException was not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        }
    }

    @Test
    public void testGetByName_ValidName_ReturnsValue() {
        String expected = "value2";
        String actual = csvRecord.get("name2");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetByName_InvalidName_ReturnsNull() {
        String actual = csvRecord.get("name4");
        Assert.assertNull(actual);
    }

    @Test
    public void testGetByName_NoMappingProvided_ThrowsIllegalStateException() {
        csvRecord = new CSVRecord(new String[3], null, "comment", 1);
        try {
            csvRecord.get("name1");
            Assert.fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }

    @Test
    public void testIsConsistent_MappingSizeEqualsValuesLength_ReturnsTrue() {
        boolean actual = csvRecord.isConsistent();
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsConsistent_MappingSizeNotEqualsValuesLength_ReturnsFalse() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("name1", 0);
        inconsistentMapping.put("name2", 1);
        boolean actual = new CSVRecord(new String[3], inconsistentMapping, "comment", 1).isConsistent();
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsMapped_MappedColumnName_ReturnsTrue() {
        boolean actual = csvRecord.isMapped("name2");
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsMapped_UnmappedColumnName_ReturnsFalse() {
        boolean actual = csvRecord.isMapped("name4");
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsSet_MappedColumnNameWithValue_ReturnsTrue() {
        boolean actual = csvRecord.isSet("name2");
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsSet_MappedColumnNameWithoutValue_ReturnsFalse() {
        boolean actual = csvRecord.isSet("name3");
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsSet_UnmappedColumnName_ReturnsFalse() {
        boolean actual = csvRecord.isSet("name4");
        Assert.assertFalse(actual);
    }

    @Test
    public void testIterator_ReturnsIteratorOverValues() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testValues_ReturnsValuesArray() {
        String[] expected = {"value1", "value2", "value3"};
        String[] actual = csvRecord.values();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetComment_ReturnsComment() {
        String expected = "comment";
        String actual = csvRecord.getComment();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        long expected = 1;
        long actual = csvRecord.getRecordNumber();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSize_ReturnsValuesLength() {
        int expected = 3;
        int actual = csvRecord.size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        String expected = "[value1, value2, value3]";
        String actual = csvRecord.toString();
        Assert.assertEquals(expected, actual);
    }
}