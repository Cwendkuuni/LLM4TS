import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

	private DateTimeSerializerBase<DataObject> serializer;
	private SerializerProvider serializerProvider;
	private JsonSerializer<Object> jsonSerializer;
	private Annotated annotated;
	private BeanProperty beanProperty;
	
	@Before
	public void setUp() {
		serializer = Mockito.mock(DateTimeSerializerBase.class, Mockito.CALLS_REAL_METHODS);
		serializerProvider = Mockito.mock(SerializerProvider.class);
		jsonSerializer = Mockito.mock(JsonSerializer.class);
		annotated = Mockito.mock(Annotated.class);
		beanProperty = Mockito.mock(BeanProperty.class);
	}
	
	@Test
	public void testWithFormat() {
		Boolean useTimestamp = true;
		DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		DateTimeSerializerBase<DataObject> result = serializer.withFormat(useTimestamp, customFormat);
		assertEquals(useTimestamp, result._useTimestamp);
		assertEquals(customFormat, result._customFormat);
	}
	
	@Test
	public void testCreateContextualWithNullPropertyShouldReturnThis() throws JsonMappingException {
		JsonFormat.Value format = null;
		Mockito.when(serializer.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
		serializer.createContextual(serializerProvider, null);
		assertEquals(serializer, serializer.createContextual(serializerProvider, null));
	}
	
	@Test
	public void testCreateContextualWithNumericFormat() throws JsonMappingException {
		JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.NUMBER);
		Mockito.when(serializer.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
		serializer.createContextual(serializerProvider, beanProperty);
		assertEquals(serializer.withFormat(Boolean.TRUE, null), serializer.createContextual(serializerProvider, beanProperty));
	}
	
	@Test
	public void testCreateContextualWithStringFormatWithoutPattern() throws JsonMappingException {
		JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, TimeZone.getTimeZone("UTC"), null, Locale.ENGLISH, null);
		Mockito.when(serializer.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
		Mockito.when(serializerProvider.getLocale()).thenReturn(Locale.ENGLISH);
		Mockito.when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));
		serializer.createContextual(serializerProvider, beanProperty);
		SimpleDateFormat expectedDateFormat = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601, Locale.ENGLISH);
		expectedDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		DateTimeSerializerBase<DataObject> expectedSerializer = serializer.withFormat(Boolean.FALSE, expectedDateFormat);
		assertEquals(expectedSerializer, serializer.createContextual(serializerProvider, beanProperty));
	}
	
	@Test
	public void testCreateContextualWithStringFormatWithPatternAndLocale() throws JsonMappingException {
		JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, TimeZone.getTimeZone("PST"), "yyyy-MM-dd", Locale.GERMANY, null);
		Mockito.when(serializer.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
		Mockito.when(serializerProvider.getLocale()).thenReturn(Locale.GERMANY);
		serializer.createContextual(serializerProvider, beanProperty);
		SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
		expectedDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
		DateTimeSerializerBase<DataObject> expectedSerializer = serializer.withFormat(Boolean.FALSE, expectedDateFormat);
		assertEquals(expectedSerializer, serializer.createContextual(serializerProvider, beanProperty));
	}
	
	@Test
	public void testIsEmptyWithValue() {
		DataObject value = new DataObject();
		value.setTimestamp(new Date().getTime());
		assertFalse(serializer.isEmpty(serializerProvider, value));
	}
	
	@Test
	public void testIsEmptyWithNullValue() {
		DataObject value = null;
		assertTrue(serializer.isEmpty(serializerProvider, value));
	}
	
	@Test
	public void testSerializeAsTimestamp() throws Exception {
		serializer._useTimestamp = true;
		DataObject value = new DataObject();
		value.setTimestamp(new Date().getTime());
		JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializerProvider = objectMapper.getSerializerProviderInstance();
		serializer.serialize(value, jsonGenerator, serializerProvider);
		Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(value.getTimestamp());
	}
	
	@Test
	public void testSerializeAsStringWithDefaultFormat() throws Exception {
		serializer._useTimestamp = null;
		DateFormat dateFormat = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601, Locale.ENGLISH);
		DataObject value = new DataObject();
		Date date = new Date();
		value.setTimestamp(date.getTime());
		JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializerProvider = objectMapper.getSerializerProviderInstance();
		serializer.serialize(value, jsonGenerator, serializerProvider);
		Mockito.verify(jsonGenerator, Mockito.times(1)).writeString(dateFormat.format(date));
	}
	
	@Test
	public void testSerializeAsStringWithCustomFormat() throws Exception {
		serializer._useTimestamp = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		DataObject value = new DataObject();
		Date date = dateFormat.parse("2021-01-01");
		value.setTimestamp(date.getTime());
		JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializerProvider = objectMapper.getSerializerProviderInstance();
		serializer.serialize(value, jsonGenerator, serializerProvider);
		Mockito.verify(jsonGenerator, Mockito.times(1)).writeString(dateFormat.format(date));
	}
	
	@Test
	public void testGetSchemaWithTimestampFormat() {
		serializer._useTimestamp = true;
		Type typeHint = DataObject.class;
		JsonNode result = serializer.getSchema(serializerProvider, typeHint);
		assertEquals("number", result.get("type").asText());
	}
	
	@Test
	public void testGetSchemaWithDefaultStringFormat() {
		serializer._useTimestamp = null;
		Type typeHint = DataObject.class;
		JsonNode result = serializer.getSchema(serializerProvider, typeHint);
		assertEquals("string", result.get("type").asText());
		assertTrue(result.get("format").asText().contains("date-time"));
	}
	
	@Test
	public void testAcceptJsonFormatVisitorWithNumericFormat() throws JsonMappingException {
		JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
		JavaType typeHint = Mockito.mock(JavaType.class);
		serializer._useTimestamp = true;
		serializer.acceptJsonFormatVisitor(visitor, typeHint);
		Mockito.verify(visitor, Mockito.times(1)).ifNumberFormat(typeHint, JsonParser.NumberType.LONG);
		Mockito.verify(visitor, Mockito.times(1)).ifFormatVisitor(typeHint);
	}
	
	@Test
	public void testAcceptJsonFormatVisitorWithStringFormat() throws JsonMappingException {
		JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
		JavaType typeHint = Mockito.mock(JavaType.class);
		serializer._useTimestamp = false;
		serializer.acceptJsonFormatVisitor(visitor, typeHint);
		Mockito.verify(visitor, Mockito.times(1)).ifStringFormat(typeHint);
		Mockito.verify(visitor, Mockito.times(1)).ifFormatVisitor(typeHint);
	}
	
	class DataObject {
		@JsonFormat(shape = JsonFormat.Shape.STRING)
		private Long timestamp;

		public Long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}	
	}
}