import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    private RecordType recordType;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testDefineProperty() {
        recordType = createRecordType();

        boolean defined = recordType.defineProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        Assert.assertTrue(defined);
    }

    @Test
    public void testDefinePropertyInferred() {
        recordType = createRecordType();

        boolean defined = recordType.defineProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), true, null);
        Assert.assertTrue(defined);
    }

    @Test
    public void testDefinePropertyFrozen() {
        recordType = createFrozenRecordType();

        boolean defined = recordType.defineProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        Assert.assertFalse(defined);
    }

    @Test
    public void testIsEquivalentTo() {
        recordType = createRecordType();
        RecordType otherRecordType = createRecordType();

        boolean equivalent = recordType.isEquivalentTo(otherRecordType);
        Assert.assertTrue(equivalent);
    }

    @Test
    public void testIsNotEquivalentTo() {
        recordType = createRecordType();
        RecordType otherRecordType = createDifferentRecordType();

        boolean equivalent = recordType.isEquivalentTo(otherRecordType);
        Assert.assertFalse(equivalent);
    }

    @Test
    public void testGetImplicitPrototype() {
        recordType = createRecordType();

        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        Assert.assertNotNull(implicitPrototype);
        Assert.assertEquals("object", implicitPrototype.getName());
    }

    @Test
    public void testIsSubtype() {
        recordType = createRecordType();
        RecordType otherRecordType = createSubtypeRecordType();

        boolean subtype = recordType.isSubtype(otherRecordType);
        Assert.assertTrue(subtype);
    }

    @Test
    public void testIsNotSubtype() {
        recordType = createRecordType();
        RecordType otherRecordType = createDifferentRecordType();

        boolean subtype = recordType.isSubtype(otherRecordType);
        Assert.assertFalse(subtype);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        recordType = createRecordType();
        JSType otherType = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherType);
        Assert.assertNotNull(greatestSubtype);
    }

    @Test
    public void testResolveInternal() {
        recordType = createRecordType();

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        Assert.assertNotNull(resolvedType);
    }

    private RecordType createRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        return new RecordType(registry, properties);
    }

    private RecordType createFrozenRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        recordType.setFrozen(true);
        return recordType;
    }

    private RecordType createDifferentRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty());
        return new RecordType(registry, properties);
    }

    private RecordType createSubtypeRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty());
        properties.put("age", new RecordProperty());
        return new RecordType(registry, properties);
    }
}