import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(mock(SerializationConfig.class), mock(BeanDescription.class));
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        try {
            PropertyBuilder propertyBuilder = new PropertyBuilder(mock(SerializationConfig.class), mock(BeanDescription.class));
            BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
            JavaType declaredType = mock(JavaType.class);
            JsonSerializer<?> ser = mock(JsonSerializer.class);
            TypeSerializer typeSer = mock(TypeSerializer.class);
            TypeSerializer contentTypeSer = mock(TypeSerializer.class);
            AnnotatedMember am = mock(AnnotatedMember.class);
            boolean defaultUseStaticTyping = true;
            BeanPropertyWriter writer = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail("Exception thrown during buildWriter test");
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(mock(SerializationConfig.class), mock(BeanDescription.class));
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", mock(AnnotatedMember.class), mock(JavaType.class));
            assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
        }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(mock(SerializationConfig.class), mock(BeanDescription.class));
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object defaultValue = propertyBuilder.getDefaultValue(stringType);
        assertEquals("", defaultValue);
    }
}