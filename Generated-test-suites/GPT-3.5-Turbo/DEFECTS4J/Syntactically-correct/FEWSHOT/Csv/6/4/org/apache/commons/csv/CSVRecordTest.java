package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetWithEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetWithIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoMappingSpecified() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMappingNotFound() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get("notFound");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        record.get("invalid");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 3);
        assertEquals(3, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
    }

    @Test
    public void testIsMappedWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("name"));
    }

    @Test
    public void testIsSetWithMappingAndValidValue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
    }

    @Test
    public void testIsSetWithMappingAndInvalidValue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIsSetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("name"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);

        int i = 1;
        for (String value : record) {
            assertEquals("value" + i, value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> map = new HashMap<>();

        record.putIn(map);

        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
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

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
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