import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    
    @Before
    public void setUp(){
        // Creating a sample CSVRecord
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        csvRecord = new CSVRecord(values, mapping, "This is a comment", 1);
    }
    
    @Test
    public void testGetByEnum(){
        // Testing normal case
        assertEquals("Value1", csvRecord.get(Enum1.VALUE1));
        
        // Testing edge case
        assertNull(csvRecord.get(Enum2.VALUE4));
    }
    
    @Test
    public void testGetByIndex(){
        // Testing normal case
        assertEquals("Value2", csvRecord.get(1));
        
        // Testing edge case
        try{
            csvRecord.get(3);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch(ArrayIndexOutOfBoundsException e){
            // Exception is expected
        }
    }
    
    @Test
    public void testGetByName(){
        // Testing normal case
        assertEquals("Value3", csvRecord.get("Column3"));
        
        // Testing edge case
        try{
            csvRecord.get("Column4");
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e){
            // Exception is expected
        }
    }
    
    @Test
    public void testGetComment(){
        // Testing normal case
        assertEquals("This is a comment", csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber(){
        // Testing normal case
        assertEquals(1, csvRecord.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent(){
        // Testing normal case
        assertTrue(csvRecord.isConsistent());
        
        // Testing an inconsistent record
        String[] inconsistentValues = {"Value1", "Value2"};
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("Column1", 0);
        inconsistentMapping.put("Column2", 1);
        csvRecord = new CSVRecord(inconsistentValues, inconsistentMapping, "This is a comment", 1);
        assertFalse(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped(){
        // Testing normal case
        assertTrue(csvRecord.isMapped("Column1"));
        
        // Testing edge case
        assertFalse(csvRecord.isMapped("Column4"));
    }
    
    @Test
    public void testIsSet(){
        // Testing normal case
        assertTrue(csvRecord.isSet("Column2"));
        
        // Testing edge case
        assertFalse(csvRecord.isSet("Column3"));
        assertFalse(csvRecord.isSet("Column4"));
    }
    
    @Test
    public void testIterator(){
        // Testing normal case
        List<String> valuesList = Arrays.asList("Value1", "Value2", "Value3");
        List<String> recordValues = new ArrayList<>();
        for(String value : csvRecord){
            recordValues.add(value);
        }
        assertEquals(valuesList, recordValues);
    }
    
    @Test
    public void testSize(){
        // Testing normal case
        assertEquals(3, csvRecord.size());
    }
    
    @Test
    public void testToMap(){
        // Testing normal case
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Column1", "Value1");
        expectedMap.put("Column2", "Value2");
        expectedMap.put("Column3", "Value3");
        assertEquals(expectedMap, csvRecord.toMap());
    }

    @Test
    public void testValues(){
        // Testing normal case
        String[] expectedValues = {"Value1", "Value2", "Value3"};
        assertArrayEquals(expectedValues, csvRecord.values());
    }
}