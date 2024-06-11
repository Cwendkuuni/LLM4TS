package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertEquals(Integer.valueOf(1), record.get("Age").getClass());
        assertNull(record.get("Unknown"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_unknownColumn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("Unknown");
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_noMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test
    public void testGetEnum() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Name", record.get(Enum.NAME));
        assertEquals("Age", record.get(Enum.AGE));
        assertEquals("City", record.get(Enum.CITY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnum_unknownEnum() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(Enum.UNKNOWN);
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(Long.valueOf(1), record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 3);
        CSVRecord recordInconsistent = new CSVRecord(values, mapping, null, 1);
        assertFalse(recordInconsistent.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));

        String[] valuesEmpty = new String[0];
        CSVRecord recordEmpty = new CSVRecord(valuesEmpty, mapping, null, 1);
        assertFalse(recordEmpty.isSet("Name"));
        assertFalse(recordEmpty.isSet("Age"));
        assertFalse(recordEmpty.isSet("City"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedValues = Arrays.asList("Name", "Age", "City");
        List<String> actualValues = new ArrayList<>();
        for (String s : record) {
            actualValues.add(s);
        }
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1);
        map.putAll(record.putIn(map));
        assertEquals("Name", map.get("Name"));
        assertEquals("Age", map.get("Age"));
        assertEquals("City", map.get("City"));
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("[Name, Age, City]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String[] expectedValues = new String[]{"Name", "Age", "City"};
        assertArrayEquals(expectedValues, record.values());
    }
}