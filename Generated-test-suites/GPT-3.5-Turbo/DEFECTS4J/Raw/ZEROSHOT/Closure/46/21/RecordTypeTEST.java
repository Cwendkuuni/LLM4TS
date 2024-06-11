import org.junit.Test;
import java.util.Map;
import java.util.TreeMap;
import static org.junit.Assert.*;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new TreeMap<>();
        properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType other = new RecordType(registry, new TreeMap<>(recordType.properties));
        assertTrue(recordType.isEquivalentTo(other));

        Map<String, RecordProperty> differentProps = new TreeMap<>(recordType.properties);
        differentProps.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        other = new RecordType(registry, differentProps);
        assertFalse(recordType.isEquivalentTo(other));

        differentProps = new TreeMap<>(recordType.properties);
        differentProps.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        other = new RecordType(registry, differentProps);
        assertFalse(recordType.isEquivalentTo(other));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertNotNull(implicitPrototype);
        assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getDisplayName());
    }

    @Test
    public void testDefineProperty() {
        String newProperty = "c";
        JSType newType = registry.getNativeType(JSTypeNative.NULL_TYPE);
        boolean inferred = false;
        Node propertyNode = null;

        assertTrue(recordType.defineProperty(newProperty, newType, inferred, propertyNode));
        assertEquals(newType, recordType.getPropertyType(newProperty));

        // Test that defining new property failed after the record type is frozen
        recordType.freeze();
        assertFalse(recordType.defineProperty(newProperty, newType, inferred, propertyNode));
    }

    @Test
    public void testGetLeastSupertype() {
        RecordType other = new RecordType(registry, new TreeMap<>(recordType.properties));
        JSType leastSupertype = recordType.getLeastSupertype(other);
        assertEquals(recordType, leastSupertype);

        Map<String, RecordProperty> properties = new TreeMap<>(recordType.properties);
        properties.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        other = new RecordType(registry, properties);
        leastSupertype = recordType.getLeastSupertype(other);
        assertEquals(JSTypeNative.UNKNOWN_TYPE, leastSupertype);

        properties = new TreeMap<>(recordType.properties);
        properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        other = new RecordType(registry, properties);
        leastSupertype = recordType.getLeastSupertype(other);
        assertEquals(JSTypeNative.UNKNOWN_TYPE, leastSupertype);
    }

    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(objectType));

        RecordType other = new RecordType(registry, new TreeMap<>(recordType.properties));
        assertTrue(recordType.isSubtype(other));

        Map<String, RecordProperty> differentProps = new TreeMap<>(recordType.properties);
        differentProps.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        other = new RecordType(registry, differentProps);
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter t = null;
        StaticScope<JSType> scope = null;
        recordType.resolveInternal(t, scope);

        // Test that properties are resolved
        for (JSType type : recordType.properties.values()) {
            assertTrue(type.isResolved());
        }
    }
}