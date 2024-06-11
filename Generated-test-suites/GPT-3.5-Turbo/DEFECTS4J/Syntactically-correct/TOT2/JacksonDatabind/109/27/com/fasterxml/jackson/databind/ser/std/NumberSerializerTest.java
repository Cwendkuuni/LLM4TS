import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializerInstance() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertNotNull(numberSerializer);
    }

    @Test
    public void testNumberSerializerContextual() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BeanProperty property = new BeanProperty.Std();
        try {
            JsonSerializer<?> contextualSerializer = numberSerializer.createContextual(provider, property);
            assertNotNull(contextualSerializer);
        } catch (JsonMappingException e) {
            fail("Exception caught while testing createContextual");
        }
    }

    @Test
    public void testNumberSerializerSerialize() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        try {
            numberSerializer.serialize(10, g, provider);
        } catch (IOException e) {
            fail("Exception caught while testing serialize");
        }
    }

    @Test
    public void testNumberSerializerGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        assertEquals(JsonNodeType.NUMBER, numberSerializer.getSchema(provider, Number.class).getNodeType());
    }

    @Test
    public void testNumberSerializerAcceptJsonFormatVisitor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = TypeFactory.defaultInstance().constructType(Number.class);
        try {
            numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Exception caught while testing acceptJsonFormatVisitor");
        }
    }
}