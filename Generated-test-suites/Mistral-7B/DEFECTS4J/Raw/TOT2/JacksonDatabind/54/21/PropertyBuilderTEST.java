import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;

public class PropertyBuilderTest {

    @Test
    public void testBasicFunctionality() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
        Object ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), null, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertEquals(beanPropertyWriter.getClass(), BeanPropertyWriter.class);
    }

    @Test
    public void testSerializationType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType secondary = Mockito.mock(JavaType.class);
        Mockito.when(propertyBuilder.findSerializationType(a, false, declaredType)).thenReturn(secondary);

        JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);

        assertEquals(result.getRawClass(), secondary.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializationTypeException() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType secondary = TypeFactory.defaultInstance().constructType(Integer.class);
        Mockito.when(propertyBuilder.findSerializationType(a, false, declaredType)).thenReturn(secondary);
        Mockito.when(secondary.isAssignableFrom(declaredType.getRawClass())).thenReturn(false);

        propertyBuilder.findSerializationType(a, true, declaredType);
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object result = propertyBuilder.getDefaultBean();

        assertNull(result);
    }

    @Test
    public void testGetDefaultBeanInstance() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object defaultBean = new Object();
        propertyBuilder._defaultBean = defaultBean;

        Object result = propertyBuilder.getDefaultBean();

        assertEquals(result, defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueNull() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String name = "test";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        try {
            propertyBuilder.getPropertyDefaultValue(name, member, type);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to get property 'test' of default null instance", e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValueEmptyString() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String name = "test";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        assertEquals("", propertyBuilder.getPropertyDefaultValue(name, member, type));
    }

    @Test
    public void testGetPropertyDefaultValueDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String name = "test";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Integer.class);

        assertEquals(0, propertyBuilder.getPropertyDefaultValue(name, member, type));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueNullValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String name = "test";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);

        try {
            propertyBuilder.getPropertyDefaultValue(name, member, type);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to get property 'test' of default null instance", e.getMessage());
        }
    }
}