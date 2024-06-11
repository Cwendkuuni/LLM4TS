import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNullMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        record.get("name");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertFalse(record.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertFalse(record.isSet("name"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertEquals(3, record.size());
    }

}