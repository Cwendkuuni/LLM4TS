package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordTest;
import org.junit.Test;

/**
 * Test class for {@link CSVRecord}.
 *
 * @author Vladimir Dinev
 * @version $Id$
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecordEmpty() {
        final CSVRecord record = new CSVRecord(null, null, null, 0);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Header"));
        assertFalse(record.isSet("Header"));
        assertSame(Arrays.asList(new String[] {}), record.toList());
        assertNull(record.getComment());
        assertNull(record.get("Header"));
        assertNull(record.get(0));
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecordBasic() {
        final String[] values = new String[] { "Header1", "Header2", "Value1", "Value2" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Header1", 0);
                put("Header2", 1);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Header1"));
        assertTrue(record.isSet("Header1"));
        assertTrue(record.isSet("Header2"));
        assertFalse(record.isMapped("NonExistentHeader"));
        assertFalse(record.isSet("NonExistentHeader"));
        assertSame("Value1", record.get("Header1"));
        assertSame("Value2", record.get("Header2"));
        assertNull(record.get("NonExistentHeader"));
        assertSame(1, record.getRecordNumber());
        assertNull(record.getComment());
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testCSVRecordGetNullString() {
        final String[] values = new String[] { "Value1", null, "Value3" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Header1", 0);
                put("Header2", 1);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Header1"));
        assertTrue(record.isSet("Header1"));
        assertTrue(record.isSet("Header2"));
        assertFalse(record.isMapped("NonExistentHeader"));
        assertFalse(record.isSet("NonExistentHeader"));
        assertSame("Value1", record.get("Header1"));
        assertNull(record.get("Header2"));
        assertNull(record.get("NonExistentHeader"));
        assertSame(1, record.getRecordNumber());
        assertNull(record.getComment());
        assertArrayEquals(values, record.values());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        final String[] values = new String[] { "Value1", "Value2", "Value3" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        fail("NoMappingException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInvalidMapping() {
        final String[] values = new String[] { "Value1", "Value2", "Value3" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Header1", 1);
                put("Header2", 0);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        fail("InvalidMappingException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInconsistentRecord() {
        final String[] values = new String[] { "Value1", "Value2", "Value3" };
        final Map<String, Integer> mapping = new HashMap<String, Integer>() {
            {
                put("Header1", 0);
                put("Header2", 1);
                put("Header3", 1);
            }
        };
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        fail("InconsistentRecordException expected");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCSVRecordAccessOutOfBounds() {
        final String[] values = new String[] { "Value1", "Value2" };
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get(2);
        fail("ArrayIndexOutOfBoundsException expected");
    }

}