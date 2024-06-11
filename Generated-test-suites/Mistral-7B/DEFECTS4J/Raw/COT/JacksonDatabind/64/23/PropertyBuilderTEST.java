import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        serializationConfig = objectMapper.getSerializationConfig();
        beanDescription = new BeanDescription(SomeClass.class);
        annotationIntrospector = serializationConfig.getDefaultAnnotationIntrospector();
    }

    @Test
    public void testConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testBuildWriter_NullSerializer() throws JsonMappingException {
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        TypeSerializer typeSer = null;
        JsonSerializer<?> ser = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        try {
            propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Null serializer provided", e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType_NoAnnotations() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(result);
    }

    @Test
    public void testFindSerializationType_ExplicitType() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType expectedType = TypeFactory.defaultInstance().constructType(Integer[].class);
        JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals(expectedType, result);
    }

    @Test
    public void testFindSerializationType_AnnotationOverrides() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(SomeClass.class);

        JavaType expectedType = TypeFactory.defaultInstance().constructType(OtherClass.class);
        when(a.getMemberName()).thenReturn("someField");
        when(a.getAnnotations()).thenReturn(new JsonSerialize.TypedAnnotation[0]);
        when(annotationIntrospector.findSerializationType(a, true, declaredType)).thenReturn(null);
        when(annotationIntrospector.findSerializationType(a, false, declaredType)).thenReturn(expectedType);

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(expectedType, result);
    }

    @Test
    public void testGetPropertyDefaultValue_NullMember() {
        AnnotatedMember member = null;
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object result = propertyBuilder.getPropertyDefaultValue("someField", member, type);
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_String() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        String expectedValue = "default";
        when(member.getValue(anyObject())).thenReturn(expectedValue);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object result = propertyBuilder.getPropertyDefaultValue("someField", member, type);
        assertEquals(expectedValue, result);
    }

    @Test
    public void testGetPropertyDefaultValue_Object() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        Object expectedDefaultBean = new Object();
        when(member.getValue(eq(expectedDefaultBean))).thenReturn(expectedDefaultBean);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);

        Object result = propertyBuilder.getPropertyDefaultValue("someField", member, type);
        assertEquals(expectedDefaultBean, result);
    }

    @Test
    public void testGetPropertyDefaultValue_Array() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        Object[] expectedDefaultValue = new Object[2];
        when(member.getValue(anyObject())).thenReturn(expectedDefaultValue);
        JavaType type = TypeFactory.defaultInstance().constructType(Object[].class);

        Object result = propertyBuilder.getPropertyDefaultValue("someField", member, type);
        assertArrayEquals(expectedDefaultValue, result);
    }

    @Test
    public void testGetDefaultValue_String() {
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        String result = (String) propertyBuilder.getDefaultValue(type);
        assertEquals("", result);
    }

    @Test
    public void testGetDefaultValue_Number() {
        JavaType type = TypeFactory.defaultInstance().constructType(Integer.class);

        int result = (int) propertyBuilder.getDefaultValue(type);
        assertEquals(0, result);
    }

    @Test
    public void testGetDefaultValue_Object() {
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);

        Object result = propertyBuilder.getDefaultValue(type);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue_Array() {
        JavaType type = TypeFactory.defaultInstance().constructType(Object[].class);

        Object result = propertyBuilder.getDefaultValue(type);
        assertNotNull(result);
        assertTrue(result instanceof ObjectNode);
        ObjectNode node = (ObjectNode) result;
        assertEquals(JsonInclude.Include.NON_DEFAULT, node.get("@class").asInt());
    }

    @Test
    public void testGetDefaultValue_Map() {
        JavaType type = TypeFactory.defaultInstance().constructType(HashMap.class);

        Object result = propertyBuilder.getDefaultValue(type);
        assertNotNull(result);
        assertTrue(result instanceof ObjectNode);
        ObjectNode node = (ObjectNode) result;
        assertEquals(JsonInclude.Include.NON_DEFAULT, node.get("@class").asInt());
        assertEquals(JsonInclude.Include.NON_DEFAULT, node.get("value").get("@class").asInt());
    }

    @Test
    public void testGetPropertyDefaultValue_ClassWithNoDefaultConstructor() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(SomeClassWithoutDefaultConstructor.class);

        try {
            propertyBuilder.getPropertyDefaultValue("someField", member, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetPropertyDefaultValue_NullBean() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        try {
            propertyBuilder.getPropertyDefaultValue("someField", member, type, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testgetDefaultBean() {
        Object result = propertyBuilder.getDefaultBean();
        assertNull(result);

        propertyBuilder.setDefaultBean(new Object());
        Object expectedDefaultBean = propertyBuilder.getDefaultBean();
        assertEquals(expectedDefaultBean, result);
    }

    @Test
    public void testgetDefaultInclusion() {
        JsonInclude.Value result = propertyBuilder.getDefaultInclusion();
        assertNotNull(result);
        assertEquals(JsonInclude.Include.NON_DEFAULT, result.getValueInclusion());
    }

    @Test
    public void testGetUseRealPropertyDefaults() {
        boolean result = propertyBuilder.getUseRealPropertyDefaults();
        assertFalse(result);

        propertyBuilder.setUseRealPropertyDefaults(true);
        boolean expectedUseRealPropertyDefaults = propertyBuilder.getUseRealPropertyDefaults();
        assertTrue(expectedUseRealPropertyDefaults);
    }

    @Test
    public void testSetDefaultBean() {
        Object defaultBean = new Object();
        propertyBuilder.setDefaultBean(defaultBean);

        Object result = propertyBuilder.getDefaultBean();
        assertEquals(defaultBean, result);
    }

    private static class SomeClass {
        @JsonSerialize(using = CustomSerializer.class)
        public String someField;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public int anotherField;
    }

    private static class SomeClassWithoutDefaultConstructor {
        public String someField;
    }

    private static class OtherClass {
        public String someField;
    }

    private static class CustomSerializer extends JsonSerializer<SomeClass> {
        @Override
        public void serialize(SomeClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("someField");
            gen.writeNull();
            gen.writeEndObject();
        }
    }
}