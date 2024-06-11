package com.example;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class SubTypeValidatorTest {

@Test
public void testValidateSubType_ValidType() throws Exception {
DeserializationContext ctxt = mock(DeserializationContext.class);
JavaType type = mock(JavaType.class);

SubTypeValidator validator = SubTypeValidator.instance();
validator.validateSubType(ctxt, type);
}

@Test
public void testValidateSubType_IllegalType() throws Exception {
DeserializationContext ctxt = mock(DeserializationContext.class);
JavaType type = mock(JavaType.class);

when(type.getRawClass().getName()).thenReturn("org.apache.commons.collections4.functors.InstantiateTransformer");

SubTypeValidator validator = SubTypeValidator.instance();
assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
}

@Test
public void testValidateSubType_NullDeserializationContext() {
JavaType type = mock(JavaType.class);
assertThrows(JsonMappingException.class, () -> SubTypeValidator.instance().validateSubType(null, type));
}

@Test
public void testValidateSubType_NullJavaType() {
DeserializationContext ctxt = mock(DeserializationContext.class);
assertThrows(JsonMappingException.class, () -> SubTypeValidator.instance().validateSubType(ctxt, null));
}

@Test
public void testValidateSubType_EmptyClassName() {
DeserializationContext ctxt = mock(DeserializationContext.class);
JavaType type = mock(JavaType.class);
when(type.getRawClass().getName()).thenReturn("");

SubTypeValidator validator = SubTypeValidator.instance();
assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
}

@Test
public void testValidateSubType_DifferentPackagesButSameSimpleName() throws Exception {
DeserializationContext ctxt = mock(DeserializationContext.class);
JavaType type1 = mock(JavaType.class);
JavaType type2 = mock(JavaType.class);
when(type1.getRawClass().getName()).thenReturn("com.example.foo.MyClass");
when(type2.getRawClass().getName()).thenReturn("com.example.bar.MyClass");

SubTypeValidator validator = SubTypeValidator.instance();
assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type1));
assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type2));
}
}