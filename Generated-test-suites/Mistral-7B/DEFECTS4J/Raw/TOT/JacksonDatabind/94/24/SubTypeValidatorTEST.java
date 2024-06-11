package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidateSubTypeWithValidType() throws Exception {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), validType);
 }

 @Test
 public void testValidateSubTypeWithNastyType() throws Exception {
 String nastyClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType nastyType = mapper.getTypeFactory().constructType(Class.forName(nastyClassName));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType));
 }

 @Test
 public void testValidateSubTypeWithSpringType() throws Exception {
 String springClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType springType = mapper.getTypeFactory().constructType(Class.forName(springClassName));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springType));
 }

 @Test
 public void testValidateSubTypeWithCustomIllegalType() throws Exception {
 Set<String> customIllegalClasses = new HashSet<>();
 customIllegalClasses.add("com.example.MyNastyClass");
 SubTypeValidator validator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = customIllegalClasses;
 };
 String customIllegalClassName = "com.example.MyNastyClass";
 JavaType customIllegalType = mapper.getTypeFactory().constructType(Class.forName(customIllegalClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), customIllegalType));
 }

 @Test
 public void testValidateSubTypeWithInterface() throws Exception {
 JavaType interfaceType = mapper.getTypeFactory().constructType(Runnable.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), interfaceType);
 }

 @Test
 public void testValidateSubTypeWithSpringInterface() throws Exception {
 JavaType springInterfaceType = mapper.getTypeFactory().constructType(ApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), springInterfaceType);
 }

 @Test
 public void testValidateSubTypeWithDeepSpringClass() throws Exception {
 String deepSpringClassName1 = "org.springframework.beans.factory.support.DefaultListableBeanFactory";
 JavaType deepSpringType1 = mapper.getTypeFactory().constructType(Class.forName(deepSpringClassName1));
 String deepSpringClassName2 = "org.springframework.beans.factory.support.DefaultSingletonBeanRegistry";
 JavaType deepSpringType2 = mapper.getTypeFactory().constructType(Class.forName(deepSpringClassName2));
 JavaType deepSpringType3 = mapper.getTypeFactory().constructType(Class.forName("org.springframework.beans.factory.support.DefaultSingletonBeanRegistry$SingletonBeanRegistryFactoryBean"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), deepSpringType3);
 }

 @Test
 public void testValidateSubTypeWithJdkClass() throws Exception {
 JavaType jdkClassType = mapper.getTypeFactory().constructType(FileHandler.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), jdkClassType);
 }

 @Test
 public void testValidateSubTypeWithCustomNastyClassInSuperClassChain() throws Exception {
 Class<?> customNastyClass = Class.forName("com.example.MyNastySuperclass");
 Class<?> nestedNastyClass = Class.forName("com.example.MyNastyNestedClass");
 JavaType type = mapper.getTypeFactory().constructType(new TypeReference<MyNestedClass>() {}.getType());
 MyNestedClass nested = new MyNestedClass();
 nested.field = new MyNastyClass();
 JavaType nestedType = mapper.getTypeFactory().constructType(nested.getClass());
 JavaType typeWithNastySuperclass = mapper.getTypeFactory().constructType(new TypeReference<MyClass>() {
 @Override public Class<?> getRawType() { return MyClass.class; }
 @Override public Type<?> getComponentType() { return null; }
 @Override public String toString() { return "MyClass"; }
 }.getType());
 MyClass myClass = new MyClass();
 myClass.field = nested;
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), typeWithNastySuperclass));
 }

 @Test
 public void testValidateSubTypeWithCustomNastyClassInInterfaceChain() throws Exception {
 interface MyInterface {
 void doSomething();
 }

 class MyImplementation implements MyInterface {
 // empty implementation
 }

 JavaType interfaceType = mapper.getTypeFactory().constructType(MyInterface.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), interfaceType));
 }
}