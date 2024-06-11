import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;

    @Test
    public void testGetByEnum() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        assertEquals("Value2", record.get(EnumValue.VALUE2));
    }

    @Test
    public void testGetByIndex() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        assertEquals("Value1", record.get(0));
    }

    @Test
    public void testGetByNameWithMapping() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        assertEquals("Value3", record.get("ColumnName3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        record.get("ColumnName2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        record.get("InvalidColumnName");
    }

    @Test
    public void testGetComment() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentWithMapping() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithValidName() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        assertTrue(record.isMapped("ColumnName1"));
        assertFalse(record.isMapped("InvalidColumnName"));
    }

    @Test
    public void testIsSetWithValidName() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        assertTrue(record.isSet("ColumnName2"));
    }

    @Test
    public void testIsSetWithInvalidName() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        assertFalse(record.isSet("InvalidColumnName"));
    }

    @Test
    public void testIterator() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        String[] expectedValues = {"Value1", "Value2", "Value3"};
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i], value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("Value1", map.get("ColumnName1"));
        assertEquals("Value3", map.get("ColumnName3"));
    }

    @Test
    public void testSize() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        List<String> expectedList = Arrays.asList("Value1", "Value2", "Value3");
        assertEquals(expectedList, record.toList());
    }

    @Test
    public void testToMap() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, createMapping(), null, 1);
        Map<String, String> expectedMap = createMapping();
        assertEquals(expectedMap, record.toMap());
    }

    @Test
    public void testValues() {
        record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        String[] expectedValues = {"Value1", "Value2", "Value3"};
        assertArrayEquals(expectedValues, record.values());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ColumnName1", 0);
        mapping.put("ColumnName2", 1);
        mapping.put("ColumnName3", 2);
        return mapping;
    }

    public enum EnumValue {
        VALUE1,
        VALUE2,
        VALUE3
    }
}