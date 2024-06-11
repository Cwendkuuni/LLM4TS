package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypename;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DatabindContextTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setUpClass() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetConfig() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertNotNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertNotNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZABLE));
        assertFalse(context.isEnabled(MapperFeature.CAN_OVERride_ACCESS_MODIFIERS));
    }

    @Test
    public void testCanOverrideAccessModifiers() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        assertNull(context.getDefaultPropertyFormat(null));
    }

    @Test
    public void testGetAttribute() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        Object attribute = context.getAttribute("some.key");
        assertNull(attribute);
    }

    @Test
    public void testSetAttribute() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getSerializationConfig();
        context.setAttribute("some.key", "some value");
        Object attribute = context.getAttribute("some.key");
        assertEquals("some value", attribute);
    }

    @Test
    public void testConstructType() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        Type type = Integer.TYPE;
        JavaType javaType = context.getTypeFactory().constructType(type);
        assertEquals(Integer.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        JavaType specializedType = context.getTypeFactory().constructSpecializedType(baseType, String.class);
        assertEquals(String.class, specializedType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.Integer");
        assertEquals(Integer.class, subType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        context.invalidTypeIdException(baseType, "java.lang.String");
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, "myIdGen");
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, "myIdResolver");
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        Annotated annotated = new AnnotatedClass(String.class);
        Object converterDef = new StringConverter();
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws Exception {
        DatabindContext context = mapper.getSerializationConfig();
        JavaType type = context.getTypeFactory().constructType(String[].class);
        String msg = "Error message";
        try {
            Object obj = context.reportBadDefinition(type, msg);
            fail("Expected JsonMappingException, but got " + obj.getClass().getName());
        } catch (JsonMappingException e) {
            // success
            assertEquals(msg, e.getMessage());
        }
    }

    private static class StringConverter implements Converter<Object, String> {
        @Override
        public String convert(Object t) {
            if (t == null) {
                return null;
            }
            return t.toString();
        }
    }

    @JsonTypeName("StringType")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = String.class, name = "StringType1"),
            @JsonSubTypes.Type(value = Integer.class, name = "StringType2")
    })
    static class MyClass {
        private String name;

        public MyClass() {}

        public MyClass(String name) {
            this.name = name;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testCustomSerialization() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupSerialization(SerializerProvider serializers) {
                serializers.findConverter(MyClass.class, String.class)
                        .setConverter(new StringSerializer());
            }
        });

        MyClass obj = new MyClass("test");
        ObjectNode node = mapper.valueToTree(obj);
        assertEquals("{\"name\":\"test\"}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    private static class StringSerializer extends StdSerializer<MyClass> {
        public StringSerializer() {
            this(null);
        }

        public StringSerializer(Class<MyClass> t) {
            this(null, t);
        }

        public StringSerializer(Class<MyClass> t, Constructor<MyClass> c) {
            this(null, t, c);
        }

        public StringSerializer(Class<MyClass> t, Constructor<MyClass> c, PropertyModifier[] pms) {
            this(null, t, c, pms, null);
        }

        public StringSerializer(Class<MyClass> t, Constructor<MyClass> c, PropertyModifier[] pms,
                AnnotationIntrospector ai) {
            this(null, t, c, pms, ai);
        }

        public StringSerializer(Class<MyClass> t, Constructor<MyClass> c, PropertyModifier[] pms,
                AnnotationIntrospector ai, SerializerProvider sp) {
            super(t, c, pms, ai, sp);
        }

        @Override
        public void serialize(MyClass t, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeString(t.getName());
        }
    }
}