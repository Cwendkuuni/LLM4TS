package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordCreationWithEmptyValues() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordCreationWithEmptyMapping() {
        String[] values = {"Alice", "25", "single"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertNull(record.mapping);
        assertEquals(values, record.values());
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordCreationWithInvalidMapping() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 1);
        mapping.put("Age", 0);
        new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetValueByEnum() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Function<Enum<?>, String> getValueFunction = (Enum<?> e) -> record.get(e);
        assertEquals("Alice", getValueFunction.apply(CSVRecord.Name.class));
        assertEquals("25", getValueFunction.apply(CSVRecord.Age.class));
        assertEquals("single", getValueFunction.apply(CSVRecord.MaritalStatus.class));
    }

    @Test
    public void testGetValueByName() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Alice", record.get("Name"));
        assertEquals("25", record.get("Age"));
        assertEquals("single", record.get("MaritalStatus"));
        assertNull(record.get("Unknown"));
    }

    @Test
    public void testGetValueByNameWithComment() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        String comment = "This record is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals("Alice", record.get("Name"));
        assertEquals("25", record.get("Age"));
        assertEquals("single", record.get("MaritalStatus"));
        assertEquals(comment, record.getComment());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameWithoutMapping() {
        String[] values = {"Alice", "25", "single"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByNameWithInvalidIndex() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByNameWithInvalidRecord() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, -1);

        record.get("Name");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        Map<String, Integer> inconsistentMapping = new HashMap<>(mapping);
        inconsistentMapping.put("Unknown", 3);
        CSVRecord inconsistentRecord = new CSVRecord(values, inconsistentMapping, null, 1);

        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("MaritalStatus"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("MaritalStatus"));
        assertFalse(record.isSet("Unknown"));

        String[] emptyValues = {};
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);

        assertFalse(emptyRecord.isSet("Name"));
        assertFalse(emptyRecord.isSet("Age"));
        assertFalse(emptyRecord.isSet("MaritalStatus"));
    }

    @Test
    public void testIterator() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = record.iterator();
        assertEquals("Alice", iterator.next());
        assertEquals("25", iterator.next());
        assertEquals("single", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = {"Alice", "25", "single"};
        Map<String, String> targetMap = new HashMap<>();
        CSVRecord record = new CSVRecord(values, new HashMap<>(), null, 1);

        Map<String, String> resultMap = record.putIn(targetMap);
        assertEquals(values.length, resultMap.size());

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], resultMap.get(mapping.get(CSVRecord.headerNames()[i])));
        }
    }

    @Test
    public void testSize() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> list = record.toList();
        assertEquals(values.length, list.size());
        assertArrayEquals(values, list.toArray(new String[0]));
    }

    @Test
    public void testToString() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("[Alice, 25, single]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = {"Alice", "25", "single"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("MaritalStatus", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String[] recordValues = record.values();
        assertEquals(values.length, recordValues.length);
        assertArrayEquals(values, recordValues);
    }
}