import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("x", new RecordProperty(JSType.UNKNOWN));
        properties.put("y", new RecordProperty(JSType.STRING));

        RecordType recordType = new RecordType(registry, properties);

        assertTrue(recordType.defineProperty("z", JSType.NUMBER, false, null));
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("x", new RecordProperty(JSType.UNKNOWN));

        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("x", new RecordProperty(JSType.UNKNOWN));

        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("x", new RecordProperty(JSType.NUMBER));

        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("x", new RecordProperty(JSType.NUMBER));
        properties2.put("y", new RecordProperty(JSType.STRING));

        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }
}