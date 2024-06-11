import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {
    
    @Mock
    private SerializerProvider provider;
    
    @Mock
    private BeanProperty property;
    
    private DateTimeSerializerBase<Object> dateTimeSerializer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dateTimeSerializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
        };
    }
    
    @Test
    public void testCreateContextual_WithNullProperty_ShouldReturnSelf() throws JsonMappingException {
        when(provider.getAnnotationIntrospector()).thenReturn(new JacksonAnnotationIntrospector());
        JsonSerializer<?> result = dateTimeSerializer.createContextual(provider, null);
        assertThat(result, sameInstance(dateTimeSerializer));
    }
    
    @Test
    public void testCreateContextual_WithNullFormat_ShouldReturnSelf() throws JsonMappingException {
        when(provider.getAnnotationIntrospector()).thenReturn(new JacksonAnnotationIntrospector());
        JsonFormat.Value format = new JsonFormat.Value();
        when(provider.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));
        when(provider.getLocale()).thenReturn(Locale.getDefault());
        when(property.getMember()).thenReturn(null);
        when(provider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        
        JsonSerializer<?> result = dateTimeSerializer.createContextual(provider, property);
        
        assertThat(result, sameInstance(dateTimeSerializer));
    }
    
    @Test
    public void testCreateContextual_WithNumericFormat_ShouldReturnWithNumericFormat() throws JsonMappingException {
        when(provider.getAnnotationIntrospector()).thenReturn(new JacksonAnnotationIntrospector());
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.NUMBER);
        when(property.getMember()).thenReturn(null);
        when(provider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        
        JsonSerializer<?> result = dateTimeSerializer.createContextual(provider, property);
        
        assertThat(result, instanceOf(DateTimeSerializerBase.class));
        assertThat(((DateTimeSerializerBase) result)._useTimestamp, is(true));
        assertThat(((DateTimeSerializerBase) result)._customFormat, nullValue());
    }
    
    @Test
    public void testCreateContextual_WithStringFormatWithPattern_ShouldReturnWithStringFormatWithPattern() throws JsonMappingException {
        when(provider.getAnnotationIntrospector()).thenReturn(new JacksonAnnotationIntrospector());
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        format = format.withPattern("yyyy-MM-dd");
        when(property.getMember()).thenReturn(null);
        when(provider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        when(provider.getTimeZone()).thenReturn(TimeZone.getTimeZone("PST"));
        
        JsonSerializer<?> result = dateTimeSerializer.createContextual(provider, property);
        
        assertThat(result, instanceOf(DateTimeSerializerBase.class));
        assertThat(((DateTimeSerializerBase) result)._useTimestamp, is(false));
        assertThat(((DateTimeSerializerBase) result)._customFormat, instanceOf(SimpleDateFormat.class));
    }
    
    @Test
    public void testCreateContextual_WithStringFormatWithoutPattern_ShouldReturnWithStringFormatWithDefaultPattern() throws JsonMappingException {
        when(provider.getAnnotationIntrospector()).thenReturn(new JacksonAnnotationIntrospector());
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        when(property.getMember()).thenReturn(null);
        when(provider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        when(provider.getTimeZone()).thenReturn(TimeZone.getTimeZone("PST"));
        when(provider.getLocale()).thenReturn(Locale.getDefault());
        
        JsonSerializer<?> result = dateTimeSerializer.createContextual(provider, property);
        
        assertThat(result, instanceOf(DateTimeSerializerBase.class));
        assertThat(((DateTimeSerializerBase) result)._useTimestamp, is(false));
        assertThat(((DateTimeSerializerBase) result)._customFormat, instanceOf(SimpleDateFormat.class));
    }
    
    @Test
    public void testIsEmpty_WithNullValue_ShouldReturnTrue() {
        boolean result = dateTimeSerializer.isEmpty(null);
        assertThat(result, is(true));
    }
    
    @Test
    public void testIsEmpty_WithZeroTimestampValue_ShouldReturnTrue() {
        boolean result = dateTimeSerializer.isEmpty(provider, new Object());
        assertThat(result, is(true));
    }
    
    @Test
    public void testGetSchema_WithTimestampAsNumber_ShouldReturnSchemaWithNumberType() {
        JsonNode schema = dateTimeSerializer.getSchema(provider, Object.class);
        assertThat(schema.get("type").asText(), is("number"));
    }
    
    @Test
    public void testGetSchema_WithTimestampAsString_ShouldReturnSchemaWithStringType() {
        when(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        JsonNode schema = dateTimeSerializer.getSchema(provider, Object.class);
        assertThat(schema.get("type").asText(), is("string"));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WithTimestampAsNumber_ShouldVisitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        
        dateTimeSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).visitIntFormat(eq(JsonParser.NumberType.LONG), any(JsonValueFormat.class));
        verifyNoMoreInteractions(visitor);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WithTimestampAsString_ShouldVisitStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        
        dateTimeSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).visitStringFormat(eq(JsonValueFormat.DATE_TIME));
        verifyNoMoreInteractions(visitor);
    }
    
    @Test
    public void testSerialize_WithNonNullValue_ShouldSerializeValue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        dateTimeSerializer.serialize(new Object(), gen, serializers);
        
        verify(gen).writeNumber(anyLong());
        verifyNoMoreInteractions(gen);
    }
}