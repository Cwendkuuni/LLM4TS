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
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord csvRecordWithName = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        csvRecordWithName.get("column1");
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
        assertNull(csvRecord.get("column4"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());

        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("column1", 0);
        inconsistentMapping.put("column2", 1);
        assertFalse(new CSVRecord(new String[]{"value1"}, inconsistentMapping, null, 1).isConsistent());
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
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
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
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}