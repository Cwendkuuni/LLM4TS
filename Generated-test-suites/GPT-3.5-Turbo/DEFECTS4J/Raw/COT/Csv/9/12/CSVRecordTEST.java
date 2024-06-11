
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertEquals("value1", record.get("NAME1"));
        assertEquals("value2", record.get("NAME2"));
        assertEquals("value3", record.get("NAME3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("NAME1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get("NAME4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get("NAME1");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isMapped("NAME1"));
        assertTrue(record.isMapped("NAME2"));
        assertTrue(record.isMapped("NAME3"));
        assertFalse(record.isMapped("NAME4"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isSet("NAME1"));
        assertTrue(record.isSet("NAME2"));
        assertTrue(record.isSet("NAME3"));
        assertFalse(record.isSet("NAME4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        String[] expectedValues = {"value1", "value2", "value3"};
        int index = 0;
        for (String value : record) {
            assertEquals(expectedValues[index], value);
            index++;
        }
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("NAME1"));
        assertEquals("value2", map.get("NAME2"));
        assertEquals("value3", map.get("NAME3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("NAME1", "value1");
        expectedMap.put("NAME2", "value2");
        expectedMap.put("NAME3", "value3");
        assertEquals(expectedMap, record.toMap());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        return mapping;
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }

}
