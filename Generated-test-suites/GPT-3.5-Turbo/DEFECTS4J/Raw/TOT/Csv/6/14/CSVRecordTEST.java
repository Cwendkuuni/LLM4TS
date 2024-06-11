import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        String comment = "// Comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        String enumValue = csvRecord.get(Enum.EXAMPLE);
        assertEquals("value1", enumValue);
    }

    @Test
    public void testGetByIndex() {
        String value = csvRecord.get(0);
        assertEquals("value1", value);
    }

    @Test
    public void testGetByName() {
        String value = csvRecord.get("name2");
        assertEquals("value2", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        csvRecord.get("name2");
        fail("Expected IllegalStateException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        csvRecord.get("invalidName");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        csvRecord.get(5);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testGetByIndexWithEmptyValueArray() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord emptyValueArrayRecord = new CSVRecord(values, mapping, null, 1);
        String value = emptyValueArrayRecord.get(0);
        assertNull(value);
    }

    @Test
    public void testGetComment() {
        String comment = csvRecord.getComment();
        assertEquals("// Comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvRecord.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent() {
        boolean consistent = csvRecord.isConsistent();
        assertTrue(consistent);
    }

    @Test
    public void testIsConsistentWithNullMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        boolean consistent = csvRecord.isConsistent();
        assertTrue(consistent);
    }

    @Test
    public void testIsConsistentWithDifferentSizeMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        boolean consistent = csvRecord.isConsistent();
        assertFalse(consistent);
    }

    @Test
    public void testIsMapped() {
        boolean mapped = csvRecord.isMapped("name2");
        assertTrue(mapped);
    }

    @Test
    public void testIsMappedWithNullMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        boolean mapped = csvRecord.isMapped("name2");
        assertFalse(mapped);
    }

    @Test
    public void testIsMappedWithInvalidName() {
        boolean mapped = csvRecord.isMapped("invalidName");
        assertFalse(mapped);
    }

    @Test
    public void testIsSet() {
        boolean set = csvRecord.isSet("name2");
        assertTrue(set);
    }

    @Test
    public void testIsSetWithNullMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        boolean set = csvRecord.isSet("name2");
        assertFalse(set);
    }

    @Test
    public void testIsSetWithInvalidName() {
        boolean set = csvRecord.isSet("invalidName");
        assertFalse(set);
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> populatedMap = csvRecord.putIn(map);
        assertNotNull(populatedMap);
        assertEquals(3, populatedMap.size());
        assertEquals("value1", populatedMap.get("name1"));
        assertEquals("value2", populatedMap.get("name2"));
        assertEquals("value3", populatedMap.get("name3"));
    }

    @Test
    public void testSize() {
        int size = csvRecord.size();
        assertEquals(3, size);
    }

    @Test
    public void testToList() {
        List<String> list = csvRecord.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
        assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testToString() {
        String str = csvRecord.toString();
        assertNotNull(str);
        assertEquals("[value1, value2, value3]", str);
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertNotNull(values);
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }
}