package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest {

	@Mock
	private Converter<Object, ?> converter;

	@Mock
	private JavaType delegateType;

	@Mock
	private JsonSerializer<Object> delegateSerializer;

	@Mock
	private SerializerProvider provider;

	@Mock
	private JsonGenerator gen;

	@Mock
	private TypeSerializer typeSer;

	@Mock
	private JsonFormatVisitorWrapper visitor;

	private StdDelegatingSerializer serializer;

	@Before
	public void setUp() {
		serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
	}

	@Test
	public void testResolve() throws JsonMappingException {
		ResolvableSerializer resolvableSerializer = mock(ResolvableSerializer.class);
		serializer._delegateSerializer = resolvableSerializer;
		serializer.resolve(provider);
	}

	@Test
	public void testCreateContextual() throws JsonMappingException {
		BeanProperty property = mock(BeanProperty.class);
		JsonSerializer<?> delSer = mock(JsonSerializer.class);
		
		serializer.createContextual(provider, property);
	}

	@Test
	public void testSerialize() throws IOException {
		Object value = new Object();
		serializer.serialize(value, gen, provider);
	}

	@Test
	public void testSerializeWithType() throws IOException {
		Object value = new Object();
		serializer.serializeWithType(value, gen, provider, typeSer);
	}

	@Test
	public void testIsEmpty() {
		Object value = new Object();
		serializer.isEmpty(provider, value);
	}

	@Test
	public void testGetSchema() throws JsonMappingException {
		Type type = mock(Type.class);
		serializer.getSchema(provider, type);
	}

	@Test
	public void testGetSchemaWithOption() throws JsonMappingException {
		Type type = mock(Type.class);
		serializer.getSchema(provider, type, true);
	}

	@Test
	public void testAcceptJsonFormatVisitor() throws JsonMappingException {
		JavaType typeHint = mock(JavaType.class);
		serializer.acceptJsonFormatVisitor(visitor, typeHint);
	}

	@Test
	public void testConvertValue() {
		Object value = new Object();
		serializer.convertValue(value);
	}
}