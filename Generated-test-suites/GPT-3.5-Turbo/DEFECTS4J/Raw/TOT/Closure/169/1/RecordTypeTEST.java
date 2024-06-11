import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = createRecordType();
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, JSType> properties = new HashMap<>();
        properties.put("a", new JSType("TYPE_1"));

        RecordType otherRecord = createRecordType();
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));
    }

    @Test
    public void testCheckRecordEquivalenceHelper_False() {
        Map<String, JSType> properties = new HashMap<>();
        properties.put("a", new JSType("TYPE_1"));
        properties.put("b", new JSType("TYPE_2"));

        RecordType otherRecord = createRecordType();
        assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, true));
    }

    @Test
    public void testGetImplicitPrototype() {
        assertNotNull(recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "property";
        JSType type = new JSType("TYPE");
        boolean inferred = false;
        Node propertyNode = new Node();

        assertTrue(recordType.defineProperty(propertyName, type, inferred, propertyNode));
        assertEquals(type, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testDefineProperty_Frozen() {
        String propertyName = "property";
        JSType type = new JSType("TYPE");
        boolean inferred = false;
        Node propertyNode = new Node();

        recordType.setFrozen(true);
        assertFalse(recordType.defineProperty(propertyName, type, inferred, propertyNode));
        assertNull(recordType.getPropertyType(propertyName));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType("THAT");

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertEquals(new JSType("NO_OBJECT_TYPE"), greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        JSType that = new JSType("THAT");
        assertFalse(recordType.isSubtype(that));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(recordType, resolvedType);
    }

    private RecordType createRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(new JSType("TYPE_1"), new Node()));

        return new RecordType(new JSTypeRegistry(), properties);
    }
}