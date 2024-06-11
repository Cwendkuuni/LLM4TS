
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder builder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node("property");
        assertNull(builder.addProperty("name", type, propertyNode));
        assertEquals(type, builder.addProperty("name", type, propertyNode).build().getPropertyType("name"));
    }

    @Test
    public void testBuild() {
        JSType recordType = builder.build();
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
    }

    @Test
    public void testBuildWithProperties() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node("property");
        builder.addProperty("name1", type, propertyNode);
        builder.addProperty("name2", type, propertyNode);
        JSType recordType = builder.build();
        assertEquals(type, recordType.getPropertyType("name1"));
        assertEquals(type, recordType.getPropertyType("name2"));
    }

    @Test
    public void testRecordProperty() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node("property");
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);
        assertEquals(type, recordProperty.getType());
        assertEquals(propertyNode, recordProperty.getPropertyNode());
    }
}
