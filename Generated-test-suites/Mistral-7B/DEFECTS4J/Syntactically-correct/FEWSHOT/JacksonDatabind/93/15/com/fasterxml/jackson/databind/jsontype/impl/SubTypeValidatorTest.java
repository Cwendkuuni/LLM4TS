package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(Parameterized.class)
@MethodSorts(MethodSorters.NAME_ASCENDING)
public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();
 private static final TypeIdResolver ID_RESOLVER = MAPper.getTypeFactory();
 private static final DeserializationContext CTX = MAPper.unbindingContext();

 private static final JavaType STRING_TYPE = TypeReference.getType(String.class);
 private static final JavaType SUB_TYPE_1 = ID_RESOLVER.knownType(SubTypeValidator.class, "SubType1");
 private static final JavaType SUB_TYPE_2 = ID_RESOLVER.knownType(SubTypeValidator.class, "SubType2");

 private final JavaType type;
 private final String name;

 public SubTypeValidatorTest(JavaType type, String name) {
 this.type = type;
 this.name = name;
 }

 @Parameterized.Parameters
 public static Iterable<Object[]> data() {
 return new Object[][] {
 // valid types
 { SubTypeValidator.class, "SubTypeValidator" },
 { SUB_TYPE_1, "SubType1" },
 { SUB_TYPE_2, "SubType2" },
 { STRING_TYPE, "java.lang.String" },
 // invalid types according to DEFAULT_NO_DESER_CLASS_NAMES
 { ObjectFactory.class, "ObjectFactory" },
 { JdbcRowSetImpl.class, "com.sun.rowset.JdbcRowSetImpl" },
 { FileHandler.class, "java.util.logging.FileHandler" },
 { UnicastRemoteObject.class, "java.rmi.server.UnicastRemoteObject" },
 // invalid types according to PREFIX_STRING check
 { AbstractPointcutAdvisor.class, "org.springframework.aop.support.AbstractPointcutAdvisor" },
 { AbstractApplicationContext.class, "org.springframework.beans.factory.config.AbstractApplicationContext" }
 };
 }

 @Test
 public void testValidateSubType() throws Exception {
 Utils.test(this::validate, type.getName());
 }

 private void validate() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(CTX, type);
 }

 @Test
 public void testValidateSubType_IllegalType() throws Exception {
 assertThrows(JsonMappingException.class, () -> {
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(CTX, Object.class.getTypeName().equals(type.getName()) ? null : type);
 });
 }

 @Test
 public void testValidateSubType_TypeDoesNotExist() {
 assertThrows(JsonMappingException.class, () -> {
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(CTX, new TypeReference<Object>() {}.getType());
 });
 }

 @Test
 public void testValidateSubType_TypeIsInterface() throws Exception {
 Class<?> raw = type.getRawClass();
 JavaType intfType = ID_RESOLVER.knownType(raw.getInterfaces()[0]);
 Utils.test(() -> SubTypeValidator.instance().validateSubType(CTX, intfType), type.getName() + " interface");
 }

 @Test
 public void testValidateSubType_NestedType() throws Exception {
 TypeDeserializer tdeserializer = MAPper.getTypeFactory()
 .constructTypeDeserializer(SubTypeValidator.class, null, new ObjectNode(MAPper.createObjectNodeFactory()));
 JavaType nestedType = tdeserializer.findDeserializableType(new TypeReference<Object>() {}.getType(), CTX);
 Utils.test(() -> SubTypeValidator.instance().validateSubType(CTX, nestedType), type.getName() + " nested type");
 }

 @Test
 public void testValidateSubType_GenericType() throws Exception {
 JavaType genType = ID_RESOLVER.knownType(SubTypeValidator.class, "SubTypeValidator<String>");
 Utils.test(() -> SubTypeValidator.instance().validateSubType(CTX, genType), type.getName() + " generic type");
 }

 @Test
 public void testValidateSubType_ArrayType() throws Exception {
 JavaType arrayType = ID_RESOLVER.knownType(Object[].class);
 TypeReference<Object[]> ref = new TypeReference<Object[]>() {}.getType();
 JavaType validatedType = ID_RESOLVER.knownType(SubTypeValidator.class, type.getName() + "[]");
 Utils.test(() -> SubTypeValidator.instance().validateSubType(CTX, validatedType), type.getName() + " array");
 Utils.test(() -> SubTypeValidator.instance().validateSubType(CTX, arrayType), type.getName() + " array type");
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalType_ThrowsException() throws Exception {
 Utils.test(() -> SubTypeValidator.instance().validateSubType(CTX, ObjectFactory.class), name + " throws JsonMappingException");
 }

 @Test(expected = InvocationTargetException.class)
 public void testValidateSubType_IllegalTypeInSuperClass_ThrowsInvocationTargetException() throws Exception {
 Class<?> raw = type.getRawClass();
 JavaType superClassType = ID_RESOLVER.knownType(raw.getSuperclass(), "AbstractPointcutAdvisor");
 Utils.test(() -> SubTypeValidator.instance().validateSubType(CTX, superClassType), name + " in super class throws InvocationTargetException");
 }
}

class Utils {
 static <T> void test(T action, String description) throws Exception {
 System.out.println("Test [" + description + "]:");
 T original = action;
 try {
 action.call();
 System.out.println("Passed");
 } catch (JsonMappingException | InvocationTargetException e) {
 System.out.println("Failed: " + e.getMessage());
 throw e;
 }
 }
}