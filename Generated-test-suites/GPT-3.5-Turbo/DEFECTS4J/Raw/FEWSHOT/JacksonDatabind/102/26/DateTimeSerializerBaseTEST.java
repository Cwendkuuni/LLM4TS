package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class DateTimeSerializerBaseTest {
	
	private DateTimeSerializerBase<Date> serializer;
	private JsonSerializer<?> mockSerializer;
	private SerializerProvider mockProvider;
	private JsonGenerator mockGenerator;
	private DateFormat mockDateFormat;
	private AtomicReference<DateFormat> mockReference;
	
	@Before
	public void setup() {
		mockSerializer = mock(JsonSerializer.class);
		mockProvider = mock(SerializerProvider.class);
		mockGenerator = mock(JsonGenerator.class);
		mockDateFormat = mock(DateFormat.class);
		mockReference = mock(AtomicReference.class);
		
		serializer = new DateTimeSerializerBase<Date>(Date.class, true, mockDateFormat) {

			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
			
		};
		
		serializer._reusedCustomFormat = mockReference;
	}
	
	@Test
	public void testEmpty() {
		assertFalse(serializer.isEmpty(mockProvider, new Date()));
	}
	
	@Test
	public void testGetSchemaTimestamp() {
		JsonNode schema = serializer.getSchema(mockProvider, Date.class);
		assertEquals("number", schema.get("type").asText());
		assertTrue(schema.get("required").asBoolean());
	}
	
	@Test
	public void testGetSchemaCustomFormat() {
		serializer._asTimestamp = false;
		when(mockProvider.getDateFormat()).thenReturn(mockDateFormat);
		JsonNode schema = serializer.getSchema(mockProvider, Date.class);
		assertEquals("string", schema.get("type").asText());
		assertTrue(schema.get("required").asBoolean());
	}
	
	@Test
	public void testAcceptJsonFormatVisitorTimestamp() throws JsonMappingException {
		JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
		JavaType mockType = mock(JavaType.class);
		
		serializer._isAsTimestamp = true;
		serializer._acceptJsonFormatVisitor(mockVisitor, mockType);
		
		verify(mockVisitor).getProvider();
		verify(mockVisitor).expectNumberFormat(mockType, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
	}
	
	@Test
	public void testAcceptJsonFormatVisitorCustomFormat() throws JsonMappingException {
		JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
		JavaType mockType = mock(JavaType.class);
		
		serializer._isAsTimestamp = false;
		serializer._acceptJsonFormatVisitor(mockVisitor, mockType);
		
		verify(mockVisitor).getProvider();
		verify(mockVisitor).expectStringFormat(mockType, JsonValueFormat.DATE_TIME);
	}
	
	@Test
	public void testSerializeTimestamp() throws IOException {
		serializer._isAsTimestamp = true;
		serializer.serialize(new Date(), mockGenerator, mockProvider);
		
		verify(mockProvider).defaultSerializeDateValue(any(Date.class), eq(mockGenerator));
	}
	
	@Test
	public void testSerializeCustomFormat() throws IOException {
		serializer._isAsTimestamp = false;
		when(mockProvider.getConfig()).thenReturn(mock(SerializationConfig.class));
		when(mockProvider.getLocale()).thenReturn(Locale.getDefault());
		when(mockProvider.getTimeZone()).thenReturn(TimeZone.getDefault());
		when(mockDateFormat.clone()).thenReturn(mockDateFormat);
		when(mockDateFormat.format(any(Date.class))).thenReturn("");
		
		serializer._serializeAsString(new Date(), mockGenerator, mockProvider);
		
		verify(mockGenerator).writeString("");
		verify(mockDateFormat).format(any(Date.class));
		verify(mockReference).compareAndSet(eq(null), eq(mockDateFormat));
	}

}