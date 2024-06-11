import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBuildWriter_DefaultValues() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new String("defaultValue");
        AnnotatedMember am = new AnnotatedMember(Default.class, "defaultProperty");
        JavaType declaredType = JavaType.construct(String[].class);
        JsonSerializer<String> ser = new JsonStringSerializer();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertEquals("defaultValue", beanPropertyWriter.getValueToSuppress());
    }

    @Test
    public void testBuildWriter_NonDefaultValues() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new String[0];
        AnnotatedMember am = new AnnotatedMember(Default[].class, "defaultProperty");
        JavaType declaredType = JavaType.construct(String[].class);
        JsonSerializer<String> ser = new JsonStringSerializer();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNull(beanPropertyWriter.getValueToSuppress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriter_IllegalAnnotations() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = new Annotated(String.class, "invalidMethod");
        JsonSerializer<String> ser = new JsonStringSerializer();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, null, ser, null, null, a, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSerializationType_InvalidContentType() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        Annotated a = new Annotated(String.class, "invalidMethod");
        JavaType declaredType = JavaType.construct(String[].class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_InvalidClass() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        Annotated a = new Annotated(int.class, "invalidMethod");
        JavaType declaredType = JavaType.construct(String[].class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testFindSerializationType_ValidAnnotations() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = new Annotated(String[].class, "validMethod");
        JavaType declaredType = JavaType.construct(String[].class);
        JavaType serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
        Assert.assertEquals(declaredType, serializationType);
    }

    @Test
    public void testFindSerializationType_Empty() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = new Annotated(String[].class, "emptyMethod");
        JavaType declaredType = JavaType.construct(String[].class);
        JavaType serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
        Assert.assertNull(serializationType);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Default.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
    }

    @Test
    public void testGetDefaultValue_Primitives() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getDefaultValue(JavaType.construct(int.class));
        Assert.assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValue_WrapperTypes() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getDefaultValue(JavaType.construct(String.class));
        Assert.assertEquals("", defaultValue);
    }

    @Test
    public void testGetDefaultValue_Arrays() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getDefaultValue(JavaType.construct(String[].class));
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultValue_Maps() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getDefaultValue(JavaType.construct(HashMap.class));
        Assert.assertEquals(JsonInclude.Include.NON_DEFAULT, defaultValue);
    }

    @Test
    public void testGetDefaultValue_Classes() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultValue = propertyBuilder.getDefaultValue(JavaType.construct(Default.class));
        Assert.assertEquals(null, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_ValidProperties() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Default.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Default();
        String propertyName = "name";
        AnnotatedMember am = new AnnotatedMember(Default.class, "name");
        JavaType type = JavaType.construct(String.class);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, am, type);
        Assert.assertEquals("defaultName", defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_InvalidProperties() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Default.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Default();
        String propertyName = "invalidProperty";
        AnnotatedMember am = new AnnotatedMember(Default.class, "name");
        JavaType type = JavaType.construct(String.class);
        propertyBuilder.getPropertyDefaultValue(propertyName, am, type);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultBean() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Default.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = null;
        String propertyName = "name";
        AnnotatedMember am = new AnnotatedMember(Default.class, "name");
        JavaType type = JavaType.construct(String.class);
        propertyBuilder.setDefaultBean(defaultBean);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, am, type);
        Assert.assertNull(defaultValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyDefaultValue_InvalidPropertyAccess() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Default.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Default();
        String propertyName = "invalidProperty";
        AnnotatedMember am = new AnnotatedMember(Default.class, "name");
        JavaType type = JavaType.construct(String.class);
        propertyBuilder.getPropertyDefaultValue(propertyName, am, type);
    }

    @Test
    public void testGetPropertyDefaultValue_ValidPropertyAccess() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Default.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Default();
        String propertyName = "name";
        AnnotatedMember am = new AnnotatedMember(Default.class, "name");
        JavaType type = JavaType.construct(String.class);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, am, type);
        Assert.assertEquals("defaultName", defaultValue);
    }

    @Test
    public void test_ThrowWrapped() throws Exception {
        Exception e = new Exception("testException");
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object defaultBean = new Default();
        String propName = "name";
        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }
}