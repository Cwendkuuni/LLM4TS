import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();

        // Add properties to the record type
        properties.put("property1", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
        properties.put("property3", new RecordTypeBuilder.RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));

        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(registry, new HashMap<>());

        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.defaultMethod()));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();

        assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getType());
    }

    @Test
    public void testDefineProperty() {
        boolean result = recordType.defineProperty("property4", JSTypeNative.STRING_TYPE, true, null);

        assertTrue(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType subtype = recordType.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.OBJECT_TYPE));

        assertEquals(JSTypeNative.NO_TYPE, subtype);
    }

    @Test
    public void testIsSubtype() {
        RecordType anotherRecordType = new RecordType(registry, new HashMap<>());

        assertTrue(recordType.isSubtype(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(recordType.isSubtype(anotherRecordType));
    }

    @Test
    public void testHasProperty() {
        assertTrue(recordType.hasProperty("property1"));
        assertFalse(recordType.hasProperty("property4"));
    }

    @Test
    public void testGetPropertyType() {
        JSType propertyType = recordType.getPropertyType("property1");

        assertEquals(JSTypeNative.STRING_TYPE, propertyType);
    }

    @Test
    public void testIsPropertyTypeDeclared() {
        assertTrue(recordType.isPropertyTypeDeclared("property1"));
        assertFalse(recordType.isPropertyTypeDeclared("property4"));
    }

}