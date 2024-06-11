
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty_basic() {
        // Arrange
        JSType type = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        Node propertyNode = new Node();

        // Act
        RecordTypeBuilder result = recordTypeBuilder.addProperty("testProperty", type, propertyNode);

        // Assert
        assertEquals(recordTypeBuilder, result);
        assertFalse(recordTypeBuilder.isEmpty);
        assertEquals(1, recordTypeBuilder.properties.size());
        assertTrue(recordTypeBuilder.properties.containsKey("testProperty"));
        assertEquals(type, recordTypeBuilder.properties.get("testProperty").getType());
        assertEquals(propertyNode, recordTypeBuilder.properties.get("testProperty").getPropertyNode());
    }

    @Test
    public void testAddProperty_duplicate() {
        // Arrange
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        RecordTypeBuilder result = recordTypeBuilder.addProperty("testProperty", type, propertyNode);

        // Act
        result = recordTypeBuilder.addProperty("testProperty", type, propertyNode);

        // Assert
        assertNull(result);
        assertFalse(recordTypeBuilder.isEmpty);
        assertEquals(1, recordTypeBuilder.properties.size());
        assertTrue(recordTypeBuilder.properties.containsKey("testProperty"));
        assertEquals(type, recordTypeBuilder.properties.get("testProperty").getType());
        assertEquals(propertyNode, recordTypeBuilder.properties.get("testProperty").getPropertyNode());
    }

    @Test
    public void testBuild_emptyRecord() {
        // Arrange
        RecordType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        recordTypeBuilder.isEmpty = true;

        // Act
        JSType result = recordTypeBuilder.build();

        // Assert
        assertEquals(expectedRecordType, result);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        // Arrange
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("testProperty", type, propertyNode);
        RecordType expectedRecordType = new RecordType(registry, Collections.unmodifiableMap(recordTypeBuilder.properties));

        // Act
        JSType result = recordTypeBuilder.build();

        // Assert
        assertEquals(expectedRecordType, result);
    }
}
