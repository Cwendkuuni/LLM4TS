
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testAddProperty_WithValidNameAndType_ShouldAddProperty() {
        // Arrange
        RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);
        String propertyName = "age";
        JSType propertyType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

        // Act
        RecordTypeBuilder result = recordBuilder.addProperty(propertyName, propertyType, null);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testAddProperty_WithDuplicateName_ShouldReturnNull() {
        // Arrange
        RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);
        String propertyName = "age";
        JSType propertyType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        recordBuilder.addProperty(propertyName, propertyType, null);

        // Act
        RecordTypeBuilder result = recordBuilder.addProperty(propertyName, propertyType, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testBuild_WithEmptyRecord_ShouldReturnObjectType() {
        // Arrange
        RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        // Act
        JSType result = recordBuilder.build();

        // Assert
        assertEquals(objectType, result);
    }

    @Test
    public void testBuild_WithNonEmptyRecord_ShouldReturnRecordType() {
        // Arrange
        RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        recordBuilder.addProperty(propertyName, propertyType, null);
        JSType expectedType = new RecordType(registry, Collections.singletonMap(propertyName, new RecordType.RecordProperty(propertyType, null)));

        // Act
        JSType result = recordBuilder.build();

        // Assert
        assertEquals(expectedType, result);
    }
}

