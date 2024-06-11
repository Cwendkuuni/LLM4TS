import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Test case for buildWriter method

        // Creating mock objects
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        // Creating PropertyBuilder object
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = annotationIntrospector;

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser,
                typeSer, contentTypeSer, am, true);

        // Verifying that the necessary methods are invoked
        verify(beanDesc).findPropertyInclusion(any());
        verify(annotationIntrospector).withOverrides(any());
        verify(annotationIntrospector).findNullSerializer(any());
        verify(annotationIntrospector).findUnwrappingNameTransformer(any());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Test case for findSerializationType method

        // Creating mock objects
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);

        // Creating PropertyBuilder object
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = annotationIntrospector;

        propertyBuilder.findSerializationType(a, true, declaredType);

        // Verifying that the necessary methods are invoked
        verify(annotationIntrospector).refineSerializationType(any(), any(), any());
        verify(annotationIntrospector).findSerializationTyping(any());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Test case for getPropertyDefaultValue method

        // Creating mock objects
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        // Creating PropertyBuilder object
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = annotationIntrospector;
        propertyBuilder._defaultBean = new Object();

        propertyBuilder.getPropertyDefaultValue("name", member, type);

        // Verifying that the necessary methods are invoked
        verify(member).getValue(any());
    }

    @Test
    public void testGetDefaultValue() {
        // Test case for getDefaultValue method

        // Creating mock objects
        JavaType type = mock(JavaType.class);

        // Creating PropertyBuilder object
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = annotationIntrospector;

        propertyBuilder.getDefaultValue(type);

        // Verifying that the necessary methods are invoked
        verify(type).getRawClass();
        verify(type).isContainerType();
        verify(type).isReferenceType();
    }
}