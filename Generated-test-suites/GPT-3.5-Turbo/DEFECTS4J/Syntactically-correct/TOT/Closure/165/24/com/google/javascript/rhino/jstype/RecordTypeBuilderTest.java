import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

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
        String name = "name";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node(JSTypeNative.STRING_TYPE);

        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);

        assertNotNull(result);
        assertTrue(recordTypeBuilder.build().toString().contains(name));
    }

    @Test
    public void testAddProperty_duplicate() {
        String name = "name";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node(JSTypeNative.STRING_TYPE);

        recordTypeBuilder.addProperty(name, type, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);

        assertNull(result);
    }

    @Test
    public void testBuild_emptyRecord() {
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        JSType result = recordTypeBuilder.build();

        assertEquals(expectedType.toString(), result.toString());
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        String name = "name";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node(JSTypeNative.STRING_TYPE);

        recordTypeBuilder.addProperty(name, type, propertyNode);

        JSType result = recordTypeBuilder.build();

        assertTrue(result.toString().contains(name));
    }

    @Test
    public void testRecordProperty_getType_typical() {
        JSType expectedType = registry.getNativeType(JSTypeNative.NULL_VOID);

        JSType type = registry.getNativeType(JSTypeNative.NULL_VOID);
        Node propertyNode = new Node(JSTypeNative.NULL_VOID);
        RecordProperty recordProperty = new RecordProperty(type, propertyNode);

        JSType result = recordProperty.getType();

        assertEquals(expectedType.toString(), result.toString());
    }

    @Test
    public void testRecordProperty_getPropertyNode_typical() {
        Node expectedNode = new Node(JSTypeNative.STRING_TYPE);

        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node(JSTypeNative.STRING_TYPE);
        RecordProperty recordProperty = new RecordProperty(type, propertyNode);

        Node result = recordProperty.getPropertyNode();

        assertEquals(expectedNode.toString(), result.toString());
    }
}