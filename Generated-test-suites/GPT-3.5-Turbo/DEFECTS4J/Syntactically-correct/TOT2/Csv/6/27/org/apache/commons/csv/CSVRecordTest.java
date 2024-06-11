import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1);
        Enum<ExampleEnum> exampleEnum = ExampleEnum.VALUE1;
        assertEquals("value1", record.get(exampleEnum));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1);
        assertThrows(IllegalStateException.class, () -> record.get("column1"));

        record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("column1", 0, "column2", 1), "", 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertThrows(IllegalArgumentException.class, () -> record.get("column3"));
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("column1", 0, "column2", 1), "", 1);
        assertTrue(record.isConsistent());

        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("column1", 0, "column2", 1), "", 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("column1", 0, "column2", 1), "", 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("column1", 0, "column2", 1), "", 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("column1", 0, "column2", 1), "", 1);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("column1", 0, "column2", 1), "", 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1);
        assertArrayEquals(new String[]{"value1", "value2"}, record.values());
    }
}

enum ExampleEnum {
    VALUE1, VALUE2
}