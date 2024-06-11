
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class DateTimeSerializerBaseTest {

    private static class TestDateTimeSerializer extends DateTimeSerializerBase<Long> {

        public TestDateTimeSerializer() {
            super(Long.class, false, null);
        }

        @Override
        public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Long value) {
            return 0;
        }

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {

        }
    }

    // Basic test for createContextual method
    @Test
    public void testCreateContextual() {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertSame(serializer, result);
        Mockito.verifyZeroInteractions(provider, property);
    }

    // Basic test for getSchema method
    @Test
    public void testGetSchema() {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();

        JsonNode result = serializer.getSchema(provider, typeHint);

        assertNotNull(result);
        assertEquals("number", result.get("type").asText());
        assertEquals(true, result.get("format").asBoolean());
        Mockito.verifyZeroInteractions(provider, typeHint);
    }

    // Basic test for acceptJsonFormatVisitor method - as number
    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).getProvider();
        Mockito.verify(visitor).setOutputFormat(JsonValueFormat.UTC_MILLISEC);
        Mockito.verify(visitor).expectsInteger();
        Mockito.verify(visitor).getProvider();
        Mockito.verifyNoMoreInteractions(visitor);
    }

    // Basic test for acceptJsonFormatVisitor method - as string
    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        serializer.withFormat(false, null);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).setOutputFormat(JsonValueFormat.DATE_TIME);
        Mockito.verify(visitor).expectsString();
        Mockito.verify(visitor).getProvider();
        Mockito.verifyNoMoreInteractions(visitor);
    }

    // Basic test for serialize method
    @Test
    public void testSerialize(){
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Long value = 1000L;

        serializer.serialize(value, generator, provider);

        Mockito.verifyZeroInteractions(generator, provider);
    }
    
    // Basic test for isEmpty method
    @Test
    public void testIsEmpty(){
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Long value = 0L;

        boolean result = serializer.isEmpty(provider, value);

        assertFalse(result);
        Mockito.verifyZeroInteractions(generator, provider);
    }

}
