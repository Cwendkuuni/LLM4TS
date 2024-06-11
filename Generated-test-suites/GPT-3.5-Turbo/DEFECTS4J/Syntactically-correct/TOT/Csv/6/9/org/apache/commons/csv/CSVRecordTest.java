import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    CSVRecord record;

    @Test
    public void testGet_byEnum() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value2", record.get(EnumTest.VALUE2));
    }

    @Test
    public void testGet_byIndex() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGet_byName() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value2", record.get("column2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_NoMapping() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("column2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_NameNotMapped() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_InvalidIndex() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get("column3");
    }

    @Test
    public void testGetComment() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 10);
        assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent_HeaderMapped() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_HeaderNotMapped() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped_Mapped() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isMapped("column2"));
    }

    @Test
    public void testIsMapped_NotMapped() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet_MappedAndHasValue() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isSet("column2"));
    }

    @Test
    public void testIsSet_MappedAndNoValue() {
        record = new CSVRecord(new String[]{"value1", "", "value3"}, createMapping(), null, 1);
        assertFalse(record.isSet("column2"));
    }

    @Test
    public void testIsSet_NotMapped() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.toList().size());
    }

    @Test
    public void testToMap() {
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        return mapping;
    }

    private enum EnumTest {
        VALUE1, VALUE2, VALUE3
    }
}