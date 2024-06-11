package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Enum<?> e = Enum.valueOf(Enum.class, "0");
        assertEquals("value1", record.get(e));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetMethod() {
        CSVRecord record = new CSVRecord(new String[]{"name1", "name2"}, null, null, 1);
        assertEquals("name1", record.get("name1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethodNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"name1", "name2"}, null, null, 1);
        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodArrayIndexOutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("name3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.isMapped("value1"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.isSet("value1"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"name1", "name2"}, null, null, 1);
        assertEquals(2, record.toMap().size());
    }

}