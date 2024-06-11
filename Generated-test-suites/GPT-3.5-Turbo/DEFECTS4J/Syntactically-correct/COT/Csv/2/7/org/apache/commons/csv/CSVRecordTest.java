import org.junit.Test;

public class CSVRecordTest {
    
    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.get(0).equals("Value1"));
        assert(record.get(1).equals("Value2"));
    }
    
    @Test
    public void testGetWithName() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.get("name") == null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        record.get("name");
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.isConsistent() == true);
    }
    
    @Test
    public void testIsConsistentWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, 
                                        Map.of("name1", 0, "name2", 1), null, 1);
        assert(record.isConsistent() == true);
    }
    
    @Test
    public void testIsConsistentWithMappingInconsistentSize() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, 
                                        Map.of("name1", 0, "name2", 1, "name3", 2), null, 1);
        assert(record.isConsistent() == false);
    }
    
    @Test
    public void testIsMappedWithName() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.isMapped("name") == false);
    }
    
    @Test
    public void testIsSetWithName() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.isSet("name") == false);
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assert(iterator.next().equals("Value1"));
        assert(iterator.next().equals("Value2"));
        assert(iterator.hasNext() == false);
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        String[] values = record.values();
        assert(values.length == 2);
        assert(values[0].equals("Value1"));
        assert(values[1].equals("Value2"));
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, "Comment", 1);
        assert(record.getComment().equals("Comment"));
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.getRecordNumber() == 1);
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.size() == 2);
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assert(record.toString().equals("[Value1, Value2]"));
    }

}