import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    @Test
    public void testConstructor() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", JSTypeNative.NUMBER_TYPE, null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", JSTypeNative.STRING_TYPE, null));
        
        RecordType recordType = new RecordType(properties);
        
        Assert.assertTrue(recordType.isSynthetic());
        Assert.assertEquals(properties.keySet(), recordType.getOwnPropertyNames());
    }
    
    @Test
    public void testDefineProperty() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", JSTypeNative.NUMBER_TYPE, null));
        
        RecordType recordType = new RecordType(properties);
        
        Node propertyNode = new Node();
        
        boolean result1 = recordType.defineProperty("newProperty1", JSTypeNative.STRING_TYPE, true, propertyNode);
        boolean result2 = recordType.defineProperty("property1", JSTypeNative.STRING_TYPE, false, propertyNode);
        
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        Assert.assertTrue(recordType.hasProperty("newProperty1"));
        Assert.assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("newProperty1"));
    }
    
    @Test
    public void testIsSubtype() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty("property1", JSTypeNative.NUMBER_TYPE, null));
        
        RecordType recordType1 = new RecordType(properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordTypeBuilder.RecordProperty("property1", JSTypeNative.NUMBER_TYPE, null));
        
        RecordType recordType2 = new RecordType(properties2);
        
        Assert.assertTrue(recordType1.isSubtype(recordType2));
        Assert.assertFalse(recordType2.isSubtype(recordType1));
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty("property1", JSTypeNative.NUMBER_TYPE, null));
        
        RecordType recordType1 = new RecordType(properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordTypeBuilder.RecordProperty("property1", JSTypeNative.NUMBER_TYPE, null));
        properties2.put("property2", new RecordTypeBuilder.RecordProperty("property2", JSTypeNative.STRING_TYPE, null));
        
        RecordType recordType2 = new RecordType(properties2);
        
        Assert.assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, null));
        Assert.assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, null)); 
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", JSTypeNative.NUMBER_TYPE, null));
        
        RecordType recordType1 = new RecordType(properties);
        RecordType recordType2 = new RecordType(properties);
        RecordType recordType3 = new RecordType(properties);
        
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", JSTypeNative.STRING_TYPE, null));
        
        RecordType recordType4 = new RecordType(properties);
        
        JSType resultType1 = recordType1.getGreatestSubtypeHelper(recordType2);
        JSType resultType2 = recordType3.getGreatestSubtypeHelper(recordType4);
        
        Assert.assertEquals(JSTypeNative.OBJECT_TYPE, resultType1);
        Assert.assertEquals(JSTypeNative.NO_TYPE, resultType2);
    }
}