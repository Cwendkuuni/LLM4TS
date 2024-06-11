package org.apache.commons.csv;

import static org.junit.Assert.*;

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
        assertEquals("Age", record.get(Enum.valueOf(CSVRecord.class, "AGE")));

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertNull(emptyRecord.get(0));
        assertNull(emptyRecord.get(1));
        assertNull(emptyRecord.get(2));
        assertNull(emptyRecord.get("Name"));
        assertNull(emptyRecord.get("Age"));
        assertNull(emptyRecord.get("City"));
        assertNull(emptyRecord.get(Enum.valueOf(CSVRecord.class, "AGE")));
    }

    @Test
    public void testGetWithInvalidName() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        try {
            record.get("InvalidName");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            record.get(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
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

        String[] valuesWithMismatch = new String[]{"Name", "Age", "City", "Extra"};
        CSVRecord recordWithMismatch = new CSVRecord(valuesWithMismatch, mapping, null, 1);
        assertFalse(recordWithMismatch.isConsistent());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertTrue(emptyRecord.isConsistent());
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

        String[] valuesWithoutMapping = new String[0];
        CSVRecord recordWithoutMapping = new CSVRecord(valuesWithoutMapping, null, null, 1);
        assertFalse(recordWithoutMapping.isMapped("Name"));
        assertFalse(recordWithoutMapping.isMapped("Age"));
        assertFalse(recordWithoutMapping.isMapped("City"));
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

        String[] valuesWithoutValue = new String[2][];
        valuesWithoutValue[0] = new String[]{"Name", "Age"};
        mapping.put("City", 2);
        CSVRecord recordWithoutCityValue = new CSVRecord(valuesWithoutValue, mapping, null, 1);
        assertFalse(recordWithoutCityValue.isSet("City"));

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertFalse(emptyRecord.isSet("Name"));
        assertFalse(emptyRecord.isSet("Age"));
        assertFalse(emptyRecord.isSet("City"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedList = Arrays.asList(values);
        Iterator<String> expectedIterator = expectedList.iterator();

        Iterator<String> actualIterator = record.iterator();

        assertEquals(expectedIterator.next(), actualIterator.next());
        assertEquals(expectedIterator.next(), actualIterator.next());
        assertEquals(expectedIterator.next(), actualIterator.next());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, String> outputMap = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "Name");
        expectedMap.put("Age", "Age");
        expectedMap.put("City", "City");

        record.putIn(outputMap);

        assertEquals(expectedMap, outputMap);

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        Map<String, String> emptyOutputMap = new HashMap<>();
        emptyRecord.putIn(emptyOutputMap);
        assertEquals(emptyOutputMap, emptyOutputMap);
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

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertEquals(0, emptyRecord.size());
    }

    @Test
    public void testToList() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedList = Arrays.asList(values);
        List<String> actualList = record.toList();

        assertEquals(expectedList, actualList);

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        List<String> emptyList = emptyRecord.toList();
        assertEquals(Arrays.asList(emptyValues), emptyList);
    }

    @Test
    public void testToMap() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "Name");
        expectedMap.put("Age", "Age");
        expectedMap.put("City", "City");

        Map<String, String> actualMap = record.toMap();

        assertEquals(expectedMap, actualMap);

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        Map<String, String> emptyMap = emptyRecord.toMap();
        assertEquals(emptyMap, emptyMap);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expectedString = "[Name, Age, City]";
        String actualString = record.toString();

        assertEquals(expectedString, actualString);

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        String emptyString = emptyRecord.toString();
        assertEquals("[]", emptyString);
    }
}