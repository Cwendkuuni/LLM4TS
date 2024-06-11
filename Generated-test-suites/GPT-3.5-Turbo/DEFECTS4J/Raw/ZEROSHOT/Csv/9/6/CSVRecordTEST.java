import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        csvRecord = new CSVRecord(values, null, "comment", 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.TEST_1));
        assertEquals("value2", csvRecord.get(TestEnum.TEST_2));
        assertEquals("value3", csvRecord.get(TestEnum.TEST_3));
    }

    @Test
    public void testGetInt() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetString() {
        assertEquals("value1", csvRecord.get("name1"));
        assertEquals("value2", csvRecord.get("name2"));
        assertEquals("value3", csvRecord.get("name3"));
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(csvRecord.isMapped("name1"));
        assertFalse(csvRecord.isMapped("name2"));
        assertFalse(csvRecord.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        assertFalse(csvRecord.isSet("name1"));
        assertFalse(csvRecord.isSet("name2"));
        assertFalse(csvRecord.isSet("name3"));
    }

    @Test
    public void testIterator() {
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testPutIn() {
        assertNotNull(csvRecord.putIn(new HashMap<>()));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertNotNull(csvRecord.toList());
    }

    @Test
    public void testToMap() {
        assertNotNull(csvRecord.toMap());
    }

    @Test
    public void testToString() {
        assertNotNull(csvRecord.toString());
    }

    private enum TestEnum {
        TEST_1,
        TEST_2,
        TEST_3
    }
}