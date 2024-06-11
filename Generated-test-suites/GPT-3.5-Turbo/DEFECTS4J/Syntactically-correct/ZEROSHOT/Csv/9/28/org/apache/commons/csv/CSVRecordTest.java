import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(csvRecord.get(TestEnum.TEST), "value1");
    }

    @Test
    public void testGetInt() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(csvRecord.get(0), "value1");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameNotMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test1", 0);
        mapping.put("test2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameArrayOutOfBound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test1", 0);
        mapping.put("test2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        csvRecord.get("name");
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals(csvRecord.getComment(), "comment");
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(csvRecord.getRecordNumber(), 1);
    }

    @Test
    public void testIsConsistent() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test1", 0);
        mapping.put("test2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(csvRecord.isMapped("test1"));
        assertFalse(csvRecord.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test1", 0);
        mapping.put("test2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(csvRecord.isSet("test1"));
        assertFalse(csvRecord.isSet("name"));
    }

    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test1", 0);
        mapping.put("test2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertNotNull(csvRecord.putIn(map));
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(csvRecord.size(), 2);
    }

    @Test
    public void testToList() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNotNull(csvRecord.toList());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test1", 0);
        mapping.put("test2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertNotNull(csvRecord.toMap());
    }

    @Test
    public void testValues() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNotNull(csvRecord.values());
    }

    private enum TestEnum {
        TEST
    }
}