
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        RecordType recordType = new RecordType();
        assertNotNull(recordType);
    }

    @Test
    public void testDeclareProperty() {
        RecordType recordType = new RecordType();
        String propertyName = "property";
        JSType type = new JSType();
        boolean inferred = true;
        Node propertyNode = new Node();
        assertTrue(recordType.defineProperty(propertyName, type, inferred, propertyNode));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        JSType that = new JSType();
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(that));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        JSType that = new JSType();
        assertFalse(recordType.isSubtype(that));
    }
}

