package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Tests for {@link CSVRecord} class.
 */
public class CSVRecordTest {

    /**
     * Test for constructor.
     */
    @Test
    public void testConstructor() {
        final String[] values = new String[] { "John Doe", "35", "Male" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("Gender", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());
        assertArrayEquals(mapping.values().toArray(), record.mapping().values().toArray());
        assertNull(record.getComment());
        assertTrue(record.getRecordNumber() == 1);
    }

    /**
     * Test for get(int) method.
     */
    @Test
    public void testGetInt() {
        final String[] values = new String[] { "John Doe", "35", "Male" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        assertArrayEquals("John Doe", record.get(0));
        assertArrayEquals("35", record.get(1));
        assertArrayEquals("Male", record.get(2));
    }

    /**
     * Test for get(String) method.
     */
    @Test
    public void testGetString() {
        final String[] values = new String[] { "Name:John Doe,Age:35,Gender:Male" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("Gender", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals("John Doe", record.get("Name"));
        assertArrayEquals("35", record.get("Age"));
        assertNull(record.get("InvalidColumnName"));
    }

    /**
     * Test for isConsistent() method.
     */
    @Test
    public void testIsConsistent() {
        final String[] values = new String[] { "Name:John Doe,Age:35,Gender:Male" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("Gender", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        final String[] valuesInconsistent = new String[] { "Name:John Doe,Age:35" };
        final CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, null, null, 1);
        assertFalse(recordInconsistent.isConsistent());
    }

    /**
     * Test for isMapped() method.
     */
    @Test
    public void testIsMapped() {
        final String[] values = new String[] { "Name:John Doe,Age:35,Gender:Male" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("Gender", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertFalse(record.isMapped("InvalidColumnName"));

        final Map<String, Integer> mappingEmpty = new HashMap<String, Integer>();
        final CSVRecord recordEmptyMapping = new CSVRecord(values, mappingEmpty, null, 1);
        assertFalse(recordEmptyMapping.isMapped("Name"));
    }

    /**
     * Test for isSet() method.
     */
    @Test
    public void testIsSet() {
        final String[] values = new String[] { "Name:John Doe,Age:35,Gender:Male" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("Gender", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));
        assertFalse(record.isSet("InvalidColumnName"));

        final String[] valuesEmpty = new String[0];
        final CSVRecord recordEmpty = new CSVRecord(valuesEmpty, null, null, 1);
        assertFalse(recordEmpty.isSet("Name"));
        assertFalse(recordEmpty.isSet("Age"));
        assertFalse(recordEmpty.isSet("Gender"));
    }

    /**
     * Test for iterator() method.
     */
    @Test
    public void testIterator() {
        final String[] values = new String[] { "John Doe", "35", "Male" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        final List<String> expected = Arrays.asList(values);
        final Iterator<String> iterator = record.iterator();
        for (int i = 0; i < values.length; i++) {
            assertArrayEquals(record.get(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test for values() method.
     */
    @Test
    public void testValues() {
        final String[] values = new String[] { "John Doe", "35", "Male" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        assertArrayEquals(values, record.values());
    }

    /**
     * Test for getComment() method.
     */
    @Test
    public void testGetComment() {
        final String[] values = new String[] { "Name:John Doe,Age:35,Gender:Male# This is a comment" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("Gender", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals("# This is a comment", record.getComment());
    }

    /**
     * Test for getRecordNumber() method.
     */
    @Test
    public void testGetRecordNumber() {
        final String[] values = new String[] { "Name:John Doe,Age:35,Gender:Male" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("Gender", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.getRecordNumber() == 1);
    }

    /**
     * Test for size() method.
     */
    @Test
    public void testSize() {
        final String[] values = new String[] { "Name:John Doe,Age:35,Gender:Male" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        assertTrue(record.size() == values.length);
    }
}