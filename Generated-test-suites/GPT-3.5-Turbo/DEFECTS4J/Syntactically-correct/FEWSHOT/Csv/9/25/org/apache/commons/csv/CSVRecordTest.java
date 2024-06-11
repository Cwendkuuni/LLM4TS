import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private static final String[] TEST_VALUES = {"value1", "value2", "value3"};
    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        csvRecord = new CSVRecord(TEST_VALUES, null, null, 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
        assertEquals("value2", csvRecord.get(TestEnum.VALUE2));
        assertEquals("value3", csvRecord.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertNull(csvRecord.get("nonExisting"));
        assertNull(csvRecord.get("nonExisting2"));
        assertNull(csvRecord.get("nonExisting3"));

        assertFalse(csvRecord.isMapped("nonExisting"));
        assertFalse(csvRecord.isMapped("nonExisting2"));
        assertFalse(csvRecord.isMapped("nonExisting3"));

        assertFalse(csvRecord.isSet("nonExisting"));
        assertFalse(csvRecord.isSet("nonExisting2"));
        assertFalse(csvRecord.isSet("nonExisting3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameIllegalState() {
        csvRecord.get("column");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIllegalArgument() {
        csvRecord = new CSVRecord(TEST_VALUES, getMapping(), null, 1);
        csvRecord.get("nonExisting");
    }

    @Test
    public void testGetComment() {
        assertNull(csvRecord.getComment());

        csvRecord = new CSVRecord(TEST_VALUES, null, "comment", 1);
        assertEquals("comment", csvRecord.getComment());

        csvRecord = new CSVRecord(TEST_VALUES, null, "", 1);
        assertEquals("", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());

        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(csvRecord.isConsistent());

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3", "value4"}, null, null, 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());

        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, csvRecord.size());

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3", "value4"}, null, null, 1);
        assertEquals(4, csvRecord.size());
    }

    @Test
    public void testToList() {
        List<String> valuesList = csvRecord.toList();
        assertEquals(3, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        assertEquals("value3", valuesList.get(2));

        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        valuesList = csvRecord.toList();
        assertEquals(2, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3", "value4"}, null, null, 1);
        valuesList = csvRecord.toList();
        assertEquals(4, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        assertEquals("value3", valuesList.get(2));
        assertEquals("value4", valuesList.get(3));
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertTrue(map.isEmpty());

        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3", "value4"}, getMapping(), null, 1);
        map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());

        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("[value1, value2]", csvRecord.toString());

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3", "value4"}, null, null, 1);
        assertEquals("[value1, value2, value3, value4]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertArrayEquals(TEST_VALUES, values);
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }

    private Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        return mapping;
    }

}