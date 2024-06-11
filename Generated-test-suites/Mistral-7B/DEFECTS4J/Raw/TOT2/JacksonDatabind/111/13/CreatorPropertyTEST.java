import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CreatorPropertyTest {

    @Test
    public void testConstructors() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_PROPERTY_METADATA
        );

        // Test default constructor
        CreatorProperty defaultProperty = new CreatorProperty(property.getName(), property.getType(), null, null, null, null, 0, null, property.getMetadata());
        JsonNode node = mapper.readValue("{\"testProperty\":\"defaultValue\"}", new TypeReference<JsonNode>() {});
        Object result = mapper.treeToValue(node, property.getType());
        assertEquals("defaultValue", result);

        // Test constructor with JsonDeserializer
        JsonDeserializer<String> deserializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "deserializedValue";
            }
        };
        CreatorProperty deserializerProperty = new CreatorProperty(property.getName(), property.getType(), null, null, null, null, 0, null, property.getMetadata(), deserializer);
        result = mapper.treeToValue(node, deserializerProperty.getType());
        assertEquals("deserializedValue", result);

        // Test constructor with TypeDeserializer
        TypeDeserializer typeDeserializer = new StdTypeDeserializer();
        CreatorProperty typeDeserializerProperty = new CreatorProperty(property.getName(), property.getType(), null, null, null, null, 0, null, property.getMetadata(), null, typeDeserializer);
        result = mapper.treeToValue(node, typeDeserializerProperty.getType());
        assertEquals("defaultValue", result); // TypeDeserializer should not affect default value

        // Test constructor with AnnotatedParameter
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(MyClass.class);
        AnnotatedMember member = annotatedClass.findMember("testProperty");
        CreatorProperty annotatedProperty = new CreatorProperty(property.getName(), property.getType(), null, null, null, (AnnotatedParameter) member, 0, null, property.getMetadata());
        result = mapper.treeToValue(node, annotatedProperty.getType());
        assertEquals("defaultValue", result);
    }

    @Test
    public void testInjectableValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyClass bean = new MyClass();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                "testInjectableValueId",
                PropertyMetadata.STANDARD_PROPERTY_METADATA
        );

        // Test findInjectableValue
        Object injectableValue = mapper.findInjectableValue("testInjectableValueId", property, bean);
        assertEquals("injectedValue", injectableValue);

        // Test inject
        mapper.injectValues(bean);
        assertEquals("injectedValue", property.get(bean));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                PropertyMetadata.STANDARD_PROPERTY_METADATA
        );
        MyClass bean = new MyClass();

        // Test deserializeAndSet
        mapper.readValue("{\"testProperty\":\"value\"}", property.getType(), bean);
        property._reportMissingSetter(null, null);
    }

    static class MyClass {
        private String testProperty;

        public String getTestProperty() {
            return testProperty;
        }

        @JsonDeserialize
        public void setTestProperty(String value) {
            this.testProperty = value;
        }
    }
}