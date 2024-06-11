import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    CSVRecord csvRecord;

    @Test
    public void testGetByEnum() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "Sample comment", 1);
        assertEquals("value1", csvRecord.get(SomeEnum.VALUE1));
    }

    @Test
    public void testGetByIndex() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "Sample comment", 1);
        assertEquals("value1", csvRecord.get(0));
    }

    @Test
    public void testGetByName() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertEquals("value1", csvRecord.get("column1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "Sample comment", 1);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        csvRecord.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByOutOfBoundsIndex() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        csvRecord.get("column2");
    }

    @Test
    public void testGetComment() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertEquals("Sample comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        csvRecord = new CSVRecord(new String[]{"value1"}, createMapping(), "Sample comment", 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertTrue(csvRecord.isMapped("column1"));
    }

    @Test
    public void testIsNotMapped() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertFalse(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertTrue(csvRecord.isSet("column1"));
    }

    @Test
    public void testIsNotSet() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertFalse(csvRecord.isSet("column2"));
    }

    @Test
    public void testIterator() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testPutIn() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        Map<String, String> newMap = csvRecord.putIn(new HashMap<>());
        assertTrue(newMap.containsKey("column1"));
        assertEquals("value1", newMap.get("column1"));
    }

    @Test
    public void testSize() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToMap() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "Sample comment", 1);
        Map<String, String> newMap = csvRecord.toMap();
        assertTrue(newMap.containsKey("column1"));
        assertEquals("value1", newMap.get("column1"));
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        return mapping;
    }

}