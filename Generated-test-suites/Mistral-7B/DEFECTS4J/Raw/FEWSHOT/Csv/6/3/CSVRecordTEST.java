package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_DefaultConstructor() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertNull(record.getMapping());
        assertNull(record.getRecordNumber());
        assertNull(record.getValues());
    }

    @Test
    public void testCSVRecord_ConstructorWithValues() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertSame(mapping, record.getMapping());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecord_ConstructorWithValuesAndMapping() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertSame(mapping, record.getMapping());
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    public void testCSVRecord_ConstructorWithValuesAndMappingWithComment() {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertSame(mapping, record.getMapping());
        assertEquals("comment", record.getComment());
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testGet_Enum() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("value1", record.get(HeaderType.HEADER1));
        assertEquals("value2", record.get(HeaderType.HEADER2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_EnumWithInvalidName() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get(HeaderType.INVALID_HEADER);
    }

    @Test
    public void testGet_Int() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_IntWithInvalidIndex() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get(3);
    }

    @Test
    public void testGet_String() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertNull(record.get("invalid_header"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_StringWithInvalidHeader() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("invalid_header");
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());

        values = new String[] {"header1", "value1", "header2", "value1", "header3"};
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord recordInconsistent = new CSVRecord(values, mapping, null, 0);
        assertFalse(recordInconsistent.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertFalse(record.isMapped("invalid_header"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertFalse(record.isSet("invalid_header"));
    }

    @Test
    public void testGetMapping() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, Integer> expectedMapping = new HashMap<>();
        expectedMapping.put("header1", 0);
        expectedMapping.put("header2", 2);
        assertEquals(expectedMapping, record.getMapping());
    }

    @Test
    public void testToString() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        String expectedToString = "[header1=value1, header2=value2]";
        assertEquals(expectedToString, record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        String[] expectedValues = {"header1", "value1", "header2", "value2"};
        assertArrayEquals(expectedValues, record.values());
    }

    @Test
    public void testIterator() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        List<String> expectedList = Arrays.asList("header1", "value1", "header2", "value2");
        Iterator<String> expectedIterator = expectedList.iterator();

        List<String> list = new ArrayList<>();
        for (String value : record) {
            list.add(value);
        }

        assertEquals(expectedList, list);
        assertEquals(expectedIterator.next(), record.iterator().next());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("header1", "value1");
        expectedMap.put("header2", "value2");

        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);
        map = record.putIn(map);

        assertEquals(expectedMap, map);
    }

    @Test
    public void testToMap() {
        String[] values = new String[] {"header1", "value1", "header2", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("header1", "value1");
        expectedMap.put("header2", "value2");

        Map<String, String> map = record.toMap();
        assertEquals(expectedMap, map);
    }
}