package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

  private DefaultDeserializationContext defaultDeserializationContext;

  @Before
  public void setUp() {
    DeserializerFactory deserializerFactory = new DeserializerFactory();
    defaultDeserializationContext = new DefaultDeserializationContext.Impl(deserializerFactory);
  }

  @Test
  public void testFindObjectId() {
    Object id = new Object();
    ObjectIdGenerator<?> gen = null;
    ObjectIdResolver resolverType = null;

    ReadableObjectId readableObjectId = defaultDeserializationContext.findObjectId(id, gen, resolverType);

    assertNull(readableObjectId);

  }

  @Test
  public void testFindObjectIdWithExistingObjectIds() {
    Object id = new Object();
    ObjectIdGenerator<?> gen = null;
    ObjectIdResolver resolverType = null;

    LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
    objectIds.put(new ObjectIdGenerator.IdKey(), new ReadableObjectId(new ObjectIdGenerator.IdKey()));

    defaultDeserializationContext = new DefaultDeserializationContext.Impl(null);
    defaultDeserializationContext._objectIds = objectIds;

    ReadableObjectId readableObjectId = defaultDeserializationContext.findObjectId(id, gen, resolverType);

    assertNotNull(readableObjectId);

  }

  @Test
  public void testFindObjectIdWithObjectIdResolvers() {
    Object id = new Object();
    ObjectIdGenerator<?> gen = null;
    ObjectIdResolver resolverType = null;

    ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
    ArrayList<ObjectIdResolver> objectIdResolvers = new ArrayList<>();
    objectIdResolvers.add(objectIdResolver);

    defaultDeserializationContext._objectIdResolvers = objectIdResolvers;

    ReadableObjectId readableObjectId = defaultDeserializationContext.findObjectId(id, gen, resolverType);

    assertNotNull(readableObjectId);

  }

  @Test
  public void testCheckUnresolvedObjectId() {
    LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
    objectIds.put(new ObjectIdGenerator.IdKey(), new ReadableObjectId(new ObjectIdGenerator.IdKey()));

    defaultDeserializationContext = new DefaultDeserializationContext.Impl(null);
    defaultDeserializationContext._objectIds = objectIds;

    try {
      defaultDeserializationContext.checkUnresolvedObjectId();
    } catch (UnresolvedForwardReference e) {
      fail("Should not throw exception");
    }
  }

  @Test(expected = UnresolvedForwardReference.class)
  public void testCheckUnresolvedObjectIdWithEnabledFailOnUnresolvedObjectIds() throws UnresolvedForwardReference {
    defaultDeserializationContext.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

    LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
    objectIds.put(new ObjectIdGenerator.IdKey(), new ReadableObjectId(new ObjectIdGenerator.IdKey()));

    defaultDeserializationContext = new DefaultDeserializationContext.Impl(null);
    defaultDeserializationContext._objectIds = objectIds;

    defaultDeserializationContext.checkUnresolvedObjectId();
  }

  @Test
  public void testDeserializerInstance() throws JsonMappingException {
    Annotated ann = null;
    Object deserDef = null;

    JsonDeserializer<Object> jsonDeserializer = defaultDeserializationContext.deserializerInstance(ann, deserDef);

    assertNull(jsonDeserializer);
  }

  @Test
  public void testKeyDeserializerInstance() throws JsonMappingException {
    Annotated ann = null;
    Object deserDef = null;

    KeyDeserializer keyDeserializer = defaultDeserializationContext.keyDeserializerInstance(ann, deserDef);

    assertNull(keyDeserializer);
  }

  @Test
  public void testWith() {
    DeserializerFactory factory = new DeserializerFactory();

    DefaultDeserializationContext deserializationContext = defaultDeserializationContext.with(factory);

    assertNotNull(deserializationContext);
  }

  @Test
  public void testCreateInstance() {
    DeserializationConfig config = null;
    JsonParser jp = null;
    InjectableValues values = null;

    DefaultDeserializationContext deserializationContext = defaultDeserializationContext.createInstance(config, jp, values);

    assertNotNull(deserializationContext);
  }

  @Test
  public void testImpl() {
    DeserializerFactory factory = new DeserializerFactory();

    Impl impl = new Impl(factory);

    assertNotNull(impl);
  }
}