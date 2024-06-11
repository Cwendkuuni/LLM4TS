package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

 private DeserializationContext context;
 private ObjectMapper objectMapper;
 private JavaTypeFactory typeFactory;
 private TypeFactory typeFactory2;
 private SubTypeValidator subTypeValidator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 context = Mockito.mock(DeserializationContext.class);
 objectMapper = new ObjectMapper();
 typeFactory = objectMapper.getTypeFactory();
 typeFactory2 = objectMapper.getTypeFactory();
 subTypeValidator = SubTypeValidator.instance();
 }

 @Test
 public void testValidType() throws Exception {
 JavaType validType = typeFactory.constructType(String.class);
 subTypeValidator.validateSubType(context, validType);
 }

 @Test
 public void testNastyClass() throws Exception {
 JavaType nastyType = typeFactory.constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, nastyType));
 }

 @Test
 public void testSubClassOfNastyClass() throws Exception {
 JavaType subClassOfNastyType = typeFactory2.constructType(org.apache.commons.collections.map.TransformedMap.Entry.class);
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, subClassOfNastyType));
 }

 @Test
 public void testSpringClass() throws Exception {
 JavaType springType = typeFactory.constructType(org.springframework.beans.factory.ObjectFactory.class);
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, springType));
 }

 @Test
 public void testSuperClassOfSpringClass() throws Exception {
 JavaType superClassOfSpringType = typeFactory.constructType(java.util.Map.class);
 subTypeValidator.validateSubType(context, superClassOfSpringType);
 }
}