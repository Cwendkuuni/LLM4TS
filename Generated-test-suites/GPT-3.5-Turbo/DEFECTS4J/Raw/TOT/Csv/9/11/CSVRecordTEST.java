import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    // Test case for get(Enum)
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertEquals("value1", record.get(TestEnum.A));
    }

    // Test case for get(int)
    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertEquals("value2", record.get(1));
    }

    // Test cases for get(String)

    // Test case for valid name
    @Test
    public void testGetValidName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        assertEquals("value1", record.get("name1"));
    }

    // Test case for name not found in mapping
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        record.get("invalidName");
    }

    // Test case for index out of bounds exception
    @Test(expected = IllegalArgumentException.class)
    public void testGetIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        record.get("name2");
        record.get("name3");
    }

    // Test cases for getComment()

    // Test case for no comment present
    @Test
    public void testGetCommentNoComment() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertNull(record.getComment());
    }

    // Test case for comment present
    @Test
    public void testGetCommentWithComment() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    // Test case for getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 3);
        assertEquals(3, record.getRecordNumber());
    }

    // Test case for isConsistent()

    // Test case for consistent record
    @Test
    public void testIsConsistentTrue() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertTrue(record.isConsistent());
    }

    // Test case for inconsistent record
    @Test
    public void testIsConsistentFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    // Test cases for isMapped(String)

    // Test case for mapped column
    @Test
    public void testIsMappedTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        assertTrue(record.isMapped("name1"));
    }

    // Test case for unmapped column
    @Test
    public void testIsMappedFalse() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertFalse(record.isMapped("name1"));
    }

    // Test cases for isSet(String)

    // Test case for set column
    @Test
    public void testIsSetTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        assertTrue(record.isSet("name1"));
    }

    // Test case for unset column
    @Test
    public void testIsSetFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1" }, mapping, null, 1);
        assertFalse(record.isSet("name2"));
    }

    // Test case for iterator()
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertNotNull(record.iterator());
    }

    // Test case for putIn(Map)
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        map = record.putIn(map);

        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    // Test case for size()
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertEquals(3, record.size());
    }

    // Test case for toMap()
    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[] { "value1", "value2" }, mapping, null, 1);
        Map<String, String> map = record.toMap();

        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    // Test case for toString()
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    // Test case for values()
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] { "value1", "value2", "value3" }, null, null, 1);
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, record.values());
    }
}