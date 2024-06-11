package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private String[] values = new String[]{"foo", "bar", "baz"};
    private Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1, "name3", 2);
    private long recordNumber = 1;

    @Before
    public void setup() {
        record = new CSVRecord(values, mapping, null, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        for (int i = 0; i < record.size(); i++) {
            assertEquals(record.get(i), values[i]);
        }
    }

    @Test
    public void testGetByName() {
        assertEquals("foo", record.get("name1"));
        assertEquals("bar", record.get("name2"));
        assertEquals("baz", record.get("name3"));
        assertNull(record.get("unknown"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("unknown"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        for (int i = 0; i < record.size(); i++) {
            assertSame(values[i], iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(values.length, record.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        record = new CSVRecord(values, null, null, recordNumber);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByIndexOutOfRange() {
        int index = values.length + 1;
        assertThrows(IllegalArgumentException.class, () -> record.get(index));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        record = new CSVRecord(values, mapping, null, recordNumber);
        record.get("invalid");
    }
}