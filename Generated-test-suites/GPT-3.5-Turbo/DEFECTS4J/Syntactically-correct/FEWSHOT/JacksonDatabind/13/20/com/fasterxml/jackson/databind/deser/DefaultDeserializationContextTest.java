package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {
	
	DefaultDeserializationContext deSerContext;

	@Before
	public void setup() {
		deSerContext = new DefaultDeserializationContext.Impl(null);
	}
	
	@Test
	public void testFindObjectId() {
		ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("", null, null);
		assertNull(deSerContext.findObjectId(idKey, new ObjectIdGenerator<ObjectIdResolver>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public ObjectIdGenerator<Object> forScope(Class<?> scope) {
				return null;
			}
			
			@Override
			public ObjectIdGenerator<Object> forPojo(Class<?> pojo) {
				return null;
			}
			
			@Override
			public ObjectIdResolver newForDeserialization(Object context) {
				return null;
			}
			
			@Override
			public ObjectIdResolver resolverForDeserialization(Object context) {
				return null;
			}
			
			@Override
			public ObjectIdResolver resolverForCreation(Object context) {
				return null;
			}
		}));
	}
	
	@Test
	public void testFindObjectIdWithType() {
		ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("", null, null);
		assertNull(deSerContext.findObjectId(idKey, new ObjectIdGenerator<ObjectIdResolver>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public ObjectIdGenerator<Object> forScope(Class<?> scope) {
				return null;
			}
			
			@Override
			public ObjectIdGenerator<Object> forPojo(Class<?> pojo) {
				return null;
			}
			
			@Override
			public ObjectIdResolver newForDeserialization(Object context) {
				return null;
			}
			
			@Override
			public ObjectIdResolver resolverForDeserialization(Object context) {
				return null;
			}
			
			@Override
			public ObjectIdResolver resolverForCreation(Object context) {
				return null;
			}
		}, new SimpleObjectIdResolver()));
	}
	
	@Test
	public void testFinishBuildingObject() {
		assertNotNull(deSerContext.finishBuildingObject(null, null));
	}
	
	@Test
	public void testWith() {
		assertNotNull(deSerContext.with(null));
	}
	
	@Test
	public void testCreateInstance() {
		assertNotNull(deSerContext.createInstance(null, null, null));
	}
	
	@Test
	public void testCachedDeserializers() {
		assertNull(deSerContext.cachedDeserializers());
	}
	
	@Test
	public void testIsTypeVisible() {
		assertEquals(true, deSerContext.isTypeVisible(null, null));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testHandleWeirdStringValue() {
		deSerContext.handleWeirdStringValue("", null, "");
	}

	@Test
	public void testHandleUnknownProperty() {
		deSerContext.handleUnknownProperty(null, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testHandleUnexpectedToken() {
		deSerContext.handleUnexpectedToken(null, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testHandleUnexpectedTokenError() {
		deSerContext.handleUnexpectedToken(null, null, null, "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testHandleUnexpectedTokenErrorWithMsg() {
		deSerContext.handleUnexpectedToken(null, null, null, "", "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrappedException() {
		deSerContext.throwRootValueInstantiationException(null, null, null);
	}
}