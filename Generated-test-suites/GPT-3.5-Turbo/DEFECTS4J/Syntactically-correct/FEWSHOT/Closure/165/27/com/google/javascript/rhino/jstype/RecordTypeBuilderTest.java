import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        Node propertyNode = new Node();

        // Test adding a property for the first time
        RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
        assertEquals(builder, result);

        // Test adding a duplicate property
        result = builder.addProperty("name", type, propertyNode);
        assertNull(result);
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test building an empty record
        JSType result = builder.build();
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);

        // Test building a non-empty record
        JSType type = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        Node propertyNode = new Node();
        builder.addProperty("name", type, propertyNode);

        result = builder.build();
        assertEquals(new RecordType(registry, Collections.unmodifiableMap(properties)), result);
    }
}