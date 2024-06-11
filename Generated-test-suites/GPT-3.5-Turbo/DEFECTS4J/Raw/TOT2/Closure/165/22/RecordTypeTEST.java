import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        // Test creating a RecordType with properties
        RecordType recordType = new RecordTypeBuilder().addField("a", JSType.STRING_TYPE).build();

        assertNotNull(recordType);
        assertTrue(recordType instanceof RecordType);
    }

    @Test
    public void testRecordTypeSubtyping() {
        // Test subtyping between two RecordType instances
        RecordType recordType1 = new RecordTypeBuilder().addField("a", JSType.STRING_TYPE).build();
        RecordType recordType2 = new RecordTypeBuilder().addField("a", JSType.STRING_TYPE).build();

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testRecordTypeResolution() {
        // Test resolving a RecordType with internal scope
        RecordType recordType = new RecordTypeBuilder().addField("a", JSType.STRING_TYPE).build();
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

        assertNotNull(resolvedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordTypeNullPropertyException() {
        // Test creating a RecordType with null property
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("nullProperty", null);

        new RecordType(JSTypeRegistry.getInstance(), properties);
    }
}