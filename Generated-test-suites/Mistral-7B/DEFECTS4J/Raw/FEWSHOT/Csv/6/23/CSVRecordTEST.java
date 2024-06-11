package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordIsSerializable() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals(record, deserialize(serialize(record)));
    }

    @Test
    public void testEmptyCSVRecord() {
        CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 0);
        assertTrue(record.isConsistent());
        assertTrue(record.isEmpty());
        assertEquals(0, record.size());
    }

    @Test
    public void testNewCSVRecord() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(record.get(0), "foo");
        assertEquals(record.get(1), "bar");
        assertEquals(record.get(2), "baz");
        assertEquals(record.get("header1"), "foo");
        assertEquals(record.get("header2"), "bar");
        assertEquals(record.get("header3"), "baz");
        assertEquals(record.size(), 3);
        assertTrue(record.isConsistent());
        assertFalse(record.isEmpty());
        assertNull(record.getComment());
        assertEquals(record.getRecordNumber(), 1);
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isMapped("unknow"));
        assertEquals(Arrays.toString(record.values()), Arrays.toString(values));
    }

    @Test
    public void testGetNullValue() {
        CSVFormat format = new CSVFormatBuilder().setNullString("").build();
        String[] values = {"foo", null, "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNull(record.get("header2"));
        assertEquals("", record.get("header2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoMappingIsThrown() {
        String[] values = {"foo", "bar"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("header1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMappingIsThrown() {
        String[] values = {"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("header1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentRecordIsThrown() {
        String[] values = {"foo"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("header2");
    }

    @Test
    public void testToString() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("[foo, bar, bas]", record.toString());
    }

    @Test
    public void testIterator() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> list = record.toList();

        Iterator<String> iterator = record.iterator();
        for (int i = 0; i < 3; i++) {
            assertEquals(list.get(i), iterator.next());
        }
    }

    @Test
    public void testPutInMap() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);
        map.putAll(record.putIn(map));

        assertEquals(record.get(0), map.get("foo"));
        assertEquals(record.get(1), map.get("bar"));
        assertEquals(record.get(2), map.get("baz"));
    }

    private byte[] serialize(Object obj) {
        byte[] bytes = null;
        try {
            bytes = new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private Object deserialize(byte[] bytes) {
        Object obj = null;
        try {
            obj = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}