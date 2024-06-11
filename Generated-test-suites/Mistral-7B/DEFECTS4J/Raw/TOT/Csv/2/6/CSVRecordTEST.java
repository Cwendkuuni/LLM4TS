package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Tests constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        final String[] values = new String[] { "first", "second", "third" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(1, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Unknown"));
        assertFalse(record.isSet("Unknown"));
        assertNull(record.get("Unknown"));
        assertSame("first", record.get(0));
        assertSame("second", record.get(1));
        assertSame("third", record.get(2));
        assertSame("second", record.get("Age"));
        assertSame("third", record.get("City"));
    }

    /**
     * Tests get method with invalid index.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        final String[] values = new String[] { "first", "second", "third" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get(values.length);
    }

    /**
     * Tests get method with invalid name.
     */
    @org.junit.Test
    public void testGetInvalidName() {
        final String[] values = new String[] { "first", "second", "third" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNull(record.get("Unknown"));
    }

    /**
     * Tests get method with null mapping.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetNullMapping() {
        final String[] values = new String[] { "first", "second", "third" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    /**
     * Tests get method with inconsistent mapping and values.
     */
    @org.junit.Test
    public void testGetInconsistent() {
        final String[] values = new String[] { "first", "second", "third" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 3);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNull(record.get("Age"));
    }

    /**
     * Tests isConsistent method with valid mapping and values.
     */
    @org.junit.Test
    public void testIsConsistentValid() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    /**
     * Tests isConsistent method with invalid mapping and values.
     */
    @org.junit.Test
    public void testIsConsistentInvalid() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("City", 1);
                put("Age", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    /**
     * Tests isMapped method with valid mapping.
     */
    @org.junit.Test
    public void testIsMappedValid() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Unknown"));
    }

    /**
     * Tests isSet method with valid mapping and values.
     */
    @org.junit.Test
    public void testIsSetValid() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Unknown"));
    }

    /**
     * Tests iterator method.
     */
    @org.junit.Test
    public void testIterator() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        final Iterator<String> it = record.iterator();
        assertEquals(values[0], it.next());
        assertEquals(values[1], it.next());
        assertEquals(values[2], it.next());
        assertFalse(it.hasNext());
    }

    /**
     * Tests values method.
     */
    @org.junit.Test
    public void testValues() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());
    }

    /**
     * Tests getComment method with valid comment.
     */
    @org.junit.Test
    public void testGetCommentValid() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York,Comment:Hello" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, "Hello", 1);
        assertEquals("Hello", record.getComment());
    }

    /**
     * Tests getComment method with no comment.
     */
    @org.junit.Test
    public void testGetCommentNoComment() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNull(record.getComment());
    }

    /**
     * Tests getRecordNumber method.
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Tests size method.
     */
    @org.junit.Test
    public void testSize() {
        final String[] values = new String[] { "Name:first,Age:30,City:New York" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Name", 0);
                put("Age", 1);
                put("City", 2);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(3, record.size());
    }
}