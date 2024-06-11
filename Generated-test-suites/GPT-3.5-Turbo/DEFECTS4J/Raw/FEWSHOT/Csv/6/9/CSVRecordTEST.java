import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));

        try {
            record.get(3);
            fail("Expected IndexOutOfBoundsException was not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, mapping);
        assertEquals("value1", record.get("col1"));
        assertEquals("value2", record.get("col2"));
        assertEquals("value3", record.get("col3"));

        try {
            record.get("col4");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetByEnum() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("COL1", 0);
        mapping.put("COL2", 1);
        mapping.put("COL3", 2);

        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, mapping);

        assertEquals("value1", record.get(TestEnum.COL1));
        assertEquals("value2", record.get(TestEnum.COL2));
        assertEquals("value3", record.get(TestEnum.COL3));

        try {
            record.get(TestEnum.COL4);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecord(null, null);
        assertNull(record.getComment());

        record = createCSVRecord(null, null);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecord(null, null);
        assertEquals(0, record.getRecordNumber());

        record = createCSVRecord(null, null);
        assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Mapping not provided
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        assertTrue(record.isConsistent());

        // Mapping provided and consistent
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        record = createCSVRecord(new String[] {"value1", "value2", "value3"}, mapping);
        assertTrue(record.isConsistent());

        // Mapping provided and inconsistent
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        record = createCSVRecord(new String[] {"value1", "value2"}, mapping);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Mapping not provided
        CSVRecord record = createCSVRecord(null, null);
        assertFalse(record.isMapped("col1"));

        // Mapping provided and column mapped
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        record = createCSVRecord(null, mapping);
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));

        // Mapping provided and column not mapped
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        record = createCSVRecord(null, mapping);
        assertFalse(record.isMapped("col3"));
        assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        // Mapping not provided
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        assertFalse(record.isSet("col1"));

        // Mapping provided and column mapped and value available
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        record = createCSVRecord(new String[] {"value1", "value2", "value3"}, mapping);
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));

        // Mapping provided and column mapped and value not available
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        record = createCSVRecord(new String[] {"value1", "value2"}, mapping);
        assertFalse(record.isSet("col3"));
        assertFalse(record.isSet("col4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        int i = 0;
        for (String value : record) {
            assertEquals("value" + (i + 1), value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        record.putIn(map);

        assertEquals("value1", map.get("0"));
        assertEquals("value2", map.get("1"));
        assertEquals("value3", map.get("2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        assertEquals(Arrays.asList("value1", "value2", "value3"), record.toList());
    }

    @Test
    public void testToMap() {
        CSVRecord record = createCSVRecord(new String[] {"value1", "value2", "value3"}, null);
        Map<String, String> map = record.toMap();

        assertEquals("value1", map.get("0"));
        assertEquals("value2", map.get("1"));
        assertEquals("value3", map.get("2"));
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping) {
        return new CSVRecord(values, mapping, "comment", 10);
    }

    private enum TestEnum {
        COL1, COL2, COL3, COL4;
    }

}