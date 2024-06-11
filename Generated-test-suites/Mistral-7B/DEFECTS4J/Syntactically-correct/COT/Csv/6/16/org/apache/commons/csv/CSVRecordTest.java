package org.apache.commons.csv.test;

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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    @SuppressWarnings("unchecked")
    private static final CSVFormat format = new CSVFormat(new String[]{"name", "age"}, ',');

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}.
     */
    @org.junit.Test
    public void testCSVRecord() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Record values should be equals to the input values", Arrays.toString(values),
                Arrays.toString(record.values()));
        assertEquals("Mapping should be equals to the input mapping", mapping, record.mapping);
        assertEquals("Comment should be equals to null", null, record.comment);
        assertEquals("Record number should be equals to 0", 0, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#get(Enum)}.
     */
    @org.junit.Test
    public void testGetEnum() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Name should be equals to John Doe", "John Doe", record.get(Enum.NAME));
        assertEquals("Age should be equals to 30", "30", record.get(Enum.AGE));
    }

    /**
     * Test method for {@link CSVRecord#get(int)}.
     */
    @org.junit.Test
    public void testGetInt() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Name should be equals to John Doe", "John Doe", record.get(0));
        assertEquals("Age should be equals to 30", "30", record.get(1));
    }

    /**
     * Test method for {@link CSVRecord#get(String)}.
     */
    @org.junit.Test
    public void testGetString() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Name should be equals to John Doe", "John Doe", record.get("name"));
        assertEquals("Age should be equals to 30", "30", record.get("age"));
    }

    /**
     * Test method for {@link CSVRecord#getComment()}.
     */
    @org.junit.Test
    public void testGetComment() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, "Comment", 0);
        assertEquals("Comment should be equals to Comment", "Comment", record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()}.
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Record number should be equals to 0", 0, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}.
     */
    @org.junit.Test
    public void testIsConsistent() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue("Record should be consistent", record.isConsistent());

        final String[] values2 = new String[]{"John Doe"};
        final Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("name", 0);
        mapping2.put("age", 1);
        final CSVRecord record2 = new CSVRecord(values2, mapping2, null, 0);
        assertFalse("Record with wrong number of values should be inconsistent", record2.isConsistent());

        final String[] values3 = new String[]{"John Doe", "30", "Foo"};
        final Map<String, Integer> mapping3 = new HashMap<>();
        mapping3.put("name", 0);
        mapping3.put("age", 1);
        final CSVRecord record3 = new CSVRecord(values3, mapping3, null, 0);
        assertFalse("Record with wrong number of mappings should be inconsistent", record3.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}.
     */
    @org.junit.Test
    public void testIsMapped() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue("name should be mapped", record.isMapped("name"));
        assertFalse("Foo should not be mapped", record.isMapped("Foo"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}.
     */
    @org.junit.Test
    public void testIsSet() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue("name should be set", record.isSet("name"));
        assertTrue("age should be set", record.isSet("age"));
        assertFalse("Foo should not be set", record.isSet("Foo"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}.
     */
    @org.junit.Test
    public void testIterator() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        int i = 0;
        for (final String value : record) {
            assertEquals("Iterator values should be equals to record values", values[i], value);
            ++i;
        }
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)}.
     */
    @org.junit.Test
    public void testPutIn() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, String> map = new HashMap<>();
        final CSVRecord record = new CSVRecord(values, null, null, 0);
        record.putIn(map);
        assertEquals("map values should be equals to record values", Arrays.toString(values), map.values().toString());
    }

    /**
     * Test method for {@link CSVRecord#size()}.
     */
    @org.junit.Test
    public void testSize() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("size should be equals to number of record values", 2, record.size());
    }

    /**
     * Test method for {@link CSVRecord#toList()}.
     */
    @org.junit.Test
    public void testToList() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        List<String> list = record.toList();
        assertEquals("List values should be equals to record values", Arrays.toString(values), list.toString());
    }

    /**
     * Test method for {@link CSVRecord#toMap()}.
     */
    @org.junit.Test
    public void testToMap() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> map = record.toMap();
        assertEquals("map values should be equals to record values", Arrays.toString(values), map.values().toString());
    }

    /**
     * Test method for {@link CSVRecord#toString()}.
     */
    @org.junit.Test
    public void testToString() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("toString should be equals to Arrays.toString(values)", Arrays.toString(values), record.toString());
    }

    /**
     * Test method for {@link CSVRecord#values()}.
     */
    @org.junit.Test
    public void testValues() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertSame("values should be equals to record values", values, record.values());
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with null mapping.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetWithNullMapping() {
        final String[] values = new String[]{"John Doe", "30"};
        final CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("name");
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with wrong mapping.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetWithWrongMapping() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("foo", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("foo");
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with wrong record values.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetWithWrongRecordValues() {
        final String[] values = new String[]{"John Doe"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("age");
    }

    /**
     * Test method for {@link CSVRecord#getComment()} with null comment.
     */
    @org.junit.Test
    public void testGetCommentWithNullComment() {
        final String[] values = new String[]{"John Doe", "30"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertNull("getComment should return null with no comment", record.getComment());
    }
}

enum Enum {
    NAME, AGE;
}