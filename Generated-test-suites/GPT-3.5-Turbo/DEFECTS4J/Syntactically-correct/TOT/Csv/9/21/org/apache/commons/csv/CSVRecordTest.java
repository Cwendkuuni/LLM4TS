import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertEquals("value2", record.get(TestEnum.ENUM2));
        
        // Edge case: empty record
        CSVRecord emptyRecord = new CSVRecord(new String[]{}, null, null, 0);
        try {
            emptyRecord.get(TestEnum.ENUM1);
            fail("Expected exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetIndex() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertEquals("value2", record.get(1));
        
        // Edge case: index out of bounds
        try {
            record.get(3);
            fail("Expected exception not thrown");
        } catch(ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetName() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertEquals("value2", record.get("name2"));
        
        // Edge case: missing header mapping
        CSVRecord noMappingRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        try {
            noMappingRecord.get("name2");
            fail("Expected exception not thrown");
        } catch(IllegalStateException e) {
            // Expected exception
        }
        
        // Edge case: missing mapping for name
        CSVRecord missingMappingRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                                                        new HashMap<>(), null, 0);
        try {
            missingMappingRecord.get("name2");
            fail("Expected exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
        
        // Edge case: invalid index
        CSVRecord invalidIndexRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                                                        new HashMap<>(), null, 0);
        try {
            invalidIndexRecord.get("name4");
            fail("Expected exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetComment() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 0);
        assertEquals("This is a comment", record.getComment());
        
        // Edge case: no comment
        CSVRecord noCommentRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertNull(noCommentRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Typical use case: consistent record
        CSVRecord consistentRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertTrue(consistentRecord.isConsistent());
        
        // Edge case: inconsistent record
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Typical use case: name is mapped
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name2", 1);
        CSVRecord mappedRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        assertTrue(mappedRecord.isMapped("name2"));
        
        // Edge case: name is not mapped
        CSVRecord notMappedRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        assertFalse(notMappedRecord.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        // Typical use case: name is mapped and has value
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name2", 1);
        CSVRecord mappedRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        assertTrue(mappedRecord.isSet("name2"));
        
        // Edge case: name is mapped but no value
        CSVRecord noValueRecord = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        assertFalse(noValueRecord.isSet("name2"));
        
        // Edge case: name is not mapped
        CSVRecord notMappedRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        assertFalse(notMappedRecord.isSet("name3"));
    }

    @Test
    public void testIterator() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        int count = 0;
        for(String value : record) {
            count++;
        }
        assertEquals(3, count);
        
        // Edge case: no values in record
        CSVRecord noValuesRecord = new CSVRecord(new String[]{}, null, null, 0);
        int noValuesCount = 0;
        for(String value : noValuesRecord) {
            noValuesCount++;
        }
        assertEquals(0, noValuesCount);
    }

    @Test
    public void testPutIn() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
        
        // Edge case: no headers in record
        CSVRecord noHeadersRecord = new CSVRecord(new String[]{}, null, null, 0);
        Map<String, String> noHeadersMap = noHeadersRecord.putIn(new HashMap<>());
        assertEquals(0, noHeadersMap.size());
    }

    @Test
    public void testSize() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertEquals(3, record.size());
        
        // Edge case: empty record
        CSVRecord emptyRecord = new CSVRecord(new String[]{}, null, null, 0);
        assertEquals(0, emptyRecord.size());
    }

    @Test
    public void testToString() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertEquals("[value1, value2, value3]", record.toString());
        
        // Edge case: empty record
        CSVRecord emptyRecord = new CSVRecord(new String[]{}, null, null, 0);
        assertEquals("[]", emptyRecord.toString());
    }

    @Test
    public void testValues() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
        
        // Edge case: empty record
        CSVRecord emptyRecord = new CSVRecord(new String[]{}, null, null, 0);
        assertArrayEquals(new String[]{}, emptyRecord.values());
    }

    private enum TestEnum {
        ENUM1,
        ENUM2,
        ENUM3
    }

}