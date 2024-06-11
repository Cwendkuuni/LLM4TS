package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.instance;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private DeserializationContext context;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 context = mapper.getDeserializationContext();
 typeDeserializerBase = new TypeDeserializerBaseForTest(mapper.getTypeFactory().constructType(String.class),
 new StdTypeIdResolver(), "", false, mapper.getTypeFactory().constructType(String.class));
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty("name", String.class);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
 assertEquals(TypeDeserializerForTest.class, typeDeserializer.getClass());
 }

 @Test
 public void testGetTypeInclusion() {
 assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() {
 assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testPropertyName() {
 assertEquals("", typeDeserializerBase.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() {
 assertEquals(StdTypeIdResolver.class, typeDeserializerBase.getTypeIdResolver().getClass());
 }

 @Test
 public void testDefaultImpl() {
 assertEquals(String.class, typeDeserializerBase.getDefaultImpl().getClass());
 }

 @Test
 public void testToString() throws Exception {
 String xml = "{\"name\":{\"@class\":\"[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase]\",\"baseType\":\"java.lang.String\",\"typePropertyName\":\"\",\"typeIdVisible\":false}}";
 JsonNode node = mapper.readTree(xml);
 String result = typeDeserializerBase.toString();
 JsonNode expected = instance.textNode(result);
 assertEquals(expected, node.get("name"));
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNullTypeId() throws Exception {
 assertEquals(NullifyingDeserializer.instance, typeDeserializerBase._findDeserializer(context, null));
 fail("should throw exception");
 }

 @Test
 public void testFindDeserializerUnknownTypeId() throws Exception {
 String typeId = "unknown";
 JsonDeserializer deserializer = new StubJsonDeserializer();
 context.registerDeserializer(TypeDeserializerForTest.class, deserializer);
 typeDeserializerBase._deserializers.put(typeId, deserializer);
 assertEquals(deserializer, typeDeserializerBase._findDeserializer(context, typeId));
 }

 @Test
 public void testFindDefaultImplDeserializerNullTypeId() throws Exception {
 assertEquals(NullifyingDeserializer.instance, typeDeserializerBase._findDefaultImplDeserializer(context));
 }

 @Test
 public void testFindDefaultImplDeserializerNotNullTypeId() throws Exception {
 JsonDeserializer deserializer = new StubJsonDeserializer();
 context.registerDeserializer(String.class, deserializer);
 synchronized (String.class) {
 typeDeserializerBase._defaultImplDeserializer = deserializer;
 }
 assertEquals(deserializer, typeDeserializerBase._findDefaultImplDeserializer(context));
 }

 @Test
 public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[]");
 assertNull(typeDeserializerBase._deserializeWithNativeTypeId(parser, context, null));
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdUnknownTypeId() throws Exception {
 String typeId = "unknown";
 JsonParser parser = mapper.getFactory().createParser("[]");
 assertNull(typeDeserializerBase._deserializeWithNativeTypeId(parser, context, typeId));
 fail("should throw exception");
 }

 @Test
 public void testDeserializeWithNativeTypeIdStringTypeId() throws Exception {
 String typeId = "String";
 JsonParser parser = mapper.getFactory().createParser("[]");
 parser.nextTokenID();
 parser.nextValue(); // consume null value
 parser.nextTokenID();
 parser.setValue(typeId);
 parser.nextTokenID(); // consume end of START_ARRAY token
 String result = (String) typeDeserializerBase._deserializeWithNativeTypeId(parser, context, typeId);
 assertEquals("String", result);
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 TypeIdResolver idResolver = new StdTypeIdResolver();
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 String typeId = "unknown";
 TypeDeserializer typeDeserializer = new TypeDeserializerBaseForTest(mapper.getTypeFactory().constructType(String.class), idResolver, "", false, javaType);
 assertEquals(javaType, typeDeserializer._handleUnknownTypeId(context, typeId, idResolver, javaType));
 }
}

class TypeDeserializerForTest {}

class StubJsonDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "Stub";
 }
}

class StdTypeIdResolver extends TypeIdResolverBase {
 @Override
 protected Map<String, JavaType> getDeserializerAnnotations() {
 return new HashMap<>();
 }
}