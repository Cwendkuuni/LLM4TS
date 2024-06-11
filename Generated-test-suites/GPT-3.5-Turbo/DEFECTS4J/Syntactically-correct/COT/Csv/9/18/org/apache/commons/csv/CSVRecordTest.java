import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.TEST1));
        assertEquals("value2", record.get(TestEnum.TEST2));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1"}, null, null, 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithMissingMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(new String[] {"value1"}, mapping, null, 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1"}, mapping, null, 1);
        record.get("name1");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {}, null, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] {}, null, null, 10);
        assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndMissingColumn() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1"}, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingAndExtraColumn() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsMappedWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSetWithMappingAndValue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIsSetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals(Arrays.asList("value1", "value2"), record.toList());
    }

    @Test
    public void testPutIn() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "value1");
        expectedMap.put("name2", "value2");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        Map<String, String> actualMap = record.putIn(new HashMap<>());

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals(new String[] {"value1", "value2"}, record.values());
    }

}