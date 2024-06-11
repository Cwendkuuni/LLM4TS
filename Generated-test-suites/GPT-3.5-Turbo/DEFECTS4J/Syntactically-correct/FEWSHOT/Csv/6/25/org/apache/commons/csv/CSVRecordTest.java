package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

public class CSVRecordTest {

    private static CSVRecord record;

    @BeforeClass
    public static void setUp() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetWithEnum() {
        assertEquals("value1", record.get(TestEnum.ONE));
        assertEquals("value2", record.get(TestEnum.TWO));
    }

    @Test
    public void testGetWithIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        recordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNameNotFound() {
        record.get("column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNameOutOfBounds() {
        record.get("column2");
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

        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testSize() {
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        List<String> list = record.toList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    private enum TestEnum {
        ONE, TWO
    }
}