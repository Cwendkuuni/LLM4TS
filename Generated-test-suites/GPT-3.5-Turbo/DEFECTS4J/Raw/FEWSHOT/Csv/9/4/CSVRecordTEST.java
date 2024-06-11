import org.junit.Test;
import org.apache.commons.csv.CSVRecord;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }
    
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        record.get("name3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        record.get("name2");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 0);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertTrue(record1.isConsistent());

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        assertTrue(record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        assertFalse(record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertFalse(record1.isMapped("name1"));

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        assertTrue(record2.isMapped("name1"));
        assertTrue(record2.isMapped("name2"));
        assertFalse(record2.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertFalse(record1.isSet("name1"));

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        assertTrue(record2.isSet("name1"));
        assertTrue(record2.isSet("name2"));
        assertFalse(record2.isSet("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "value1");
        expectedMap.put("name2", "value2");

        assertEquals(expectedMap, record.toMap());
    }

    private enum TestEnum {
        VALUE1, VALUE2
    } 
}