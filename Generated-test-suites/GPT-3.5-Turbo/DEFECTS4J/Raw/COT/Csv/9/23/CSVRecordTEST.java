import junit.framework.TestCase;

public class CSVRecordTest extends TestCase {
    
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals("value1", record.get(TestEnum.TEST1));
        assertEquals("value2", record.get(TestEnum.TEST2));
        assertEquals("value3", record.get(TestEnum.TEST3));
    }
    
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }
    
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }
    
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 5);
        assertEquals(5, record.getRecordNumber());
    }
    
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertTrue(record1.isConsistent());
        
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1);
        assertFalse(record2.isConsistent());
    }
    
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }
    
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }
    
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }
    
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals(3, record.size());
    }
    
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }
    
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        String[] values = record.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }
    
    private static enum TestEnum {
        TEST1, TEST2, TEST3;
    }
}