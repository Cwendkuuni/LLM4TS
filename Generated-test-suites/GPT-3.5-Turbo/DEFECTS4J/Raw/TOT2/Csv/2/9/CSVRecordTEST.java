import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1);
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", csvRecord.get("name1"));
        assertEquals("value2", csvRecord.get("name2"));
        assertNull(csvRecord.get("name3"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1);
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(csvRecord.isConsistent());

        mapping = Map.of("name1", 0);
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMappedAndIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1);
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(csvRecord.isMapped("name1"));
        assertTrue(csvRecord.isSet("name1"));
        assertFalse(csvRecord.isMapped("name3"));
        assertFalse(csvRecord.isSet("name3"));
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(3, csvRecord.size());
    }
}