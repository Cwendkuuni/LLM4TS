import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder builder;
    private JSTypeRegistry registry;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType(); // Dummy type
        Node propertyNode = new Node(); // Dummy node
        RecordTypeBuilder.RecordProperty property = builder.addProperty("name", type, propertyNode);
        
        assertTrue(builder.isEmpty);
        assertNull(property);
        
        builder.addProperty("name", type, propertyNode);
        assertEquals(type, builder.properties.get("name").getType());
        assertEquals(propertyNode, builder.properties.get("name").getPropertyNode());
        
        builder.addProperty("age", type, propertyNode);
        assertEquals(type, builder.properties.get("age").getType());
        assertEquals(propertyNode, builder.properties.get("age").getPropertyNode());
    }

    @Test
    public void testBuild() {
        JSType type = new JSType(); // Dummy type
        Node propertyNode = new Node(); // Dummy node
        
        builder.addProperty("name", type, propertyNode);
        builder.addProperty("age", type, propertyNode);
        JSType builtType = builder.build();

        assertEquals(type, builtType.getProperties().get("name").getType());
        assertEquals(propertyNode, builtType.getProperties().get("name").getPropertyNode());

        assertEquals(type, builtType.getProperties().get("age").getType());
        assertEquals(propertyNode, builtType.getProperties().get("age").getPropertyNode());
    }
}