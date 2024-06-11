package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private Map<String, Integer> mapping;
    private String[] values;

    @Before
    public void setUp() {
        mapping = new HashMap<>();
        values = new String[]{"value1", "value2", "value3"};
        record = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetWithEnum() {
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetWithIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithName() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithName_NoMapping() {
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithName_InvalidName() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        record.get("column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithName_InvalidIndex() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        record.get("column2");
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
        
        mapping.put("column1", 0);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertFalse(record.isMapped("column1"));
        
        mapping.put("column1", 0);
        assertTrue(record.isMapped("column1"));
    }
    
    @Test
    public void testIsSet() {
        assertFalse(record.isSet("column1"));
        
        mapping.put("column1", 0);
        assertTrue(record.isSet("column1"));
    }
    
    @Test
    public void testIterator() {
        assertEquals("value1", record.iterator().next());
    }
    
    @Test
    public void testPutIn() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }
    
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }
}