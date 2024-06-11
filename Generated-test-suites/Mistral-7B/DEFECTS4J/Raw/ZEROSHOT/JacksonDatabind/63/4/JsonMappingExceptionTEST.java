package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException$Reference;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link JsonMappingException}
 */
public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        // Test constructor with message only
        JsonMappingException exception = new JsonMappingException("Message only");
        assertEquals("Message only", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(JsonMappingException.class, exception.getClass());

        // Test constructor with message and cause
        Throwable cause = new IOException("IO exception");
        JsonMappingException exceptionWithCause = new JsonMappingException("Message with cause", cause);
        assertEquals("Message with cause", exceptionWithCause.getMessage());
        assertEquals(cause, exceptionWithCause.getCause());

        // Test constructor with message, cause, and location
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        JsonMappingException exceptionWithLocation = new JsonMappingException(JsonParser.class, "Message with location", cause, location);
        assertEquals("Message with location", exceptionWithLocation.getMessage());
        assertEquals(cause, exceptionWithLocation.getCause());
        assertEquals(location, exceptionWithLocation.getLocation());

        // Test constructor from JsonParser
        JsonParser parser = createMock(JsonParser.class);
        when(parser.getTokenLocation()).thenReturn(location);
        JsonMappingException exceptionFromParser = JsonMappingException.from(parser, "Message from parser");
        assertEquals("Message from parser", exceptionFromParser.getMessage());
        assertEquals(location, exceptionFromParser.getLocation());
        assertEquals(parser, exceptionFromParser.getProcessor());

        // Test constructor from JsonGenerator
        JsonGenerator generator = createMock(JsonGenerator.class);
        JsonMappingException exceptionFromGenerator = JsonMappingException.from(generator, "Message from generator");
        assertEquals("Message from generator", exceptionFromGenerator.getMessage());
        assertNull(exceptionFromGenerator.getLocation());
        assertEquals(generator, exceptionFromGenerator.getProcessor());

        // Test constructor from DeserializationContext
        DeserializationContext context = createMock(DeserializationContext.class);
        JsonParser parserFromContext = createMock(JsonParser.class);
        when(context.getParser()).thenReturn(parserFromContext);
        JsonMappingException exceptionFromContext = JsonMappingException.from(context, "Message from context");
        assertEquals("Message from context", exceptionFromContext.getMessage());
        assertEquals(parserFromContext, exceptionFromContext.getProcessor());
        assertEquals(location, exceptionFromContext.getLocation());

        // Test constructor from SerializerProvider
        JsonGenerator generatorFromContext = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        when(provider.getGenerator()).thenReturn(generatorFromContext);
        JsonMappingException exceptionFromProvider = JsonMappingException.from(provider, "Message from provider");
        assertEquals("Message from provider", exceptionFromProvider.getMessage());
        assertNull(exceptionFromProvider.getLocation());
        assertEquals(generatorFromContext, exceptionFromProvider.getProcessor());

        // Test factory method from IOException
        JsonMappingException exceptionFromIOException = JsonMappingException.fromUnexpectedIOE(new IOException("Unexpected IO exception"));
        assertEquals("Unexpected IO exception (of type IOException): Unexpected IO exception", exceptionFromIOException.getMessage());
        assertNull(exceptionFromIOException.getCause());
        assertEquals(exceptionFromIOException.getClass(), JsonMappingException.class);

        // Test wrapWithPath method with non-indexed object
        JsonProcessingException jsonProcessingException = new JsonProcessingException("JsonProcessingException");
        Object referrer = new Object();
        String fieldName = "fieldName";
        JsonMappingException exceptionWrappedWithPath1 = JsonMappingException.wrapWithPath(jsonProcessingException, referrer, fieldName);
        assertEquals("JsonProcessingException (through reference chain: [Object \"referrer\"->" + fieldName + "])", exceptionWrappedWithPath1.getMessage());
        assertEquals(jsonProcessingException, exceptionWrappedWithPath1.getCause());
        List<JsonMappingException$Reference> path1 = exceptionWrappedWithPath1.getPath();
        assertEquals(1, path1.size());
        JsonMappingException$Reference reference1 = path1.get(0);
        assertEquals(referrer, reference1.getFrom());
        assertEquals(fieldName, reference1.getFieldName());
        assertEquals("Object \"referrer\"", reference1.getDescription());

        // Test wrapWithPath method with indexed object
        JsonProcessingException jsonProcessingExceptionWithArray = new JsonProcessingException("JsonProcessingException with array");
        Object referrerWithArray = new Object[2];
        int index = 0;
        JsonMappingException exceptionWrappedWithPath2 = JsonMappingException.wrapWithPath(jsonProcessingExceptionWithArray, referrerWithArray, index);
        assertEquals("JsonProcessingException (through reference chain: [Object[2] \"referrerWithArray\"->[0]]]", exceptionWrappedWithPath2.getMessage());
        assertEquals(jsonProcessingExceptionWithArray, exceptionWrappedWithPath2.getCause());
        List<JsonMappingException$Reference> path2 = exceptionWrappedWithPath2.getPath();
        assertEquals(1, path2.size());
        JsonMappingException$Reference reference2 = path2.get(0);
        assertEquals(referrerWithArray, reference2.getFrom());
        assertEquals("Object[2] \"referrerWithArray\"", reference2.getDescription());
        assertEquals(index, reference2.getIndex());
    }

    // Add any additional test methods as needed

    /**
     * Helper method to create a mock JsonParser
     */
    private JsonParser createMockJsonParser() {
        return createMock(JsonParser.class);
    }

    /**
     * Helper method to create a mock JsonGenerator
     */
    private JsonGenerator createMockJsonGenerator() {
        return createMock(JsonGenerator.class);
    }

    /**
     * Helper method to create a mock DeserializationContext
     */
    private DeserializationContext createMockDeserializationContext() {
        return createMock(DeserializationContext.class);
    }

    /**
     * Helper method to create a mock SerializerProvider
     */
    private SerializerProvider createMockSerializerProvider() {
        return createMock(SerializerProvider.class);
    }

    /**
     * Helper method to create a mock JsonLocation
     */
    private JsonLocation createMockJsonLocation() {
        return createMock(JsonLocation.class);
    }

    /**
     * Helper method to create a mock ClassUtil
     */
    private ClassUtil createMockClassUtil() {
        return createMock(ClassUtil.class);
    }
}