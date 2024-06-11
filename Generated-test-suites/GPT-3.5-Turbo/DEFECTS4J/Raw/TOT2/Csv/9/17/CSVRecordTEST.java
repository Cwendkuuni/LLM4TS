import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("value1", record.get(TestEnum.VALUE_1));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        assertEquals("value1", record.get("name1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        record.get("name2");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createIncompleteMapping(), "comment", 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        assertTrue(record.isMapped("name1"));
    }

    @Test
    public void testIsNotMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        assertFalse(record.isMapped("invalidName"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        assertTrue(record.isSet("name1"));
    }

    @Test
    public void testIsNotSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "comment", 1);
        assertFalse(record.isSet("name2"));
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        return mapping;
    }

    private Map<String, Integer> createIncompleteMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        return mapping;
    }

}