package com.fasterxml.jackson.databind;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static org.junit.Assert.*;

import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test cases for {@link DatabindContext}.
 */
public class DatabindContextTest {

    /**
     * Test for {@link DatabindContext#getDefaultPropertyFormat(Class)}.
     */
    @Test
    public void testDefaultPropertyFormat() {
        DatabindContext context = new BaseDatabindContext();
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertNotNull(format);
    }

    /**
     * Test for {@link DatabindContext#getAttribute(Object)}.
     */
    @Test
    public void testAttributeGet() {
        DatabindContext context = new BaseDatabindContext();
        Object attribute = context.getAttribute("test");
        assertNull(attribute);

        context.setAttribute("test", "value");
        attribute = context.getAttribute("test");
        assertEquals("value", attribute);
    }

    /**
     * Test for {@link DatabindContext#getAttribute(Object)}.
     */
    @Test
    public void testAttributeSet() {
        DatabindContext context = new BaseDatabindContext();
        context.setAttribute("test", "value");
        assertEquals("value", context.getAttribute("test"));
    }

    /**
     * Test for {@link DatabindContext#constructType(Type)}.
     */
    @Test
    public void testConstructType() {
        DatabindContext context = new BaseDatabindContext();
        Type type = String[].class;
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
        assertEquals(String[].class, javaType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#constructSpecializedType(JavaType, Class)}.
     */
    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = new BaseDatabindContext();
        JavaType baseType = context.constructType(String.class);
        Class<?> subclass = String[].class;
        JavaType javaType = context.constructSpecializedType(baseType, subclass);
        assertNotNull(javaType);
        assertEquals(String[].class, javaType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#resolveSubType(JavaType, String)}.
     */
    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = new BaseDatabindContext();
        JavaType baseType = context.constructType(String.class);
        String subClassName = "java.util.ArrayList<java.lang.String>";
        JavaType javaType = context.resolveSubType(baseType, subClassName);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = new BaseDatabindContext();
        JavaType baseType = context.constructType(String.class);
        String typeId = "not-a-subtype";
        context.invalidTypeIdException(baseType, typeId, "Not a subtype");
    }

    /**
     * Test for {@link DatabindContext#getTypeFactory()}.
     */
    @Test
    public void testTypeFactory() {
        DatabindContext context = new BaseDatabindContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    /**
     * Test for {@link DatabindContext#objectIdGeneratorInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = new BaseDatabindContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> idGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(idGenerator);
    }

    /**
     * Test for {@link DatabindContext#objectIdResolverInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = new BaseDatabindContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdResolver idResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(idResolver);
    }

    /**
     * Test for {@link DatabindContext#converterInstance(Annotated, Object)}.
     */
    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = new BaseDatabindContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        Object converterDef = new StringConverter();
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    /**
     * Test for {@link DatabindContext#reportBadDefinition(JavaType, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = new BaseDatabindContext();
        JavaType javaType = context.constructType(String.class);
        context.reportBadDefinition(javaType, "Error message");
    }
}

/**
 * Base implementation of {@link DatabindContext}.
 */
class BaseDatabindContext extends DatabindContext {
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
        return true;
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
    public JavaType constructType(Type type) {
        return null;
    }

    @Override
    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
        return null;
    }

    @Override
    public JavaType resolveSubType(JavaType baseType, String subClass) throws JsonMappingException {
        return null;
    }

    @Override
    public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
        throw new JsonMappingException(msg);
    }

    @Override
    public TypeFactory getTypeFactory() {
        return new TypeFactoryBuilder().build();
    }

    @Override
    public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
            throws JsonMappingException {
        return null;
    }

    @Override
    public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
        return null;
    }

    @Override
    public <T> T converterInstance(Annotated annotated, Object converterDef) throws JsonMappingException {
        return null;
    }

    @Override
    public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
        throw new JsonMappingException(msg);
    }
}

/**
 * Mock converter for testing {@link DatabindContext#converterInstance(Annotated, Object)}.
 */
class StringConverter {}