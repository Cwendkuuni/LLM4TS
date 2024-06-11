package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
	
	private TypeDeserializerBase testTypeDeserializerBase;

	@Before
	public void setUp() {
		JavaType baseType = mock(JavaType.class);
		TypeIdResolver idResolver = mock(TypeIdResolver.class);
		String typePropertyName = "";
		boolean typeIdVisible = false;
		JavaType defaultImpl = mock(JavaType.class);
		BeanProperty property = mock(BeanProperty.class);
		testTypeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {

			private static final long serialVersionUID = 1L;

			@Override
			public TypeDeserializer forProperty(BeanProperty prop) {
				return null;
			}

			@Override
			public JsonTypeInfo.As getTypeInclusion() {
				return null;
			}
			
			@Override
			public String baseTypeName() {
	            return null;
	        }

	        @Override
	        public String getPropertyName() {
	            return null;
	        }
	        
	        @Override
	        public TypeIdResolver getTypeIdResolver() {
	            return null;
	        }

	        @Override
	        public Class<?> getDefaultImpl() {
	            return null;
	        }
	        
	        @Override
	        public String toString() {
	            return null;
	        }
	        
	        @Override
	        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
	                throws IOException {
	            return null;
	        }
	        
	        @Override
	        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
	                throws IOException {
	            return null;
	        }
	        
	        @Override
	        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt)
	                throws IOException {
	            return null;
	        }
	        
	        @Override
	        public Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
	                throws IOException {
	            return null;
	        }
	        
	        @Override
	        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
	                TypeIdResolver idResolver, JavaType baseType)
	            throws IOException {
	            return null;
	        }
			
		};
	}

	@Test
	public void testBaseTypeName() {
		assertEquals(null, testTypeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals(null, testTypeDeserializerBase.getPropertyName());
    }
    
    @Test    
    public void testGetTypeIdResolver() {
        assertEquals(null, testTypeDeserializerBase.getTypeIdResolver());
    }
    
    @Test    
    public void testGetDefaultImpl() {
        assertEquals(null, testTypeDeserializerBase.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        assertEquals(null, testTypeDeserializerBase.toString());
    }
    
    @Test
    public void testFindDeserializer() throws IOException {
    	DeserializationContext ctxt = mock(DeserializationContext.class);
    	String typeId = "test";
    	JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
    	Map<String,JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<String, JsonDeserializer<Object>>();
    	deserializers.put(typeId, deser);
    	assertEquals(deser, testTypeDeserializerBase._findDeserializer(ctxt, typeId));
    }
    
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
    	DeserializationContext ctxt = mock(DeserializationContext.class);
    	JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
    	assertEquals(deser, testTypeDeserializerBase._findDefaultImplDeserializer(ctxt));
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
    	JsonParser jp = mock(JsonParser.class);
    	DeserializationContext ctxt = mock(DeserializationContext.class);
    	Object typeId = "test";
    	JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
    	when(testTypeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId)).thenReturn(null);
    	assertEquals(null, testTypeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId));
    }
    
    @Test
    public void testDeserializeWithUnknownTypeId() throws IOException {
    	DeserializationContext ctxt = mock(DeserializationContext.class);
    	String typeId = "test";
    	TypeIdResolver idResolver = mock(TypeIdResolver.class);
    	JavaType baseType = mock(JavaType.class);
    	assertEquals(null, testTypeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType));
    }
    
}