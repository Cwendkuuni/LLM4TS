package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class DatabindContextTest {
  
  @Test
  public void testGetConfig() {
    DatabindContext context = new DatabindContextImpl();
    assertNotNull(context.getConfig());
  }
  
  @Test
  public void testGetAnnotationIntrospector() {
    DatabindContext context = new DatabindContextImpl();
    assertNotNull(context.getAnnotationIntrospector());
  }
  
  @Test
  public void testIsEnabled() {
    DatabindContext context = new DatabindContextImpl();
    assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
  }
  
  @Test
  public void testCanOverrideAccessModifiers() {
    DatabindContext context = new DatabindContextImpl();
    assertFalse(context.canOverrideAccessModifiers());
  }
  
  @Test
  public void testGetActiveView() {
    DatabindContext context = new DatabindContextImpl();
    assertNull(context.getActiveView());
  }
  
  @Test
  public void testGetLocale() {
    DatabindContext context = new DatabindContextImpl();
    assertNotNull(context.getLocale());
  }
  
  @Test
  public void testGetTimeZone() {
    DatabindContext context = new DatabindContextImpl();
    assertNotNull(context.getTimeZone());
  }
  
  @Test
  public void testGetDefaultPropertyFormat() {
    DatabindContext context = new DatabindContextImpl();
    assertNotNull(context.getDefaultPropertyFormat(String.class));
  }
  
  @Test
  public void testGetAttribute() {
    DatabindContext context = new DatabindContextImpl();
    context.setAttribute("key", "value");
    assertEquals("value", context.getAttribute("key"));
  }
  
  @Test
  public void testSetAttribute() {
    DatabindContext context = new DatabindContextImpl();
    context.setAttribute("key", "value");
    assertEquals("value", context.getAttribute("key"));
  }
  
  @Test
  public void testConstructType() {
    DatabindContext context = new DatabindContextImpl();
    assertNotNull(context.constructType(String.class));
  }
  
  @Test
  public void testConstructSpecializedType() {
    DatabindContext context = new DatabindContextImpl();
    JavaType baseType = context.constructType(Object.class);
    assertNotNull(context.constructSpecializedType(baseType, String.class));
  }
  
  @Test
  public void testResolveSubType() {
    DatabindContext context = new DatabindContextImpl();
    JavaType baseType = context.constructType(Object.class);
    assertNull(context.resolveSubType(baseType, "com.example.Foo"));
  }
  
  @Test(expected = JsonMappingException.class)
  public void testInvalidTypeIdException() {
    DatabindContext context = new DatabindContextImpl();
    JavaType baseType = context.constructType(Object.class);
    context.invalidTypeIdException(baseType, "foo", "Invalid type ID");
  }
  
  @Test
  public void testObjectIdGeneratorInstance() {
    DatabindContext context = new DatabindContextImpl();
    assertNull(context.objectIdGeneratorInstance(null, null));
  }
  
  @Test
  public void testObjectIdResolverInstance() {
    DatabindContext context = new DatabindContextImpl();
    assertNull(context.objectIdResolverInstance(null, null));
  }
  
  @Test
  public void testConverterInstance() {
    DatabindContext context = new DatabindContextImpl();
    assertNull(context.converterInstance(null, null));
  }
  
  @Test(expected = JsonMappingException.class)
  public void testReportBadDefinitionWithType() {
    DatabindContext context = new DatabindContextImpl();
    JavaType type = context.constructType(Object.class);
    context.reportBadDefinition(type, "Invalid definition");
  }
  
  @Test(expected = JsonMappingException.class)
  public void testReportBadDefinitionWithClass() {
    DatabindContext context = new DatabindContextImpl();
    context.reportBadDefinition(Object.class, "Invalid definition");
  }
  
  private static class DatabindContextImpl extends DatabindContext {
    @Override
    public MapperConfig<?> getConfig() {
      return null;
    }
  
    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
      return null;
    }
  
    @Override
    public boolean isEnabled(MapperFeature feature) {
      return true;
    }
  
    @Override
    public boolean canOverrideAccessModifiers() {
      return false;
    }
  
    @Override
    public Class<?> getActiveView() {
      return null;
    }
  
    @Override
    public Locale getLocale() {
      return Locale.getDefault();
    }
  
    @Override
    public TimeZone getTimeZone() {
      return TimeZone.getDefault();
    }
  
    @Override
    public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
      return null;
    }
  
    @Override
    public Object getAttribute(Object key) {
      return null;
    }
  
    @Override
    public DatabindContext setAttribute(Object key, Object value) {
      return this;
    }
  
    @Override
    public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
      return null;
    }
  
    @Override
    public TypeFactory getTypeFactory() {
      return null;
    }
  
    @Override
    public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
      return null;
    }
    protected String _truncate(String desc) {
      return desc;
    }
  }
}