package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordIsSerializable() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Serialize and deserialize the record
        byte[] serializedRecordBytes = SerializedObjectTester.serialize(record);
        CSVRecord deserializedRecord = (CSVRecord) SerializedObjectTester.deserialize(serializedRecordBytes);

        // Check that the deserialized record is the same as the original one
        assertArrayEquals(values, deserializedRecord.values());
        assertEquals(mapping, deserializedRecord.mapping);
        assertEquals(1, deserializedRecord.recordNumber);
        assertNull(deserializedRecord.comment);
    }

    @Test
    public void testGetValueByEnum() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Enum<String> fooEnum = Enum.valueOf(String.class, "FOO");
        Enum<String> barEnum = Enum.valueOf(String.class, "BAR");
        Enum<String> bazEnum = Enum.valueOf(String.class, "BAZ");

        assertEquals("foo", record.get(fooEnum));
        assertEquals("bar", record.get(barEnum));
        assertEquals("baz", record.get(bazEnum));
    }

    @Test
    public void testGetValueByIndex() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], record.get(i));
        }
    }

    @Test
    public void testGetValueByName() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get("A"));
        assertEquals("bar", record.get("B"));
        assertEquals("baz", record.get("C"));

        assertNull(record.get("D"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameWhenNoMappingIsProvided() {
        String[] values = new String[]{"foo,bar,baz"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("A");
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"foo,bar,baz"};
        String comment = "This is a comment";
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        // Test inconsistent record
        String[] inconsistentValues = new String[]{"foo,bar"};
        String[] inconsistentMappingValues = new String[]{"A", "B", "C"};

        CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, Arrays.asList(inconsistentMappingValues), null, 1);

        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("A"));
        assertTrue(record.isMapped("B"));
        assertTrue(record.isMapped("C"));

        assertFalse(record.isMapped("D"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo,bar,baz,qux"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        mapping.put("D", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("A"));
        assertTrue(record.isSet("B"));
        assertTrue(record.isSet("C"));
        assertTrue(record.isSet("D"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = record.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            assertEquals(values[index++], iterator.next());
        }
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, String> targetMap = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);

        Map<String, String> resultMap = record.putIn(targetMap);

        assertEquals(targetMap, resultMap);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        String[] values = new String[]{"foo,bar,baz"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> resultList = record.toList();

        assertSame(Arrays.asList(values), resultList);
    }

    @Test
    public void testToMap() {
        String[] values = new String[]{"foo,bar,baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> resultMap = record.toMap();

        for (Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(values[entry.getValue().intValue()], resultMap.get(entry.getKey()));
        }
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo,bar,baz"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[foo, bar, baz]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"foo,bar,baz"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertSame(values, record.values());
    }
}