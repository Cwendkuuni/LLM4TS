import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
        assertNull(record.get("column4"));
    }

    @Test (expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "", 1);
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithNullMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "", 1);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInconsistentSize() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        int count = 0;
        for (String value : record) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "This is a comment", 1);

        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "", 5);

        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        assertEquals("[value1, value2, value3]", record.toString());
    }
}