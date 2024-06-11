import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private final String[] values = {"value1", "value2", "value3"};
    private final Map<String, Integer> mapping = new HashMap<>();
    private final String comment = "This is a comment";
    private final long recordNumber = 1L;

    @Before
    public void setUp() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(EnumClass.COLUMN1));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, comment, recordNumber);
        recordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        csvRecord.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        csvRecord.get(3);
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
    }

    @Test
    public void testIsNotMapped() {
        assertFalse(csvRecord.isMapped("invalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column2"));
    }

    @Test
    public void testIsNotSet() {
        assertFalse(csvRecord.isSet("column3"));
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
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

}