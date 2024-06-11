package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class DatabindContextTest {

	@Test
	public void testGetConfig() {
		DatabindContext context = new DatabindContextImpl();
		assertNotNull(context.getConfig());
	}

	@Test
	public void testGetAnnotationIntrospector() {
		DatabindContext context = new DatabindContextImpl();
		assertNotNull(context.getAnnotationIntrospector());
	}

	@Test
	public void testIsEnabled() {
		DatabindContext context = new DatabindContextImpl();
		assertTrue(context.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS));
	}

	@Test
	public void testCanOverrideAccessModifiers() {
		DatabindContext context = new DatabindContextImpl();
		assertTrue(context.canOverrideAccessModifiers());
	}

	@Test
	public void testGetActiveView() {
		DatabindContext context = new DatabindContextImpl();
		assertNull(context.getActiveView());
	}

	@Test
	public void testGetLocale() {
		DatabindContext context = new DatabindContextImpl();
		assertNotNull(context.getLocale());
	}

	@Test
	public void testGetTimeZone() {
		DatabindContext context = new DatabindContextImpl();
		assertNotNull(context.getTimeZone());
	}

	@Test
	public void testGetDefaultPropertyFormat() {
		DatabindContext context = new DatabindContextImpl();
		Class<?> baseType = Object.class;
		assertNotNull(context.getDefaultPropertyFormat(baseType));
	}

	@Test
	public void testGetAttribute() {
		DatabindContext context = new DatabindContextImpl();
		Object key = new Object();
		assertNull(context.getAttribute(key));
	}

	@Test
	public void testSetAttribute() {
		DatabindContext context = new DatabindContextImpl();
		Object key = new Object();
		Object value = new Object();
		DatabindContext updatedContext = context.setAttribute(key, value);
		assertEquals(value, updatedContext.getAttribute(key));
	}

	@Test
	public void testConstructType() {
		DatabindContext context = new DatabindContextImpl();
		Type type = Object.class;
		assertNotNull(context.constructType(type));
	}

	@Test
	public void testConstructSpecializedType() {
		DatabindContext context = new DatabindContextImpl();
		JavaType baseType = context.constructType(Object.class);
		Class<?> subclass = Object.class;
		assertNotNull(context.constructSpecializedType(baseType, subclass));
	}

	@Test
	public void testResolveSubType() throws JsonMappingException {
		DatabindContext context = new DatabindContextImpl();
		JavaType baseType = context.constructType(Object.class);
		String subClass = "java.lang.Object";
		assertNotNull(context.resolveSubType(baseType, subClass));
	}

	@Test
	public void testObjectIdGeneratorInstance() throws JsonMappingException {
		DatabindContext context = new DatabindContextImpl();
		Annotated annotated = null;
		ObjectIdInfo objectIdInfo = null;
		assertNotNull(context.objectIdGeneratorInstance(annotated, objectIdInfo));
	}

	@Test
	public void testObjectIdResolverInstance() {
		DatabindContext context = new DatabindContextImpl();
		Annotated annotated = null;
		ObjectIdInfo objectIdInfo = null;
		assertNotNull(context.objectIdResolverInstance(annotated, objectIdInfo));
	}

	@Test
	public void testConverterInstance() throws JsonMappingException {
		DatabindContext context = new DatabindContextImpl();
		Annotated annotated = null;
		Object converterDef = null;
		assertNotNull(context.converterInstance(annotated, converterDef));
	}

	@Test
	public void testReportBadDefinitionWithJavaType() throws JsonMappingException {
		DatabindContext context = new DatabindContextImpl();
		JavaType type = context.constructType(Object.class);
		String msg = "Bad definition";
		assertNotNull(context.reportBadDefinition(type, msg));
	}

	@Test
	public void testReportBadDefinitionWithClass() throws JsonMappingException {
		DatabindContext context = new DatabindContextImpl();
		Class<?> type = Object.class;
		String msg = "Bad definition";
		assertNotNull(context.reportBadDefinition(type, msg));
	}
}