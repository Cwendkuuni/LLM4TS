import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder recordTypeBuilder;

    @BeforeEach
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty_WithValidNameAndType_ShouldReturnBuilderInstance() {
        // Arrange
        String name = "property1";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node(Node.Token.ADD);
        
        // Act
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
        
        // Assert
        Assertions.assertNotNull(result);
    }
    
    @Test
    public void testAddProperty_WithDuplicateName_ShouldReturnNull() {
        // Arrange
        String name = "property1";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node(Node.Token.ADD);
        
        // Act
        recordTypeBuilder.addProperty(name, type, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
        
        // Assert
        Assertions.assertNull(result);
    }
    
    @Test
    public void testBuild_WithNoProperties_ShouldReturnObjectType() {
        // Act
        JSType result = recordTypeBuilder.build();
        
        // Assert
        Assertions.assertEquals(JSTypeNative.OBJECT_TYPE, result);
    }
    
    @Test
    public void testBuild_WithProperties_ShouldReturnRecordTypeWithProperties() {
        // Arrange
        String name1 = "property1";
        JSType type1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode1 = new Node(Node.Token.ADD);
        
        String name2 = "property2";
        JSType type2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode2 = new Node(Node.Token.SUB);
        
        // Act
        recordTypeBuilder.addProperty(name1, type1, propertyNode1);
        recordTypeBuilder.addProperty(name2, type2, propertyNode2);
        JSType result = recordTypeBuilder.build();
        Map<String, RecordTypeBuilder.RecordProperty> properties = ((RecordType) result).getProperties();
        
        // Assert
        Assertions.assertEquals(2, properties.size());
        Assertions.assertEquals(type1, properties.get(name1).getType());
        Assertions.assertEquals(type2, properties.get(name2).getType());
    }

}