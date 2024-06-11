import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        // Test getting value by enum
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(SomeEnum.VALUE1));
        assertEquals("B", record.get(SomeEnum.VALUE2));
        assertEquals("C", record.get(SomeEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        // Test getting value by index
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        // Test getting value by name
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);
        mapping.put("Col3", 2);
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertEquals("A", record.get("Col1"));
        assertEquals("B", record.get("Col2"));
        assertEquals("C", record.get("Col3"));
    }

    @Test
    public void testGetComment() {
        // Test getting comment
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Test getting record number
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Test checking consistency
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);
        CSVRecord record = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping.put("Col3", 2);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Test checking if column is mapped
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);
        CSVRecord record = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        assertTrue(record.isMapped("Col1"));
        assertTrue(record.isMapped("Col2"));
        assertFalse(record.isMapped("Col3"));
    }

    @Test
    public void testIsSet() {
        // Test checking if column is mapped and set
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);
        CSVRecord record = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        assertTrue(record.isSet("Col1"));
        assertTrue(record.isSet("Col2"));
        assertFalse(record.isSet("Col3"));
    }

    @Test
    public void testPutIn() {
        // Test putting values in map
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);
        CSVRecord record = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        Map<String, String> valuesMap = new HashMap<>();
        record.putIn(valuesMap);
        assertEquals(2, valuesMap.size());
        assertEquals("A", valuesMap.get("Col1"));
        assertEquals("B", valuesMap.get("Col2"));
    }

    @Test
    public void testSize() {
        // Test getting size
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        // Test converting values to list
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        List<String> valuesList = record.toList();
        assertEquals(3, valuesList.size());
        assertEquals("A", valuesList.get(0));
        assertEquals("B", valuesList.get(1));
        assertEquals("C", valuesList.get(2));
    }

    @Test
    public void testToMap() {
        // Test converting values to map
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);
        CSVRecord record = new CSVRecord(new String[]{"A", "B"}, mapping, null, 1);
        Map<String, String> valuesMap = record.toMap();
        assertEquals(2, valuesMap.size());
        assertEquals("A", valuesMap.get("Col1"));
        assertEquals("B", valuesMap.get("Col2"));
    }

    @Test
    public void testValues() {
        // Test getting values
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertArrayEquals(new String[]{"A", "B", "C"}, record.values());
    }
}