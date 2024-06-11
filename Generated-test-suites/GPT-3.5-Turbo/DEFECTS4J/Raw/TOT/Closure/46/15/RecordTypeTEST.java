import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry(new ErrorReporter(null));
        recordType = createRecordType();
    }

    @After
    public void tearDown() {
        registry = null;
        recordType = null;
    }

    @Test
    public void testIsEquivalentTo() {
        // Test case for equivalent record types
        RecordType otherRecordType = createRecordType();
        assertTrue(recordType.isEquivalentTo(otherRecordType));

        // Test case for non-equivalent record types
        otherRecordType = createNonEquivalentRecordType();
        assertFalse(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getDisplayName());
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "newProperty";
        JSType type = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        boolean inferred = false;
        Node propertyNode = new Node();

        // Test case for defining a new property
        boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
        assertTrue(result);
        assertTrue(recordType.hasProperty(propertyName));
        assertEquals(type, recordType.getPropertyType(propertyName));

        // Test case for trying to define a property when the record type is frozen
        recordType.defineDeclaredProperty("frozenProperty", registry.getNativeType(JSTypeNative.STRING_TYPE), propertyNode);
        result = recordType.defineProperty("frozenProperty", registry.getNativeType(JSTypeNative.NUMBER_TYPE), inferred, propertyNode);
        assertFalse(result);
        assertTrue(recordType.getPropertyType("frozenProperty").isEquivalentTo(registry.getNativeType(JSTypeNative.STRING_TYPE)));
    }

    @Test
    public void testGetLeastSupertype() {
        // Test case for record type as least supertype
        JSType that = createRecordType();
        assertEquals(recordType, recordType.getLeastSupertype(that));

        // Test case for non-record type as least supertype
        that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getLeastSupertype(that).getDisplayName());

        // Test case for no common properties between record types
        that = createNonOverlappingRecordType();
        assertEquals(JSTypeNative.NO_OBJECT_TYPE, recordType.getLeastSupertype(that).getDisplayName());
    }

    @Test
    public void testIsSubtype() {
        // Test case for subtyping of record types
        JSType that = createSubtypeRecordType();
        assertTrue(recordType.isSubtype(that));

        // Test case for record type not being a subtype of non-record type
        that = registry.getNativeType(JSTypeNative.STRING_TYPE);
        assertFalse(recordType.isSubtype(that));

        // Test case for record type not being a subtype of record type with different properties
        that = createNonOverlappingRecordType();
        assertFalse(recordType.isSubtype(that));
    }

    @Test
    public void testResolveInternal() {
        // Test case for resolving properties
        Node propertyNode = new Node();
        JSType resolvedType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        recordType.defineProperty("propertyToResolve", resolvedType, true, propertyNode);
        JSType resolvedRecordType = recordType.resolveInternal(null, new StaticScope<>());
        assertFalse(resolvedRecordType.hasProperty("propertyToResolve"));
        assertEquals(resolvedType, resolvedRecordType.getPropertyType("propertyToResolve"));
    }

    private RecordType createRecordType() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));

        return new RecordType(registry, properties);
    }

    private RecordType createNonEquivalentRecordType() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property3", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("property4", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));

        return new RecordType(registry, properties);
    }

    private RecordType createNonOverlappingRecordType() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property5", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));

        return new RecordType(registry, properties);
    }

    private JSType createSubtypeRecordType() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("extraProperty", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));

        return new RecordType(registry, properties);
    }

}