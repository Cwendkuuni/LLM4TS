import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {
    private RecordType recordType;

    @Before
    public void setUp() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(Type1.class, null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(Type2.class, null));

        recordType = new RecordType(new JSTypeRegistry(), properties);
    }

    @Test
    public void testIsEquivalentTo() {
        assertFalse(recordType.isEquivalentTo(null));

        RecordType otherRecordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
        assertTrue(recordType.isEquivalentTo(recordType));
        assertFalse(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testGetImplicitPrototype() {
        assertNull(recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("property3", new Type3(), false, null));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        RecordType otherRecordType = builder.build();
        assertEquals(otherRecordType, recordType.getLeastSupertype(otherRecordType));

        builder.addProperty("property1", new Type1(), null);
        otherRecordType = builder.build();
        assertEquals(otherRecordType, recordType.getLeastSupertype(otherRecordType));

        builder.addProperty("property2", new Type2Subclass(), null);
        otherRecordType = builder.build();
        assertEquals(recordType, recordType.getLeastSupertype(otherRecordType));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        assertTrue(recordType.getGreatestSubtypeHelper(null).isEmptyType());
    }

    @Test
    public void testIsSubtype() {
        assertFalse(recordType.isSubtype(null));
        assertFalse(recordType.isSubtype(new ObjectType(new JSTypeRegistry())));

        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        RecordType otherRecordType = builder.build();
        assertTrue(recordType.isSubtype(otherRecordType));

        builder.addProperty("property1", new Type1(), null);
        otherRecordType = builder.build();
        assertTrue(recordType.isSubtype(otherRecordType));

        builder.addProperty("property2", new Type3(), null);
        otherRecordType = builder.build();
        assertFalse(recordType.isSubtype(otherRecordType));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> staticScope = new StaticScope<>();

        assertNull(recordType.resolveInternal(errorReporter, staticScope));
    }

    static class Type1 implements JSType {}
    static class Type2 implements JSType {}
    static class Type2Subclass extends Type2 {}
    static class Type3 implements JSType {}
}