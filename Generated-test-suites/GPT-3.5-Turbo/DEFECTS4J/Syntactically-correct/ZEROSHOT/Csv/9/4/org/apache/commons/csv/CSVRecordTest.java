import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", record.get(TestEnum.COLUMN1));
    }

    @Test
    public void testGet() {
        assertEquals("value1", record.get(0));
    }

    @Test
    public void testGetName() {
        assertEquals("value1", record.get("column1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value"}, null, null, 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        record.get("invalidColumn");
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("invalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        for (String value : record) {
            assertTrue(value.equals("value1") || value.equals("value2"));
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testSize() {
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2]", record.toString());
    }

    private enum TestEnum {
        COLUMN1
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(CSVRecordTest.class);
    }
}