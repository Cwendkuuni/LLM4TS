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

    private PropertyBuilder propertyBuilder;
    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setUp() {
        serializationConfig = new SerializationConfig();
        beanDescription = new BeanDescription(null);
        annotationIntrospector = new StandardAnnotationIntrospector();
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", null, null, null, null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.defaultInstance();
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeWithIllegalType() throws JsonMappingException {
        Annotated a = new AnnotatedMember(null, null, null) {
            @Override
            public Class<?> getRawType() {
                return Integer.class;
            }
        };

        propertyBuilder.findSerializationType(a, false, null);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        String name = "name";
        AnnotatedMember member = new AnnotatedMember(null, null, null) {
            @Override
            public Object getValue(Object o) throws Exception {
                return "value";
            }
        };
        JavaType type = TypeFactory.defaultInstance();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("", defaultValue);

        propertyBuilder._defaultBean = new Object();
        defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNotNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = TypeFactory.defaultInstance();
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);

        type = TypeFactory.constructType(String[].class);
        defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullValue() throws Exception {
        String name = "name";
        AnnotatedMember member = new AnnotatedMember(null, null, null) {
            @Override
            public Object getValue(Object o) throws Exception {
                return null;
            }
        };
        JavaType type = TypeFactory.defaultInstance();

        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }
}