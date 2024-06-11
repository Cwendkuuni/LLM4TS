import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Test
    public void testGetByIndex() {
        String[] values = {"A", "B", "C"};
        csvRecord = new CSVRecord(values, null, null, 1);
        assertEquals("A", csvRecord.get(0));
        assertEquals("B", csvRecord.get(1));
        assertEquals("C", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        csvRecord = new CSVRecord(values, mapping, null, 1);
        assertEquals("A", csvRecord.get("col1"));
        assertEquals("B", csvRecord.get("col2"));
        assertEquals("C", csvRecord.get("col3"));
        assertNull(csvRecord.get("col4"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isConsistent());

        mapping.put("col3", 2);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isMapped("col1"));
        assertTrue(csvRecord.isMapped("col2"));
        assertFalse(csvRecord.isMapped("col3"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
        assertFalse(csvRecord.isSet("col3"));
    }

    // Add more test cases as needed...

}