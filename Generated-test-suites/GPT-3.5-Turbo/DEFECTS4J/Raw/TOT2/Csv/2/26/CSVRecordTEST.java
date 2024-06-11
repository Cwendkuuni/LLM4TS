import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, Map.of("first", 0, "second", 1, "third", 2), null, 1);
        assertEquals("A", record.get("first"));
        assertEquals("B", record.get("second"));
        assertEquals("C", record.get("third"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get("first");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, Map.of("first", 0, "second", 1, "third", 2), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, Map.of("first", 0, "second", 1, "third", 2), null, 1);
        assertTrue(record.isMapped("first"));
        assertFalse(record.isMapped("fourth"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, Map.of("first", 0, "second", 1, "third", 2), null, 1);
        assertTrue(record.isSet("first"));
        assertFalse(record.isSet("fourth"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("[A, B, C]", record.toString());
    }
}