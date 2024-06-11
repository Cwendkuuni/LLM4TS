import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVRecord;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("a", record.get("col1"));
        assertEquals("b", record.get("col2"));
        assertEquals("c", record.get("col3"));
        assertNull(record.get("invalid"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1L);
        recordWithoutMapping.get("col1");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("col1", 0);
        assertFalse(new CSVRecord(new String[]{"a", "b", "c"}, inconsistentMapping, null, 1L).isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
        assertFalse(record.isMapped("invalid"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));
        assertFalse(record.isSet("invalid"));
    }

    @Test
    public void testIterator() {
        StringBuilder sb = new StringBuilder();
        for (String value : record) {
            sb.append(value);
        }
        assertEquals("abc", sb.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"a", "b", "c"}, record.values());
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[a, b, c]", record.toString());
    }

}