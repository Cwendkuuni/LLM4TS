import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.TEST1));
        assertEquals("value2", csvRecord.get(TestEnum.TEST2));
        assertEquals("value3", csvRecord.get(TestEnum.TEST3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("name1"));
        assertEquals("value2", csvRecord.get("name2"));
        assertEquals("value3", csvRecord.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        csvRecord.get("name4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        csvRecord.get("name1");
    }

    @Test
    public void testGetComment() {
        assertEquals(comment, csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithNullValues() {
        CSVRecord record = new CSVRecord(null, mapping, comment, recordNumber);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("name1"));
        assertTrue(csvRecord.isMapped("name2"));
        assertTrue(csvRecord.isMapped("name3"));
        assertFalse(csvRecord.isMapped("name4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("name1"));
        assertTrue(csvRecord.isSet("name2"));
        assertTrue(csvRecord.isSet("name3"));
        assertFalse(csvRecord.isSet("name4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for(String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(csvRecord.get("name1"), map.get("name1"));
        assertEquals(csvRecord.get("name2"), map.get("name2"));
        assertEquals(csvRecord.get("name3"), map.get("name3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(values.length, csvRecord.toList().size());
        assertEquals(values[0], csvRecord.toList().get(0));
        assertEquals(values[1], csvRecord.toList().get(1));
        assertEquals(values[2], csvRecord.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(csvRecord.get("name1"), map.get("name1"));
        assertEquals(csvRecord.get("name2"), map.get("name2"));
        assertEquals(csvRecord.get("name3"), map.get("name3"));
    }

    @Test
    public void testValues() {
        assertEquals(values.length, csvRecord.values().length);
        assertEquals(values[0], csvRecord.values()[0]);
        assertEquals(values[1], csvRecord.values()[1]);
        assertEquals(values[2], csvRecord.values()[2]);
    }

    private enum TestEnum {
        TEST1,
        TEST2,
        TEST3
    }
}