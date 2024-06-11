import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        csvRecord = new CSVRecord(values, mapping, "This is a comment", 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.COLUMN1));
        assertEquals("value2", csvRecord.get(TestEnum.COLUMN2));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, "", 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        csvRecord.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        csvRecord.get(2);
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
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
    public void testIsNotConsistent() {
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "", 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertFalse(csvRecord.isMapped("invalid"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertFalse(csvRecord.isSet("invalid"));
    }

    @Test
    public void testIterator() {
        assertTrue(csvRecord.iterator().hasNext());
        assertEquals("value1", csvRecord.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(2, csvRecord.toList().size());
        assertEquals("value1", csvRecord.toList().get(0));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(2, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2]", csvRecord.toString());
    }

    enum TestEnum {
        COLUMN1,
        COLUMN2
    }
}