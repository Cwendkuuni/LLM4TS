
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class DateTimeSerializerBaseTest {
    @Test
    public void testConstructor() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        assertTrue(serializer._useTimestamp);
        assertNull(serializer._customFormat);
        
        serializer = new DateTimeSerializerBaseImpl(String.class, false, new SimpleDateFormat());
        assertFalse(serializer._useTimestamp);
        assertNotNull(serializer._customFormat);
    }
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        DateTimeSerializerBase<String> updatedSerializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat());
        
        assertFalse(updatedSerializer._useTimestamp);
        assertNotNull(updatedSerializer._customFormat);
    }
    
    @Test
    public void testCreateContextualWithNullProperty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        JsonSerializer<?> updatedSerializer = serializer.createContextual(null, null);
        
        assertTrue(updatedSerializer instanceof DateTimeSerializerBase);
        assertEquals(serializer, updatedSerializer);
    }
    
    @Test
    public void testCreateContextualWithNumericShape() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, false, null);
        JsonSerializer<?> updatedSerializer = serializer.createContextual(null, null);
        
        assertTrue(updatedSerializer instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase<?>) updatedSerializer)._useTimestamp);
    }
    
    @Test
    public void testCreateContextualWithStringShape() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, false, null);
        JsonSerializer<?> updatedSerializer = serializer.createContextual(null, null);
        
        assertTrue(updatedSerializer instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase<?>) updatedSerializer)._useTimestamp);
        assertNotNull(((DateTimeSerializerBase<?>) updatedSerializer)._customFormat);
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        
        assertTrue(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty("value"));
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        
        JsonNode schemaNode = serializer.getSchema(null, null);
        
        assertNotNull(schemaNode);
        assertEquals("number", schemaNode.get("type").asText());
        assertTrue(schemaNode.get("required").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).getProvider();
        Mockito.verify(visitor).setProvider(Mockito.any(SerializerProvider.class));
        Mockito.verify(visitor).expectNumberFormat(typeHint, JsonParser.NumberType.LONG);
    }
    
    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        String value = "2020-01-01T00:00:00Z";
        serializer.serialize(value, gen, provider);
        
        Mockito.verify(gen).writeString(value);
    }
}

class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<String> {
    
    public DateTimeSerializerBaseImpl(Class<String> type, Boolean useTimestamp, DateFormat customFormat) {
        super(type, useTimestamp, customFormat);
    }

    @Override
    public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return new DateTimeSerializerBaseImpl(handledType(), timestamp, customFormat);
    }

    @Override
    protected long _timestamp(String value) {
        return 0;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value);
    }
}
