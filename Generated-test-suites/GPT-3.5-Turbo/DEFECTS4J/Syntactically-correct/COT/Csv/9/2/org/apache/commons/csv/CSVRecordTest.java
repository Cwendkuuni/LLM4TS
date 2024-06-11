import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private final String[] values = {"Value1", "Value2", "Value3"};
    private final Map<String, Integer> mapping = new HashMap<>();
    private final String comment = "This is a comment";
    private final long recordNumber = 1;
    private final CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

    @Test
    public void testGetByEnum() {
        String value = record.get(TestEnum.ENUM1);
        assertEquals("Value1", value);
    }

    @Test
    public void testGetByIndex() {
        String value = record.get(1);
        assertEquals("Value2", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        mapping.put("Column1", 0);
        record.get("InvalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        mapping.put("Column1", 1);
        record.get("Column1");
    }

    @Test
    public void testGetComment() {
        String comment = record.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistentWithNoMapping() {
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistentWithMapping() {
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistentWithInvalidMappingSize() {
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        boolean isConsistent = record.isConsistent();
        assertFalse(isConsistent);
    }

    @Test
    public void testIsMappedWithMapping() {
        mapping.put("Column1", 0);
        boolean isMapped = record.isMapped("Column1");
        assertTrue(isMapped);
    }

    @Test
    public void testIsMappedWithNoMapping() {
        boolean isMapped = record.isMapped("Column1");
        assertFalse(isMapped);
    }

    @Test
    public void testIsSetWithInvalidName() {
        mapping.put("Column1", 1);
        boolean isSet = record.isSet("Column1");
        assertFalse(isSet);
    }

    @Test
    public void testIsSetWithValidName() {
        mapping.put("Column1", 0);
        boolean isSet = record.isSet("Column1");
        assertTrue(isSet);
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("Value1", map.get("Column1"));
        assertEquals("Value2", map.get("Column2"));
        assertEquals("Value3", map.get("Column3"));
    }

    @Test
    public void testSize() {
        int size = record.size();
        assertEquals(3, size);
    }

    @Test
    public void testToList() {
        List<String> list = record.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals("Value1", list.get(0));
        assertEquals("Value2", list.get(1));
        assertEquals("Value3", list.get(2));
    }

    @Test
    public void testToMap() {
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        Map<String, String> map = record.toMap();
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("Value1", map.get("Column1"));
        assertEquals("Value2", map.get("Column2"));
        assertEquals("Value3", map.get("Column3"));
    }

    private enum TestEnum {
        ENUM1,
        ENUM2,
        ENUM3;
    }
}