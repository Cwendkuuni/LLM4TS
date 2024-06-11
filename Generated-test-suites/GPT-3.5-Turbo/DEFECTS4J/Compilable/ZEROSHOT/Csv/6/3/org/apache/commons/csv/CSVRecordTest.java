package org.apache.commons.csv;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(TestEnum.A));
        assertEquals("B", record.get(TestEnum.B));
        assertEquals("C", record.get(TestEnum.C));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertEquals("A", record.get("A"));
        assertEquals("B", record.get("B"));
        assertEquals("C", record.get("C"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get("A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        record.get("D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get(3);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertTrue(record.isConsistent());

        record = new CSVRecord(new String[]{"A", "B"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertTrue(record.isMapped("A"));
        assertTrue(record.isMapped("B"));
        assertTrue(record.isMapped("C"));
        assertFalse(record.isMapped("D"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, mapping, null, 1);
        assertTrue(record.isSet("A"));
        assertTrue(record.isSet("B"));
        assertTrue(record.isSet("C"));
        assertFalse(record.isSet("D"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        StringBuilder sb = new StringBuilder();
        for (String value : record) {
            sb.append(value);
        }
        assertEquals("ABC", sb.toString());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.putIn(map);
        assertEquals("A", map.get("A"));
        assertEquals("B", map.get("B"));
        assertEquals("C", map.get("C"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertArrayEquals(new String[]{"A", "B", "C"}, record.values());
    }

    private enum TestEnum {
        A, B, C
    }

}