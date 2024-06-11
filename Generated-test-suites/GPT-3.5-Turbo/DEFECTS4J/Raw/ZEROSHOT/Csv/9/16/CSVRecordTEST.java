import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        String value = record.get(TestEnum.VALUE1);
        assertEquals("value1", value);
    }

    @Test
    public void testGetByIndex() {
        String value = record.get(1);
        assertEquals("value2", value);
    }

    @Test
    public void testGetByName() {
        String value = record.get("column3");
        assertEquals("value3", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(record.values, null, record.comment, record.recordNumber);
        recordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        record.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        record.get(5);
    }

    @Test
    public void testGetComment() {
        String comment = record.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent() {
        boolean consistent = record.isConsistent();
        assertTrue(consistent);
    }

    @Test
    public void testIsNotConsistent() {
        String[] inconsistentValues = {"value1", "value2"};
        CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, record.mapping, record.comment, record.recordNumber);
        boolean consistent = inconsistentRecord.isConsistent();
        assertFalse(consistent);
    }

    @Test
    public void testIsMapped() {
        boolean mapped = record.isMapped("column1");
        assertTrue(mapped);
    }

    @Test
    public void testIsNotMapped() {
        boolean mapped = record.isMapped("invalidColumn");
        assertFalse(mapped);
    }

    @Test
    public void testIsSet() {
        boolean set = record.isSet("column2");
        assertTrue(set);
    }

    @Test
    public void testIsNotSet() {
        boolean set = record.isSet("column3");
        assertFalse(set);
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        int size = record.size();
        assertEquals(3, size);
    }

    @Test
    public void testToList() {
        List<String> list = record.toList();
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
        assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertArrayEquals(EMPTY_STRING_ARRAY, values);
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}