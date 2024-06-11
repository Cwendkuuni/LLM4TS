import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        csvRecord = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetEnum() {
        String value = csvRecord.get(TestEnum.ENUM1);
        assertEquals("value1", value);
    }

    @Test
    public void testGetByIndex() {
        String value = csvRecord.get(1);
        assertEquals("value2", value);
    }

    @Test
    public void testGetByName() {
        String value = csvRecord.get("column3");
        assertEquals("value3", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        csvRecord.get("column1");
    }

    @Test
    public void testGetComment() {
        String comment = csvRecord.getComment();
        assertEquals("comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvRecord.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent() {
        boolean consistent = csvRecord.isConsistent();
        assertTrue(consistent);
    }

    @Test
    public void testIsConsistentWithNoMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        boolean consistent = csvRecord.isConsistent();
        assertTrue(consistent);
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "comment", 1);
        boolean consistent = csvRecord.isConsistent();
        assertFalse(consistent);
    }

    @Test
    public void testIsMapped() {
        boolean mapped = csvRecord.isMapped("column3");
        assertTrue(mapped);
    }

    @Test
    public void testIsNotMapped() {
        boolean mapped = csvRecord.isMapped("column4");
        assertFalse(mapped);
    }

    @Test
    public void testIsSet() {
        boolean set = csvRecord.isSet("column2");
        assertTrue(set);
    }

    @Test
    public void testIsNotSet() {
        boolean set = csvRecord.isSet("column4");
        assertFalse(set);
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        assertEquals(3, map.size());
        assertTrue(map.containsKey("column1"));
        assertTrue(map.containsKey("column2"));
        assertTrue(map.containsKey("column3"));
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        int size = csvRecord.size();
        assertEquals(3, size);
    }

    @Test
    public void testToList() {
        List<String> list = csvRecord.toList();
        assertEquals(3, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
        assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(3, map.size());
        assertTrue(map.containsKey("column1"));
        assertTrue(map.containsKey("column2"));
        assertTrue(map.containsKey("column3"));
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString() {
        String string = csvRecord.toString();
        assertEquals("[value1, value2, value3]", string);
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    private enum TestEnum {
        ENUM1, ENUM2, ENUM3
    }
}