import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
    
    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType stringType = new JSType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node("VAR");
        RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(stringType, propertyNode);
        assertNull(builder.addProperty("name", stringType, propertyNode));
        assertEquals(builder, builder.addProperty("name", stringType, propertyNode));
    }

    @Test
    public void testBuild() {
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType stringType = new JSType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node("VAR");
        
        assertEquals(objectType, builder.build());
        
        builder.addProperty("name", stringType, propertyNode);
        JSType recordType = new RecordType(registry, Collections.unmodifiableMap(builder.properties));
        assertEquals(recordType, builder.build());
    }
}