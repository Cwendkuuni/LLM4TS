import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    
    private RecordType recordType;
    private JSTypeRegistry registry;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty());
        properties.put("property2", new RecordTypeBuilder.RecordProperty());
        recordType = new RecordType(registry, properties);
    }
    
    @Test
    public void testIsEquivalentTo() {
        RecordType other = new RecordType(registry, new HashMap<>());
        assertFalse(recordType.isEquivalentTo(other));
        
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty());
        properties.put("property2", new RecordTypeBuilder.RecordProperty());
        RecordType same = new RecordType(registry, properties);
        assertTrue(recordType.isEquivalentTo(same));
        
        properties.put("property3", new RecordTypeBuilder.RecordProperty());
        RecordType different = new RecordType(registry, properties);
        assertFalse(recordType.isEquivalentTo(different));
    }
    
    @Test
    public void testGetImplicitPrototype() {
        ObjectType prototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.getImplicitPrototype().isEquivalentTo(prototype));
    }
    
    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("property3", new JSType(registry), false, null));
        
        recordType.defineProperty("property3", new JSType(registry), true, null);
        assertNotNull(recordType.getPropertyType("property3"));
    }
    
    @Test
    public void testGetLeastSupertype() {
        JSType otherType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.getLeastSupertype(otherType).isEquivalentTo(otherType));
        
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty());
        RecordType other = new RecordType(registry, properties);
        assertTrue(recordType.getLeastSupertype(other).isEquivalentTo(other));
        
        properties.put("property2", new RecordTypeBuilder.RecordProperty());
        RecordType combined = new RecordType(registry, properties);
        assertTrue(recordType.getLeastSupertype(combined).isEquivalentTo(combined));
    }
    
    @Test
    public void testToMaybeRecordType() {
        assertTrue(recordType.toMaybeRecordType().isEquivalentTo(recordType));
    }
    
    @Test
    public void testIsSubtype() {
        JSType otherType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(otherType));
        
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty());
        RecordType other = new RecordType(registry, properties);
        assertTrue(recordType.isSubtype(other));
        
        properties.put("property2", new RecordTypeBuilder.RecordProperty());
        RecordType combined = new RecordType(registry, properties);
        assertTrue(recordType.isSubtype(combined));
    }
    
    @Test
    public void testIsSubtype_assertFalse() {
        JSType otherType = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        assertFalse(recordType.isSubtype(otherType));
    }
    
    @Test
    public void testResolveInternal() {
        // TODO: Implement this test
    }
}