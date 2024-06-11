import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() {
        config = new SerializationConfig();
        beanDesc = new BeanDescription();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @After
    public void tearDown() {
        propertyBuilder = null;
        config = null;
        beanDesc = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
        assertNotNull(propertyBuilder._config);
        assertNotNull(propertyBuilder._beanDesc);
        assertNotNull(propertyBuilder._defaultInclusion);
        assertNotNull(propertyBuilder._annotationIntrospector);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
        assertEquals(propDef.getName(), writer.getName());
        assertEquals(am, writer.getMember());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object defaultPropertyValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(defaultPropertyValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

}