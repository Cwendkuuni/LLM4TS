import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {
    
    @Test
    public void testRecordTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("propertyName", new RecordProperty("PROPERTY_TYPE", "PROPERTY_NODE"));
        
        RecordType recordType = new RecordType(registry, properties);
        
        assertTrue(recordType.getProperties().containsKey("propertyName"));
        assertNotNull(recordType.getProperties().get("propertyName"));
        assertTrue(recordType.isEquivalentTo(recordType));
        assertTrue(recordType.isFrozen());
    }
    
    @Test
    public void testRecordTypeIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("propertyName", new RecordProperty("PROPERTY_TYPE", "PROPERTY_NODE"));
        
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("propertyName", new RecordProperty("PROPERTY_TYPE", "PROPERTY_NODE"));
        
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }
    
    @Test
    public void testRecordTypeGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("propertyName", new RecordProperty("PROPERTY_TYPE", "PROPERTY_NODE"));
        
        RecordType recordType = new RecordType(registry, properties);
        
        assertNotNull(recordType.getImplicitPrototype());
        assertEquals("OBJECT_TYPE", recordType.getImplicitPrototype().toString());
    }
    
    @Test
    public void testRecordTypeDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("propertyName", new RecordProperty("PROPERTY_TYPE", "PROPERTY_NODE"));
        
        RecordType recordType = new RecordType(registry, properties);
        
        String newPropertyName = "newPropertyName";
        JSType newPropertyType = new JSType("NEW_PROPERTY_TYPE");
        Node newPropertyNode = new Node("NEW_PROPERTY_NODE");
        
        assertFalse(recordType.defineProperty(newPropertyName, newPropertyType, false, newPropertyNode));
        assertTrue(recordType.getProperties().containsKey(newPropertyName));
        assertNotNull(recordType.getProperties().get(newPropertyName));
    }
    
    @Test
    public void testRecordTypeGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        properties2.put("property2", new RecordProperty("PROPERTY_TYPE_2", "PROPERTY_NODE_2"));
        
        RecordType recordType2 = new RecordType(registry, properties2);
        
        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
        
        assertTrue(leastSupertype.getProperties().containsKey("property1"));
        assertNotNull(leastSupertype.getProperties().get("property1"));
        assertTrue(leastSupertype.getProperties().containsKey("property2"));
        assertNotNull(leastSupertype.getProperties().get("property2"));
    }
    
    @Test
    public void testRecordTypeGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        properties2.put("property3", new RecordProperty("PROPERTY_TYPE_3", "PROPERTY_NODE_3"));
        
        RecordType recordType2 = new RecordType(registry, properties2);
        
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        
        assertTrue(greatestSubtype.getProperties().containsKey("property1"));
        assertNotNull(greatestSubtype.getProperties().get("property1"));
        assertFalse(greatestSubtype.getProperties().containsKey("property3"));
        assertNull(greatestSubtype.getProperties().get("property3"));
    }
    
    @Test
    public void testRecordTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        properties2.put("property2", new RecordProperty("PROPERTY_TYPE_2", "PROPERTY_NODE_2"));
        
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(recordType1.isSubtype(recordType2));
        
        JSType objectType = new JSType("OBJECT_TYPE");
        assertTrue(recordType1.isSubtype(objectType));
    }
    
    @Test
    public void testRecordTypeIsSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordProperty("PROPERTY_TYPE_1", "PROPERTY_NODE_1"));
        properties2.put("property2", new RecordProperty("PROPERTY_TYPE_2", "PROPERTY_NODE_2"));
        
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }
    
    @Test
    public void testRecordTypeResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("propertyName", new RecordProperty("PROPERTY_TYPE", "PROPERTY_NODE"));
        
        RecordType recordType = new RecordType(registry, properties);
        
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        
        assertNotNull(resolvedType);
        assertEquals("RecordType", resolvedType.toString());
    }
}