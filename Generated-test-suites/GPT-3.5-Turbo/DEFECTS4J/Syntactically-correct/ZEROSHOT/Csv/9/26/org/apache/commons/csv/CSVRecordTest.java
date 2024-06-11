import org.junit.Test;

import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.TEST1));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertEquals("value1", record.get("col1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        record.get("col3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        record.get("col2");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithValidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getInvalidMapping(), null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(record.isMapped("col1"));
    }

    @Test
    public void testIsMappedWithValidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertTrue(record.isMapped("col1"));
    }

    @Test
    public void testIsMappedWithInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getInvalidMapping(), null, 1);
        assertFalse(record.isMapped("col1"));
    }

    @Test
    public void testIsSetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(record.isSet("col1"));
    }

    @Test
    public void testIsSetWithValidMappingAndValidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertTrue(record.isSet("col1"));
    }

    @Test
    public void testIsSetWithValidMappingAndInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertFalse(record.isSet("col2"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNotNull(record.iterator());
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertNotNull(record.putIn(new HashMap<String, String>()));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, getMapping(), null, 1);
        assertNotNull(record.toMap());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNotNull(record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNotNull(record.values());
        assertEquals(2, record.values().length);
        assertEquals("value1", record.values()[0]);
    }

    private Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        return mapping;
    }

    private Map<String, Integer> getInvalidMapping() {
        Map<String, Integer> invalidMapping = new HashMap<>();
        invalidMapping.put("col1", 0);
        invalidMapping.put("col2", 2);
        return invalidMapping;
    }

    private enum TestEnum {
        TEST1, TEST2
    }

}