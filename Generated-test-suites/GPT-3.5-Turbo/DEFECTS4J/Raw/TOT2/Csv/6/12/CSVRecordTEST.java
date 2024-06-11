import static org.junit.Assert.*;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        assertNull(record.get(TestEnum.FIRST));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetValueByName() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertEquals("value1", record.get("column1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByNameNotFound() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        record.get("column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByInvalidName() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        record.get("invalidColumn");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        assertFalse(record.isMapped("column1"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertTrue(record.isSet("column1"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertTrue(record.iterator().hasNext());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertEquals(2, record.putIn(new HashMap<String, String>()).size());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertEquals(2, record.toMap().size());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertArrayEquals(new String[] { "value1", "value2" }, record.values());
    }

}