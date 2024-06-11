package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private Map<String, Integer> mapping;
    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        String[] values = {"John", "25"};
        csvRecord = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("John", csvRecord.get(CSVColumns.NAME));
        assertEquals("25", csvRecord.get(CSVColumns.AGE));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("John", csvRecord.get(0));
        assertEquals("25", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("John", csvRecord.get("Name"));
        assertEquals("25", csvRecord.get("Age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByInvalidNameNoMapping() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(new String[]{"John", "25"}, null, null, 1);
        csvRecordWithoutMapping.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidNameInvalidMapping() {
        csvRecord.get("InvalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        csvRecord.get(2);
    }

    @Test
    public void testGetComment() {
        assertNull(csvRecord.getComment());
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
        assertTrue(csvRecord.isMapped("Name"));
        assertTrue(csvRecord.isMapped("Age"));
    }

    @Test
    public void testIsSetMappedAndHasValue() {
        assertTrue(csvRecord.isSet("Name"));
        assertTrue(csvRecord.isSet("Age"));
    }

    @Test
    public void testIsSetMappedButHasNoValue() {
        CSVRecord emptyCsvRecord = new CSVRecord(new String[0], mapping, null, 1);
        assertFalse(emptyCsvRecord.isSet("Age"));

        CSVRecord incompleteCsvRecord = new CSVRecord(new String[]{"John"}, mapping, null, 1);
        assertFalse(incompleteCsvRecord.isSet("Age"));
    }

    @Test
    public void testToString() {
        assertEquals("[John, 25]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        String[] expectedValues = {"John", "25"};
        assertArrayEquals(expectedValues, csvRecord.values());
    }

    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals("John", map.get("Name"));
        assertEquals("25", map.get("Age"));
    }

    @Test
    public void testToList() {
        assertEquals(2, csvRecord.toList().size());
        assertEquals("John", csvRecord.toList().get(0));
        assertEquals("25", csvRecord.toList().get(1));
    }

    @Test
    public void testToMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Age", "25");
        
        Map<String, String> actualMap = csvRecord.toMap();
        
        assertEquals(expectedMap, actualMap);
    }

    enum CSVColumns {
        NAME,
        AGE
    }

}