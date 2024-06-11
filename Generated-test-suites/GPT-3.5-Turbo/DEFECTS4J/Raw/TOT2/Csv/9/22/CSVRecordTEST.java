import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 5);
        assertEquals(5, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(csvRecord.isSet("column2"));
    }

    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3", "value4"}, null, null, 1);
        assertEquals(4, csvRecord.size());
    }

    @Test
    public void testToMap() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("column1"));
    }

    @Test
    public void testValues() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }

}