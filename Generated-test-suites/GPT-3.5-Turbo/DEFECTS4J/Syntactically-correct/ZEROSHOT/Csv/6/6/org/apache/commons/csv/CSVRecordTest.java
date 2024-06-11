import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value3", csvRecord.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameNotFound() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        csvRecord.get("name4");
    }

    @Test
    public void testGetComment() {
        assertNull(csvRecord.getComment());
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(csvRecord.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        assertFalse(csvRecord.isSet("name"));
    }

    @Test
    public void testIterator() {
        assertTrue(csvRecord.iterator().hasNext());
        assertEquals("value1", csvRecord.iterator().next());
    }

    @Test
    public void testPutIn() {
        // Test with empty map
        csvRecord.putIn(new HashMap<>());

        // Test with existing map
        csvRecord.putIn(csvRecord.toMap());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(csvRecord.toList().size(), csvRecord.size());
    }

    @Test
    public void testToMap() {
        assertEquals(0, csvRecord.toMap().size());
    }

    @Test
    public void testValues() {
        assertEquals(3, csvRecord.values().length);
    }
}

enum TestEnum {
    VALUE1,
    VALUE2,
    VALUE3
}