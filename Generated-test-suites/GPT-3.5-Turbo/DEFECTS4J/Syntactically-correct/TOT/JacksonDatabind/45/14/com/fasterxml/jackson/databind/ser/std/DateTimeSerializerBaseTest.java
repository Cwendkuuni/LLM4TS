import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializers;
    
    @Mock
    private BeanProperty property;
    
    @Mock
    private Annotated annotated;
    
    @Mock
    private JsonFormat.Value format;
    
    private DateTimeSerializerBaseTestImpl serializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        serializer = new DateTimeSerializerBaseTestImpl(Timestamp.class, true, null);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Timestamp> newSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
        assertNotNull(newSerializer);
        assertEquals(false, newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        when(property.getMember()).thenReturn(annotated);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss");
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.getDefault());
        
        DateTimeSerializerBaseTestImpl newSerializer = (DateTimeSerializerBaseTestImpl) serializer.createContextual(serializers, property);
        assertNotNull(newSerializer);
        assertEquals(false, newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", ((SimpleDateFormat) newSerializer._customFormat).toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        serializer.createContextual(serializers, null);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null));
        
        Timestamp timestamp = new Timestamp(0L);
        assertTrue(serializer.isEmpty(timestamp));
        
        timestamp = new Timestamp(123456789L);
        assertFalse(serializer.isEmpty(timestamp));
    }

    @Test
    public void testIsEmptyWithSerializerProvider() {
        assertTrue(serializer.isEmpty(serializers, null));
        
        Timestamp timestamp = new Timestamp(0L);
        assertTrue(serializer.isEmpty(serializers, timestamp));
        
        timestamp = new Timestamp(123456789L);
        assertFalse(serializer.isEmpty(serializers, timestamp));
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = serializer.getSchema(serializers, Timestamp.class);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        serializer.acceptJsonFormatVisitor(visitor, null);
        
        verify(serializer).visitIntFormat(visitor, null, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        
        Timestamp timestamp = new Timestamp(123456789L);
        
        serializer.serialize(timestamp, gen, serializers);
        
        verify(gen).writeString(anyString());
    }
    
    private class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase<Timestamp> {
        
        public DateTimeSerializerBaseTestImpl(Class<Timestamp> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Timestamp> withFormat(Boolean timestamp, DateFormat customFormat) {
            return this;
        }

        @Override
        protected long _timestamp(Timestamp value) {
            // Do nothing
        }

        @Override
        public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.toString());
        }
        
    }

}