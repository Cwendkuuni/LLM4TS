import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FieldPropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StandardSerializer(String.class));
        ObjectMapper mapper = new ObjectMapper(new JsonFactory(), module);

        FieldProperty fieldProperty = new FieldProperty(
                null, // BeanPropertyDefinition
                null, // JavaType
                null, // TypeDeserializer
                null, // Annotations
                null // AnnotatedField
        );
        Object testInstance = new Object();

        // Basic test case
        String value = "test";
        JsonParser parser = mapper.getFactory().createParser("{\"value\": \"" + value + "\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        fieldProperty.deserializeAndSet(parser, context, testInstance);
        assertEquals(value, testInstance.getClass().getField("fieldValue").get(testInstance));

        // Edge case: null value
        parser = mapper.getFactory().createParser("{\"value\": null}");
        fieldProperty.deserializeAndSet(parser, context, testInstance);
        assertNull(testInstance.getClass().getField("fieldValue").get(testInstance));

        // Edge case: empty string value
        parser = mapper.getFactory().createParser("{\"value\": \"\"}");
        fieldProperty.deserializeAndSet(parser, context, testInstance);
        assertNull(testInstance.getClass().getField("fieldValue").get(testInstance));

        // Error case: non-String value type
        ObjectNode node = mapper.createObjectNode();
        node.put("value", 123);
        parser = mapper.getFactory().createParser(node.toString());
        try {
            fieldProperty.deserializeAndSet(parser, context, testInstance);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}