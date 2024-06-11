import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Test
    public void testGetEnum() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.ENUM1));
        assertEquals("value2", record.get(TestEnum.ENUM2));
    }

    @Test
    public void testGetInt() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetString() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStringWithoutMapping() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidMapping() {
        record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidIndex() {
        record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        record.get("name3");
    }

    @Test
    public void testGetComment() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        record = new CSVRecord(new String[]{"value1"}, getMapping(), null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<String, String>();
        record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        record.putIn(map);
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    @Test
    public void testSize() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    @Test
    public void testToString() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("[value1, value2]", record.toString());
    }

    private Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        return mapping;
    }

    private enum TestEnum {
        ENUM1,
        ENUM2
    }
}