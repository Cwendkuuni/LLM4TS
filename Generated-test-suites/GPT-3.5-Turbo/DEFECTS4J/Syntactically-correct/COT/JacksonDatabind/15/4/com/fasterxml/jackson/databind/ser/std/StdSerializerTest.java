import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class StdSerializerTest {

    @Test
    public void testHandledType() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class, true);
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerialize() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                // do nothing
            }
        };
        assertNull(serializer.serialize(null, null, null));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };
        assertNull(serializer.getSchema(null, null));
    }

    @Test
    public void testGetSchema_optional() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) throws JsonMappingException {
                return null;
            }
        };
        assertNull(serializer.getSchema(null, null, true));
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        assertNotNull(serializer.createObjectNode());
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        assertNotNull(serializer.createSchemaNode("string"));
    }

    @Test
    public void testCreateSchemaNode_optional() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        assertNotNull(serializer.createSchemaNode("string", true));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // do nothing
            }
        };
        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test(expected = IOException.class)
    public void testWrapAndThrow_IOException() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new IOException(), null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrow_JsonMappingException() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new JsonMappingException(""), null, null);
    }

    @Test(expected = Error.class)
    public void testWrapAndThrow_Error() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new Error(), null, null);
    }

    @Test(expected = RuntimeException.class)
    public void testWrapAndThrow_RuntimeException() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new RuntimeException(), null, null);
    }

    @Test(expected = IOException.class)
    public void testWrapAndThrow_IOException_index() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new IOException(), null, 0);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrow_JsonMappingException_index() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new JsonMappingException(""), null, 0);
    }

    @Test(expected = Error.class)
    public void testWrapAndThrow_Error_index() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new Error(), null, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testWrapAndThrow_RuntimeException_index() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new RuntimeException(), null, 0);
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        assertTrue(serializer.isDefaultSerializer(null));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        assertNull(serializer.findConvertingContentSerializer(null, null, null));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        assertNull(serializer.findPropertyFilter(null, null, null));
    }

}