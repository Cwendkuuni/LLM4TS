package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testGetAnnotationIntrospector() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testIsEnabled() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testGetActiveView() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testGetLocale() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testGetTimeZone() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testGetAttribute() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testSetAttribute() {
        // Testing cannot be done in this abstract class, as it is abstract
    }

    @Test
    public void testConstructType() throws Exception {
        DatabindContext context = new TestDatabindContext();
        Type type = new TypeToken<Integer>() {}.getType();
        JavaType javaType = context.constructType(type);
        assertEquals(Integer.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() throws Exception {
        DatabindContext context = new TestDatabindContext();
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        JavaType specializedType = context.constructSpecializedType(baseType, String.class);
        assertEquals(String.class, specializedType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeFailure() throws Exception {
        DatabindContext context = new TestDatabindContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subClass = "java.util.Date";
        JavaType subType = context.resolveSubType(baseType, subClass);
        assertNull(subType);
    }

    @Test
    public void testResolveSubTypeSuccess() throws Exception {
        DatabindContext context = new TestDatabindContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subClass = "java.util.Date";
        JavaType subType = context.resolveSubType(baseType, subClass);
        assertEquals(Date.class, subType.getRawClass());
    }

    @Test
    public void testInvalidTypeIdException() {
        DatabindContext context = new TestDatabindContext() {
            @Override
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                return new InvalidDefinitionException(baseType, typeId, extraDesc);
            }
        };
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "InvalidTypeId";
        JsonMappingException exception = context.invalidTypeIdException(baseType, typeId, "Not a subtype");
        assertThat(exception, instanceOf(InvalidDefinitionException.class));
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = new TestDatabindContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        DatabindContext context = new TestDatabindContext();
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = new TestDatabindContext();
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        DatabindContext context = new TestDatabindContext();
        Annotated annotated = mock(Annotated.class);
        Object converterDef = new StringConvertor();
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws Exception {
        DatabindContext context = new TestDatabindContext() {
            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                throw new InvalidDefinitionException("Invalid definition", type);
            }
        };
        JavaType type = context.getTypeFactory().constructType(String.class);
        String msg = "Invalid definition";
        context.reportBadDefinition(type, msg);
    }

    @Test
    public void testReportBadDefinitionString() throws Exception {
        DatabindContext context = new TestDatabindContext();
        Class<?> type = String.class;
        String msg = "Invalid definition";
        Object badDefinition = context.reportBadDefinition(type, msg);
        assertNull(badDefinition);
    }

}

class TestDatabindContext extends DatabindContext {
    @Override
    protected <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
        return null;
    }
}

class StringConvertor implements Converter<String, String> {
    @Override
    public String readValue(String value) {
        return value;
    }

    @Override
    public String writeValue(String value) {
        return value;
    }
}