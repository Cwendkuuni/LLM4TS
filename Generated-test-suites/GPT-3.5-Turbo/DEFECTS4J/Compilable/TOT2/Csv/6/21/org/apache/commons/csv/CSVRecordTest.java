package org.apache.commons.csv;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        String[] values = {"valueA", "valueB"};
        csvRecord = new CSVRecord(values, mapping, "This is a comment", 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("valueA", csvRecord.get(TestEnum.A));
        assertEquals("valueB", csvRecord.get(TestEnum.B));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("valueA", csvRecord.get(0));
        assertEquals("valueB", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("valueA", csvRecord.get("A"));
        assertEquals("valueB", csvRecord.get("B"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, "", 1);
        record.get("A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        csvRecord.get("C");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        csvRecord.get(2);
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

        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"valueA"}, new HashMap<>(), "", 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("A"));
        assertTrue(csvRecord.isMapped("B"));
        assertFalse(csvRecord.isMapped("C"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("A"));
        assertTrue(csvRecord.isSet("B"));
        assertFalse(csvRecord.isSet("C"));
    }

    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(2, count);
    }

    enum TestEnum {
        A, B
    }
}