import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testConstructorWithDeclaredProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(new JSTypeRegistry().getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        
        RecordType recordType = new RecordType(registry, properties, true);
        
        assertNotNull(recordType);
        assertEquals(2, recordType.getOwnPropertyNames().size());
        assertTrue(recordType.hasProperty("property1"));
        assertTrue(recordType.hasProperty("property2"));
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("property1"));
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("property2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", null);
        
        new RecordType(registry, properties, true);
    }
    
    @Test
    public void testIsSynthetic() {
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), new HashMap<>(), true);
        assertFalse(recordType1.isSynthetic());
        
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), new HashMap<>(), false);
        assertTrue(recordType2.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1, true);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType2 = new RecordType(registry, properties2, true);
        
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.getDefaultEquivalenceMethod()));
        
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node()));
        RecordType recordType3 = new RecordType(registry, properties2, true);
        
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.getDefaultEquivalenceMethod()));
    }
    
    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>(), true);
        ObjectType objectType = recordType.getImplicitPrototype();
        
        assertNotNull(objectType);
        assertEquals(JSTypeNative.OBJECT_TYPE, objectType.getType());
    }
    
    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        RecordType recordType = new RecordType(registry, properties, true);
        
        assertFalse(recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, new Node()));
        assertFalse(recordType.defineProperty("property2", registry.getNativeType(JSTypeNative.STRING_TYPE), true, new Node()));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1, true);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        RecordType recordType2 = new RecordType(registry, properties2, true);
        
        JSType result1 = recordType1.getGreatestSubtypeHelper(recordType2);
        
        assertNotNull(result1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
        properties3.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties3.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType3 = new RecordType(registry, properties3, true);
        
        JSType result2 = recordType1.getGreatestSubtypeHelper(recordType3);
        
        assertNotNull(result2);
    }
    
    @Test
    public void testToMaybeRecordType() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>(), true);
        
        assertNotNull(recordType.toMaybeRecordType());
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
    
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1, true);
        
        assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType2 = new RecordType(registry, properties2, true);
        
        assertTrue(recordType1.isSubtype(recordType2));
        
        Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
        properties3.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties3.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node()));
        RecordType recordType3 = new RecordType(registry, properties3, true);
        
        assertFalse(recordType1.isSubtype(recordType3));
    }
    
    @Test
    public void testIsSubtype_Static() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1, true);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType2 = new RecordType(registry, properties2, true);
        
        assertTrue(RecordType.isSubtype(recordType1, recordType2));
        
        Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
        properties3.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties3.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node()));
        RecordType recordType3 = new RecordType(registry, properties3, true);
        
        assertFalse(RecordType.isSubtype(recordType1, recordType3));
    }
}