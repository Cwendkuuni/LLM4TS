package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CSVRecord}.
 */
public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
    }

    @Test
    public void testGetValueByIndex() {
        String value = record.get(0);
        assertTrue("Value by index should be 'value1'", value.equals("value1"));
    }

    @Test
    public void testGetValueByName() {
        String value = record.get("name1");
        assertTrue("Value by name should be 'value1'", value.equals("value1"));

        value = record.get("name2");
        assertTrue("Value by name should be 'value2'", value.equals("value2"));

        value = record.get("nonExistentName");
        assertNull("Value by non-existent name should be null", value);
    }

    @Test
    public void testIsConsistent() {
        assertTrue("Record with matching size to header size should be consistent", record.isConsistent());

        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        assertFalse("Record with different size to header size should not be consistent", record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue("Column 'name1' should be mapped", record.isMapped("name1"));
        assertFalse("Column 'nonExistentName' should not be mapped", record.isMapped("nonExistentName"));
    }

    @Test
    public void testIsSet() {
        assertTrue("Column 'name1' should be set", record.isSet("name1"));
        assertFalse("Column 'nonExistentName' should not be set", record.isSet("nonExistentName"));

        record = new CSVRecord(new String[]{"value1", null}, mapping, null, 0);
        assertFalse("Column with null value is not set", record.isSet("name1"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iter = record.iterator();
        String value = iter.next();
        assertTrue("Value from iterator should be 'value1'", value.equals("value1"));

        value = iter.next();
        assertTrue("Value from iterator should be 'value2'", value.equals("value2"));

        assertFalse("Iterator should have no more values", iter.hasNext());
    }

    @Test
    public void testValues() {
        assertArrayEquals("Values array should contain 'value1' and 'value2'", new String[]{"value1", "value2"}, record
                .values());
    }

    @Test
    public void testComment() {
        String comment = "test comment";
        CSVRecord recordWithComment = new CSVRecord(new String[]{"value1", "value2"}, mapping, comment, 0);
        assertEquals("Comment for record with comment should be 'test comment'", comment, recordWithComment.getComment());

        recordWithComment = new CSVRecord(new String[]{"value1", "value2"}, null, comment, 0);
        assertNull("Comment for record without comment should be null", recordWithComment.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals("Record number for record with number 0 should be 0", 0, record.getRecordNumber());

        record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals("Record number for record with number 1 should be 1", 1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals("Size of record should be 2", 2, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("ToString should return [value1, value2]", "[value1, value2]", record.toString());
    }
}