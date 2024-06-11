import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", csvRecord.get(0));
        assertEquals("B", csvRecord.get(1));
        assertEquals("C", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertNull(csvRecord.get("Name"));
        csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, Map.of("Column1", 0, "Column2", 1, "Column3", 2), null, 1);
        assertEquals("A", csvRecord.get("Column1"));
    }

    @Test
    public void testIsConsistent() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B"}, Map.of("Column1", 0, "Column2", 1), null, 1);
        assertTrue(csvRecord.isConsistent());
        csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, Map.of("Column1", 0, "Column2", 1), null, 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, Map.of("Column1", 0, "Column2", 1), null, 1);
        assertTrue(csvRecord.isMapped("Column1"));
        assertFalse(csvRecord.isMapped("Name"));
    }

    @Test
    public void testIsSet() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B"}, Map.of("Column1", 0, "Column2", 1), null, 1);
        assertTrue(csvRecord.isSet("Column1"));
        assertFalse(csvRecord.isSet("Column2"));
    }

    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, null, "Test Comment", 1);
        assertEquals("Test Comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 5);
        assertEquals(5, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("[A, B, C]", csvRecord.toString());
    }

}