import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
    }

    @Test
    public void getClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void buildWriter() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanPropertyDefinition propertyDefinition = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer serializer = new JsonSerializer();
        TypeSerializer typeSerializer = new TypeSerializer();
        TypeSerializer contentTypeSerializer = new TypeSerializer();
        AnnotatedMember annotatedMember = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(provider, propertyDefinition, declaredType, serializer, typeSerializer, contentTypeSerializer, annotatedMember, defaultUseStaticTyping);

        assertNotNull(writer);
    }

    @Test
    public void findSerializationType() throws JsonMappingException {
        Annotated annotated = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        JavaType serializationType = propertyBuilder.findSerializationType(annotated, useStaticTyping, declaredType);

        assertNull(serializationType);
    }

    @Test
    public void getDefaultBean() {
        Object defaultValue = propertyBuilder.getDefaultBean();

        assertNull(defaultValue);
    }

    @Test
    public void getPropertyDefaultValue() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(propertyDefaultValue);
    }

    @Test
    public void getDefaultValue() {
        JavaType primitiveType = new JavaType(int.class);
        Object primitiveDefaultValue = propertyBuilder.getDefaultValue(primitiveType);
        assertNotNull(primitiveDefaultValue);
        assertEquals(0, primitiveDefaultValue);

        JavaType wrapperType = new JavaType(Integer.class);
        Object wrapperDefaultValue = propertyBuilder.getDefaultValue(wrapperType);
        assertNotNull(wrapperDefaultValue);
        assertEquals(0, wrapperDefaultValue);

        JavaType stringType = new JavaType(String.class);
        Object stringDefaultValue = propertyBuilder.getDefaultValue(stringType);
        assertNotNull(stringDefaultValue);
        assertEquals("", stringDefaultValue);

        JavaType containerType = new JavaType(List.class);
        Object containerDefaultValue = propertyBuilder.getDefaultValue(containerType);
        assertNotNull(containerDefaultValue);
        assertEquals(JsonInclude.Include.NON_EMPTY, containerDefaultValue);

        JavaType referenceType = new JavaType(Object.class);
        Object referenceDefaultValue = propertyBuilder.getDefaultValue(referenceType);
        assertNull(referenceDefaultValue);
    }

    @Test
    public void testExceptions() {
        try {
            propertyBuilder._throwWrapped(new IllegalArgumentException("Test"), "test", new Object());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }
}