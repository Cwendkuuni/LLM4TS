
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
  
  @Test
  public void testFindForCollection() throws JsonMappingException {
    DeserializationContext ctxt = null;
    JavaType type = null;

    assertNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));
  }

  @Test
  public void testFindForMap() throws JsonMappingException {
    DeserializationContext ctxt = null;
    JavaType type = null;

    assertNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, type));
  }

  @Test
  public void testJavaUtilCollectionsConverter() {
    int kind = 0;
    JavaType inputType = null;

    JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = 
        new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, inputType);

    Object value = null;
    assertNull(converter.convert(value));

    TypeFactory typeFactory = null;
    assertEquals(inputType, converter.getInputType(typeFactory));
    assertEquals(inputType, converter.getOutputType(typeFactory));
  }
}
