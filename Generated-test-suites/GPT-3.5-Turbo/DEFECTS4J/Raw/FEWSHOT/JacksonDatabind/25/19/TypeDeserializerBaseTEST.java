package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
	private TypeIdResolver idResolver;
	private JavaType baseType;
	private String typePropertyName;
	private boolean typeIdVisible;
	private Class<?> defaultImpl;
	private TypeDeserializerBase typeDeserializerBase;
	private BeanProperty property;
	
	@Before
	public void setup() {
		idResolver = mock(TypeIdResolver.class);
		baseType = mock(JavaType.class);
		typePropertyName = "TestType";
		typeIdVisible = false;
		defaultImpl = null;
		property = mock(BeanProperty.class);
		typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
			
			@Override
			public TypeDeserializer forProperty(BeanProperty prop) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public com.fasterxml.jackson.annotation.JsonTypeInfo.As getTypeInclusion() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	@Test
	public void testBaseTypeName() {
		when(baseType.getRawClass()).thenReturn(Object.class);
		assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
	}
	
	@Test
	public void testGetPropertyName() {
		assertEquals("TestType", typeDeserializerBase.getPropertyName());
	}
	
	@Test
	public void testGetTypeIdResolver() {
		assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
	}
	
	@Test
	public void testGetDefaultImpl() {
		assertEquals(null, typeDeserializerBase.getDefaultImpl());
	}
	
	@Test
	public void testFindDeserializer() throws Exception {
		DeserializationContext ctxt = mock(DeserializationContext.class);
		JsonDeserializer<Object> deser = new NullifyingDeserializer();
		when(idResolver.typeFromId(ctxt, "1")).thenReturn(baseType);
		when(ctxt.findContextualValueDeserializer(baseType, property)).thenReturn(deser);
		assertEquals(deser, typeDeserializerBase._findDeserializer(ctxt, "1"));
	}
	
	@Test(expected = JsonMappingException.class)
	public void testFindDeserializerThrows() throws Exception {
		DeserializationContext ctxt = mock(DeserializationContext.class);
		when(idResolver.typeFromId(ctxt, "1")).thenReturn(null);
		typeDeserializerBase._findDeserializer(ctxt, "1");
	}
	
	@Test
	public void testFindDefaultImplDeserializer() throws Exception {
		DeserializationContext ctxt = mock(DeserializationContext.class);
		assertEquals(NullifyingDeserializer.instance, typeDeserializerBase._findDefaultImplDeserializer(ctxt));
	}
	
	@Test
	public void testDeserializeWithNativeTypeId() throws Exception {
		JsonDeserializer<Object> deser = new NullifyingDeserializer();
		JsonDeserializer<Object> spy = org.mockito.Mockito.spy(deser);
		typeDeserializerBase._deserializeWithNativeTypeId(null, null, spy);
		org.mockito.Mockito.verify(spy, org.mockito.Mockito.never()).deserialize(null, null);
	}
	
	@Test
	public void testHandleUnknownTypeId() throws Exception {
		DeserializationContext ctxt = mock(DeserializationContext.class);
		String typeId = "1";
		String extraDesc = "known type ids are not statically known";
		when(idResolver instanceof TypeIdResolverBase).thenReturn(true);
		when(((TypeIdResolverBase) idResolver).getDescForKnownTypeIds()).thenReturn(extraDesc);
		try {
			typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
		} catch(JsonMappingException e) {
			String expectedMessage = "Unknown type id (1) at (java.lang.Object), known type ids = " + extraDesc;
			assertEquals(expectedMessage, e.getMessage());
		}
	}
}