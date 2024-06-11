
package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);

        Hashtable<String, Object> configs = new Hashtable<String, Object>();
        configs.put("key1", "value1");
        DeserializationConfig config = new DeserializationConfig(RuntimeEnvironment.application.getApplicationContext(), DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, HandlerInstantiator.canCreateUsingDefault(true), configs);
        assertNotNull(deserializationContext.findObjectId(null, null, null));
    }

    @Test
    public void testFindObjectIdWithId() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
       
        Hashtable<String,Object> configs = new Hashtable<String,Object>();
        configs.put("key1","value1");
        DeserializationConfig config = new DeserializationConfig(RuntimeEnvironment.application.getApplicationContext(),DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,HandlerInstantiator.canCreateUsingDefault(true),configs);
        assertNotNull(deserializationContext.findObjectId(ObjectIdGenerator._bsonUuid, null, null));
    }

    @Test
    public void testFindObjectIdWithIdAndGenerator() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);

        Hashtable<String,Object> configs = new Hashtable<String,Object>();        
        configs.put("key1","value1");
        DeserializationConfig config = new DeserializationConfig(RuntimeEnvironment.application.getApplicationContext(),DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,HandlerInstantiator.canCreateUsingDefault(true),configs);
        assertNotNull(deserializationContext.findObjectId(ObjectIdGenerator._bsonRoot, ObjectIdResolver.construct(va)));

    }

    @Test
    public void testFindObjectIdWithGenAndResolver() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);

        Hashtable<String,Object> configs = new Hashtable<String,Object>();          
        configs.put("key1","value1");
        DeserializationConfig config = new DeserializationConfig(RuntimeEnvironment.application.getApplicationContext(),DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,HandlerInstantiator.canCreateUsingDefault(true),configs);
        assertNotNull(deserializationContext.findObjectId(ObjectIdGenerator._root, ObjectIdResolver.construct(va)));

    }

    @Test
    public void testFindObjectIdWithGenAndResolverAndId() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);

        Hashtable<String,Object> configs = new Hashtable<String,Object>();        
        configs.put("key1","value1");
        DeserializationConfig config = new DeserializationConfig(RuntimeEnvironment.application.getApplicationContext(),DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,HandlerInstantiator.canCreateUsingDefault(true),configs);
        assertNotNull(deserializationContext.findObjectId(ObjectIdGenerator._bean, ObjectIdResolver.construct(va)));
    }

    @Test
    public void testCheckUnresolvedObjectIdWithoutObjectIds() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);

        Hashtable<String,Object> configs = new Hashtable<String,Object>();
        configs.put("key1","value1");
        DeserializationConfig config = new DeserializationConfig(RuntimeEnvironment.application.getApplicationContext(),DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,HandlerInstantiator.canCreateUsingDefault(true),configs);
        deserializationContext.checkUnresolvedObjectId();

    }

    @Test
    public void testCheckUnresolvedObjectIdWithObjectIds() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);

        Hashtable<String,Object> configs = new Hashtable<String,Object>();
        configs.put("key1","value1");
        DeserializationConfig config = new DeserializationConfig(RuntimeEnvironment.application.getApplicationContext(),DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,HandlerInstantiator.canCreateUsingDefault(true),configs);
        deserializationContext._objectIds = new LinkedHashMap<IdKey, ReadableObjectId>();
        deserializationContext.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstanceWithNullDef() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        assertNull(deserializationContext.deserializerInstance(null, null));
    }

    @Test(expected=IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDef() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        deserializationContext.deserializerInstance(null, "invalid");
    }

    @Test
    public void testDeserializerInstanceWithValidDef() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        deserializationContext.deserializerInstance(null, Object.class);
    }

    @Test
    public void testKeyDeserializerInstanceWithNullDef() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        assertNull(deserializationContext.keyDeserializerInstance(null, null));
    }

    @Test(expected=IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidDef() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        deserializationContext.keyDeserializerInstance(null, "invalid");
    }

    @Test
    public void testKeyDeserializerInstanceWithValidDef() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        deserializationContext.keyDeserializerInstance(null, Object.class);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        assertNull(deserializationContext.with(null));
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(null);
        assertNull(deserializationContext.createInstance(null, null, null));
    }

}
