import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private static final String[] VALUES = { "value1", "value2", "value3" };
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    private static final String COMMENT = "This is a comment";
    private static final long RECORD_NUMBER = 1L;

    private CSVRecord record;

    @Before
    public void setUp() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);

        record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidMapping() {
        record.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameOutOfBounds() {
        record.get("column1");
        record.get("column5");
    }

    @Test
    public void testGetComment() {
        assertEquals(COMMENT, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentNullMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentDifferentSizeMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        assertFalse(new CSVRecord(VALUES, mapping, COMMENT, RECORD_NUMBER).isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("invalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("invalidColumn"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            assertEquals(VALUES[count], value);
            count++;
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);

        assertEquals(VALUES[0], map.get("column1"));
        assertEquals(VALUES[1], map.get("column2"));
        assertEquals(VALUES[2], map.get("column3"));
    }

    @Test
    public void testSize() {
        assertEquals(VALUES.length, record.size());
    }

    @Test
    public void testToList() {
        assertEquals(Arrays.asList(VALUES), record.toList());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();

        assertEquals(VALUES[0], map.get("column1"));
        assertEquals(VALUES[1], map.get("column2"));
        assertEquals(VALUES[2], map.get("column3"));
    }

    @Test
    public void testToString() {
        assertEquals(Arrays.toString(VALUES), record.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(VALUES, record.values());
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }

}