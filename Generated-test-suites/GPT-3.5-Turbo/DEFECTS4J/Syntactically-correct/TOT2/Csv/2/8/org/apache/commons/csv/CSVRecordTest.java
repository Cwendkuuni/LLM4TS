import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNull(record.get("NotExists"));
        assertEquals("A", record.get("Col1"));
        assertEquals("B", record.get("Col2"));
        assertEquals("C", record.get("Col3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Col1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Col1"));
        assertFalse(record.isMapped("Col2"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Col1"));
        assertFalse(record.isSet("Col2"));
    }

    @Test
    public void testIterator() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Iterator<String> it = record.iterator();
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
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