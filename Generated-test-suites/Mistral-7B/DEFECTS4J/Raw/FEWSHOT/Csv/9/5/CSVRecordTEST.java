package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_Empty() {
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 0;
        String[] values = CSVRecord.EMPTY_STRING_ARRAY;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecord_Simple() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(2, record.size());
        assertTrue(record.isConsistent());
        assertEquals("A simple comment", record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertNull(record.get("col3"));
    }

    @Test
    public void testCSVRecord_GetByEnum() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", record.get(Enum.ONE));
        assertEquals("value2", record.get(Enum.TWO));

        assertThrows(NullPointerException.class, () -> record.get(null));
        assertThrows(IllegalArgumentException.class, () -> record.get(Enum.THREE));
    }

    @Test
    public void testCSVRecord_GetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", record.get("col1"));
        assertEquals("value2", record.get("col2"));
        assertEquals("value3", record.get("col3"));
        assertNull(record.get("col4"));
        assertThrows(IllegalArgumentException.class, () -> record.get("col5"));
    }

    @Test
    public void testCSVRecord_GetByIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
        assertNull(record.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
    }

    @Test
    public void testCSVRecord_GetByInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(values.length + 1));
    }

    @Test
    public void testCSVRecord_GetComment() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("A simple comment", record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testCSVRecord_GetRecordNumber() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(1, record.getRecordNumber());
        assertThrows(IllegalStateException.class, () -> record.getRecordNumber());
    }

    @Test
    public void testCSVRecord_IsConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isConsistent());
        mapping = null;
        record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecord_IsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
        assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testCSVRecord_IsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertFalse(record.isSet("col3"));
        assertFalse(record.isSet("col4"));
    }

    @Test
    public void testCSVRecord_Iterator() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Iterator<String> iterator = record.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
    }

    @Test
    public void testCSVRecord_PutInMap() {
        String[] values = new String[] {"value1", "value2", "value3"};

        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);

        record.putIn(map);

        assertEquals(values[0], map.get("value1"));
        assertEquals(values[1], map.get("value2"));
        assertEquals(values[2], map.get("value3"));
        assertNull(map.get("value4"));
    }

    @Test
    public void testCSVRecord_ToMap() {
        String[] values = new String[] {"value1", "value2", "value3"};

        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);

        map = record.toMap();

        assertEquals(values[0], map.get("value1"));
        assertEquals(values[1], map.get("value2"));
        assertEquals(values[2], map.get("value3"));
        assertNull(map.get("value4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_ToMap_InvalidMapping() {
        String[] values = new String[] {"value1", "value2", "value3"};

        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);

        map = record.toMap();
        map.put("value4", "value4");
    }

    @Test
    public void testCSVRecord_ToString() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        String comment = "A simple comment";
        long recordNumber = 1;
        String[] values = new String[] {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("[value1, value2, value3]", record.toString());
    }

    enum Enum {
        ONE(0), TWO(1);

        private final int value;

        Enum(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

}