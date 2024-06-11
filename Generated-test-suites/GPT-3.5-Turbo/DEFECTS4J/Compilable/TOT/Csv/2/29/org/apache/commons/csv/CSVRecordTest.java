package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGet_ByIndex() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
        assertEquals("Value3", record.get(2));
    }

    @Test
    public void testGet_ByName() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("Value1", record.get("Column1"));
        assertEquals("Value2", record.get("Column2"));
        assertEquals("Value3", record.get("Column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_ByName_HeaderMappingNotProvided() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        record.get("Column1");
    }

    @Test
    public void testGet_ByName_ColumnNameNotFound() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertNull(record.get("Column4"));
    }

    @Test
    public void testIsConsistent_MatchingRecordSizeToHeaderSize() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_UnmatchedRecordSizeToHeaderSize() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistent_NullHeaderMapping() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped_MappedColumn() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isMapped("Column1"));
    }

    @Test
    public void testIsMapped_UnmappedColumn() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertFalse(record.isMapped("Column4"));
    }

    @Test
    public void testIsSet_MappedColumnWithValue() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isSet("Column1"));
    }

    @Test
    public void testIsSet_MappedColumnWithoutValue() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertFalse(record.isSet("Column2"));
    }

    @Test
    public void testIsSet_UnmappedColumn() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String,Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertFalse(record.isSet("Column4"));
    }

    @Test
    public void testIterator() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        int count = 0;
        for (String value : record) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testValues() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment_NoComment() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertNull(record.getComment());
    }

    @Test
    public void testGetComment_WithComment() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 0);

        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 5);

        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertEquals("[Value1, Value2, Value3]", record.toString());
    }

}
