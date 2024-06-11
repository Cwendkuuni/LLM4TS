import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdSerializerTest {

    private ObjectMapper objectMapper;
    private StdSerializer<String> stdSerializer;
    private JsonSerializer<Object> defaultSerializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", "string");
        JsonNode schema = node;
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        when(stringSerializer.getSchema(any(SerializerProvider.class), any(Type.class)))
                .thenReturn(schema);
        stdSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };
        when(stringSerializer.handledType()).thenReturn(String.class);
        defaultSerializer = spy(stringSerializer);
    }

    @Test
    public void testHandleType() {
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class);
        assertEquals(Integer.class, intSerializer.handledType());
    }

    @Test
    public void testAlternateConstructor() throws Exception {
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class, false);
        assertEquals(Integer.class, intSerializer.handledType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectNode schemaNode = createSchemaNode();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectAnyFormat(any(JavaType.class))).thenReturn(null);
        stdSerializer.acceptJsonFormatVisitor(visitor, null);
        verify(visitor, times(1)).expectAnyFormat(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaWithoutProvider() throws JsonProcessingException {
        stdSerializer.getSchema(null);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectNode schema = stdSerializer.getSchema(serializerProvider, null);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonProcessingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectNode schema = stdSerializer.getSchema(serializerProvider, null, true);
        assertEquals("string", schema.get("type").asText());
        assertEquals(false, schema.get("required").booleanValue());
    }

    @Test
    public void testCreateObjectNode() {
        ObjectNode schema = stdSerializer.createObjectNode();
        assertEquals(ObjectNode.class, schema.getClass());
    }

    @Test
    public void testCreateSchemaNode() {
        ObjectNode schema = stdSerializer.createSchemaNode("number");
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        Object bean = new Object();
        String fieldName = "field";
        Throwable t = new RuntimeException("Test error");
        stdSerializer.wrapAndThrow(null, t, bean, fieldName);
        throw t;
    }

    @Test
    public void testIsDefaultSerializer() {
        assertTrue(stdSerializer.isDefaultSerializer(defaultSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer()
            throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty prop = mock(BeanProperty.class);
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        Object convDef = new Object() {};
        Converter<Object, Object> converter = mock(Converter.class);
        when(annotationIntrospector.findSerializationContentConverter(any()))
                .thenReturn(convDef);
        when(provider.converterInstance(any(), eq(convDef))).thenReturn(converter);
        when(converter.getOutputType(any(TypeFactory.class))).thenReturn(Object.class);
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
        when(converter.getSerializerProvider().findValueSerializer(Object.class))
                .thenReturn(jsonSerializer);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", "number");
        JsonNode schema = objectMapper.treeToNode(node);
        when(prop.getMember()).thenReturn(null);
        JsonSerializer<String> serializer = spy(stdSerializer);
        Object result = serializer.findConvertingContentSerializer(serializerProvider, prop, null);
        assertEquals(jsonSerializer, result);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        Object filterId = new Object() {};
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Object valueToFilter = new Object();
        PropertyFilter propertyFilter = mock(PropertyFilter.class);
        FilterProvider filterProvider = mock(FilterProvider.class);
        when(filterProvider.findPropertyFilter(any(), any())).thenReturn(propertyFilter);
        when(serializerProvider.getFilterProvider()).thenReturn(filterProvider);
        JsonSerializer<String> jsonSerializer = spy(stdSerializer);
        Object result = jsonSerializer.findPropertyFilter(serializerProvider, filterId, valueToFilter);
        assertEquals(propertyFilter, result);
    }

    private ObjectNode createSchemaNode() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", "string");
        return node;
    }
}