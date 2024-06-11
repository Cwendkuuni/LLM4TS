import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
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
    public void setup() {
        config = new SerializationConfig(new AnnotationIntrospector(), new ObjectMapper.DefaultSerializerProvider());
        beanDesc = new BeanDescription(Mockito.mock(Class.class));
        annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = annotationIntrospector;
    }

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        Mockito.when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertEquals(beanDesc.getClassAnnotations(), annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        Mockito.when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        Mockito.when(beanDesc.getPropertyDefinition("testProperty")).thenReturn(Mockito.mock(BeanPropertyDefinition.class));
        Mockito.when(beanDesc.getType()).thenReturn(Mockito.mock(JavaType.class));
        Mockito.when(beanDesc.findDefaultViews()).thenReturn(new Class[0]);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        Mockito.when(annotationIntrospector.findNullSerializer(Mockito.any(Annotated.class))).thenReturn(Mockito.mock(JsonSerializer.class));
        Mockito.when(annotationIntrospector.findUnwrappingNameTransformer(Mockito.any(Annotated.class))).thenReturn(Mockito.mock(NameTransformer.class));

        BeanPropertyWriter writer = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), ser, typeSer, contentTypeSer, null, false);

        assertNotNull(writer);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterException() throws JsonMappingException {
        Mockito.when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        Mockito.when(beanDesc.getPropertyDefinition("testProperty")).thenReturn(Mockito.mock(BeanPropertyDefinition.class));
        Mockito.when(beanDesc.getType()).thenReturn(Mockito.mock(JavaType.class));
        Mockito.when(beanDesc.findDefaultViews()).thenReturn(new Class[0]);
        Mockito.when(annotationIntrospector.findNullSerializer(Mockito.any(Annotated.class))).thenThrow(new JsonMappingException("Test exception"));

        propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(JsonSerializer.class), Mockito.mock(TypeSerializer.class), Mockito.mock(TypeSerializer.class), null, false);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Test with no annotations
        Annotated a = Mockito.mock(Annotated.class);
        Mockito.when(a.getClass()).thenReturn(JsonSerialize.class);
        Mockito.when(a.getRawType()).thenReturn(String.class);
        Mockito.when(a.getMember()).thenReturn(null);
        JavaType declaredType = Mockito.mock(JavaType.class);

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(result);

        // Test with a JsonSerialize annotation
        Mockito.when(a.getRawType()).thenReturn(Integer.class);
        JsonSerialize jsonSerialize = Mockito.mock(JsonSerialize.class);
        JsonSerialize.Typing typing = Mockito.mock(JsonSerialize.Typing.class);
        Mockito.when(jsonSerialize.getType()).thenReturn(int[].class);
        Mockito.when(jsonSerialize.getTyping()).thenReturn(typing);
        Mockito.when(a.getValue()).thenReturn(jsonSerialize);

        Mockito.when(typing.getValue()).thenReturn(JsonSerialize.Typing.STATIC);
        JavaType expected = Mockito.mock(JavaType.class);
        Mockito.when(expected.withStaticTyping()).thenReturn(expected);

        JavaType result2 = propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals(expected, result2);

        // Test with a JsonInclude annotation
        JsonInclude jsonInclude = Mockito.mock(JsonInclude.class);
        Mockito.when(jsonInclude.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        Mockito.when(a.getValue()).thenReturn(jsonInclude);

        assertNull(propertyBuilder.findSerializationType(a, false, declaredType));
    }
}