
package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        record.get(3);
    }
    
    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertNull(record.get(null));
        assertNull(record.get("d"));
        assertNull(record.get("e"));
        assertNull(record.get("f"));
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertTrue(record.isConsistent());
        
        record = new CSVRecord(new String[] {"a", "b", "c", "d"}, null, null, 1);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertFalse(record.isMapped(null));
        assertFalse(record.isMapped("d"));
        assertFalse(record.isMapped("e"));
        assertFalse(record.isMapped("f"));
    }
    
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertFalse(record.isSet(null));
        assertFalse(record.isSet("d"));
        assertFalse(record.isSet("e"));
        assertFalse(record.isSet("f"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertNotNull(record.iterator());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        String[] values = record.values();
        assertArrayEquals(new String[] {"a", "b", "c"}, values);
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, "This is a comment.", 1);
        assertEquals("This is a comment.", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 1);
        assertEquals("[a, b, c]", record.toString());
    }
}
