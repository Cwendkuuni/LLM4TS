import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2"};
        csvRecord = new CSVRecord(values, null, "This is a comment", 1L);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("name1"));
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(csvRecord.isMapped("nonExistentName"));
        assertTrue(csvRecord.isMapped("name1"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("name1"));
        assertFalse(csvRecord.isSet("name2"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testPutIn() {
        assertEquals("value1", csvRecord.putIn(new HashMap<String, String>()).get("name1"));
    }

    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToMap() {
        assertEquals("value1", csvRecord.toMap().get("name1"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2]", csvRecord.toString());
    }

    enum TestEnum {
        VALUE1, VALUE2
    }

}