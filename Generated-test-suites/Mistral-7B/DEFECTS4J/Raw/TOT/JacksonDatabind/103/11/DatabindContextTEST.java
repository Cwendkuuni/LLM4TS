package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        JsonFactory jsonFactory = new JsonFactory();
        objectMapper = jsonFactory.createObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        if (objectMapper != null) {
            objectMapper.close();
        }
    }

    @Test
    public void testGenericConfigAccess() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        MapperConfig<?> config = context.getConfig();
        assertEquals(MapperFeature.AUTO_DETECT_TYPES, config.getConfiguredMapperFeature(MapperFeature.AUTO_DETECT_TYPES));

        context.setEnabled(MapperFeature.AUTO_DETECT_TYPES, false);
        assertFalse(config.getConfiguredMapperFeature(MapperFeature.AUTO_DETECT_TYPES));

        context = objectMapper.getSerializerProvider().getDefaultContext();
        assertEquals(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, context.canOverrideAccessModifiers());

        context.setLocale(Locale.US);
        assertEquals(Locale.US, context.getLocale());

        context.setTimeZone(TimeZone.getDefault());
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGenericAttributes() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();

        Object attr1 = new Object();
        context.setAttribute("testAttr1", attr1);
        assertEquals(attr1, context.getAttribute("testAttr1"));

        Object attr2 = new Object();
        context.setAttribute("testAttr2", attr2);
        assertEquals(attr2, context.getAttribute("testAttr2"));

        context.setAttribute("testAttr1", null);
        assertNull(context.getAttribute("testAttr1"));
    }

    @Test
    public void testTypeInstantiationResolution() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();

        JavaType baseType = typeFactory.constructType(String.class);
        JavaType subType = context.constructSpecializedType(baseType, String[].class);
        JavaType resolvedType = context.resolveSubType(baseType, "java.lang.String[]");
        assertEquals(resolvedType, subType);

        JavaType javaType = typeFactory.constructType(MyClass.class);
        assertNull(context.resolveSubType(baseType, MyClass.class.getName()));
    }

    @Test
    public void testHelperObjectConstruction() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();

        Annotated annotated = new AnnotatedClassWrapper(MyClassWithId.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, null);
        assertNotNull(objectIdGenerator);

        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, null);
        assertNotNull(objectIdResolver);

        Annotated annotated2 = new AnnotatedClassWrapper(MyConverter.class);
        Converter<Object, Object> converter = context.converterInstance(annotated2, null);
        assertNotNull(converter);
    }

    @Test
    public void testErrorReporting() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();

        JavaType javaType = context.getTypeFactory().constructType(MyInvalidType.class);
        assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(javaType, ""));
    }
}

class AnnotatedClassWrapper {
    private final Class<?> clazz;

    public AnnotatedClassWrapper(Class<?> clazz) {
        this.clazz = clazz;
    }

    @JsonProperty("type")
    public Object getType() {
        return clazz;
    }
}

class MyClass {
    // Empty class
}

class MyClassWithId {
    // Empty class annotated with @JsonIdentityInfo and @JsonIdentityProperty
}

class MyConverter {
    // Empty class annotated with @JsonDeserialize and @JsonSerialize
}

class MyInvalidType {
    // Empty class with invalid type definition
}