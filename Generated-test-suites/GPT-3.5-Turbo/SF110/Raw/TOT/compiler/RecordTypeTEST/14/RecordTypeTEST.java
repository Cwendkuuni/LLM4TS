import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordType.RecordProperty;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private static JSTypeRegistry registry;
    private static RecordType recordType;

    @BeforeClass
    public static void setup() {
        registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        builder.addProperty("property2", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        builder.addProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null);
        recordType = builder.build();
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper_SameRecords() {
        RecordType otherRecord = new RecordType(registry, recordType.getProperties());
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUALS));
    }

    @Test
    public void testCheckRecordEquivalenceHelper_DifferentProperty() {
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType otherRecord = builder.build();
        assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUALS));
    }

    @Test
    public void testCheckRecordEquivalenceHelper_DifferentType() {
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType otherRecord = builder.build();
        assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUALS));
    }

    @Test
    public void testDefineProperty_IsFrozen() {
        assertFalse(recordType.defineProperty("property4", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper_RecordType() {
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        RecordType that = builder.build();

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelper_ObjectType() {
        JSType objectType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(objectType);
        assertEquals(recordType, greatestSubtype);
    }

    @Test
    public void testIsSubtype_SameRecordType() {
        assertTrue(recordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_DifferentRecordType() {
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        RecordType otherRecord = builder.build();
        assertTrue(recordType.isSubtype(otherRecord));
    }

    @Test
    public void testIsSubtype_NotRecordType() {
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
    }

}