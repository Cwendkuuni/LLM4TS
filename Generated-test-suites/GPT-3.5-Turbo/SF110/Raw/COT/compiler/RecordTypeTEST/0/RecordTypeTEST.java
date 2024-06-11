import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testRecordTypeConstructor() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        RecordType recordType = new RecordType(properties);
        assertNotNull(recordType);
    }

    @Test
    public void testDefineDeclaredProperty() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        RecordType recordType = new RecordType(properties);
        assertTrue(recordType.defineProperty("propertyName", JSType, true, Node));
    }

    @Test
    public void testDefineSynthesizedProperty() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        RecordType recordType = new RecordType(properties);
        assertTrue(recordType.defineProperty("propertyName", JSType, false, Node));
    }

    @Test
    public void testIsSynthetic() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        RecordType recordType = new RecordType(properties);
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        RecordType recordType1 = new RecordType(properties1);
        RecordType recordType2 = new RecordType(properties2);
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod));
    }

    @Test
    public void testGetImplicitPrototype() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        RecordType recordType = new RecordType(properties);
        assertNotNull(recordType.getImplicitPrototype());
    }

    @Test
    public void testIsSubtype() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        RecordType recordType1 = new RecordType(properties1);
        RecordType recordType2 = new RecordType(properties2);
        assertFalse(recordType1.isSubtype(recordType2));
    }

}