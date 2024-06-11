package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

	@Test
	public void testForProperty() {
		JavaType type = mock(JavaType.class);
		TypeIdResolver idRes = mock(TypeIdResolver.class);
		BeanProperty prop = mock(BeanProperty.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, idRes, "property", true, type, As.PROPERTY);

		TypeDeserializer result = deserializer.forProperty(prop);

		assertEquals(deserializer, result);
		verify(prop).getType();
	}

	@Test
	public void testDeserializeTypedFromAny_ArrayToken() throws IOException {
		TokenBuffer tb = mock(TokenBuffer.class);
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);

		deserializer.deserializeTypedFromAny(p, ctxt);

		verify(p).getCurrentToken();
		verify(deserializer).deserializeTypedFromArray(p, ctxt);
	}

	@Test
	public void testDeserializeTypedFromAny_ObjectToken() throws IOException {
		TokenBuffer tb = mock(TokenBuffer.class);
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);

		deserializer.deserializeTypedFromAny(p, ctxt);

		verify(p).getCurrentToken();
		verify(deserializer).deserializeTypedFromObject(p, ctxt);
	}

	@Test
	public void testDeserializeTypedFromObject_WithTypeId() throws IOException {
		JsonParser p = mock(JsonParser.class);
		TokenBuffer tb = mock(TokenBuffer.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);
		deserializer._typeIdVisible = true;
		
		deserializer.deserializeTypedFromObject(p, ctxt);
		
		verify(p).canReadTypeId();
		verify(p).getTypeId();
		verify(deserializer).deserializeTypedUsingDefaultImpl(p, ctxt, null);
	}

	@Test
	public void testDeserializeTypedFromObject_StartObjectToken() throws IOException {
		TokenBuffer tb = mock(TokenBuffer.class);
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);

		deserializer.deserializeTypedFromObject(p, ctxt);

		verify(p).getCurrentToken();
		verify(p).nextToken();
		verify(deserializer)._deserializeTypedForId(p, ctxt, tb);
	}

	@Test
	public void testDeserializeTypedFromObject_NotStartObjectToken_NullTokenBuffer() throws IOException {
		TokenBuffer tb = mock(TokenBuffer.class);
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);

		deserializer.deserializeTypedFromObject(p, ctxt);

		verify(p).getCurrentToken();
		verify(p).nextToken();
		verify(deserializer)._deserializeTypedUsingDefaultImpl(p, ctxt, null);
	}

	@Test
	public void testDeserializeTypedForId() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		TokenBuffer tb = mock(TokenBuffer.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);
		deserializer._typeIdVisible = true;

		deserializer._deserializeTypedForId(p, ctxt, tb);

		verify(p).getText();
		verify(ctxt).reportWrongTokenException(p, JsonToken.FIELD_NAME,
                "missing property 'property' that is to contain type id  (for class JavaType)");
	}

	@Test
	public void testDeserializeTypedUsingDefaultImpl_DeserializeFromToken() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		TokenBuffer tb = mock(TokenBuffer.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);

		deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);

		verify(deserializer)._findDefaultImplDeserializer(ctxt);
		verify(p).getCurrentToken();
		verify(deserializer).deserializeTypedFromAny(p, ctxt);
	}

	@Test
	public void testDeserializeTypedUsingDefaultImpl_NaturalEnd() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		TokenBuffer tb = mock(TokenBuffer.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);

		deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);

		verify(deserializer)._findDefaultImplDeserializer(ctxt);
		verify(p).getCurrentToken();
		verify(deserializer)._baseType;
		verify(p).nextToken();
		verify(TypeDeserializer).deserializeIfNatural(p, ctxt, deserializer._baseType);
	}

	@Test
	public void testDeserializeTypedUsingDefaultImplDeserializeIfNatural() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		TokenBuffer tb = mock(TokenBuffer.class);
		AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mock(TypeIdResolver.class), "property", true, mock(JavaType.class), As.PROPERTY);

		deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);

		verify(p).getCurrentToken();
		verify(p).nextToken();
		verify(deserializer)._deserializeTypedFromAny(p, ctxt, tb);
	}
}