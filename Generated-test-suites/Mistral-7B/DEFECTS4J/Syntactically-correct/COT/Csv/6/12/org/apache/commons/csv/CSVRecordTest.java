package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVRecord
 *
 * @author your.name
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecord_Basic() {
        String[] values = new String[]{"Alice", "25", "female", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("Gender", 2);
            put("City", 3);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals("Alice", record.get(0));
        assertEquals("25", record.get(1));
        assertEquals("female", record.get(2));
        assertEquals("New York", record.get(3));
        assertEquals(null, record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));
        assertTrue(record.isSet("City"));

        List<String> valuesList = record.toList();
        assertEquals("Alice,25,female,New York", valuesList.toString());

        Map<String, String> valuesMap = record.toMap();
        assertEquals("Name=Alice, Age=25, Gender=female, City=New York", valuesMap.toString());
    }

    @Test
    public void testCSVRecord_NullMapping() {
        String[] values = new String[]{"Alice", "25", "female", "New York"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Age"));
        assertFalse(record.isMapped("Gender"));
        assertFalse(record.isMapped("City"));
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("Gender"));
        assertFalse(record.isSet("City"));

        try {
            record.get("Name");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            record.get(5);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }

        try {
            record.isConsistent();
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testCSVRecord_EmptyMapping() {
        String[] values = new String[]{"Alice", "25", "female", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Age"));
        assertFalse(record.isMapped("Gender"));
        assertFalse(record.isMapped("City"));
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("Gender"));
        assertFalse(record.isSet("City"));

        try {
            record.get("Name");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            record.get(5);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }

        try {
            record.isConsistent();
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testCSVRecord_EmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("Gender", 2);
            put("City", 3);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Age"));
        assertFalse(record.isMapped("Gender"));
        assertFalse(record.isMapped("City"));
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("Gender"));
        assertFalse(record.isSet("City"));

        try {
            record.get("Name");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            record.get(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }

        try {
            record.isConsistent();
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }
}