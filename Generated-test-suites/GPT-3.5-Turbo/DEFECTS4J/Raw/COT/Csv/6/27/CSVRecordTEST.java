import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
    
    @Test
    public void testGetWithEnum() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertEquals("one", record.get(Enum.ONE));
        assertEquals("two", record.get(Enum.TWO));
        assertEquals("three", record.get(Enum.THREE));
    }
    
    @Test
    public void testGetWithIndex() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertEquals("one", record.get(0));
        assertEquals("two", record.get(1));
        assertEquals("three", record.get(2));
    }
    
    @Test
    public void testGetWithName() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertNull(record.get("nonexistent"));
        assertEquals("one", record.get("one"));
        assertEquals("two", record.get("two"));
        assertEquals("three", record.get("three"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        record.get("one");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        record.get("four");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        record.get(3);
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 10);
        assertEquals(10, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        CSVRecord record2 = new CSVRecord(new String[]{"one", "two"}, null, null, 1);
        CSVRecord record3 = new CSVRecord(new String[]{"one", "two", "three", "four"}, null, null, 1);
        assertTrue(record1.isConsistent());
        assertFalse(record2.isConsistent());
        assertFalse(record3.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertFalse(record.isMapped("nonexistent"));
        assertTrue(record.isMapped("one"));
        assertTrue(record.isMapped("two"));
        assertTrue(record.isMapped("three"));
    }
    
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertFalse(record.isSet("nonexistent"));
        assertTrue(record.isSet("one"));
        assertTrue(record.isSet("two"));
        assertTrue(record.isSet("three"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        record.putIn(map);
        assertEquals("one", map.get("one"));
        assertEquals("two", map.get("two"));
        assertEquals("three", map.get("three"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertEquals(Arrays.asList("one", "two", "three"), record.toList());
    }
    
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        HashMap<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("one", "one");
        expectedMap.put("two", "two");
        expectedMap.put("three", "three");
        assertEquals(expectedMap, record.toMap());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertEquals("[one, two, three]", record.toString());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"one", "two", "three"}, null, null, 1);
        assertArrayEquals(new String[]{"one", "two", "three"}, record.values());
    }
    
    private enum Enum {
        ONE, TWO, THREE
    }
    
}