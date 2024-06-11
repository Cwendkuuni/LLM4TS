package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {
	private SetterlessProperty setterlessProperty;
	private BeanPropertyDefinition propDef;
	private JavaType type;
	private TypeDeserializer typeDeser;
	private Annotations contextAnnotations;
	private AnnotatedMethod method;
	private Method annotated;
	private JsonDeserializer<?> deser;
	private NullValueProvider nva;

	@Before
	public void setup() {
		propDef = mock(BeanPropertyDefinition.class);
		type = mock(JavaType.class);
		typeDeser = mock(TypeDeserializer.class);
		contextAnnotations = mock(Annotations.class);
		method = mock(AnnotatedMethod.class);
		annotated = mock(Method.class);
		deser = mock(JsonDeserializer.class);
		nva = mock(NullValueProvider.class);

		setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
		setterlessProperty = new SetterlessProperty(setterlessProperty, deser, nva);
		setterlessProperty = new SetterlessProperty(setterlessProperty, new PropertyName("newName"));
		setterlessProperty = (SetterlessProperty) setterlessProperty.withValueDeserializer(deser);
		setterlessProperty = (SetterlessProperty) setterlessProperty.withNullProvider(nva);
		setterlessProperty.fixAccess(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
	}

	@Test
	public void testWithNullPropertyName() {
		assertEquals(setterlessProperty, setterlessProperty.withName(null));
	}

	@Test
	public void testDeserializeAndSet() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = new Object();
		Object toModify = new Object();

		when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
		when(method.getAnnotated()).thenReturn(annotated);
		when(annotated.invoke(instance, (Object[]) null)).thenReturn(toModify);

		setterlessProperty.deserializeAndSet(p, ctxt, instance);

		verify(p).getCurrentToken();
		verify(p).readValueAs(type);
		verify(annotated).invoke(instance, (Object[]) null);
		verify(deser).deserialize(p, ctxt, toModify);
	}

	@Test
	public void testDeserializeAndSetWithNullValue() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = new Object();

		when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
		when(method.getAnnotated()).thenReturn(annotated);
		when(annotated.invoke(instance, (Object[]) null)).thenReturn(null);

		setterlessProperty.deserializeAndSet(p, ctxt, instance);

		verify(p).getCurrentToken();
		verify(p, never()).readValueAs(type);
		verify(annotated).invoke(instance, (Object[]) null);
	}

	@Test
	public void testDeserializeSetAndReturn() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = new Object();
		Object toModify = new Object();

		when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
		when(method.getAnnotated()).thenReturn(annotated);
		when(annotated.invoke(instance, (Object[]) null)).thenReturn(toModify);

		setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);

		verify(p).getCurrentToken();
		verify(p).readValueAs(type);
		verify(annotated).invoke(instance, (Object[]) null);
		verify(deser).deserialize(p, ctxt, toModify);
	}

	@Test
	public void testSet() throws IOException {
		Object instance = new Object();
		Object value = new Object();

		try {
			setterlessProperty.set(instance, value);
		} catch (UnsupportedOperationException e) {
			assertEquals("Should never call `set()` on setterless property ('name')", e.getMessage());
		}
	}

	@Test
	public void testSetAndReturn() throws IOException {
		Object instance = new Object();
		Object value = new Object();

		try {
			setterlessProperty.setAndReturn(instance, value);
		} catch (UnsupportedOperationException e) {
			assertEquals("Should never call `set()` on setterless property ('name')", e.getMessage());
		}
	}

	@Test
	public void testGetAnnotation() {
		Class<Annotation> acls = Annotation.class;

		assertEquals(null, setterlessProperty.getAnnotation(acls));
	}

	@Test
	public void testGetMember() {
		assertEquals(method, setterlessProperty.getMember());
	}
}