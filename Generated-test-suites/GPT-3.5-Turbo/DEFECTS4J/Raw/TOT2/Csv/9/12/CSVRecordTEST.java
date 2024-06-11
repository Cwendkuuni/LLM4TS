import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("value1", csvRecord.get(Enum.valueOf("COLUMN1", "COLUMN1")));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("value1", csvRecord.get(0));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        assertEquals("value1", csvRecord.get("col1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        csvRecord.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        csvRecord.get("col2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 2);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        csvRecord.get("col1");
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        assertTrue(csvRecord.isMapped("col1"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        assertTrue(csvRecord.isSet("col1"));
    }

    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testPutIn() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        assertEquals("value1", map.get("col1"));
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("col1"));
    }

    @Test
    public void testToString() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("[value1, value2]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertArrayEquals(new String[]{"value1", "value2"}, csvRecord.values());
    }
}