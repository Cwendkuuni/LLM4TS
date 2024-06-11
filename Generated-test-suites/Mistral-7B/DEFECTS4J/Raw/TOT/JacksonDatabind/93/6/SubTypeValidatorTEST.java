package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;
 private DeserializationContext ctxt;
 private SubTypeValidator subTypeValidator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(SubTypeValidator.class, new SubTypeValidatorDeserializer());
 objectMapper.registerModule(module);
 ctxt = objectMapper.getDeserializationContext();
 subTypeValidator = SubTypeValidator.instance();
 }

 @Test
 public void testValidType() throws JsonMappingException {
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 subTypeValidator.validateSubType(ctxt, validType);
 }

 @Test
 public void testNastyClass() throws JsonMappingException {
 JavaType nastyClass = objectMapper.getTypeFactory().constructType(InvokerTransformer.class);
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(ctxt, nastyClass));
 }

 @Test
 public void testSpringApplicationContext() throws JsonMappingException {
 JavaType springApplicationContext = objectMapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 assertFalse(subTypeValidator._cfgIllegalClassNames.contains(springApplicationContext.getRawClass().getCanonicalName()));
 }

 @Test
 public void testSpringAbstractPointcutAdvisor() throws JsonMappingException {
 JavaType springAbstractPointcutAdvisor = objectMapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 assertFalse(subTypeValidator._cfgIllegalClassNames.contains(springAbstractPointcutAdvisor.getRawClass().getCanonicalName()));

 // Simulate a super class that is illegal
 JavaType superIllegalClass = objectMapper.getTypeFactory().constructType(AbstractPointcutAdvisorSuperclass.class);
 doReturn(superIllegalClass).when(ctxt).findDeserializerForValue("badClass");
 JavaType typeToDeserialize = objectMapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(ctxt, typeToDeserialize));
 }

 @Test
 public void testCustomIllegalClass() throws JsonMappingException {
 Set<String> customIllegalClasses = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 customIllegalClasses.add("com.example.CustomNastyClass");
 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = customIllegalClasses;
 };
 JavaType customNastyClass = objectMapper.getTypeFactory().constructType(CustomNastyClass.class);
 assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(ctxt, customNastyClass));
 }

 @Test
 public void testEmptySet() throws JsonMappingException {
 Set<String> emptySet = new HashSet<>();
 SubTypeValidator emptyValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = emptySet;
 };
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 emptyValidator.validateSubType(ctxt, validType);
 }
}

class SubTypeValidatorDeserializer extends StdDeserializer<SubTypeValidator> {
 SubTypeValidatorDeserializer() {
 this(null);
 }

 SubTypeValidatorDeserializer(Class<?> vc) {
 super(vc);
 }

 @Override
 public SubTypeValidator deserialize(DeserializationContext ctxt, JsonParser jp) throws IOException {
 return SubTypeValidator.instance();
 }
}

class AbstractPointcutAdvisorSuperclass {
 // Empty class used to simulate a super class for AbstractPointcutAdvisor
}