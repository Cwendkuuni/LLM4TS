import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeConstructor() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType = new RecordType(null, properties);
        assertNotNull(recordType);
    }

    @Test
    public void testRecordTypeIsSynthetic() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType = new RecordType(null, properties);
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testRecordTypeCheckRecordEquivalenceHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties1.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType1 = new RecordType(null, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties2.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType2 = new RecordType(null, properties2);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, null));
    }

    @Test
    public void testRecordTypeDefineProperty() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(null, properties);
        assertTrue(recordType.defineProperty("prop1", null, false, null));
    }

    @Test
    public void testRecordTypeGetGreatestSubtypeHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties1.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType1 = new RecordType(null, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties2.put("prop3", new RecordTypeBuilder.RecordProperty());
        RecordType recordType2 = new RecordType(null, properties2);

        assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType2));
    }

    @Test
    public void testRecordTypeIsSubtype() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties1.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType1 = new RecordType(null, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties2.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType2 = new RecordType(null, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testRecordTypeSubtypeInvalid() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties1.put("prop2", new RecordTypeBuilder.RecordProperty());
        RecordType recordType1 = new RecordType(null, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty());
        properties2.put("prop3", new RecordTypeBuilder.RecordProperty());
        RecordType recordType2 = new RecordType(null, properties2);

        assertFalse(recordType1.isSubtype(recordType2));
    }
}