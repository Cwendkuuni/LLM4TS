package org.apache.commons.csv;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    private CSVRecord record;
    
    @Before
    public void setUp() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("Value1", record.get("Column1"));
        assertEquals("Value2", record.get("Column2"));
        assertEquals("Value3", record.get("Column3"));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
        assertEquals("Value3", record.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord newRecord = new CSVRecord(record.values(), null, record.getComment(), record.getRecordNumber());
        newRecord.get("Column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        record.get("InvalidColumn");
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
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithEmptyMapping() {
        CSVRecord newRecord = new CSVRecord(record.values(), new HashMap<>(), record.getComment(), record.getRecordNumber());
        assertFalse(newRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("Column1"));
        assertTrue(record.isMapped("Column2"));
        assertTrue(record.isMapped("Column3"));
    }

    @Test
    public void testIsNotMapped() {
        assertFalse(record.isMapped("InvalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("Column1"));
        assertTrue(record.isSet("Column2"));
        assertTrue(record.isSet("Column3"));
    }

    @Test
    public void testIsNotSet() {
        assertFalse(record.isSet("InvalidColumn"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertEquals("Value1", iterator.next());
        assertEquals("Value2", iterator.next());
        assertEquals("Value3", iterator.next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("Value1", map.get("Column1"));
        assertEquals("Value2", map.get("Column2"));
        assertEquals("Value3", map.get("Column3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        List<String> values = record.toList();
        assertEquals("Value1", values.get(0));
        assertEquals("Value2", values.get(1));
        assertEquals("Value3", values.get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("Value1", map.get("Column1"));
        assertEquals("Value2", map.get("Column2"));
        assertEquals("Value3", map.get("Column3"));
    }

    @Test
    public void testToString() {
        assertEquals("[Value1, Value2, Value3]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertEquals("Value1", values[0]);
        assertEquals("Value2", values[1]);
        assertEquals("Value3", values[2]);
    }
}