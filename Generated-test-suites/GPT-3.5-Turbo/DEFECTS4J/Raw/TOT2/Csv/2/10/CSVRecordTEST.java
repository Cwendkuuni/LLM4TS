import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
        assertNull(csvRecord.get("nonExistentColumn"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertFalse(csvRecord.isMapped("nonExistentColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertFalse(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("nonExistentColumn"));
    }

    @Test
    public void testRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

}