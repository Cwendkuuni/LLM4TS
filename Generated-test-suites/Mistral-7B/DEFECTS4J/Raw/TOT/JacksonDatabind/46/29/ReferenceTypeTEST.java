package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 @Test
 public void testReferenceTypeWithSimpleClasses() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
 JavaType intType = TypeFactory.defaultInstance().constructType(intClass);

 ReferenceType referenceType = new ReferenceType(intClass, stringType, null, null, false);
 JavaType result = referenceType.withContentTypeHandler(new IdentityTypeHandler<String>());

 assertEquals(stringType, result.getContentType());
 }

 @Test
 public void testReferenceTypeWithValueHandler() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
 JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
 ObjectMapper mapper = new ObjectMapper();
 StringValueHandler stringHandler = mapper.getTypeHandlerForValue(String.class);

 ReferenceType referenceType = new ReferenceType(intClass, stringType, null, null, false);
 JavaType result = referenceType.withValueHandler(stringHandler);

 assertEquals(stringType, result.getContentType());
 }

 @Test
 public void testReferenceTypeWithTypeHandler() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
 JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
 TypeFactory typeFactory = mapper.getTypeFactory();
 StringTypeHandler stringTypeHandler = typeFactory.constructTypeHandler(String.class);

 ReferenceType referenceType = new ReferenceType(intClass, stringType, null, stringTypeHandler, false);
 JavaType result = referenceType.withTypeHandler(null);

 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeWithInvalidTypeHandler() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
 JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
 ObjectMapper mapper = new ObjectMapper();
 StringValueHandler stringHandler = mapper.getTypeHandlerForValue(Integer.class);

 ReferenceType referenceType = new ReferenceType(intClass, stringType, null, null, false);
 JavaType result = referenceType.withTypeHandler(stringHandler);
 }

 @Test
 public void testReferenceTypeWithStaticTyping() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
 JavaType intType = TypeFactory.defaultInstance().constructType(intClass);

 ReferenceType referenceType = new ReferenceType(intClass, stringType, null, null, false);
 JavaType result = referenceType.withStaticTyping();

 assertTrue(result.isStatic());
 }

 @Test
 public void testReferenceTypeGetReferencedType() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
 JavaType intType = TypeFactory.defaultInstance().constructType(intClass);

 ReferenceType referenceType = new ReferenceType(intClass, stringType, null, null, false);
 JavaType result = referenceType.getReferencedType();

 assertEquals(stringType, result);
 }

 @Test
 public void testReferenceTypeIsReferenceType() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
 JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
 JavaType referenceType = new ReferenceType(intClass, stringType, null, null, false);

 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testNarrowingReferenceType() {
 Class<String> stringClass = String.class; Class<Integer> intClass = Integer.class; JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass); JavaType intType = TypeFactory.defaultInstance().constructType(intClass); JavaType intStringType = TypeFactory.defaultInstance().constructType(intClass, String.class);

 ReferenceType referenceType = new ReferenceType(intClass, stringType, null, null, false); JavaType narrowedType = referenceType.narrow(String.class); assertSame(intStringType, narrowedType); }

 @Test
 public void testConstruct() { Class<String> stringClass = String.class; Class<Integer> intClass = Integer.class; JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass); JavaType intType = TypeFactory.defaultInstance().constructType(intClass); ObjectMapper mapper = new ObjectMapper(); StringTypeHandler stringTypeHandler = mapper.getTypeHandlerForType(intType);

 ReferenceType referenceType = ReferenceType.construct(String.class, intType, null, stringTypeHandler); JavaType result = referenceType.withTypeHandler(null); assertNull(result); }
}