
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetMethod_withEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetMethod_withIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetMethod_withName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethod_withNullMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethod_withInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethod_withInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get(3);
    }

    @Test
    public void testGetComment_withComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetComment_withoutComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent_withMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_withoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped_withMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
    }

    @Test
    public void testIsMapped_withoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet_withMappingAndValidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
    }

    @Test
    public void testIsSet_withMappingAndInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertFalse(record.isSet("invalidName"));
    }

    @Test
    public void testIsSet_withoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testPutIn_withMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertTrue(map.containsKey("name1"));
        assertTrue(map.containsKey("name2"));
        assertTrue(map.containsKey("name3"));
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testPutIn_withoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        return mapping;
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}
