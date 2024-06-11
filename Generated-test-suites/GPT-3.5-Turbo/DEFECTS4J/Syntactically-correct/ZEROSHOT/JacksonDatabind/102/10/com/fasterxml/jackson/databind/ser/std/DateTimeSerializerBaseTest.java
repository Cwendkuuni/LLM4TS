import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {
  @Mock
  private SerializerProvider serializers;
  
  @Mock
  private BeanProperty property;
  
  @Mock
  private JsonFormat.Value format;
  
  @Mock
  private JsonFormatVisitorWrapper visitor;
  
  @Mock
  private JavaType typeHint;
  
  @Mock
  private JsonGenerator gen;
  
  @Mock
  private Date value;
  
  private DateTimeSerializerBase<TestDate> serializer;
  
  @Before
  public void setup() {
    serializer = new DateTimeSerializerBase<TestDate>(TestDate.class, true, null) {
      
      @Override
      public DateTimeSerializerBase<TestDate> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }
      
      @Override
      protected void serialize(TestDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        
      }
      
      @Override
      protected long _timestamp(TestDate value) {
        return 0;
      }
      
      @Override
      protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
        
      }
    };
  }
  
  @Test
  public void testCreateContextual_withTimestampShape_shouldReturnWithFormatTrue() throws JsonMappingException {
    when(property.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), eq(TestDate.class))).thenReturn(format);
    when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
    
    JsonSerializer<?> actual = serializer.createContextual(serializers, property);
    
    assertEquals(serializer.withFormat(true, null), actual);
  }
  
  @Test
  public void testCreateContextual_withPatternAndLocaleShape_shouldReturnWithFormatFalseAndNewSimpleDateFormat() throws JsonMappingException {
    when(property.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), eq(TestDate.class))).thenReturn(format);
    when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
    when(format.hasPattern()).thenReturn(true);
    when(format.hasLocale()).thenReturn(true);
    when(format.getLocale()).thenReturn(Locale.US);
    when(format.hasTimeZone()).thenReturn(false);
    when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());
    when(serializers.getConfig()).thenReturn(mock(SerializationConfig.class));
    when(serializers.getConfig().getDateFormat()).thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    
    JsonSerializer<?> actual = serializer.createContextual(serializers, property);
    
    assertEquals(serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)), actual);
  }
  
  @Test
  public void testCreateContextual_withPatternAndTimeZoneShape_shouldReturnWithFormatFalseAndNewSimpleDateFormat() throws JsonMappingException {
    when(property.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), eq(TestDate.class))).thenReturn(format);
    when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
    when(format.hasPattern()).thenReturn(true);
    when(format.hasLocale()).thenReturn(false);
    when(format.hasTimeZone()).thenReturn(true);
    when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));
    when(serializers.getConfig()).thenReturn(mock(SerializationConfig.class));
    when(serializers.getConfig().getDateFormat()).thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    
    JsonSerializer<?> actual = serializer.createContextual(serializers, property);
    
    assertEquals(serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())), actual);
  }
  
  @Test
  public void testCreateContextual_withLocaleShape_shouldReturnWithFormatFalseAndNewSimpleDateFormat() throws JsonMappingException {
    when(property.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), eq(TestDate.class))).thenReturn(format);
    when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
    when(format.hasPattern()).thenReturn(false);
    when(format.hasLocale()).thenReturn(true);
    when(format.getLocale()).thenReturn(Locale.US);
    when(format.hasTimeZone()).thenReturn(false);
    when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());
    when(serializers.getConfig()).thenReturn(mock(SerializationConfig.class));
    when(serializers.getConfig().getDateFormat()).thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    
    JsonSerializer<?> actual = serializer.createContextual(serializers, property);
    
    assertEquals(serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)), actual);
  }
  
  @Test
  public void testCreateContextual_withTimeZoneShape_shouldReturnWithFormatFalseAndNewSimpleDateFormat() throws JsonMappingException {
    when(property.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), eq(TestDate.class))).thenReturn(format);
    when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
    when(format.hasPattern()).thenReturn(false);
    when(format.hasLocale()).thenReturn(false);
    when(format.hasTimeZone()).thenReturn(true);
    when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));
    when(serializers.getConfig()).thenReturn(mock(SerializationConfig.class));
    when(serializers.getConfig().getDateFormat()).thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    
    JsonSerializer<?> actual = serializer.createContextual(serializers, property);
    
    assertEquals(serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())), actual);
  }
  
  @Test
  public void testCreateContextual_withoutChanges_shouldReturnCurrentSerializer() throws JsonMappingException {
    when(property.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), eq(TestDate.class))).thenReturn(format);
    when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
    when(format.hasPattern()).thenReturn(false);
    when(format.hasLocale()).thenReturn(false);
    when(format.hasTimeZone()).thenReturn(false);
    
    JsonSerializer<?> actual = serializer.createContextual(serializers, property);
    
    assertEquals(serializer, actual);
  }
  
  @Test
  public void testSerialize_withCustomDateFormat_shouldSerializeWithCustomDateFormat() throws IOException {
    SimpleDateFormat customFormat = new SimpleDateFormat("yyyy/MM/dd");
    serializer = serializer.withFormat(false, customFormat);
    when(serializers.getConfig()).thenReturn(mock(SerializationConfig.class));
    when(serializers.getConfig().getDateFormat()).thenReturn(customFormat);
    
    serializer.serialize(value, gen, serializers);
    
    verify(gen).writeString(customFormat.format(value));
  }
  
  @Test
  public void testSerialize_withoutCustomDateFormat_shouldDefaultSerialize() throws IOException {
    when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
    
    serializer.serialize(value, gen, serializers);
    
    verify(serializers).defaultSerializeDateValue(value, gen);
  }
  
  @Test
  public void testIsEmpty_withEmptyValue_shouldReturnFalse() {
    when(serializer._timestamp(value)).thenReturn(0L);
    
    boolean actual = serializer.isEmpty(serializers, value);
    
    assertEquals(false, actual);
  }
  
  @Test
  public void testGetSchema_withTimestampShape_shouldReturnNumberSchemaNode() {
    when(serializer._asTimestamp(serializers)).thenReturn(true);
    
    JsonNode actual = serializer.getSchema(serializers, null);
    
    assertEquals("number", actual.get("type").textValue());
  }
  
  @Test
  public void testGetSchema_withNonTimestampShape_shouldReturnStringSchemaNode() {
    when(serializer._asTimestamp(serializers)).thenReturn(false);
    
    JsonNode actual = serializer.getSchema(serializers, null);
    
    assertEquals("string", actual.get("type").textValue());
  }
  
  @Test
  public void testAcceptJsonFormatVisitor_withTimestampSerializer_shouldVisitIntFormat() throws JsonMappingException {
    when(visitor.getProvider()).thenReturn(serializers);
    when(visitor.getProvider().getTypeFactory()).thenReturn(mock(TypeFactory.class));
    when(visitor.getProvider().getTypeFactory().constructType(TestDate.class)).thenReturn(typeHint);
    when(serializer._asTimestamp(visitor.getProvider())).thenReturn(true);
    
    serializer.acceptJsonFormatVisitor(visitor, typeHint);
    
    verify(visitor).expectIntFormat(typeHint, JsonParser.NumberType.LONG);
  }
  
  @Test
  public void testAcceptJsonFormatVisitor_withNonTimestampSerializer_shouldVisitStringFormat() throws JsonMappingException {
    when(visitor.getProvider()).thenReturn(serializers);
    when(visitor.getProvider().getTypeFactory()).thenReturn(mock(TypeFactory.class));
    when(visitor.getProvider().getTypeFactory().constructType(TestDate.class)).thenReturn(typeHint);
    when(serializer._asTimestamp(visitor.getProvider())).thenReturn(false);
    
    serializer.acceptJsonFormatVisitor(visitor, typeHint);
    
    verify(visitor).expectStringFormat(typeHint);
  }
  
  @Test
  public void testSerializeAsString_withoutCustomDateFormat_shouldDefaultSerializeDateValue() throws IOException {
    when(serializers.getConfig().getDateFormat()).thenReturn(null);
    
    serializer._serializeAsString(value, gen, serializers);
    
    verify(serializers).defaultSerializeDateValue(value, gen);
  }
  
  @Test
  public void testSerializeAsString_withCustomDateFormat_shouldSerializeWithCustomDateFormat() throws IOException {
    SimpleDateFormat customFormat = new SimpleDateFormat("yyyy/MM/dd");
    when(serializer._reusedCustomFormat.getAndSet(null)).thenReturn(null);
    when(serializer._reusedCustomFormat.compareAndSet(null, null)).thenReturn(true);
    when(serializer._customFormat.clone()).thenReturn(customFormat);
    
    serializer._serializeAsString(value, gen, serializers);
    
    verify(gen).writeString(customFormat.format(value));
  }
  
  private static class TestDate extends Date {
    private static final long serialVersionUID = 1L;
  }
}