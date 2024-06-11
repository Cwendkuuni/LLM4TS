package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_Empty() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertTrue(record.isEmpty());
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecord_Valuesonly() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(3, record.size());
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
        assertEquals("Value3", record.get(2));
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecord_ValuesMap() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Header1", 0);
            put("Header2", 1);
            put("Header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Value1", record.get("Header1"));
        assertEquals("Value2", record.get("Header2"));
        assertEquals("Value3", record.get("Header3"));
        assertEquals(null, record.get("Header4"));
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_ValuesMap_NoMapping() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("Header1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_ValuesMap_MismatchMapping() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Header1", 0);
            put("Header2", 3);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("Header1");
    }

    @Test
    public void testCSVRecord_ValuesMap_Comment() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Header1", 0);
            put("Header2", 1);
            put("Header3", 2);
        }};
        String comment = "Comment1";
        CSVRecord record = new CSVRecord(values, mapping, comment, 0);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testCSVRecord_ValuesMap_RecordNumber() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Header1", 0);
            put("Header2", 1);
            put("Header3", 2);
        }};
        long recordNumber = 1;
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testCSVRecord_ValuesMap_Consistent() {
        String[] values = {"Value1", "Value2", "Value3", "Value4"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Header1", 0);
            put("Header2", 1);
            put("Header3", 2);
            put("Header4", 3);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecord_ValuesMap_Inconsistent() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Header1", 0);
            put("Header2", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecord_ValuesMap_GetByEnum() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("Header1", 0);
            put("Header2", 1);
            put("Header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Value1", record.get(Enum.ONE));
        assertEquals("Value2", record.get(Enum.TWO));
        assertEquals("Value3", record.get(Enum.THREE));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_ValuesMap_NoMapping_Enum() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get(Enum.ONE);
    }

    @Test
    public void testCSVRecord_ValuesMap_PutIn() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, String> map = new HashMap<String, String>();
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.putIn(map);
        assertEquals("Value1", map.get("Value1"));
        assertEquals("Value2", map.get("Value2"));
        assertEquals("Value3", map.get("Value3"));
    }

    @Test
    public void testCSVRecord_ValuesMap_ToString() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("[Value1, Value2, Value3]", record.toString());
    }

    enum Enum {
        ONE, TWO, THREE
    }
}