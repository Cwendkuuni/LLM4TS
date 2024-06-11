import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        record = new CSVRecord(values, null, null, 1);
    }

    @Test
    public void testGetByEnum() {
        String expected = "value1";
        String actual = record.get(TestEnum.VALUE1);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByIndex() {
        String expected = "value2";
        String actual = record.get(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByName() {
        String expected = "value3";
        String actual = record.get("column3");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_noMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_mappingNotFound() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_indexOutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("column2");
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long expected = 1;
        long actual = record.getRecordNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_inconsistent() {
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(record.isMapped("column1"));
    }

    @Test
    public void testIsSet() {
        assertFalse(record.isSet("column1"));
    }

    @Test
    public void testIterator() {
        assertNotNull(record.iterator());
    }

    @Test
    public void testPutIn() {
        assertTrue(record.putIn(new HashMap<>()).isEmpty());
    }

    @Test
    public void testSize() {
        int expected = 3;
        int actual = record.size();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMap() {
        assertTrue(record.toMap().isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "[value1, value2, value3]";
        String actual = record.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        String[] expected = {"value1", "value2", "value3"};
        String[] actual = record.values();
        assertArrayEquals(expected, actual);
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }

}