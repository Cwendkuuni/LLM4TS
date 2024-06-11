import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(new JSType()));
        properties.put("b", new RecordProperty(new JSType()));
        
        RecordType recordType = new RecordType(registry, properties);
        
        assertNotNull(recordType);
        assertEquals(registry, recordType.getRegistry());
        assertTrue(recordType.isDeclared());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty(new JSType()));
        properties1.put("b", new RecordProperty(new JSType()));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("b", new RecordProperty(new JSType()));
        properties2.put("a", new RecordProperty(new JSType()));
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    }
    
    // Add more test cases for other methods in RecordType class

}