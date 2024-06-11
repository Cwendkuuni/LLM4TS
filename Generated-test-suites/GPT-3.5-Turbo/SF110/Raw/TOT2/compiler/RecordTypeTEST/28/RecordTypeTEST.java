import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        // Test creating a RecordType with properties
        // Arrange
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("String", new Node()));
        properties.put("age", new RecordProperty("Number", new Node()));

        // Act
        RecordType recordType = new RecordType(registry, properties);

        // Assert
        assertNotNull(recordType);
        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testRecordEquivalenceCheck() {
        // Test checking equivalence between two RecordTypes
        // Arrange
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty("String", new Node()));
        properties1.put("age", new RecordProperty("Number", new Node()));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("name", new RecordProperty("String", new Node()));
        properties2.put("age", new RecordProperty("Number", new Node()));
        RecordType recordType2 = new RecordType(registry, properties2);

        // Act
        boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.EQUALS);

        // Assert
        assertTrue(result);
    }

    // Add more test cases for each method in the RecordType class as needed
    

}