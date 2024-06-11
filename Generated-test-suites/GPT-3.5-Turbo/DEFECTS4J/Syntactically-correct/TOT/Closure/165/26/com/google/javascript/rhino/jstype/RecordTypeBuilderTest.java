import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty_SingleProperty_Success() {
        // Arrange
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();

        // Act
        RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testAddProperty_DuplicateProperty_ReturnsNull() {
        // Arrange
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        builder.addProperty(propertyName, propertyType, propertyNode);

        // Act
        RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);

        // Assert
        assertNull(result);
    }

    @Test
    public void testBuild_EmptyRecord_ReturnsObjectType() {
        // Act
        JSType result = builder.build();

        // Assert
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testBuild_NonEmptyRecord_Success() {
        // Arrange
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        builder.addProperty(propertyName, propertyType, propertyNode);

        // Act
        JSType result = builder.build();

        // Assert
        assertNotNull(result);
        assertEquals(RecordType.class, result.getClass());
    }
}