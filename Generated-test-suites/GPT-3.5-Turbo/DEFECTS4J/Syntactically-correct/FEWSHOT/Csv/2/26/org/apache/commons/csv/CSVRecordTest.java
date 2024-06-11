package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[] { "value1", "value2", "value3" };
        mapping = mock(Map.class);
        comment = "This is a comment";
        recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        when(mapping.get("name1")).thenReturn(0);
        when(mapping.get("name2")).thenReturn(1);
        when(mapping.get("name3")).thenReturn(2);
        
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        record.get("name");
    }

    @Test
    public void testGetByNameNotFound() {
        when(mapping.get("name")).thenReturn(null);
        
        assertNull(record.get("name"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingSizeMismatch() {
        when(mapping.size()).thenReturn(4);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        record = new CSVRecord(values, null, comment, recordNumber);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        when(mapping.containsKey("name")).thenReturn(true);
        
        assertTrue(record.isMapped("name"));
    }

    @Test
    public void testIsMappedWithoutMapping() {
        assertFalse(record.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        when(mapping.containsKey("name")).thenReturn(true);
        when(mapping.get("name")).thenReturn(0);
        
        assertTrue(record.isSet("name"));
    }

    @Test
    public void testIsSetWithoutMapping() {
        assertFalse(record.isSet("name"));
    }

    @Test
    public void testIsSetWithMappingIndexOutOfRange() {
        when(mapping.containsKey("name")).thenReturn(true);
        when(mapping.get("name")).thenReturn(3);
        
        assertFalse(record.isSet("name"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        String[] recordValues = record.values();
        
        assertEquals(3, recordValues.length);
        assertEquals("value1", recordValues[0]);
        assertEquals("value2", recordValues[1]);
        assertEquals("value3", recordValues[2]);
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

}