import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), null));
      
        RecordType recordType1 = new RecordType(registry, properties);
      
        RecordType recordType2 = new RecordType(registry, properties);
    
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), null));
      
        RecordType recordType = new RecordType(registry, properties);
      
        ObjectType prototype = recordType.getImplicitPrototype();
      
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), null));
      
        RecordType recordType = new RecordType(registry, properties);
      
        boolean result = recordType.defineProperty("d", registry.getNativeType(JSTypeNative.STRING_TYPE), true, null);
      
        assertTrue(result);
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties1.put("c", new RecordTypeBuilder.RecordProperty(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), null));
      
        RecordType recordType1 = new RecordType(registry, properties1);
      
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
      
        RecordType recordType2 = new RecordType(registry, properties2);
      
        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
      
        assertEquals(recordType1, leastSupertype);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties1.put("c", new RecordTypeBuilder.RecordProperty(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), null));
      
        RecordType recordType1 = new RecordType(registry, properties1);
      
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
      
        RecordType recordType2 = new RecordType(registry, properties2);
      
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
      
        assertEquals(recordType1, greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties1.put("c", new RecordTypeBuilder.RecordProperty(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), null));
      
        RecordType recordType1 = new RecordType(registry, properties1);
      
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
      
        RecordType recordType2 = new RecordType(registry, properties2);
      
        boolean result = recordType1.isSubtype(recordType2);
      
        assertTrue(result);
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), null));
      
        RecordType recordType = new RecordType(registry, properties);
      
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
      
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
      
        assertEquals(recordType, resolvedType);
    }
}