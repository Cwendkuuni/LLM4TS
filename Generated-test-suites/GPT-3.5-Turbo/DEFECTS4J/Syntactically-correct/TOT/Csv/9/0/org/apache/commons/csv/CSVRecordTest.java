import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = { "John", "Doe", "30" };
        record = new CSVRecord(values, null, "This is a comment", 1);
    }

    @Test
    public void testGetForEnumValidValue() {
        String expected = "John";
        assertEquals(expected, record.get(TestEnum.NAME));
    }

    @Test
    public void testGetForEnumInvalidValue() {
        String expected = null;
        assertEquals(expected, record.get(TestEnum.UNKNOWN));
    }

    @Test
    public void testGetForIndexValidValue() {
        String expected = "Doe";
        assertEquals(expected, record.get(1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetForIndexInvalidValue() {
        record.get(3);
    }

    @Test
    public void testGetForNameValidValue() {
        String expected = "30";
        assertEquals(expected, record.get("2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetForNameNoMapping() {
        record.get("age");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetForNameInvalidValue() {
        record.get("3");
    }

    @Test
    public void testGetComment() {
        String expected = "This is a comment";
        assertEquals(expected, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long expected = 1;
        assertEquals(expected, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentWithMapping() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(record.values(), null, record.getComment(), record.getRecordNumber());
        assertFalse(recordWithoutMapping.isConsistent());
    }

    @Test
    public void testIsMappedWithMapping() {
        assertFalse(record.isMapped("age"));
        assertTrue(record.isMapped("2"));
    }

    @Test
    public void testIsMappedWithoutMapping() {
        assertFalse(record.isMapped("name"));
    }

    @Test
    public void testIsSetWithValidValue() {
        assertTrue(record.isSet("2"));
    }

    @Test
    public void testIsSetWithInvalidValue() {
        assertFalse(record.isSet("3"));
    }

    @Test
    public void testIterator() {
        assertNotNull(record.iterator());
    }

    @Test
    public void testPutIn() {
        CSVRecord recordWithMapping = new CSVRecord(record.values(), createMapping(), record.getComment(), record.getRecordNumber());
        assertNotNull(recordWithMapping.putIn(new HashMap<String, String>()));
    }

    @Test
    public void testSize() {
        int expected = 3;
        assertEquals(expected, record.size());
    }

    @Test
    public void testToMap() {
        assertNotNull(record.toMap());
    }

    @Test
    public void testToString() {
        String expected = "[John, Doe, 30]";
        assertEquals(expected, record.toString());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("0", 0);
        mapping.put("1", 1);
        mapping.put("2", 2);
        return mapping;
    }

    private enum TestEnum {
        NAME,
        UNKNOWN
    }

}