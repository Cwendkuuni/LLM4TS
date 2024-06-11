package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link DatabindContext} class.
 */
public class DatabindContextTest {

    /**
     * Test case for {@link DatabindContext#getConfig()}.
     */
    @Test
    public void testGetConfig() {
        // Create a mock MapperConfig
        MapperConfig<?> mockConfig = mock(MapperConfig.class);

        // Create a mock DatabindContext with the mock MapperConfig
        DatabindContext databindContext = mock(DatabindContext.class);
        when(databindContext.getConfig()).thenReturn(mockConfig);

        // Test the getConfig() method
        MapperConfig<?> result = databindContext.getConfig();
        assertEquals(mockConfig, result);
    }

    /**
     * Test case for {@link DatabindContext#isEnabled(MapperFeature)}.
     */
    @Test
    public void testIsEnabled() {
        // Create a mock DatabindContext with a configured MapperFeature flag
        DatabindContext databindContext = mock(DatabindContext.class);
        when(databindContext.isEnabled(any(MapperFeature.class))).thenReturn(true);

        // Test the isEnabled() method
        assertTrue(databindContext.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_PRIMITIVES));
    }

    /**
     * Test case for {@link DatabindContext#getDefaultPropertyFormat(Class)}.
     */
    @Test
    public void testgetDefaultPropertyFormat() {
        // Create a mock DatabindContext with a configured default property format
        DatabindContext databindContext = mock(DatabindContext.class);
        when(databindContext.getDefaultPropertyFormat(any(Class.class))).thenReturn(null);

        // Test the getDefaultPropertyFormat() method
        JsonFormat.Value result = databindContext.getDefaultPropertyFormat(String.class);
        assertNull(result);
    }

    /**
     * Test case for {@link DatabindContext#getAttribute(Object)}.
     */
    @Test
    public void testGetAttribute() {
        // Create a mock DatabindContext with a configured attribute
        DatabindContext databindContext = mock(DatabindContext.class);
        Object attribute = new Object();
        when(databindContext.getAttribute(anyObject())).thenReturn(attribute);

        // Test the getAttribute() method
        Object result = databindContext.getAttribute(new Object());
        assertEquals(attribute, result);
    }

    /**
     * Test case for {@link DatabindContext#setAttribute(Object, Object)}.
     */
    @Test
    public void testSetAttribute() {
        // Create a mock DatabindContext
        DatabindContext databindContext = mock(DatabindContext.class);

        // Test setting and getting an attribute
        Object key = new Object();
        Object value = new Object();
        databindContext.setAttribute(key, value);
        assertEquals(value, databindContext.getAttribute(key));
    }

    /**
     * Test case for {@link DatabindContext#getTypeFactory()}.
     */
    @Test
    public void testTypeFactory() {
        // Create a mock DatabindContext with a TypeFactory instance
        DatabindContext databindContext = mock(DatabindContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        when(databindContext.getTypeFactory()).thenReturn(typeFactory);

        // Test the getTypeFactory() method
        TypeFactory result = databindContext.getTypeFactory();
        assertEquals(typeFactory, result);
    }

    /**
     * Test case for {@link DatabindContext#constructType(Type)}.
     */
    @Test
    public void testConstructType() {
        // Create a mock DatabindContext with a TypeFactory instance
        DatabindContext databindContext = mock(DatabindContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        when(databindContext.getTypeFactory()).thenReturn(typeFactory);

        // Test constructing a JavaType instance
        Type type = new TypeToken<String>() {}.getType();
        JavaType result = databindContext.constructType(type);
        assertEquals(type, result.getType());
    }

    /**
     * Test case for {@link DatabindContext#resolveSubType(JavaType, String)}.
     */
    @Test
    public void testResolveSubType() throws JsonMappingException {
        // Create a mock DatabindContext with a TypeFactory instance
        DatabindContext databindContext = mock(DatabindContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        when(databindContext.getTypeFactory()).thenReturn(typeFactory);

        // Test resolving a subtype by name
        JavaType baseType = typeFactory.constructType(int[].class);
        String subTypeName = "java.util.ArrayList<java.lang.Integer>";
        JavaType result = databindContext.resolveSubType(baseType, subTypeName);
        assertNotNull(result);
        assertEquals(Integer.class, result.getRawClass());
    }

    /**
     * Test case for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        // Create a mock DatabindContext
        DatabindContext databindContext = mock(DatabindContext.class);

        // Test the invalidTypeIdException() method
        JavaType baseType = new TypeFactory().constructType(int[].class);
        String typeId = "com.example.MyClass";
        databindContext.invalidTypeIdException(baseType, typeId, null);
    }
}