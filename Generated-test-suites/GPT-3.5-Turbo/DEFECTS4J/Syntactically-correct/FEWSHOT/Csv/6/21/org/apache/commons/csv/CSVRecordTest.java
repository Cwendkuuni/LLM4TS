package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    
    private CSVRecord csvRecord;
    
    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.COLUMN1));
        assertEquals("value2", csvRecord.get(TestEnum.COLUMN2));
        assertEquals("value3", csvRecord.get(TestEnum.COLUMN3));
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }
    
    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 0);
        record.get("column1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        csvRecord.get("column");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        csvRecord.get("column3");
        csvRecord.get("column4");
    }
    
    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertFalse(inconsistentRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
        assertFalse(csvRecord.isMapped("column4"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("column4"));
    }
    
    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }
    
    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
    
    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }
    
    private enum TestEnum {
        COLUMN1, COLUMN2, COLUMN3
    }

}