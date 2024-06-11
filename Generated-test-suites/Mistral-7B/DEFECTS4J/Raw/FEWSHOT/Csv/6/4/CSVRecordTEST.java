package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_whenGivenValidValues_thenCreateValidCSVRecord() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1L, record.getRecordNumber());
        assertEquals(Arrays.asList(values), record.iterator().asList());
        assertEquals("name1", record.get("name1"));
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value1", record.get(new Integer(0)));
        assertEquals("value2", record.get(new Integer(1)));
        assertFalse(record.isConsistent());
        assertEquals(null, record.getComment());
        assertEquals(2, record.size());
    }

    @Test
    public void testCSVRecord_whenGivenEmptyValues_thenCreateValidCSVRecord() {
        String[] values = new String[0];
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testGet_whenGivenValidEnum_thenReturnCorrespondingValue() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", record.get(Enum.valueOf(CSVRecord.class, "NAME1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_whenGivenInvalidName_thenThrowIllegalArgumentException() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        record.get("unknownName");
    }

    @Test
    public void testGet_whenGivenNullMapping_thenThrowIllegalStateException() {
        String[] values = new String[] {"name1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertThrows(IllegalStateException.class, () -> record.get("name1"));
    }

    @Test
    public void testIsConsistent_whenGivenValidValuesAndMapping_thenReturnTrue() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_whenGivenInvalidValuesAndMapping_thenReturnFalse() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_whenGivenInvalidIndex_thenThrowIllegalArgumentException() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        record.get(3);
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_whenGivenValidIndexButInvalidValuesLength_thenThrowIllegalArgumentException() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        record.get(1);
    }

    @Test
    public void testIsMapped_whenGivenValidName_thenReturnTrue() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
    }

    @Test
    public void testIsSet_whenGivenValidName_thenReturnTrueIfValueIsPresent() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testPutIn_whenGivenValidMap_thenPutValuesIntoMap() {
        String[] values = new String[] {"name1", "value1", "value2"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1L);

        record.putIn(map);

        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals(null, map.get("name3"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutIn_whenGivenNullMap_thenThrowNullPointerException() {
        String[] values = new String[] {"name1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1L);

        record.putIn(null);
    }

    @Test
    public void testToString_whenInvoked_thenReturnStringRepresentationOfValues() {
        String[] values = new String[] {"name1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertEquals("[name1, value1, value2]", record.toString());
    }

    @Test
    public void testValues_whenInvoked_thenReturnStringArray() {
        String[] values = new String[] {"name1", "value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1L);

        String[] actualValues = record.values();

        assertArrayEquals(values, actualValues);
    }
}