import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private String[] values = {"value1", "value2", "value3"};
    private Map<String, Integer> mapping = new HashMap<>();
    private String comment = "This is a comment";
    private long recordNumber = 1;

    @Before
    public void setUp() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetEnumValue() {
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
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidName() {
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        csvRecord.get("column1");
        csvRecord.get("column4");
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
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            assertNotNull(value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");
        map.put("column3", "value3");

        assertEquals(map, csvRecord.putIn(new HashMap<>()));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(Arrays.asList(values), csvRecord.toList());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");
        map.put("column3", "value3");

        assertEquals(map, csvRecord.toMap());
    }

    @Test
    public void testValues() {
        assertArrayEquals(values, csvRecord.values());
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}