import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setup() {
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
    public void testGetValueByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetValueByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameNoMapping() {
        CSVRecord csvRecordNoMapping = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        csvRecordNoMapping.get("column1");
    }

    @Test
    public void testGetValueByNameNotFound() {
        assertNull(csvRecord.get("column4"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingMismatch() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("column1", 0);
        inconsistentMapping.put("column2", 1);
        assertFalse(new CSVRecord(new String[]{"value1", "value2"}, inconsistentMapping, null, 1).isConsistent());
    }

    @Test
    public void testIsConsistentWithNoMapping() {
        assertTrue(new CSVRecord(new String[]{"value1", "value2"}, null, null, 1).isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsMappedWithNoMapping() {
        assertFalse(new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1).isMapped("column1"));
    }

    @Test
    public void testIsMappedWithNonExistentColumnName() {
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
    }

    @Test
    public void testIsSetWithNoMapping() {
        assertFalse(new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1).isSet("column1"));
    }

    @Test
    public void testIsSetWithNonExistentColumnName() {
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
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
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