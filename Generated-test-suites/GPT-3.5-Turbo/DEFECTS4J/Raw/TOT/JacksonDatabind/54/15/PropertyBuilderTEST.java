import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<Object>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Annotated a = new Annotated();
        boolean useStaticTyping = false;
        JavaType declaredType = new JavaType();

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType type = new JavaType(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertEquals("", defaultValue);
    }

}