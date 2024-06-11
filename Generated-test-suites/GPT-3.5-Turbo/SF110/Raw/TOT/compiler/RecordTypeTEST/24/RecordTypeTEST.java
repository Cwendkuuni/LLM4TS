import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {
    
    private JSTypeRegistry registry;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
    }
    
    @Test
    public void testRecordTypeConstructor() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        properties.put("property2", new RecordProperty(new JSType(), new Node()));
        
        RecordType recordType = new RecordType(registry, properties);
        
        assertNotNull(recordType);
        assertEquals(false, recordType.isSynthetic());
        
        assertTrue(recordType.hasProperty("property1"));
        assertTrue(recordType.hasProperty("property2"));
        assertFalse(recordType.hasProperty("property3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRecordTypeConstructor_NullRecordProperty() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", null);
        
        new RecordType(registry, properties);
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordProperty property1 = new RecordProperty(new JSType(), new Node());
        RecordProperty property2 = new RecordProperty(new JSType(), new Node());
        
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", property1);
        properties1.put("property2", property2);
        
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", property1);
        properties2.put("property2", property2);
        
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.EQUALS));
    }
    
    @Test
    public void testIsSubtype() {
        JSTypeRegistry otherRegistry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordProperty(new JSType(), new Node()));
        properties1.put("property2", new RecordProperty(new JSType(), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordProperty(new JSType(), new Node()));
        properties2.put("property2", new RecordProperty(new JSType(), new Node()));
        RecordType recordType2 = new RecordType(otherRegistry, properties2);
        
        assertFalse(recordType1.isSubtype(recordType2));
    }
    
    @Test
    public void testIsSubtype_RecordTypeIsSubtype() {
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordProperty(new JSType(), new Node()));
        properties1.put("property2", new RecordProperty(new JSType(), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordProperty(new JSType(), new Node()));
        properties2.put("property2", new RecordProperty(new JSType(), new Node()));
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(recordType1.isSubtype(recordType2));
    }
    
    @Test
    public void testIsSubtype_TopOfRecordTypesIsObject() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        assertTrue(recordType.isSubtype(objectType));
    }
    
    @Test
    public void testDefineProperty_isFrozen_ReturnsFalse() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        
        assertFalse(recordType.defineProperty("property2", new JSType(), true, new Node()));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper_RecordTypeParameter() {
        JSType nativeType = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        
        RecordProperty property = new RecordProperty(new JSType(), new Node());
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", property);
        RecordType recordType = new RecordType(registry, properties);
        
        RecordType recordTypeParameter = new RecordType(registry, properties);
        
        assertEquals(nativeType, recordType.getGreatestSubtypeHelper(recordTypeParameter));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper_NotRecordTypeParameter() {
        JSType nativeType = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType restrictedType = objectType.getGreatestSubtype(new JSType());
        
        RecordProperty property = new RecordProperty(new JSType(), new Node());
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", property);
        RecordType recordType = new RecordType(registry, properties);
        
        assertEquals(restrictedType, recordType.getGreatestSubtypeHelper(new JSType()));
    }
    
    @Test
    public void testToMaybeRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
    
    @Test
    public void testIsSynthetic() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        RecordType recordType = new RecordType(registry, properties, false);
        
        assertEquals(true, recordType.isSynthetic());
    }
    
    @Test
    public void testIsSynthetic_IsNotSynthetic() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        
        assertEquals(false, recordType.isSynthetic());
    }
    
    @Test
    public void testIsSynthetic_IsNotSynthetic_EmptyProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        
        assertEquals(false, recordType.isSynthetic());
    }
    
    @Test
    public void testGetImplicitPrototype() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        
        assertEquals(objectType, recordType.getImplicitPrototype());
    }
    
    @Test
    public void testIsFrozen() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        
        assertEquals(true, recordType.isFrozen());
    }
    
    @Test
    public void testUnityEngine_Create() {
        Map<String, RecordProperty> properties = new HashMap<>();
        
        properties.put("property1", new RecordProperty(new JSType(), new Node()));
        properties.put("property2", new RecordProperty(new JSType(), new Node()));
        properties.put("property3", new RecordProperty(new JSType(), new Node()));
        
        RecordType recordType = new RecordType(registry, properties);
        
        assertTrue(recordType.hasProperty("property1"));
        assertTrue(recordType.hasProperty("property2"));
        assertTrue(recordType.hasProperty("property3"));
        assertFalse(recordType.hasProperty("property4"));
    }
}