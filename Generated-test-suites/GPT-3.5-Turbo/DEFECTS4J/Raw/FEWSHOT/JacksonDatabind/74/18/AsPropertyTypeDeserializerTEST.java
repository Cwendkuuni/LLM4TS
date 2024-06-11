package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.TokenBuffer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsPropertyTypeDeserializerTest {
	
	@Mock
	private JsonParser parser;
	
	@Mock
	private DeserializationContext context;

	@Mock
	private TokenBuffer tokenBuffer;

	@Mock
	private BeanProperty beanProperty;

	@Mock
	private JsonDeserializer<Object> deserializer;
	
	@Mock
	private JavaType baseType;
	
	@Mock
	private TypeIdResolver idResolver;

	@Test
	public void testAsPropertyTypeDeserializer() {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType, As.PROPERTY);
		assertEquals(baseType, deserializer.baseType);
		assertEquals(idResolver, deserializer.idResolver);
		assertEquals("type", deserializer._typePropertyName);
		assertEquals(true, deserializer._typeIdVisible);
		assertEquals(baseType, deserializer.defaultImpl);
		assertEquals(As.PROPERTY, deserializer._inclusion);
	}

	@Test
	public void testAsPropertyTypeDeserializerWithInclusion() {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType, As.WRAPPER_ARRAY);
		assertEquals(baseType, deserializer.baseType);
		assertEquals(idResolver, deserializer.idResolver);
		assertEquals("type", deserializer._typePropertyName);
		assertEquals(true, deserializer._typeIdVisible);
		assertEquals(baseType, deserializer.defaultImpl);
		assertEquals(As.WRAPPER_ARRAY, deserializer._inclusion);
	}

	@Test
	public void testAsPropertyTypeDeserializerWithPropertyBean() {
		AsPropertyTypeDeserializer src = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(beanProperty.getName()).thenReturn("property");
		AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) src.forProperty(beanProperty);
		assertEquals(baseType, deserializer.baseType);
		assertEquals(idResolver, deserializer.idResolver);
		assertEquals("type", deserializer._typePropertyName);
		assertEquals(true, deserializer._typeIdVisible);
		assertEquals(baseType, deserializer.defaultImpl);
		assertEquals(As.PROPERTY, deserializer._inclusion);
		assertEquals(beanProperty, deserializer._property);
	}

	@Test
	public void testGetTypeInclusion() {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
	}

	@Test
	public void testDeserializeTypedFromObjectWithTypeId() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.canReadTypeId()).thenReturn(true);
		when(parser.getTypeId()).thenReturn("id");
		assertEquals(deserializer._deserializeWithNativeTypeId(parser, context, "id"),
				deserializer.deserializeTypedFromObject(parser, context));
		verify(parser, times(1)).getTypeId();
	}

	@Test
	public void testDeserializeTypedFromObjectWithStartObject() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
		when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
		assertEquals(
				deserializer._deserializeTypedUsingDefaultImpl(parser, context, null),
				deserializer.deserializeTypedFromObject(parser, context));
		verify(parser, times(1)).getCurrentToken();
		verify(parser, times(2)).nextToken();
	}

	@Test
	public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
		assertEquals(deserializer._deserializeTypedUsingDefaultImpl(parser, context, null),
				deserializer.deserializeTypedFromObject(parser, context));
		verify(context, times(1)).reportWrongTokenException(parser, JsonToken.FIELD_NAME,
				"missing property 'type' that is to contain type id  (for class " + baseType + ")");
	}

	@Test
	public void testDeserializeTypedForIdWithTypeIdVisible() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.getText()).thenReturn("id");
		when(_findDeserializer(context, "id")).thenReturn(deserializer);
		assertEquals(deserializer.deserialize(parser, context), deserializer._deserializeTypedForId(parser, context, tokenBuffer));
		verify(_findDeserializer(context, "id"), times(1)).deserialize(parser, context);
	}

	@Test
	public void testDeserializeTypedForIdWithTypeIdNotVisible() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", false, baseType);
		when(parser.getText()).thenReturn("id");
		assertEquals(deserializer.deserialize(parser, context), deserializer._deserializeTypedForId(parser, context, tokenBuffer));
		verify(_findDeserializer(context, "id"), times(1)).deserialize(parser, context);
	}

	@Test
	public void testDeserializeTypedForIdWithTypeBufferNull() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.getText()).thenReturn("id");
		when(_findDeserializer(context, "id")).thenReturn(deserializer);
		when(parser.getCurrentName()).thenReturn("name");
		assertEquals(deserializer.deserialize(parser, context), deserializer._deserializeTypedForId(parser, context, null));
		verify(_findDeserializer(context, "id"), times(1)).deserialize(parser, context);
	}

	@Test
	public void testDeserializeTypedForIdWithTokenBuffer() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.getText()).thenReturn("id");
		when(_findDeserializer(context, "id")).thenReturn(deserializer);
		when(parser.getCurrentName()).thenReturn("name");
		assertEquals(deserializer.deserialize(parser, context),
				deserializer._deserializeTypedForId(parser, context, tokenBuffer));
		verify(_findDeserializer(context, "id"), times(1)).deserialize(parser, context);
	}
	
	@Test
	public void testDeserializeTypedUsingDefaultImplWithDefaultImpl() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", false, baseType);
		when(baseType.isJavaLangObject()).thenReturn(true);
		assertEquals(deserializer.deserialize(parser, context), deserializer._deserializeTypedUsingDefaultImpl(parser, context, null));
	}
	
	@Test
	public void testDeserializeTypedFromAnyWithStartArray() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
		assertEquals(deserializer.deserializeTypedFromArray(parser, context), deserializer.deserializeTypedFromAny(parser, context));
	}
	
	@Test
	public void testDeserializeTypedFromAnyWithStartObject() throws IOException {
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, "type", true, baseType);
		when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
		assertEquals(deserializer.deserializeTypedFromObject(parser, context), deserializer.deserializeTypedFromAny(parser, context));
	}
}