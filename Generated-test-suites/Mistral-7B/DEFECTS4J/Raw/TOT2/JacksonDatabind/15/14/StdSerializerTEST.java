import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.*;
import static org.junit.Assert.*;

public class StdSerializerTest {

    private ObjectNode testNode;
    private StdSerializer<Object> stdSerializer;
    private JsonSerializer<Object> jsonSerializer;
    private ObjectMapper mapper;
    private JsonFactory factory;
    private JsonSerializerProvider provider;
    private AnnotatedClass<StdSerializer<Object>> annotatedClass;

    @Before
    public void setUp() throws Exception {
        factory = new JsonFactory();
        mapper = new ObjectMapper(factory);
        provider = mapper.getSerializerProvider();
        annotatedClass = new AnnotatedClass<StdSerializer<Object>>(StdSerializer.class);
        stdSerializer = new StdSerializer<Object>(Object.class) {};
        jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.handleValueSequence(any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);
    }

    @Test
    public void testSerialize() throws Exception {
        ObjectNode inputNode = createObjectNode();
        inputNode.put("test", "testValue");

        Object testValue = mapper.readValue(inputNode, Object.class);
        ObjectNode outputNode = createObjectNode();

        stdSerializer.serialize(testValue, factory.createGenerator(System.out), provider);
        stdSerializer.serialize(testValue, new JsonGenerator(outputNode), provider);

        assertEquals(inputNode, outputNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectAnyFormat(any(JavaType.class))).thenReturn(null);

        stdSerializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrongException() throws Exception {
        when(jsonSerializer.handleValueSequence(any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenThrow(new IllegalArgumentException("Wrong exception"));

        stdSerializer.delegate = jsonSerializer;
        stdSerializer.serialize(null, factory.createGenerator(System.out), provider);
    }

    @Test
    public void testIsDefaultSerializer() throws Exception {
        assertTrue(StdSerializerTest.class.getProtectedField(StdSerializer.class, "_handledType").get(stdSerializer)
                instanceof Class<Object>);
        assertFalse(isDefaultSerializer(new MyCustomSerializer<>()));
    }
}