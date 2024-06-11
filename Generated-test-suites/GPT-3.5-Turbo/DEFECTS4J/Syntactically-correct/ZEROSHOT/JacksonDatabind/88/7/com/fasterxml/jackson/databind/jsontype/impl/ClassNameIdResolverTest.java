package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

	@Test
	public void testGetMechanism() {
		ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
		assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
	}

	@Test
	public void testRegisterSubtype() {
		ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
		Class<?> type = Object.class;
		String name = "name";
		resolver.registerSubtype(type, name);
	}

	@Test
	public void testIdFromValue() {
		ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
		Object value = new Object();
		String id = resolver.idFromValue(value);
		assertNotNull(id);
	}

	@Test
	public void testIdFromValueAndType() {
		ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
		Object value = new Object();
		Class<?> type = value.getClass();
		String id = resolver.idFromValueAndType(value, type);
		assertNotNull(id);
	}

	@Test
	public void testTypeFromId() throws IOException {
		ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
		DatabindContext context = null;
		String id = "id";
		JavaType javaType = resolver.typeFromId(context, id);
		assertNotNull(javaType);
	}

}