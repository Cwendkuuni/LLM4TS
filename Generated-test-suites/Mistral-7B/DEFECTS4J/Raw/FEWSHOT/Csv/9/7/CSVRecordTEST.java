package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(3, record.size());
    }

    @Test
    public void testGet() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertEquals("name1", record.get(0).getClass());
        assertEquals("name2", record.get(1).getClass());
        assertEquals("name1", record.get("name1").getClass());
        assertEquals("name2", record.get("name2").getClass());
    }

    @Test
    public void testGetNull() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertNull(record.get("nonExistingName"));
    }

    @Test
    public void testGetEnum() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("foo", record.get(Enum.Name.NAME));
    }

    @Test
    public void testGetIllegalStateException() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertThrows(IllegalStateException.class, () -> record.get("name"));
    }

    @Test
    public void testGetIllegalArgumentException() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertThrows(IllegalArgumentException.class, () -> record.get(3));
        assertThrows(IllegalArgumentException.class, () -> record.get("nonExistingColumn"));
    }

    @Test
    public void testGetArrayIndexOutOfBoundsException() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isConsistent());

        String[] inconsistentValues = new String[]{"foo", "bar"};
        CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, mapping, null, 0);

        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("nonExisting"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("nonExisting"));

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 0);

        assertFalse(emptyRecord.isSet("Name"));
        assertFalse(emptyRecord.isSet("Age"));
        assertFalse(emptyRecord.isSet("City"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        List<String> expectedList = Arrays.asList("foo", "bar", "baz");
        List<String> actualList = new ArrayList<>();

        for (String value : record) {
            actualList.add(value);
        }

        assertEquals(expectedList, actualList);

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 0);

        Iterator<String> iterator = emptyRecord.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "foo");
        expectedMap.put("Age", "bar");
        expectedMap.put("City", "baz");

        Map<String, String> actualMap = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);
        actualMap = record.putIn(actualMap);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        String expectedString = "CSVRecord[values=[foo, bar, baz], mapping=null, comment=null, recordNumber=0]";
        String actualString = record.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        String[] expectedValues = {"foo", "bar", "baz"};
        String[] actualValues = record.values();

        assertSame(expectedValues, actualValues);
    }
}