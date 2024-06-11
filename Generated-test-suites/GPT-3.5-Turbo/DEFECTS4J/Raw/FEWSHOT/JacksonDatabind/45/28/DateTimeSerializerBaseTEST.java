
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
	
	private class MockDateTimeSerializer<T> extends DateTimeSerializerBase<T> {
		
		private boolean isSerializeMethodCalled;

		public MockDateTimeSerializer(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
			super(type, useTimestamp, customFormat);
		}

		@Override
		public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
			return null;
		}

		@Override
		public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) {
			isSerializeMethodCalled = true;
		}

		@Override
		protected long _timestamp(T value) {
			return 0L;
		}
        
        public boolean isSerializeMethodCalled() {
        	return isSerializeMethodCalled;
        }
	}

	@Test
	public void testCreateContextualWithNumericTimestamp() throws JsonMappingException {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, null, null);
		JsonSerializer<?> result = serializer.createContextual(null, null);
		assertEquals(Boolean.TRUE, result.withFormat);
	}

	@Test
	public void testCreateContextualWithDateFormat() throws JsonMappingException {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, null, null);
		
		JsonFormat.Value format = new JsonFormat.Value();
		format.setShape(JsonFormat.Shape.STRING);
		
		MockBeanProperty property = new MockBeanProperty();
		property.addMember(format);
		
		JsonSerializer<?> result = serializer.createContextual(null, property);
		assertEquals(Boolean.FALSE, result.withFormat);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateContextualWithNullProperty() throws JsonMappingException {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, null, null);
		serializer.createContextual(null, null);
	}

	@Test
	public void testIsEmptyWithValueIsNull() {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, null, null);
		boolean result = serializer.isEmpty((String) null);
		assertEquals(true, result);
	}

	@Test
	public void testIsEmptyWithValueIsNotNull() {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, null, null);
		boolean result = serializer.isEmpty("");
		assertEquals(true, result);
	}

	@Test
	public void testIsEmptyWithSerializerProviderIsNull() {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, null, null);
		boolean result = serializer.isEmpty(null, "");
		assertEquals(true, result);
	}

	@Test
	public void testIsEmptyWithSerializerProviderIsNotNull() {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, null, null);
		JsonSerializer<String> jsonSerializer = new MockStringSerializer();
		boolean result = serializer.isEmpty(jsonSerializer, "");
		assertEquals(true, result);
	}

	@Test
	public void testGetSchemaWithTimeStamp() {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, true, null);
		
		SerializerProvider serializers = new SerializerProvider();
		Type type = new Type();
		
		JsonNode result = serializer.getSchema(serializers, type);
		assertEquals("number", result.getType());
	}

	@Test
	public void testGetSchemaWithDateTime() {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, false, null);
		
		SerializerProvider serializers = new SerializerProvider();
		Type type = new Type();
		
		JsonNode result = serializer.getSchema(serializers, type);
		assertEquals("string", result.getType());
	}

	@Test
	public void testGetSchemaWithNullType() {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, false, null);
		
		SerializerProvider serializers = new SerializerProvider();
		
		JsonNode result = serializer.getSchema(serializers, null);
		assertEquals("string", result.getType());
	}

	@Test
	public void testAcceptJsonFormatVisitorWithNumericTimestamp() throws JsonMappingException {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, true, null);
		
		JsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
		JavaType typeHint = new JavaType();
		
		serializer.acceptJsonFormatVisitor(visitor, typeHint);
		
		MockVisitorWithMember visitorWithMember = (MockVisitorWithMember) visitor;
		assertEquals(JsonParser.NumberType.LONG, visitorWithMember.getVisitingType());
		assertEquals(JsonValueFormat.UTC_MILLISEC, visitorWithMember.getVisitingFormat());
	}

	@Test
	public void testAcceptJsonFormatVisitorWithDateTime() throws JsonMappingException {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, false, null);
		
		JsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
		JavaType typeHint = new JavaType();
		
		serializer.acceptJsonFormatVisitor(visitor, typeHint);
		
		MockVisitorWithMember visitorWithMember = (MockVisitorWithMember) visitor;
		assertEquals(JsonValueFormat.DATE_TIME, visitorWithMember.getVisitingFormat());
	}

	@Test
	public void testSerialize() throws IOException {
		MockDateTimeSerializer<String> serializer = new MockDateTimeSerializer<>(String.class, false, null);
		
		JsonGenerator gen = new MockJsonGenerator();
		SerializerProvider serializers = new SerializerProvider();
		
		serializer.serialize("2021-05-01T12:00:00Z", gen, serializers);
		
		assertEquals(true, serializer.isSerializeMethodCalled());
	}

	class MockStringSerializer extends JsonSerializer<String> {
		@Override
		public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			// TODO Auto-generated method stub
			
		}
	}

	class MockJsonGenerator extends JsonGenerator {
		@Override
		public void flush() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeStartArray(int arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeEndArray() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeStartObject() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeEndObject() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeFieldName(String arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeString(String arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeNumber(int arg0) throws IOException {
			// TODO Auto-generated method stub
			
		}
	}

	class MockJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper {
		@Override
		public JsonArrayFormatVisitor expectArrayFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonBooleanFormatVisitor expectBooleanFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonIntegerFormatVisitor expectIntegerFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonMapFormatVisitor expectMapFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonNullFormatVisitor expectNullFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonNumberFormatVisitor expectNumberFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonObjectFormatVisitor expectObjectFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonStringFormatVisitor expectStringFormat(JavaType arg0) throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class MockVisitorWithMember extends JsonFormatVisitorWrapper.Base implements JsonNumberFormatVisitor {

		private JsonParser.NumberType type;
		private JsonValueFormat format;
		
		@Override
		public void numberType(JsonParser.NumberType foundType) {
			type = foundType;
		}

		@Override
		public void format(JsonValueFormat format) {
			this.format = format;
		}
		
		public JsonParser.NumberType getVisitingType() {
			return type;
		}
		
		public JsonValueFormat getVisitingFormat() {
			return format;
		}
	}

	class MockBeanProperty extends BeanProperty {
		private JsonFormat.Value format;

		public void addMember(JsonFormat.Value format) {
			this.format = format;
		}
		
		@Override
		public <A extends Annotation> A getAnnotation(Class<A> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <A extends Annotation> A getContextAnnotation(Class<A> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Annotated getMember() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Type getRawPrimaryType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<?>[] findViews() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonFormat.Value findFormatOverrides(AnnotationIntrospector ai) {
			return format;
		}

		@Override
		public JsonIgnoreType findIgnoreType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonInclude.Value findPropertyInclusion(AnnotationIntrospector ai) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AnnotatedMember getPrimaryMember() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class SerializerProvider {
		public Locale getLocale() {
			// TODO Auto-generated method stub
			return null;
		}

		public TimeZone getTimeZone() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEnabled(SerializationFeature writeDatesAsTimestamps) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	class Type {

	}

	class JavaType {

	}

	public void testDateTimeSerializerBaseMethods() {
		testCreateContextualWithNumericTimestamp();
		testCreateContextualWithDateFormat();
		testCreateContextualWithNullProperty();
    	testIsEmptyWithValueIsNull();
    	testIsEmptyWithValueIsNull();
    	testIsEmptyWithValueIsNotNull();
    	testIsEmptyWithSerializerProviderIsNotNull();
    	testGetSchemaWithTimeStamp();
    	testGetSchemaWithDateTime();
    	testGetSchemaWithNullType();
    	testAcceptJsonFormatVisitorWithNumericTimestamp();
    	testAcceptJsonFormatVisitorWithDateTime();
    	testSerialize();
	}

}
