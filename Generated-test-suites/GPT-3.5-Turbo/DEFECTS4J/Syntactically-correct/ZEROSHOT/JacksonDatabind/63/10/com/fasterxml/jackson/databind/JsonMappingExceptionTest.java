package com.fasterxml.jackson.databind;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.util.ClassUtil;


public class JsonMappingExceptionTest {
  
  @Test
  public void testGetPath() {
      JsonMappingException exception = new JsonMappingException("Test exception");
      List<JsonMappingException.Reference> path = exception.getPath();
      assertNotNull(path);
      assertTrue(path.isEmpty());
  }

  @Test
  public void testGetPathReference() {
      JsonMappingException exception = new JsonMappingException("Test exception");
      String pathReference = exception.getPathReference();
      assertEquals("", pathReference);
  }
  
  @Test
  public void testPrependPath() {
      JsonMappingException exception = new JsonMappingException("Test exception");
      exception.prependPath("Referrer", "fieldName");
      List<JsonMappingException.Reference> path = exception.getPath();
      assertFalse(path.isEmpty());
      assertEquals(1, path.size());
      assertEquals("Referrer", path.get(0).getFrom());
      assertEquals("fieldName", path.get(0).getFieldName());
  }
  
  @Test
  public void testAppendPathDesc() {
      StringBuilder sb = new StringBuilder();
      JsonMappingException exception = new JsonMappingException("Test exception");
      exception.prependPath("Referrer", "fieldName");
      exception.prependPath("Referrer2", "fieldName2");
      exception._appendPathDesc(sb);
      assertEquals("Referrer2.fieldName2->Referrer.fieldName", sb.toString());
  }
}