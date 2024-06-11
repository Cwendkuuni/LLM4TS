import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndexValid() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByIndexOutOfBounds() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    public void testGetByNameValid() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1, "column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("A", record.get("column1"));
        assertEquals("B", record.get("column2"));
        assertEquals("C", record.get("column3"));
    }

    @Test
    public void testGetByNameNotFound() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNull(record.get("column3"));
    }

    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1, "column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMappedTrue() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
    }

    @Test
    public void testIsMappedFalse() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSetTrue() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
    }

    @Test
    public void testIsSetFalse() {
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isSet("column2"));
    }

    @Test
    public void testIterator() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("[A, B, C]", record.toString());
    }

}