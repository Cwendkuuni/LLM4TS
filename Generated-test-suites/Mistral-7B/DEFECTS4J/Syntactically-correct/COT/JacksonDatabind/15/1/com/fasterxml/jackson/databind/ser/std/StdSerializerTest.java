import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for StdSerializer class")
public class StdSerializerTest {

    @Test
    @DisplayName("Test constructor with Class<T>")
    void testStdSerializerConstructorWithClass() {
        StdSerializer<String> stdSerializer = new StdSerializer<>(String.class);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    @DisplayName("Test constructor with JavaType")
    void testStdSerializerConstructorWithJavaType() {
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        StdSerializer<String> stdSerializer = new StdSerializer<>(javaType);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    @DisplayName("Test constructor with Class<T> and boolean")
    void testStdSerializerConstructorWithClassAndBoolean() {
        StdSerializer<String> stdSerializer = new StdSerializer<>(String.class, false);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    @DisplayName("Test getSchema method with null provider and typeHint")
    void testGetSchemaMethodWithNullProviderAndTypeHint() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        assertNotNull(stdSerializer.getSchema(null, null));
    }

    @Test
    @DisplayName("Test getSchema method with null provider, typeHint and isOptional")
    void testGetSchemaMethodWithNullProviderTypeHintAndIsOptional() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        ObjectNode schema = stdSerializer.getSchema(null, null, false);
        assertNotNull(schema);
        assertEquals("object", schema.get("type").asText());
    }

    @Test
    @DisplayName("Test acceptJsonFormatVisitor method with null provider and typeHint")
    void testAcceptJsonFormatVisitorMethodWithNullProviderAndTypeHint() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        stdSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(null, null), null);
    }

    @Test
    @DisplayName("Test wrapAndThrow method with null provider, non-JsonMappingException t and bean")
    void testWrapAndThrowMethodWithNullProviderNonJsonMappingExceptionAndBean() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        Throwable t = new RuntimeException("Test Exception");
        assertThrows(IOException.class, () -> stdSerializer.wrapAndThrow(null, t, null, null));
    }

    @Test
    @DisplayName("Test wrapAndThrow method with null provider, JsonMappingException t and bean")
    void testWrapAndThrowMethodWithNullProviderJsonMappingExceptionAndBean() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        Throwable t = new JsonMappingException("Test JsonMappingException");
        assertThrows(JsonMappingException.class, () -> stdSerializer.wrapAndThrow(null, t, null, null));
    }

    @Test
    @DisplayName("Test wrapAndThrow method with null provider, non-JsonMappingException t, bean and index")
    void testWrapAndThrowMethodWithNullProviderNonJsonMappingExceptionBeanAndIndex() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        Throwable t = new RuntimeException("Test Exception");
        assertThrows(IOException.class, () -> stdSerializer.wrapAndThrow(null, t, null, 0));
    }

    @Test
    @DisplayName("Test wrapAndThrow method with null provider, JsonMappingException t, bean and index")
    void testWrapAndThrowMethodWithNullProviderJsonMappingExceptionBeanAndIndex() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        Throwable t = new JsonMappingException("Test JsonMappingException");
        assertThrows(JsonMappingException.class, () -> stdSerializer.wrapAndThrow(null, t, null, 0));
    }

    @Test
    @DisplayName("Test isDefaultSerializer method with null serializer")
    void testIsDefaultSerializerMethodWithNullSerializer() {
        assertFalse(StdSerializer.isDefaultSerializer(null));
    }

    @Test
    @DisplayName("Test findConvertingContentSerializer method with null provider, null prop and null existingSerializer")
    void testFindConvertingContentSerializerMethodWithNullProviderNullPropAndNullExistingSerializer() {
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        StdSerializer<ObjectNode> stdSerializer = new StdSerializer<>(ObjectNode.class);
        assertNull(stdSerializer.findConvertingContentSerializer(provider, null, node));
    }

    @Test
    @DisplayName("Test findConvertingContentSerializer method with non-null provider, null prop and null existingSerializer")
    void testFindConvertingContentSerializerMethodWithNonNullProviderNullPropAndNullExistingSerializer() {
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        StdSerializer<ObjectNode> stdSerializer = new StdSerializer<>(ObjectNode.class);
        assertNull(stdSerializer.findConvertingContentSerializer(provider, null, node));
    }

    @Test
    @DisplayName("Test findConvertingContentSerializer method with non-null provider, null prop and non-null existingSerializer")
    void testFindConvertingContentSerializerMethodWithNonNullProviderNullPropAndNonnullExistingSerializer() {
        SerializerProvider provider = new SerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        StdSerializer<ObjectNode> stdSerializer = new StdSerializer<>(ObjectNode.class);
        JsonSerializer<Object> existingSerializer = mapper.findValueSerializer(ObjectNode.class);
        JsonSerializer<Object> convertingSerializer = stdSerializer.findConvertingContentSerializer(provider, null, node);
        assertNotNull(convertingSerializer);
        assertTrue(convertingSerializer instanceof StdDelegatingSerializer);
    }

    @Test
    @DisplayName("Test findPropertyFilter method with null provider, filterId and valueToFilter")
    void testFindPropertyFilterMethodWithNullProviderFilterIdAndValueToFilter() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        assertNull(stdSerializer.findPropertyFilter(null, "testFilterId", null));
    }

    @Test
    @DisplayName("Test findPropertyFilter method with non-null provider, filterId and valueToFilter")
    void testFindPropertyFilterMethodWithNonNullProviderFilterIdAndValueToFilter() {
        SerializerProvider provider = new SerializerProvider();
        FilterProvider filters = provider.getFilterProvider();
        Object valueToFilter = new Object();
        PropertyFilter filter = stdSerializer.findPropertyFilter(provider, "testFilterId", valueToFilter);
        assertEquals("testFilterId", filter.getId());
    }

}