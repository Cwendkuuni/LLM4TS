import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
    
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty_ValidNameAndType_ReturnsBuilder() {
        // Arrange
        String name = "propertyName";
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        // Act
        RecordTypeBuilder returnedBuilder = recordTypeBuilder.addProperty(name, type, propertyNode);
        
        // Assert
        assertNotNull(returnedBuilder);
    }

    @Test
    public void testAddProperty_DuplicateName_ReturnsNull() {
        // Arrange
        String name = "propertyName";
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty(name, type, propertyNode);
        
        // Act
        RecordTypeBuilder returnedBuilder = recordTypeBuilder.addProperty(name, type, propertyNode);
        
        // Assert
        assertNull(returnedBuilder);
    }

    @Test
    public void testBuild_EmptyRecord_ReturnsObjectType() {
        // Act
        JSType result = recordTypeBuilder.build();
        
        // Assert
        assertEquals(JSTypeNative.OBJECT_TYPE, result);
    }

    @Test
    public void testBuild_NonEmptyRecord_ReturnsRecordType() {
        // Arrange
        String name = "propertyName";
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty(name, type, propertyNode);
        
        // Act
        JSType result = recordTypeBuilder.build();
        
        // Assert
        assertTrue(result instanceof RecordType);
    }

    @Test
    public void testBuild_NonEmptyRecord_ReturnsRecordTypeWithProperties() {
        // Arrange
        String name = "propertyName";
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty(name, type, propertyNode);
        
        // Act
        JSType result = recordTypeBuilder.build();
        RecordType recordType = (RecordType) result;
        
        // Assert
        assertNotNull(recordType.getProperties());
        assertEquals(1, recordType.getProperties().size());
        assertTrue(recordType.getProperties().containsKey(name));
        assertEquals(type, recordType.getProperties().get(name).getType());
    }
}