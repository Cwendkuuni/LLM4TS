import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    private final Map<String, RecordProperty> properties = new HashMap<>();

    @Before
    public void setUp() {
        properties.put("property1", new RecordProperty("property1", JSTypeNative.STRING_TYPE, new Node()));
        properties.put("property2", new RecordProperty("property2", JSTypeNative.NUMBER_TYPE, new Node()));
        properties.put("property3", new RecordProperty("property3", JSTypeNative.BOOLEAN_TYPE, new Node()));
        recordType = new RecordType(new JSTypeRegistry(), properties);
    }

    @Test
    public void testRecordTypeCreation() {
        assertNotNull(recordType);
        assertNotNull(recordType.getOwnPropertyNames());
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testRecordTypeIsSubtype() {
        assertTrue(recordType.isSubtype(new ObjectType()));
        assertTrue(recordType.isSubtype(new RecordType(new JSTypeRegistry(), properties)));
        assertFalse(recordType.isSubtype(null));
        assertFalse(recordType.isSubtype(new UnionType()));
    }

    @Test
    public void testRecordTypeGetPropertyType() {
        assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("property1"));
        assertEquals(JSTypeNative.NUMBER_TYPE, recordType.getPropertyType("property2"));
        assertEquals(JSTypeNative.BOOLEAN_TYPE, recordType.getPropertyType("property3"));
        assertNull(recordType.getPropertyType("undefinedProperty"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordTypeDefineProperty() {
        recordType.defineProperty("property1", JSTypeNative.STRING_TYPE, true, new Node());
    }

    @Test
    public void testRecordTypeGetGreatestSubtypeHelper() {
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(new JSTypeRegistry().getNativeType(JSTypeNative.ARRAY_TYPE));
        assertTrue(greatestSubtype instanceof ObjectType);
        assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype.toMaybeObjectType());
    }

    @Test
    public void testRecordTypeToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testRecordTypeIsInvariant() {
        assertTrue(recordType.isInvariant(recordType));
        assertFalse(recordType.isInvariant(new ObjectType()));
    }

    @Test
    public void testRecordTypeCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), properties);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, new EquivalenceMethod()));
        assertFalse(recordType.checkRecordEquivalenceHelper(new RecordTypeBuilder(new JSTypeRegistry()).build(), new EquivalenceMethod()));
    }

    @Test
    public void testRecordTypeGetImplicitPrototype() {
        assertNotNull(recordType.getImplicitPrototype());
    }

    @Test
    public void testRecordTypeIsFrozen() {
        assertTrue(recordType.isFrozen());
    }

    @Test
    public void testRecordTypeIsDeclared() {
        assertTrue(recordType.isDeclared());
    }

    @Test
    public void testRecordTypeIsTypeInhabitable() {
        assertTrue(recordType.isTypeInhabitable());
    }

    @Test
    public void testRecordTypeGetTypeOfThis() {
        assertNotNull(recordType.getTypeOfThis());
    }

    @Test
    public void testRecordTypeGetDisplayName() {
        assertNotNull(recordType.getDisplayName());
    }

    @Test
    public void testRecordTypeHasInstanceTypeParameters() {
        assertFalse(recordType.hasInstanceTypeParameters());
    }

    @Test
    public void testRecordTypeIsStructuralInterface() {
        assertFalse(recordType.isStructuralInterface());
    }

    @Test
    public void testRecordTypeGetStringRepresentation() {
        assertNotNull(recordType.getStringRepresentation());
    }

    @Test
    public void testRecordTypeCheckIndexableValueType() {
        assertNull(recordType.checkIndexableValueType(0));
    }

    @Test
    public void testRecordTypeGetKeys() {
        assertNotNull(recordType.getKeys());
    }

    @Test
    public void testRecordTypeGetDeclaredPropNames() {
        assertNotNull(recordType.getDeclaredPropNames());
    }

    @Test
    public void testRecordTypeIsObject() {
        assertFalse(recordType.isObject());
    }

    @Test
    public void testRecordTypeCheckPropertyQuery() {
        assertFalse(recordType.checkPropertyQuery(JSTypeNative.STRING_TYPE, false));
    }

    @Test
    public void testRecordTypeIsPropertyTypeDeclared() {
        assertFalse(recordType.isPropertyTypeDeclared("property1"));
    }

    @Test
    public void testRecordTypeHasProperty() {
        assertTrue(recordType.hasProperty("property1"));
        assertFalse(recordType.hasProperty("undefinedProperty"));
    }

    @Test
    public void testRecordTypeIsUnionType() {
        assertFalse(recordType.isUnionType());
    }

    @Test
    public void testRecordTypeIsEmptyType() {
        assertFalse(recordType.isEmptyType());
    }

    @Test
    public void testRecordTypeToObjectType() {
        assertNotNull(recordType.toObjectType());
    }

    @Test
    public void testRecordTypeGetEquivalentValueType() {
        assertNotNull(recordType.getEquivalentValueType());
    }

    @Test
    public void testRecordTypeCheckSuperTypeCommonality() {
        assertFalse(recordType.checkSuperTypeCommonality(recordType));
    }

    @Test
    public void testRecordTypeGetLeastCommonInstantiatedType() {
        assertNull(recordType.getLeastCommonInstantiatedType(recordType));
    }

    @Test
    public void testRecordTypeGetCommonTypes() {
        assertNotNull(recordType.getCommonTypes(recordType));
    }

    @Test
    public void testRecordTypeGetOnlyIncludeInstanceTypes() {
        assertTrue(recordType.getOnlyIncludeInstanceTypes());
    }

    @Test
    public void testRecordTypeGetAllTypesInUnion() {
        assertNotNull(recordType.getAllTypesInUnion());
    }

    @Test
    public void testRecordTypeGetSomeLeastType() {
        assertNotNull(recordType.getSomeLeastType());
    }

    @Test
    public void testRecordTypeGetSomeTopType() {
        assertNotNull(recordType.getSomeTopType());
    }

    @Test
    public void testRecordTypeGetGreatestSubtype() {
        assertNotNull(recordType.getGreatestSubtype(recordType));
    }

}