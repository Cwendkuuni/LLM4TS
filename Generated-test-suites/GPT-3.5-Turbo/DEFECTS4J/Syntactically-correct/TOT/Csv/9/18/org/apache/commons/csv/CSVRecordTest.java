import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(null, record.get(TestEnum.TEST1));
    }

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testGetInt_OutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get(2);
    }

    @Test(expected=IllegalStateException.class)
    public void testGetName_NoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get("name");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetName_MappingNotFound() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get("name");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetName_IndexOutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get("index");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 10);
        assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_MismatchSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertFalse(record.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertFalse(record.isSet("name"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.putIn(new HashMap<String, String>()).get(0));
        assertEquals("value2", record.putIn(new HashMap<String, String>()).get(1));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.toList().get(0));
        assertEquals("value2", record.toList().get(1));
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.toMap().get(0));
        assertEquals("value2", record.toMap().get(1));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("[value1, value2]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.values()[0]);
        assertEquals("value2", record.values()[1]);
    }
}