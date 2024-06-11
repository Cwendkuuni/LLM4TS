import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        config = new SerializationConfig(null);
        beanDesc = new BeanDescription(null, null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null);
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "test";
        AnnotatedMember member = null;
        JavaType type = null;

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        JavaType type = null;

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        Exception e = new Exception();
        String propName = "test";
        Object defaultBean = new Object();

        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }
}