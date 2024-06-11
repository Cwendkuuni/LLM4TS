import static org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    @Test
    public void testGetInt() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String expected = "Column1";
        String result = record.get(0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNull() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNull(record.get("NON_EXISTENT_COLUMN"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String expectedExceptionMessage = "No header mapping was specified, the record values can't be accessed by name";
        record.get("Column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentData() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> mapping = Map.of("Column1", 0, "Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String expectedExceptionMessage = "Illegal argument. Record inconsistent.";
        record.get(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get(values.length);
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> mapping = Map.of("Column1", 0, "Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Column1"));
        assertFalse(record.isMapped("NON_EXISTENT_COLUMN"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        Map<String, Integer> mapping = Map.of("Column1", 0, "Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Column1"));
        assertFalse(record.isSet("NON_EXISTENT_COLUMN"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertEquals("Column1", iterator.next());
        assertEquals("Column2", iterator.next());
        assertEquals("Column3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String[] result = record.values();
        assertArrayEquals(values, result);
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, null, comment, 1);
        String expected = "This is a comment";
        String result = record.getComment();
        assertEquals(expected, result);
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        long expected = 1;
        long result = record.getRecordNumber();
        assertEquals(expected, result);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        int expected = 3;
        int result = record.size();
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Column1", "Column2", "Column3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String expected = "[Column1, Column2, Column3]";
        String result = record.toString();
        assertEquals(expected, result);
    }
}