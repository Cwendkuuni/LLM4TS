package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationTestCase;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
	
	@Test
	public void testBaseTypeName() {
		TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTester();
		assertEquals("java.util.HashMap", typeDeserializerBase.baseTypeName());
	}
	
	@Test
	public void testGetPropertyName() {
		TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTester();
		assertEquals("", typeDeserializerBase.getPropertyName());
	}
	
	@Test
	public void testGetTypeIdResolver() {
		TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTester();
		TypeIdResolver typeIdResolver = new TypeIdResolver() {
			@Override
			public void init(JavaType baseType) { }

			@Override
			public String idFromValue(Object value) { return null; }

			@Override
			public String idFromValueAndType(Object value, Class<?> suggestedType) { return null; }

			@Override
			public JavaType typeFromId(DatabindContext context, String id) { return null; }

			@Override
			public String idFromBaseType() { return null; }
		};
		assertSame(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
	}
	
	@Test
	public void testGetDefaultImpl() {
		TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTester();
		assertEquals(null, typeDeserializerBase.getDefaultImpl());
	}
	
	@Test
	public void testToString() {
		TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTester();
		assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTester; base-type:java.util.HashMap; id-resolver: null]", typeDeserializerBase.toString());
	}
	
	@Test
	public void testDeserializeWithNativeTypeId() throws Exception {
		TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTester();
		JsonDeserializer<Object> result = typeDeserializerBase._deserializeWithNativeTypeId(null, null, null);
		assertEquals(null, result);	
	}
	
	@Test
	public void testHandleUnknownTypeId() throws Exception {
		TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTester();
		DeserializationContext ctxt = new DeserializationContext(null, null, null);
		assertEquals(null, typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", null, null));
	}
	
	private class TypeDeserializerBaseTester extends TypeDeserializerBase {
		private static final long serialVersionUID = 1L;

		public TypeDeserializerBaseTester() {
			super(null, null, null, true, null);
		}

		@Override
	    public TypeDeserializer forProperty(BeanProperty prop) {
	    	return null;
	    }

	    @Override
	    public JsonTypeInfo.As getTypeInclusion() {
	    	return null;
	    }

	    @Override
	    protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
	            TypeIdResolver idResolver, JavaType baseType)
	        throws IOException
	    {
	        return null;
	    }
	}
}