import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    private static final String COMMENT = "This is a comment";
    private static final long RECORD_NUMBER = 1;

    private final CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);

    @Test
    public void testGetEnum() {
        String value = record.get(TestEnum.VALUE_1);
        Assert.assertEquals("value1", value);
    }

    @Test
    public void testGetIndex() {
        String value = record.get(1);
        Assert.assertEquals("value2", value);
    }

    @Test
    public void testGetName() {
        String value = record.get("name");
        Assert.assertNull(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord emptyRecord = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        emptyRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidName() {
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidIndex() {
        record.get("name");
    }

    @Test
    public void testGetComment() {
        String comment = record.getComment();
        Assert.assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = record.getRecordNumber();
        Assert.assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent() {
        boolean consistent = record.isConsistent();
        Assert.assertTrue(consistent);
    }

    @Test
    public void testIsMapped() {
        boolean mapped = record.isMapped("name");
        Assert.assertFalse(mapped);
    }

    @Test
    public void testIsSet() {
        boolean set = record.isSet("name");
        Assert.assertFalse(set);
    }

    @Test
    public void testIterator() {
        Assert.assertEquals(record.toList(), record.iterator());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = record.putIn(map);
        Assert.assertEquals(record.toMap(), result);
    }

    @Test
    public void testSize() {
        int size = record.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        Assert.assertArrayEquals(VALUES, values);
    }

    private enum TestEnum {
        VALUE_1
    }
}



// Individual test methods

