package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactoryBuilder;

public class ClassNameIdResolverTest {

 private TypeIdResolver resolver;
 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 TypeFactory typeFactory = TypeFactoryBuilder.defaultInstance();
 resolver = new ClassNameIdResolver(JavaType.JAVA_LANG_OBJECT, typeFactory);
 objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule() {{
 setMixInAnnotation(Object.class, JsonTypeInfo.As.CLASS.getAnnotation());
 setSerializer(Object.class, new StdSerializer<>((Class<?>) null) {
 @Override
 public void serializeValue(Object value, JsonGenerator jgen, SerializerProvider serializers)
 throws IOException {
 jgen.writeStringValue(resolver.idFromValue(value));
 }
 });
 setDeserializer(Object.class, new BaseJsonDeserializer<>((Class<?>) null) {
 @Override
 public JavaType getType(DeserializationContext ctxt, BeanProperty property, TypeDeserializer valueDeserializer)
 throws IOException {
 return resolver.typeFromId(ctxt, ctxt.getTextToken().getValue());
 }
 });
 }});
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new ArrayList<>();
 JavaType javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), resolver.idFromValue(value));
 assertEquals(ArrayList.class, javaType.getRawClass());

 value = new HashMap<>();
 javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), resolver.idFromValue(value));
 assertEquals(HashMap.class, javaType.getRawClass());

 value = EnumSet.allOf(Enum.class);
 javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), resolver.idFromValue(value));
 assertEquals(EnumSet.class, javaType.getRawClass());

 value = Arrays.asList("one", "two", "three");
 javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), resolver.idFromValue(value));
 assertEquals(Arrays.asList(String.class, String.class, String.class).getType(), javaType.getGenericType());
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new HashMap<>();
 String id = resolver.idFromValueAndType(value, HashMap.class);
 assertEquals("java.util.HashMap", id);

 value = EnumSet.allOf(Enum.class);
 id = resolver.idFromValueAndType(value, EnumSet.class);
 assertEquals("java.util.EnumSet", id);

 value = Arrays.asList("one", "two", "three");
 id = resolver.idFromValueAndType(value, ArrayList.class);
 assertEquals("java.util.ArrayList", id);
 }

 @Test
 public void testTypeFromIdNullContext() {
 assertNull(resolver.typeFromId(null, "java.util.ArrayList"));
 }

 @Test
 public void testTypeFromIdInvalidId() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 assertNull(resolver.typeFromId(context, "InvalidTypeId"));
 }

 @Test
 public void testTypeFromIdUnknownClass() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 assertNull(resolver.typeFromId(context, "com.unknown.UnknownClass"));
 }

 @Test
 public void testGetDescForKnownTypeIds() {
 assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
 }
}