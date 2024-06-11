package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test cases for {@link StdSerializer} class.
 */
public class StdSerializerTest {

    /**
     * Test constructor with Java class.
     */
    @Test
    public void testStdSerializerConstructorWithJavaClass() {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = new StdSerializer<>(stringClass);
        assertEquals(String.class, serializer.handledType());
    }

    /**
     * Test constructor with Java type.
     */
    @Test
    public void testStdSerializerConstructorWithJavaType() {
        JavaType javaStringType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        StdSerializer<String> serializer = new StdSerializer<>(javaStringType);
        assertEquals(String.class, serializer.handledType());
    }

    /**
     * Test constructor with Java class and dummy boolean.
     */
    @Test
    public void testStdSerializerConstructorWithJavaClassAndDummyBoolean() {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = new StdSerializer<>(stringClass, false);
        assertEquals(String.class, serializer.handledType());
    }

    /**
     * Test getSchema method.
     */
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonNode schema = serializer.getSchema(null, null);
        assertEquals("string", schema.get("type").asText());
    }

    /**
     * Test getSchema method with optional.
     */
    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonNode schema = serializer.getSchema(null, null, true);
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.hasNonNull("required") && schema.get("required").asBoolean());
    }

    /**
     * Test createObjectNode method.
     */
    @Test
    public void testCreateObjectNode() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        ObjectNode schema = serializer.createObjectNode();
        assertNotNull(schema);
    }

    /**
     * Test createSchemaNode method with string type.
     */
    @Test
    public void testCreateSchemaNodeWithStringType() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        ObjectNode schema = serializer.createSchemaNode("string");
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }

    /**
     * Test createSchemaNode method with string type and optional.
     */
    @Test
    public void testCreateSchemaNodeWithStringTypeAndOptional() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        ObjectNode schema = serializer.createSchemaNode("string", true);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.hasNonNull("required") && schema.get("required").asBoolean());
    }

    /**
     * Test acceptJsonFormatVisitor method.
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonFormat.Feature.AUTO_UNBOX_NON_NULL, visitor.expectAnyFormat(null).getFormatFeatures().get(JsonFormat.Feature.AUTO_UNBOX_NON_NULL));
    }

    /**
     * Test wrapAndThrow method with null provider and IOException.
     */
    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithNullProviderAndIOException() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Throwable t = new IOException("Error occurred during serialization");
        serializer.wrapAndThrow(null, t, null, null);
    }

    /**
     * Test wrapAndThrow method with null provider and non-IOException.
     */
    @Test
    public void testWrapAndThrowWithNullProviderAndNonIOException() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Throwable t = new RuntimeException("Error occurred during serialization");
        serializer.wrapAndThrow(null, t, null, null);
        assertNull(t);
    }

    /**
     * Test wrapAndThrow method with null provider and JsonMappingException.
     */
    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithNullProviderAndJsonMappingException() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Throwable t = new JsonMappingException("Error occurred during serialization");
        serializer.wrapAndThrow(null, t, null, null);
    }

    /**
     * Test wrapAndThrow method with non-null provider and IOException.
     */
    @Test(expected = IOException.class)
    public void testWrapAndThrowWithNonNullProviderAndIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Throwable t = new IOException("Error occurred during serialization");
        serializer.wrapAndThrow(mapper.getSerializerProvider(), t, null, null);
    }

    /**
     * Test wrapAndThrow method with non-null provider and non-IOException.
     */
    @Test
    public void testWrapAndThrowWithNonNullProviderAndNonIOException() {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Throwable t = new RuntimeException("Error occurred during serialization");
        serializer.wrapAndThrow(mapper.getSerializerProvider(), t, null, null);
        assertNull(t);
    }

    /**
     * Test wrapAndThrow method with non-null provider and JsonMappingException.
     */
    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithNonNullProviderAndJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Throwable t = new JsonMappingException("Error occurred during serialization");
        serializer.wrapAndThrow(mapper.getSerializerProvider(), t, null, null);
    }

    /**
     * Test isDefaultSerializer method.
     */
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        JsonSerializer<Object> anotherSerializer = new CustomSerializer<>();
        assertTrue(serializer.isDefaultSerializer(serializer));
        assertFalse(serializer.isDefaultSerializer(anotherSerializer));
    }

    /**
     * Test findConvertingContentSerializer method with null provider.
     */
    @Test
    public void testFindConvertingContentSerializerWithNullProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        assertNull(serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, null));
    }

    /**
     * Test findConvertingContentSerializer method with non-null provider.
     */
    @Test
    public void testFindConvertingContentSerializerWithNonNullProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        JsonSerializer<List<String>> listSerializer = mapper.findValueSerializer(JavaType.constructParametricType(List.class, String.class));
        StdDelegatingSerializer<List<String>, String> delegatingSerializer = new StdDelegatingSerializer<>(null, JavaType.constructParametricType(List.class, String.class), listSerializer);
        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("propertyName", "testProperty");
        PropertyName propertyName = PropertyName.construct("testProperty", null);
        AnnotatedMember member = PropertyModel.construct(propertyName, new AnnotatedClass(Map.class), null, null);
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        Object propertyDefinition = introspector.findSerializationContentConverter(member);
        Converter<Object, Object> converter = mapper.getConverterInstance(member, propertyDefinition);
        JavaType delegateType = converter.getOutputType(mapper.getTypeFactory());
        assertEquals(List.class, delegateType.getRawClass());
        JsonSerializer<List<String>> expectedSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), propertyDefinition, null);
        assertEquals(delegatingSerializer.getClass(), expectedSerializer.getClass());
    }

    /**
     * Test findPropertyFilter method with null provider.
     */
    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithNullProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Object filterId = "testFilter";
        serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, null);
    }

    /**
     * Test findPropertyFilter method with non-null provider.
     */
    @Test
    public void testFindPropertyFilterWithNonNullProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        Object filterId = "testFilter";
        PropertyFilter propertyFilter = serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, null);
        assertNull(propertyFilter);
    }
}