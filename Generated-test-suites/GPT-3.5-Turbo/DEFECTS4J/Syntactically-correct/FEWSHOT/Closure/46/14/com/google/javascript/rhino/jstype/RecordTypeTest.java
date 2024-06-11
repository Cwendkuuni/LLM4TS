import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testConstructor() {
        RecordType recordType = createRecordType();
        Map<String, RecordProperty> properties = recordType.getProperties();
        
        assertEquals(properties.size(), 2);
        assertEquals(properties.get("a"), createRecordProperty("a", createJSType("A")));
        assertEquals(properties.get("b"), createRecordProperty("b", createJSType("B")));
    }
    
    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = createRecordType();
        assertEquals(true, recordType1.isEquivalentTo(recordType2));
        
        RecordType recordType3 = createRecordType();
        RecordType recordType4 = createRecordType();
        recordType4.addProperty("c", createJSType("C"), null);
        assertEquals(false, recordType3.isEquivalentTo(recordType4));
    }
    
    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = createRecordType();
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertEquals("Object", implicitPrototype.getName());
    }
    
    @Test
    public void testDefineProperty() {
        RecordType recordType = createRecordType();
        boolean isDefined = recordType.defineProperty("c", createJSType("C"), true, null);
        assertEquals(true, isDefined);
        assertEquals(3, recordType.getProperties().size());
        assertEquals(createRecordProperty("c", createJSType("C")), recordType.getProperties().get("c"));
        
        boolean isDefined2 = recordType.defineProperty("a", createJSType("A"), false, null);
        assertEquals(false, isDefined2);
        assertEquals(createRecordProperty("a", createJSType("A")), recordType.getProperties().get("a"));
    }
    
    @Test
    public void testGetLeastSupertype() {
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = createRecordType();
        recordType2.addProperty("c", createJSType("C"), null);
        
        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertEquals(true, leastSupertype.isRecordType());
        RecordType leastSupertypeRecordType = leastSupertype.toMaybeRecordType();
        Map<String, RecordProperty> properties = leastSupertypeRecordType.getProperties();
        
        assertEquals(properties.size(), 2);
        assertEquals(properties.get("a"), createRecordProperty("a", createJSType("A")));
        assertEquals(properties.get("b"), createRecordProperty("b", createJSType("B")));
        assertNull(properties.get("c"));
    }
    
    @Test
    public void testIsSubtype() {
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = createRecordType();
        assertEquals(true, recordType1.isSubtype(recordType2));
        
        RecordType recordType3 = createRecordType();
        RecordType recordType4 = createRecordType();
        recordType4.addProperty("c", createJSType("C"), null);
        assertEquals(false, recordType3.isSubtype(recordType4));
        
        ObjectType objectType = createObjectType();
        assertEquals(true, recordType1.isSubtype(objectType));
        
        JSType unknownType = createUnknownType();
        assertEquals(true, recordType1.isSubtype(unknownType));
    }
    
    @Test
    public void testResolveInternal() {
        RecordType recordType = createRecordType();
        ErrorReporter errorReporter = createErrorReporter();
        StaticScope<JSType> scope = createScope();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        
        assertEquals(recordType, resolvedType);
        Map<String, RecordProperty> properties = recordType.getProperties();
        assertEquals(properties.size(), 2);
        assertEquals(properties.get("a").getType(), createJSType("A"));
        assertEquals(properties.get("b").getType(), createJSType("B"));
    }
    
    private RecordType createRecordType() {
        JSTypeRegistry registry = createJSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", createRecordProperty("a", createJSType("A")));
        properties.put("b", createRecordProperty("b", createJSType("B")));
        
        return new RecordType(registry, properties);
    }
    
    private RecordProperty createRecordProperty(String propertyName, JSType type) {
        return new RecordProperty(propertyName, type, null);
    }
    
    private JSType createJSType(String name) {
        return new JSType(name);
    }
    
    private ObjectType createObjectType() {
        return new ObjectType();
    }
    
    private JSType createUnknownType() {
        return new JSType("unknown");
    }
    
    private ErrorReporter createErrorReporter() {
        return new ErrorReporter();
    }
    
    private StaticScope<JSType> createScope() {
        return new StaticScope<>();
    }
    
    private JSTypeRegistry createJSTypeRegistry() {
        return new JSTypeRegistry();
    }
}