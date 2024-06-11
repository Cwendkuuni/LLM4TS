import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * ClassNameIdResolverTest.
 */
public class ClassNameIdResolverTest {

 private ObjectMapper objectMapper;
 private ClassNameIdResolver classNameIdResolver;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 classNameIdResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object testObject = new Object() {
 };
 String id = classNameIdResolver.idFromValue(testObject);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object testObject = new Object() {
 };
 Class<?> testClass = testObject.getClass();
 String id = classNameIdResolver.idFromValueAndType(testObject, testClass);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.lang.String";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 String id = "InvalidTypeId";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 }

 @Test(expected = IOException.class)
 public void testUnknownClass() throws Exception {
 String id = "com.non.existent.package.NonExistentClass";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 }

 @Test
 public void testIdFromEnum() throws Exception {
 Enum testEnum = Enum.valueOf(String.class, "TestEnumValue");
 String id = classNameIdResolver.idFromValue(testEnum);
 Class<?> enumClass = ClassUtil.findEnumType(testEnum.getClass());
 TypeReference typeReference = new TypeReference<EnumMap<String, String>>() {
 };
 String expectedId = typeFactory.constructMapType(EnumMap.class, String.class, String.class).toCanonical();
 assertEquals(expectedId, id);
 }

 @Test
 public void testIdFromCollectionOfEnum() throws Exception {
 Set<Enum> enumSet = EnumSet.allOf(String.class);
 Class<?> enumClass = ClassUtil.findEnumType(enumSet.getClass());
 TypeReference typeReference = new TypeReference<EnumSet<String>>() {
 };
 String expectedId = typeFactory.constructCollectionType(EnumSet.class, enumClass).toCanonical();
 String id = classNameIdResolver.idFromValue(enumSet);
 assertEquals(expectedId, id);
 }

 @Test
 public void testIdFromMapOfEnumAndString() throws Exception {
 Map<Enum, String> enumMap = new EnumMap<>(String.class);
 Class<?> enumClass = ClassUtil.findEnumType(enumMap.getClass());
 TypeReference typeReference = new TypeReference<Map<Enum, String>>() {
 };
 String expectedId = typeFactory.constructMapType(EnumMap.class, enumClass, String.class).toCanonical();
 String id = classNameIdResolver.idFromValue(enumMap);
 assertEquals(expectedId, id);
 }
}