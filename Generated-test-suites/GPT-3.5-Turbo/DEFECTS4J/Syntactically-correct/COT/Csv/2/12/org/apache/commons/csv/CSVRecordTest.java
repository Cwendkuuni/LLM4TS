import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertNull(record.get("name1"));
        assertNull(record.get("name2"));
        assertNull(record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }
}