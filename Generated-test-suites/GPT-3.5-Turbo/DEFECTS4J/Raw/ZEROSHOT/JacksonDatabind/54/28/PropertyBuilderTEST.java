import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        SerializationConfig config = new SerializationConfig(null, null, null, null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider(null, null, null);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null);
        JavaType declaredType = new JavaType(null);
        JsonSerializer<?> ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType(null);

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType(null);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertEquals("", defaultValue);
    }
}