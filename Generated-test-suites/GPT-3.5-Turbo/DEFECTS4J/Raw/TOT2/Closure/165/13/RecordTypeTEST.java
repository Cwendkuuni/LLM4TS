import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(registry, Maps.newTreeMap());
        String propertyName = "testProperty";
        JSType type = new JSType();
        boolean inferred = false;
        Node propertyNode = new Node();

        assertTrue(recordType.defineProperty(propertyName, type, inferred, propertyNode));

        JSType retrievedType = recordType.getPropertyType(propertyName);
        assertNotNull(retrievedType);
        assertEquals(type, retrievedType);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType(registry, Maps.newTreeMap());
        RecordType recordType2 = new RecordType(registry, Maps.newTreeMap());

        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType(registry, Maps.newTreeMap());
        RecordType recordType2 = new RecordType(registry, Maps.newTreeMap());

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        assertNotNull(greatestSubtype);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, Maps.newTreeMap());
        RecordType recordType2 = new RecordType(registry, Maps.newTreeMap());

        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(registry, Maps.newTreeMap());
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

        assertNotNull(resolvedType);
    }
}