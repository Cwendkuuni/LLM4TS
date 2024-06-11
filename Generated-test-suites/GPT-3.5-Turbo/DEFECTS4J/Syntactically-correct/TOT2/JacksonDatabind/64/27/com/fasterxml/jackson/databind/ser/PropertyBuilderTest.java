import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
    
    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", annotatedMember, JavaType);
        assertNotNull(defaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultValue = propertyBuilder.getDefaultValue(JavaType);
        assertNotNull(defaultValue);
    }
    
    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType serializationType = propertyBuilder.findSerializationType(annotated, true, declaredType);
        assertNotNull(serializationType);
    }
    
    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;
        
        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }
    
    @Test
    public void testConstructor() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }
    
    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }
    
}