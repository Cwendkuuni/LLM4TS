import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testConstructorAndGetters() {
        // Given
        PropertyName name = new PropertyName("testName");
        JavaType type = SimpleType.construct(Integer.class);
        PropertyName wrapperName = new PropertyName("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 1;
        Object injectableValueId = "injectableValueId";
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;

        // When
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);

        // Then
        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getConstructorParameter());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
    }

    @Test
    public void testWithNullProvider() {
        // Given
        CreatorProperty source = mock(CreatorProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        // When
        CreatorProperty newProperty = new CreatorProperty(source, deser, nva);

        // Then
        assertEquals(deser, newProperty.getValueDeserializer());
        assertEquals(nva, newProperty.getNullValueProvider());
    }

    // Other test cases for remaining methods

}