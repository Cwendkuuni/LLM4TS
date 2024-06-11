import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.HashMap;
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
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNullMapping() {
        CSVRecord record = new CSVRecord(csvRecord.values(), null, csvRecord.getComment(), csvRecord.getRecordNumber());
        record.get("column1");
    }

    @Test
    public void testGetByNameWithMissingName() {
        assertNull(csvRecord.get("column4"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithNullMapping() {
        CSVRecord record = new CSVRecord(csvRecord.values(), null, csvRecord.getComment(), csvRecord.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInconsistentRecord() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(csvRecord.values(), mapping, csvRecord.getComment(), csvRecord.getRecordNumber());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsMappedWithNullMapping() {
        CSVRecord record = new CSVRecord(csvRecord.values(), null, csvRecord.getComment(), csvRecord.getRecordNumber());
        assertFalse(record.isMapped("column1"));
    }

    @Test
    public void testIsMappedWithMissingName() {
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
    }

    @Test
    public void testIsSetWithNullMapping() {
        CSVRecord record = new CSVRecord(csvRecord.values(), null, csvRecord.getComment(), csvRecord.getRecordNumber());
        assertFalse(record.isSet("column1"));
    }

    @Test
    public void testIsSetWithMissingName() {
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            if (count == 0) {
                assertEquals("value1", value);
            } else if (count == 1) {
                assertEquals("value2", value);
            } else if (count == 2) {
                assertEquals("value3", value);
            }
            count++;
        }
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertNotNull(values);
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