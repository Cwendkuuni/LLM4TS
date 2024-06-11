import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setup() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.ENUM1));
        assertEquals("value2", csvRecord.get(TestEnum.ENUM2));
        assertEquals("value3", csvRecord.get(TestEnum.ENUM3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("col1"));
        assertEquals("value2", csvRecord.get("col2"));
        assertEquals("value3", csvRecord.get("col3"));
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertTrue(csvRecord.isMapped("col2"));
        assertTrue(csvRecord.isMapped("col3"));
        assertFalse(csvRecord.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
        assertTrue(csvRecord.isSet("col3"));
        assertFalse(csvRecord.isSet("col4"));
    }

    @Test
    public void testIterator() {
        assertTrue(csvRecord.iterator().hasNext());
        assertEquals("value1", csvRecord.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map = csvRecord.putIn(map);
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(3, csvRecord.toList().size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    private enum TestEnum {
        ENUM1,
        ENUM2,
        ENUM3
    }

}