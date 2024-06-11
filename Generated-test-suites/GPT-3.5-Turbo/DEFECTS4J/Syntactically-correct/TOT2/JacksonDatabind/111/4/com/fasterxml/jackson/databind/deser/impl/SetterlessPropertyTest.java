import static org.junit.Assert.*;
import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testWithName() {
        // Test with valid PropertyName
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        PropertyName newName = new PropertyName("newName");
        property = (SetterlessProperty) property.withName(newName);
        assertEquals(newName, property.getMember().getName());
    }

    @Test
    public void testWithValueDeserializer() {
        // Test with valid JsonDeserializer
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonDeserializer<?> deser = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
        };
        property = (SetterlessProperty) property.withValueDeserializer(deser);
        assertNotNull(property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        // Test with valid NullValueProvider
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return "";
            }
        };
        property = (SetterlessProperty) property.withNullProvider(nva);
        assertNotNull(property.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        // Test with enabled public access modifiers
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        DeserializationConfig config = new DeserializationConfig(null);
        config.enable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        property.fixAccess(config);
        // Assert true when public access is enabled
        assertTrue(property.getMember().isPublic());
    }

    // Add more test cases for other methods as needed

}