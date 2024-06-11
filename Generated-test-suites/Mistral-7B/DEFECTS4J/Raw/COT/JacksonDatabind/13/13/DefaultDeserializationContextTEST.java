package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.util.*;

public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;
 private DefaultDeserializationContext.Impl contextImpl;

 @Before
 public void setUp() {
 context = new DefaultDeserializationContext.Impl(null);
 contextImpl = (DefaultDeserializationContext.Impl) context;
 }

 @Test
 public void testConstructor() {
 // Test the no-args constructor
 DefaultDeserializationContext noArgsContext = new DefaultDeserializationContext.Impl(null);
 // Test the constructor with DeserializerFactory and DeserializerCache
 DeserializerFactory df = new TestDeserializerFactory();
 DefaultDeserializationContext withFactoryAndCacheContext = new DefaultDeserializationContext(df, null);
 // Test the copy constructor
 DefaultDeserializationContext copiedContext = context.copy();
 // Test the constructor from another DefaultDeserializationContext
 DefaultDeserializationContext srcContext = new DefaultDeserializationContext.Impl(context);
 DefaultDeserializationContext fromSrcContext = new DefaultDeserializationContext(srcContext);
 }

 @Test
 public void testFindObjectId() {
 Object obj = new Object();
 ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new IdKey("testKey");
 }
 };
 ObjectIdResolver resolver = new ObjectIdResolver() {
 @Override
 public ObjectIdIdentifier idIdentifier(DeserializationContext ctxt, Object id) {
 return new ObjectIdIdentifier();
 }
 };

 ReadableObjectId expectedResult = new ReadableObjectId(new IdKey("testKey"));
 ReadableObjectId result = contextImpl.findObjectId(obj, gen, resolver);
 assertEquals(expectedResult, result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 contextImpl._objectIds = new LinkedHashMap<>();
 contextImpl._objectIds.put(new IdKey("testKey"), new ReadableObjectId(new IdKey("testKey")));
 contextImpl._objectIds.get(new IdKey("testKey")).setReferringProperties(Arrays.asList(new Referring("testProperty", "testType", null)));
 contextImpl.checkUnresolvedObjectId();
 }

 @Test
 public void testDeserializerInstance() {
 // Test with null deserDef
 JsonDeserializer<Object> deserializer = context.deserializerInstance(null, null);
 assertNull(deserializer);

 // Test with JsonDeserializer
 JsonDeserializer<String> jsonDeserializer = new JsonDeserializer<String>() {};
 JsonDeserializer<Object> result = context.deserializerInstance(new AnnotatedClassTest(), jsonDeserializer);
 assertEquals(jsonDeserializer, result);

 // Test with Class<JsonDeserializer>
 JsonDeserializer<Integer> jsonDeserializerClass = JsonDeserializer.instance(Integer.class);
 JsonDeserializer<Object> resultClass = context.deserializerInstance(new AnnotatedClassTest(), jsonDeserializerClass.getClass());
 assertEquals(jsonDeserializerClass, resultClass);
 }

 @Test
 public void testKeyDeserializerInstance() {
 // Test with null deserDef
 KeyDeserializer keyDeserializer = context.keyDeserializerInstance(null, null);
 assertNull(keyDeserializer);

 // Test with KeyDeserializer
 KeyDeserializer keyDeserializerInstance = new KeyDeserializer() {};
 KeyDeserializer result = context.keyDeserializerInstance(new AnnotatedClassTest(), keyDeserializerInstance);
 assertEquals(keyDeserializerInstance, result);

 // Test with Class<KeyDeserializer>
 KeyDeserializer keyDeserializerClass = KeyDeserializer.instance(String.class);
 KeyDeserializer resultClass = context.keyDeserializerInstance(new AnnotatedClassTest(), keyDeserializerClass.getClass());
 assertEquals(keyDeserializerClass, resultClass);
 }

 @Test
 public void testWith() {
 // Test with null factory
 DefaultDeserializationContext withNullFactoryContext = context.with(null);
 assertEquals(context, withNullFactoryContext);

 // Test with DeserializerFactory
 DeserializerFactory df = new TestDeserializerFactory();
 DefaultDeserializationContext withFactoryContext = context.with(df);
 assertNotSame(context, withFactoryContext);
 assertTrue(contextImpl._deserializerFactory instanceof TestDeserializerFactory);
 assertTrue(withFactoryContext.getClass().getName().startsWith("com.fasterxml.jackson.databind.deser.DefaultDeserializationContext$Impl"));
 assertFalse(withFactoryContext.getClass().getName().startsWith("com.fasterxml.jackson.databind.deser.DefaultDeserializationContext$Impl$Impl"));

 // Test with null config
 DefaultDeserializationContext withNullConfigContext = context.with(null, new TestDeserializerFactory());
 assertEquals(context, withNullConfigContext);

 // Test with DeserializationConfig
 DefaultDeserializationContext config = new TestDeserializationConfig();
 DefaultDeserializationContext withConfigContext = context.with(config, new TestDeserializerFactory());
 assertNotSame(context, withConfigContext);
 assertFalse(contextImpl._deserializerFactory instanceof TestDeserializerFactory);
 assertTrue(withConfigContext.getClass().getName().startsWith("com.fasterxml.jackson.databind.deser.DefaultDeserializationContext$Impl"));
 assertFalse(withConfigContext.getClass().getName().startsWith("com.fasterxml.jackson.databind.deser.DefaultDeserializationContext$Impl$Impl"));
 }

 @Test
 public void testCreateInstance() {
 // Test with null config
 DefaultDeserializationContext contextWithNullConfig = context.createInstance(null, null, null);
 assertEquals(context, contextWithNullConfig);

 // Test with JsonParser and InjectableValues
 JsonParser parser = mock(JsonParser.class);
 InjectableValues values = new TestInjectableValues();
 DefaultDeserializationContext createdContext = context.createInstance(null, parser, values);
 assertNotSame(context, createdContext);

 // Test with DeserializationConfig, JsonParser and InjectableValues
 DefaultDeserializationConfig config = new TestDeserializationConfig();
 DefaultDeserializationContext createdContextWithConfig = context.createInstance(config, parser, values);
 assertNotSame(context, createdContextWithConfig);
 assertTrue(createdContextWithConfig.getClass().getName().startsWith("com.fasterxml.jackson.databind.deser.DefaultDeserializationContext$Impl"));
 assertFalse(createdContextWithConfig.getClass().getName().startsWith("com.fasterxml.jackson.databind.deser.DefaultDeserializationContext$Impl$Impl"));
 }

}

class AnnotatedClassTest implements Annotated {
 @ObjectIdGenerator(JsonDeserializer.None.class)
 Object id;
}

class TestDeserializerFactory implements DeserializerFactory {
 @Override
 public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config,
         BeanDescription beanDesc, Class<?> valueType) throws JsonMappingException {
 return null;
 }

 @Override
 public KeyDeserializer createKeyDeserializer(DeserializationConfig config,
         BeanDescription beanDesc, Class<?> keyType) throws JsonMappingException {
 return null;
 }
}

class TestInjectableValues implements InjectableValues {
 @Override
 public Object getValueProvider() {
 return null;
 }

 @Override
 public Object getValueProviderInstance(Object bean) {
 return null;
 }

 @Override
 public Object getValueProviderInstance(BeanProperty property) {
 return null;
 }
}