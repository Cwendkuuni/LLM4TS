package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private static final String TEST_CLASS_NAME = "com.example.test.TestClass";

    private DatabindContext context;
    private MapperConfig<ObjectMapper> config;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getConfig();
        context = new BaseDatabindContext(config);
    }

    @Test
    public void testGetConfig() {
        assertEquals(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertEquals(config.getNodeFactory().getDefaultIntrospector(), context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        assertFalse(context.isEnabled(MapperFeature.CAN_HANDLE_FINAL_FIELDS));
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Class<?> baseType = Object.class;
        JsonFormat.Value defaultFormat = context.getDefaultPropertyFormat(baseType);
        assertNull(defaultFormat);
    }

    @Test
    public void testGetAttribute() {
        Object key = "testKey";
        Object value = "testValue";
        context.setAttribute(key, value);
        assertEquals(value, context.getAttribute(key));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.Integer");
        assertEquals(Integer.class, subType.getRawClass());

        JavaType interfaceType = context.getTypeFactory().constructType(Object.class);
        JavaType implementationType = context.resolveSubType(interfaceType, "com.example.test.TestClassImpl");
        assertEquals(TestClassImpl.class, implementationType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        context.invalidTypeIdException(baseType, "java.lang.String");
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertEquals(typeFactory, context.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        ObjectIdInfo objectIdInfo = new DefaultObjectIdGenerator.ObjectIdGeneratorInfo(TEST_CLASS_NAME, 0);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(new AnnotatedClass(TEST_CLASS_NAME), objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        ObjectIdInfo objectIdInfo = new DefaultObjectIdResolver.ObjectIdResolverInfo(TEST_CLASS_NAME, null);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(new AnnotatedClass(TEST_className), objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        AnnotatedMember<?> member = new AnnotatedMember(TEST_CLASS_NAME, "testField");
        JsonTypeInfo.Id testIdAnnotation = new JsonTypeInfo.IdAnnotated(JsonTypeInfo.Id.CLASS);
        member.addAnnotation(testIdAnnotation);

        ObjectNode testNode = JsonNodeFactory.instance.objectNode();
        testNode.put("value", "testValue");

        Converter<Object, Object> converter = context.converterInstance(member, testNode);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType(Integer.class);
        String msg = "Test error message";
        Object badDefinition = context.reportBadDefinition(type, msg);
        assertNull(badDefinition);
    }
}

class BaseDatabindContext extends DatabindContext {
    public BaseDatabindContext(MapperConfig<?> config) {
        super();
        this.config = config;
    }

    @Override
    public MapperConfig<?> getConfig() {
        return config;
    }

    @Override
    protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
        return new InvalidDefinitionException(String.format("Cannot construct instance of %s%s: %s",
                _desc(baseType.getRawClass().getName()), _colonConcat(extraDesc, ""), _desc(typeId)));
    }

    @Override
    public TypeFactory getTypeFactory() {
        return config.getTypeFactory();
    }
}