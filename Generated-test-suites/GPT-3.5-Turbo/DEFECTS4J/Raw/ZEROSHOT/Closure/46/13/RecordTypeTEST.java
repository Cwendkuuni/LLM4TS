import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setup() {
        recordType = new RecordType();
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType other = new RecordType();
        assertTrue(recordType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_notEquivalent() {
        RecordType other = new RecordType();
        other.addProperty("a", new JSType());
        assertFalse(recordType.isEquivalentTo(other));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType expectedPrototype = new ObjectType();
        assertEquals(expectedPrototype, recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "prop";
        JSType type = new JSType();
        boolean inferred = false;
        Node propertyNode = new Node();
        assertTrue(recordType.defineProperty(propertyName, type, inferred, propertyNode));
        assertEquals(type, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testDefineProperty_notInferred() {
        String propertyName = "prop";
        JSType type = new JSType();
        boolean inferred = false;
        Node propertyNode = new Node();
        recordType.defineProperty(propertyName, type, inferred, propertyNode);

        propertyName = "anotherProp";
        type = new JSType();
        inferred = false;
        propertyNode = new Node();
        assertTrue(recordType.defineProperty(propertyName, type, inferred, propertyNode));
        assertEquals(type, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testDefineProperty_frozen() {
        String propertyName = "prop";
        JSType type = new JSType();
        boolean inferred = false;
        Node propertyNode = new Node();
        recordType.setFrozen(true);
        assertFalse(recordType.defineProperty(propertyName, type, inferred, propertyNode));
        assertNull(recordType.getPropertyType(propertyName));
    }

    @Test
    public void testGetLeastSupertype() {
        JSType other = new JSType();
        RecordTypeBuilder builder = new RecordTypeBuilder();
        builder.addProperty("a", new JSType(), new Node());
        builder.addProperty("b", new JSType(), new Node());
        JSType expectedSupertype = builder.build();
        assertEquals(expectedSupertype, recordType.getLeastSupertype(other));
    }

    @Test
    public void testGetLeastSupertype_notRecordType() {
        JSType other = new JSType();
        JSType expectedSupertype = new JSType();
        assertEquals(expectedSupertype, recordType.getLeastSupertype(other));
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType maybeRecordType = recordType.toMaybeRecordType();
        assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsSubtype() {
        JSType other = new JSType();
        assertTrue(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_notRecordType() {
        JSType other = new JSType();
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_sameRecordType() {
        RecordType other = new RecordType();
        assertTrue(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_notEquivalent() {
        RecordType other = new RecordType();
        other.addProperty("a", new JSType());
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_subtype() {
        RecordType other = new RecordType();
        other.addProperty("a", new JSType());
        other.addProperty("b", new JSType());
        recordType.addProperty("a", new JSType());
        assertTrue(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_propertyTypeDeclared() {
        RecordType other = new RecordType();
        other.addProperty("a", new JSType());
        recordType.addProperty("a", new JSType());
        recordType.setPropertyTypeDeclared("a", true);
        assertTrue(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_propertyTypeDeclared_notEquivalent() {
        RecordType other = new RecordType();
        other.addProperty("a", new JSType());
        recordType.addProperty("a", new JSType());
        recordType.setPropertyTypeDeclared("a", true);
        recordType.setPropertyType("a", new JSType());
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_propertyTypeDeclared_notDeclared() {
        RecordType other = new RecordType();
        other.addProperty("a", new JSType());
        other.addProperty("b", new JSType());
        recordType.addProperty("a", new JSType());
        recordType.addProperty("b", new JSType());
        recordType.setPropertyTypeDeclared("a", true);
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtypeHelper() {
        ObjectType typeA = new ObjectType();
        ObjectType typeB = new ObjectType();
        assertTrue(RecordType.isSubtypeHelper(typeA, typeB));
    }

    @Test
    public void testIsSubtypeHelper_notSubtype() {
        ObjectType typeA = new ObjectType();
        JSType typeB = new JSType();
        assertFalse(RecordType.isSubtypeHelper(typeA, typeB));
    }

    @Test
    public void testIsSubtypeHelper_objectTypeSubtype() {
        ObjectType typeA = new ObjectType();
        ObjectType typeB = new ObjectType();
        typeB.setPropertyType("a", new JSType());
        assertTrue(RecordType.isSubtypeHelper(typeA, typeB));
    }

    @Test
    public void testIsSubtypeHelper_recordTypeSubtype() {
        RecordType typeA = new RecordType();
        typeA.addProperty("a", new JSType());
        RecordType typeB = new RecordType();
        typeB.addProperty("a", new JSType());
        assertTrue(RecordType.isSubtypeHelper(typeA, typeB));
    }

    @Test
    public void testIsSubtypeHelper_recordTypeNotSubtype() {
        RecordType typeA = new RecordType();
        typeA.addProperty("a", new JSType());
        RecordType typeB = new RecordType();
        assertFalse(RecordType.isSubtypeHelper(typeA, typeB));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(t, scope);
        assertEquals(recordType, resolvedType);
    }
}