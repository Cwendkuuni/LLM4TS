import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private SerializationConfig configuration;
    private BeanDescription beanDescription;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        configuration = new SerializationConfig();
        configuration.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        ObjectMapper objectMapper = new ObjectMapper();
        beanDescription = objectMapper.getTypeFactory().constructType(PropertyBuilder.class);
        propertyBuilder = new PropertyBuilder(configuration, beanDescription);
    }

    @Test
    public void testGetClassAnnotations() {
        assertEquals(propertyBuilder.getClassAnnotations(), beanDescription.getClassAnnotations());
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter actual = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterInvalidType() throws JsonMappingException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Object.class);
        JsonSerializer<String> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = null;
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindSerializationTypeNoAnnotation() throws JsonMappingException {
        Annotated a = null;
        JavaType declaredType = TypeFactory.defaultInstance().constructType(int.class);

        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        assertNull(actualType);
    }

    @Test
    public void testFindSerializationTypeSameType() throws JsonMappingException {
        Annotated a = null;
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);

        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        assertEquals(declaredType, actualType);
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() throws JsonMappingException {
        Annotated a = null;
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);

        assertEquals(declaredType, actualType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException, IllegalAccessException, InstantiationException {
        String propName = "name";
        AnnotatedMember member = new AnnotatedMember() {};
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);

        assertNull(defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueString() throws JsonMappingException, IllegalAccessException, InstantiationException {
        String propName = "name";
        AnnotatedMember member = new AnnotatedMember() {};
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object defaultBean = propertyBuilder.getDefaultBean();
        String defaultValue = (String) propertyBuilder.getPropertyDefaultValue(propName, member, type);

        assertEquals("", defaultValue);
    }

    @Test
    public void testGetDefaultBean() throws JsonMappingException {
        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNull(defaultBean);
    }
}