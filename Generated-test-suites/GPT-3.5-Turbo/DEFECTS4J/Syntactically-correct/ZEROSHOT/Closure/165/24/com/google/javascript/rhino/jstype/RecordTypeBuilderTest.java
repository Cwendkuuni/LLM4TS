import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;
    private JSType type;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
        type = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    }

    @Test
    public void testAddProperty() {
        String name = "property";
        builder.addProperty(name, type, null);
        RecordTypeBuilder.RecordProperty property = builder.properties.get(name);
        assertNotNull(property);
        assertEquals(type, property.getType());
    }

    @Test
    public void testAddPropertyDuplicate() {
        String name = "property";
        builder.addProperty(name, type, null);
        RecordTypeBuilder.RecordProperty property = builder.addProperty(name, type, null);
        assertNull(property);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType recordType = builder.build();
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(objectType, recordType);
    }

    @Test
    public void testBuildRecord() {
        String name = "property";
        builder.addProperty(name, type, null);
        JSType recordType = builder.build();
        RecordType expectedType = new RecordType(registry, Collections.singletonMap(name, new RecordTypeBuilder.RecordProperty(type, null)));
        assertEquals(expectedType, recordType);
    }
}