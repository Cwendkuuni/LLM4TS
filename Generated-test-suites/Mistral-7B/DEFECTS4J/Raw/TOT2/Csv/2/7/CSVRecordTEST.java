package org.apache.commons.csv.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expected = "Doe";
        String actual = record.get(1);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentData() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1); // Mapping size != Values size
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.isConsistent();
    }

    @Test
    public void testNonExistentColumn() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expected = null;
        String actual = record.get("Gender");

        assertEquals(expected, actual);
    }

    @Test
    public void testOutOfBoundsIndex() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expected = null;
        String actual = record.get(3);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "Comment: This is a test record"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expected = "This is a test record";
        String actual = record.getComment();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMapping() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(null, mapping, null, 1);
        record.get("Name");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValuesComment() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.getComment();
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean expected = true;
        boolean actual = record.isMapped("Name");

        assertEquals(expected, actual);
    }

    @Test
    public void testIsMappedFalse() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean expected = false;
        boolean actual = record.isMapped("Gender");

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean expected = true;
        boolean actual = record.isSet("Name");

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSetFalse() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean expected = false;
        boolean actual = record.isSet("Gender");

        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        int expected = 4;
        int actual = record.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expected = "[Name, John Doe, Age, 30]";
        String actual = record.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        long expected = 1;
        long actual = record.getRecordNumber();

        assertEquals(expected, actual);
    }
}