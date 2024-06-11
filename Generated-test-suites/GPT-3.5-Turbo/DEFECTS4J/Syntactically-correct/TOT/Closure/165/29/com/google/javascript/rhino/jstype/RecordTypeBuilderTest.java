import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty_typical() {
        // Arrange
        String name = "name";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();

        // Act
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);

        // Assert
        Assert.assertNotNull(result);
        Map<String, RecordProperty> properties = recordTypeBuilder.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.containsKey(name));
        RecordProperty recordProperty = properties.get(name);
        Assert.assertEquals(type, recordProperty.getType());
        Assert.assertEquals(propertyNode, recordProperty.getPropertyNode());
    }

    @Test
    public void testAddProperty_duplicate() {
        // Arrange
        String name = "name";
        JSType type1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        JSType type2 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode1 = new Node();
        Node propertyNode2 = new Node();
        recordTypeBuilder.addProperty(name, type1, propertyNode1);

        // Act
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type2, propertyNode2);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testBuild_emptyRecord() {
        // Act
        JSType result = recordTypeBuilder.build();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        // Arrange
        String name = "name";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty(name, type, propertyNode);

        // Act
        JSType result = recordTypeBuilder.build();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(RecordType.class, result.getClass());
        Map<String, RecordProperty> properties = ((RecordType) result).getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.containsKey(name));
        RecordProperty recordProperty = properties.get(name);
        Assert.assertEquals(type, recordProperty.getType());
        Assert.assertEquals(propertyNode, recordProperty.getPropertyNode());
    }

}