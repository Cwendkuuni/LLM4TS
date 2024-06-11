package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {
	
	private WritableObjectId objectId;
	private ObjectIdGenerator<?> generator;
	private ObjectMapper mapper;
	private JsonGenerator gen;
	private SerializerProvider provider;
	private ObjectIdWriter writer;
	
	@Before
	public void setUp() {
		generator = new ObjectIdGenerator<Object>() {
			@Override
			public Object generateId(Object forPojo) {
				return null;
			}
		};
		objectId = new WritableObjectId(generator);
		mapper = new ObjectMapper();
		gen = mapper.getFactory().createGenerator(System.out);
		provider = mapper.getSerializerProvider();
		writer = new ObjectIdWriter(null);
	}
	
	@Test
	public void testWriteAsId() throws IOException {
		assertFalse(objectId.writeAsId(gen, provider, writer));
		
		objectId.id = "testid";
		assertFalse(objectId.writeAsId(gen, provider, writer));
		
		objectId.idWritten = true;
		assertTrue(objectId.writeAsId(gen, provider, writer));
	}
	
	@Test
	public void testGenerateId() {
		objectId.generateId(null);
		assertNull(objectId.id);
	}
	
	@Test
	public void testWriteAsField() throws IOException {
		gen.setCodec(mapper);
		
		objectId.idWritten = false;
		objectId.writeAsField(gen, provider, writer);
		assertFalse(objectId.idWritten);
		
		objectId.id = "testid";
		objectId.writeAsField(gen, provider, writer);
		assertTrue(objectId.idWritten);
		
		// Test when gen.canWriteObjectId() is true
		gen = mapper.getFactory().createGenerator(System.out);
		gen.setCodec(mapper);
		((com.fasterxml.jackson.core.json.JsonGenerator) gen).setFeatureMask(com.fasterxml.jackson.core.json.JsonWriteFeature.WRITE_OBJECID_AS_ARRAY.mask);
		objectId.writeAsField(gen, provider, writer);
		assertTrue(objectId.idWritten);
	}
}