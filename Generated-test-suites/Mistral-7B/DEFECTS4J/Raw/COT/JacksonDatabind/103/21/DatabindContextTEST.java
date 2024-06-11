package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

/**
 * Tests for {@link DatabindContext}.
 */
public class DatabindContextTest {

    

    // Test for getConfig() method
    @Test
    public void testGetConfig() throws Exception {
        DatabindContext context = createTestContext();
        assertNotNull(context.getConfig());
    }

    // Test for getAnnotationIntrospector() method
    @Test
    public void testGetAnnotationIntrospector() throws Exception {
        DatabindContext context = createTestContext();
        assertNotNull(context.getAnnotationIntrospector());
    }

    // Test for isEnabled(MapperFeature feature) method
    @Test
    public void testIsEnabled() throws Exception {
        DatabindContext context = createTestContext();
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_USE_ANNOTATIONS));
        assertFalse(context.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPES));
    }

    // Test for canOverrideAccessModifiers() method
    @Test
    public void testCanOverrideAccessModifiers() throws Exception {
        DatabindContext context = createTestContext();
        assertTrue(context.canOverrideAccessModifiers());
    }

    // Test for getActiveView() method
    @Test
    public void testGetActiveView() throws Exception {
        DatabindContext context = createTestContext();
        assertNull(context.getActiveView());
    }

    // Test for getLocale() method
    @Test
    public void testGetLocale() throws Exception {
        DatabindContext context = createTestContext();
        assertNull(context.getLocale());
    }

    // Test for getTimeZone() method
    @Test
    public void testGetTimeZone() throws Exception {
        DatabindContext context = createTestContext();
        assertNull(context.getTimeZone());
    }

    // Test for getDefaultPropertyFormat(Class<?> baseType) method
    @Test
    public void testGetDefaultPropertyFormat() throws Exception {
        DatabindContext context = createTestContext();
        assertNull(context.getDefaultPropertyFormat(Object.class));
    }

    // Test for getAttribute(Object key) method
    @Test
    public void testGetAttribute() throws Exception {
        DatabindContext context = createTestContext();
        Object attr = context.getAttribute("testAttr");
        assertNull(attr);
    }

    // Test for setAttribute(Object key, Object value) method
    @Test
    public void testSetAttribute() throws Exception {
        DatabindContext context = createTestContext();
        context.setAttribute("testAttr", "testValue");
        Object attr = context.getAttribute("testAttr");
        assertEquals("testValue", attr);
    }

    // Test for constructType(Type type) method
    @Test
    public void testConstructType() throws Exception {
        DatabindContext context = createTestContext();
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test for constructSpecializedType(JavaType baseType, Class<?> subclass) method
    @Test
    public void testConstructSpecializedType() throws Exception {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.constructSpecializedType(baseType, StringBuffer.class);
        assertNotNull(subType);
        assertEquals(StringBuffer.class, subType.getRawClass());
    }

    // Test for resolveSubType(JavaType baseType, String subClass) method: simple case
    @Test
    public void testResolveSubTypeSimpleCase() throws Exception {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.StringBuffer");
        assertNotNull(subType);
        assertEquals(StringBuffer.class, subType.getRawClass());
    }

    // Test for resolveSubType(JavaType baseType, String subClass) method: failure case
    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeFailureCase() throws Exception {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.resolveSubType(baseType, "java.util.Date");
    }

    // Test for invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) method: failure case
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdExceptionFailureCase() throws Exception {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "java.util.Date", "Not a subtype");
    }

    // Test for getTypeFactory() method
    @Test
    public void testgetTypeFactory() throws Exception {
        DatabindContext context = createTestContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    // Test for objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo) method
    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        DatabindContext context = createTestContext();
        ObjectIdGenerator<?> generator = context.objectIdGeneratorInstance(null, null);
        assertNull(generator);
    }

    // Test for objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) method
    @Test
    public void testObjectIdResolverInstance() throws Exception {
        DatabindContext context = createTestContext();
        ObjectIdResolver resolver = context.objectIdResolverInstance(null, null);
        assertNull(resolver);
    }

    // Test for converterInstance(Annotated annotated, Object converterDef) method
    @Test
    public void testConverterInstance() throws Exception {
        DatabindContext context = createTestContext();
        Converter<String, Integer> converter = context.converterInstance(null, null);
        assertNull(converter);
    }

    // Test for reportBadDefinition(JavaType type, String msg) method: simple case
    @Test
    public void testReportBadDefinitionSimpleCase() throws Exception {
        DatabindContext context = createTestContext();
        assertEquals("expected: java.lang.String\nactual: com.example.MyStringClass\n",
                context.reportBadDefinition(String.class, "expected: java.lang.String\nactual: com.example.MyStringClass").getMessage());
    }

    // Test for reportBadDefinition(Class<?> type, String msg) method: simple case
    @Test
    public void testReportBadDefinitionSimpleCaseClass() throws Exception {
        DatabindContext context = createTestContext();
        assertEquals("expected: com.example.MyClass\nactual: java.lang.String\n",
                context.reportBadDefinition(MyClass.class, "expected: com.example.MyClass\nactual: java.lang.String").getMessage());
    }

    // Test for reportBadDefinition(JavaType type, String msg) method: edge case
    @Test
    public void testReportBadDefinitionEdgeCase() throws Exception {
        DatabindContext context = createTestContext();
        String msg = "Error message: ";
        String desc = "This is a very long error message that goes beyond the limit of 500 characters.";
        assertEquals(msg + _truncate(desc) + "...[" + desc.substring(desc.length() - MAX_ERROR_STR_LEN) + "]",
                context.reportBadDefinition(String.class, msg + desc).getMessage());
    }

    // Test for _format(String msg, Object... msgArgs) method
    @Test
    public void testFormat() throws Exception {
        DatabindContext context = createTestContext();
        String msg = "Error: {0} {1} {2}";
        Object[] msgArgs = {"Unexpected", "error", "message"};
        assertEquals("Error: Unexpected error message", context._format(msg, msgArgs));
    }

    // Test for _quotedString(String desc) method
    @Test
    public void testQuotedString() throws Exception {
        DatabindContext context = createTestContext();
        String desc = "This is a test string";
        assertEquals(_quotedString(_desc(desc)), _quotedString("This is a test string"));
    }

    // Test for _colonConcat(String msgBase, String extra) method
    @Test
    public void testColonConcat() throws Exception {
        DatabindContext context = createTestContext();
        String msgBase = "Base message: ";
        String extra = "Extra info";
        assertEquals(msgBase + ": " + extra, context._colonConcat(msgBase, extra));
    }

    // Test for _desc(String desc) method
    @Test
    public void testDesc() throws Exception {
        DatabindContext context = createTestContext();
        String desc = "This is a test description";
        assertEquals(_desc(desc), _desc("This is a test description"));
    }

    private DatabindContext createTestContext() throws Exception {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        return config.getInstance(DatabindContext.class);
    }

    private static class MyClass {
        @JsonIdentityInfo(finder = ObjectIdGenerator.None.class)
        public static class MyNestedClass {
            private int id;
            private String name;

            @JsonProperty
            public int getId() {
                return id;
            }

            @JsonProperty
            public void setId(int id) {
                this.id = id;
            }

            @JsonProperty
            public String getName() {
                return name;
            }

            @JsonProperty
            public void setName(String name) {
                this.name = name;
            }
        }
    }

    
}