package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        String value = record.get(TestEnum.COL1);
        assertEquals("value1", value);
    }

    @Test
    public void testGetByIndex() {
        String value = record.get(0);
        assertEquals("value1", value);
    }

    @Test
    public void testGetByName() {
        String value = record.get("col1");
        assertEquals("value1", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByInvalidNameWithoutMapping() {
        record = new CSVRecord(null, null, null, 0);
        record.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidNameWithMapping() {
        record.get("invalidcol");
    }

    @Test
    public void testGetComment() {
        String comment = record.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent() {
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistentWithInvalidRecord() {
        record = new CSVRecord(new String[]{"value1"}, null, null, 0);
        boolean isConsistent = record.isConsistent();
        assertFalse(isConsistent);
    }

    @Test
    public void testIsMappedWithMappedName() {
        boolean isMapped = record.isMapped("col1");
        assertTrue(isMapped);
    }

    @Test
    public void testIsMappedWithUnmappedName() {
        boolean isMapped = record.isMapped("invalidcol");
        assertFalse(isMapped);
    }

    @Test
    public void testIsSetWithSetMappedName() {
        boolean isSet = record.isSet("col1");
        assertTrue(isSet);
    }

    @Test
    public void testIsSetWithUnsetMappedName() {
        boolean isSet = record.isSet("col2");
        assertFalse(isSet);
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testSize() {
        int size = record.size();
        assertEquals(3, size);
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testToString() {
        String str = record.toString();
        assertEquals("[value1, value2, value3]", str);
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertNotNull(values);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    private enum TestEnum {
        COL1,
        COL2,
        COL3
    }
}