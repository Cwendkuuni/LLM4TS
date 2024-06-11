import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.json.JsonMappingException;

import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        SerializerProvider provider = null;
        BeanPropertyDefinition propertyDef = null;
        JavaType declaredType = null;
        JsonSerializer<Object> serializer = null;
        TypeSerializer typeSerializer = null;
        TypeSerializer contentTypeSerializer = null;
        AnnotatedMember annotatedMember = null;
        boolean defaultUseStaticTyping = false;

        try {
            BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(provider, propertyDef, declaredType, serializer, typeSerializer, contentTypeSerializer, annotatedMember, defaultUseStaticTyping);
            assertNotNull(beanPropertyWriter);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        AnnotatedMember annotatedMember = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, useStaticTyping, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType stringType = JavaType.construct(String.class);
        JavaType objectType = JavaType.construct(Object.class);
        JavaType mapType = JavaType.construct(Map.class);

        Object defaultString = propertyBuilder.getDefaultValue(stringType);
        Object defaultObject = propertyBuilder.getDefaultValue(objectType);
        Object defaultMap = propertyBuilder.getDefaultValue(mapType);

        assertEquals("", defaultString);
        assertNull(defaultObject);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultMap);
    }
}