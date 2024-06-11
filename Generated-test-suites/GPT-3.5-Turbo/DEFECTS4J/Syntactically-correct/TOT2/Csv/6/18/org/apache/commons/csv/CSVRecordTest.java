package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get("column1"));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetName() {
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoHeaderMapping() {
        CSVRecord record = new CSVRecord(new String[]{}, null, "", 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidName() {
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidIndex() {
        csvRecord.get("column1");
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
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column2"));
        assertFalse(csvRecord.isSet("column3"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }
}