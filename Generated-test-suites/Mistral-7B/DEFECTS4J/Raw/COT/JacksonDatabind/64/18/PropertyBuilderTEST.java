import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        beanDesc = new BeanDescription(Object.class);
        annotationIntrospector = config.getDefaultAnnotationIntrospector();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_Null() {
        assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriter_Empty() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", null, new TypeReference<Object>() {});
        JavaType declaredType = new TypeReference<Object>() {}.getType();
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        try {
            propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal null argument(s)", e.getMessage());
        }
    }

    @Test
    public void testBuildWriter_Default() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", null, new TypeReference<Object>() {});
        JavaType declaredType = new TypeReference<Object>() {}.getType();
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_IllegalAnnotations() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = new TypeReference<Object>() {}.getType();

        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testFindSerializationType_ValidAnnotations() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = new TypeReference<Object>() {}.getType();
        JavaType secondary = new TypeReference<Object>() {}.getType();

        when(annotationIntrospector.refineSerializationType(config, a, declaredType))
                .thenReturn(secondary);

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(secondary, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_IllegalTypes() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = new TypeReference<Object>() {}.getType();

        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue_String() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeReference<String>() {}.getType();

        Object defaultBean = new String("default");
        when(member.getValue(defaultBean)).thenReturn("value");

        propertyBuilder._defaultBean = defaultBean;
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("value", result);
    }

    @Test
    public void testGetPropertyDefaultValue_Object() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeReference<Object>() {}.getType();

        propertyBuilder._defaultBean = new Object();
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_Array() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new TypeReference<int[]>() {}.getType();

        int[] defaultBean = {1, 2, 3};
        when(member.getValue(defaultBean)).thenReturn(new int[]{1, 2, 3});

        propertyBuilder._defaultBean = defaultBean;
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertArrayEquals(new int[]{1, 2, 3}, result);
    }

    @Test
    public void testGetPropertyDefaultValue_Map() {
        String name = "name";
        AnnotatedMember member = mock(AnnotedMember.class);
        JavaType type = new TypeReference<HashMap<String, String>>() {}.getType();

        HashMap<String, String> defaultBean = new HashMap<>();
        when(member.getValue(defaultBean)).thenReturn(defaultBean);

        propertyBuilder._defaultBean = defaultBean;
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(defaultBean, result);
    }

    @Test
    public void testGetPropertyDefaultValue_Null() {
        String name = "name";
        AnnotatedMember member = mock(AnnotedMember.class);
        JavaType type = new TypeReference<String>() {}.getType();

        propertyBuilder._defaultBean = null;
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_NoDefault() {
        String name = "name";
        AnnotatedMember member = mock(AnnotedMember.class);
        JavaType type = new TypeReference<Object>() {}.getType();

        propertyBuilder._defaultBean = null;
        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(result);
    }
}