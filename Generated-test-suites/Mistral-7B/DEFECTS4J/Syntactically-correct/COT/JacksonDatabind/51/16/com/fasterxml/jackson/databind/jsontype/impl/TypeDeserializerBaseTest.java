package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private DeserializationContext context;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 testee = new TypeDeserializerBase(null, null, "", false, null);
 context = new TestDeserializationContext();
 mapper = new ObjectMapper();
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty(null, "", null);
 TypeDeserializer typeDeserializer = testee.forProperty(property);
 assertNotNull(typeDeserializer);
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, testee.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 assertNull(testee.baseTypeName());
 }

 @Test
 public void testGetPropertyName() throws Exception {
 assertNull(testee.getPropertyName());
 }

 @Test
 public void testToString() throws Exception {
 String expected = "[TypeDeserializerBase; base-type:null; id-resolver:null]";
 assertEquals(expected, testee.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForProperty_Exception() throws Exception {
 BeanProperty property = new BeanProperty("test", "", null);
 testee.forProperty(property);
 }

 @Test
 public void testFindDeserializer_NullTypeId() throws Exception {
 assertNull(testee._findDeserializer(context, null));
 }

 @Test(expected = IOException.class)
 public void testFindDeserializer_UnknownTypeId() throws Exception {
 testee._deserializers.put("UnknownTypeId", new JsonDeserializer<Object>(null));
 assertNull(testee._findDeserializer(context, "UnknownTypeId"));
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 assertNull(testee._findDefaultImplDeserializer(context));
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 TypeIdResolver idResolver = new TypeIdResolverBase();
 JavaType expected = mapper.getTypeFactory().constructType(String[].class);
 assertEquals(expected, testee._handleUnknownTypeId(context, "UnknownTypeId", idResolver, null));
 }
}

class TestDeserializationContext extends DeserializationContext {

 @Override
 public JavaType typeFromId(String id) {
 return null;
 }

 @Override
 public void reportMappingException(String msg) {
 fail(msg);
 }

 @Override
 public JavaType constructType(Class<?> type) {
 return null;
 }

 @Override
 public JavaType constructType(Class<?> type, Class<?>... interfaces) {
 return null;
 }

 @Override
 public JavaType constructType(Class<?> type, Map<String, Class<?>> huddle) {
 return null;
 }

 @Override
 public JavaType constructSpecializedType(JavaType baseType, Class<?> erasedType) {
 return null;
 }
}