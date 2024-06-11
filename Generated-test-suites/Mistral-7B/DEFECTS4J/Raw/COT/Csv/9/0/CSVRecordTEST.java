package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    @org.junit.Test
    public void testCSVRecord_Constructor() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, csvRecord.getRecordNumber());
        assertEquals(2, csvRecord.size());
        assertEquals("John", csvRecord.get(0));
        assertEquals("Doe", csvRecord.get(1));
        assertEquals("30", csvRecord.get(2));
        assertNull(csvRecord.getComment());
        assertTrue(csvRecord.isConsistent());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_Get_NullMapping() {
        String[] values = new String[]{"John", "Doe", "30"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        csvRecord.get("Name");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_Get_InvalidIndex() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get(3);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_Get_InvalidName() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get("InvalidName");
    }

    @org.junit.Test
    public void testCSVRecord_Get_Enum() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", csvRecord.get(CSVRecord.class.getEnumConstants()[0]));
        assertEquals("Doe", csvRecord.get(CSVRecord.class.getEnumConstants()[1]));
        assertEquals("30", csvRecord.get(CSVRecord.class.getEnumConstants()[2]));
    }

    @org.junit.Test
    public void testCSVRecord_Get_Comment() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        String comment = "This is a comment";

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, 1);

        assertEquals("This is a comment", csvRecord.getComment());
    }

    @org.junit.Test
    public void testCSVRecord_Get_InvalidMappingIndex() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        try {
            csvRecord.get(30);
        } catch (IllegalArgumentException e) {
            assertEquals("Index for header '30' is 30 but CSVRecord only has 2 values!", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCSVRecord_PutIn() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Age", "30");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Map<String, String> actualMap = csvRecord.putIn(new HashMap<String, String>());

        assertEquals(expectedMap, actualMap);
    }

    @org.junit.Test
    public void testCSVRecord_IsMapped() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertTrue(csvRecord.isMapped("Name"));
        assertTrue(csvRecord.isMapped("Age"));
        assertFalse(csvRecord.isMapped("InvalidName"));
    }

    @org.junit.Test
    public void testCSVRecord_IsSet() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertTrue(csvRecord.isSet("Name"));
        assertTrue(csvRecord.isSet("Age"));
        assertFalse(csvRecord.isSet("InvalidName"));
    }

    @org.junit.Test
    public void testCSVRecord_ToString() {
        String[] values = new String[]{"John", "Doe", "30"};

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        assertEquals("[John, Doe, 30]", csvRecord.toString());
    }

    @org.junit.Test
    public void testCSVRecord_Values() {
        String[] values = new String[]{"John", "Doe", "30"};

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        assertArrayEquals(values, csvRecord.values());
    }

    @org.junit.Test
    public void testCSVRecord_Get_InvalidMappingName() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        try {
            csvRecord.get("InvalidName");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for InvalidName not found, expected one of Name, Age", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCSVRecord_IsConsistent() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertTrue(csvRecord.isConsistent());

        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 3);

        CSVRecord inconsistentRecord = new CSVRecord(values, mapping, null, 1);

        assertFalse(inconsistentRecord.isConsistent());
    }
}