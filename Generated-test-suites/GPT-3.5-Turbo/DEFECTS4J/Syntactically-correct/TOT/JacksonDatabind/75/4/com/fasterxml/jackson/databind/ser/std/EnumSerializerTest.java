import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private EnumValues enumValues;
    private Boolean serializeAsIndex;

    @Before
    public void setup() {
        enumValues = mock(EnumValues.class);
        serializeAsIndex = false;
        enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);
    }

    @Test
    public void testSerialize_Index() throws IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(true);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen, times(1)).writeNumber(en.ordinal());
    }

    @Test
    public void testSerialize_EnumsUsingToString() throws IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen, times(1)).writeString(en.toString());
    }

    @Test
    public void testSerialize_EnumsUsingSerializedValue() throws IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen, times(1)).writeString(enumValues.serializedValueFor(en));
    }

    @Test
    public void testGetSchema_Index() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        when(enumSerializer._serializeAsIndex(provider)).thenReturn(true);

        enumSerializer.getSchema(provider, typeHint);

        verify(provider, times(1)).constructType(typeHint);
    }

    @Test
    public void testGetSchema_EnumsAsArray() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        when(enumSerializer._serializeAsIndex(provider)).thenReturn(false);
        when(provider.constructType(typeHint)).thenReturn(JavaType.enumType());

        enumSerializer.getSchema(provider, typeHint);

        verify(provider, times(1)).constructType(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_Index() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(true);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectIntFormat(typeHint, JsonParser.NumberType.INT);
    }

    @Test
    public void testAcceptJsonFormatVisitor_EnumsUsingToString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectStringFormat(typeHint);
        verify(visitor.getProvider(), times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    @Test
    public void testAcceptJsonFormatVisitor_EnumsUsingSerializedValue() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectStringFormat(typeHint);
        verify(visitor.getProvider(), times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    @Test
    public void testContextualSerializer_SerializeAsIndexChanged() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        enumSerializer._serializeAsIndex = false;
        when(property.getType().getRawClass()).thenReturn(ExampleEnum.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        EnumSerializer newSerializer = (EnumSerializer) enumSerializer.createContextual(serializers, property);

        assert newSerializer != null;
        assert newSerializer._values == enumValues;
        assert newSerializer._serializeAsIndex;
    }

    @Test
    public void testContextualSerializer_SerializeAsIndexSame() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        enumSerializer._serializeAsIndex = false;
        when(property.getType().getRawClass()).thenReturn(ExampleEnum.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);

        EnumSerializer newSerializer = (EnumSerializer) enumSerializer.createContextual(serializers, property);

        assert newSerializer != null;
        assert newSerializer._values == enumValues;
        assert !newSerializer._serializeAsIndex;
    }

    @Test
    public void testContextualSerializer_PropertyNull() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        enumSerializer._serializeAsIndex = false;

        EnumSerializer newSerializer = (EnumSerializer) enumSerializer.createContextual(serializers, null);

        assert newSerializer != null;
        assert newSerializer == enumSerializer;
        assert newSerializer._values == enumValues;
        assert !newSerializer._serializeAsIndex;
    }

    @Test
    public void testEnumSerializerConstruct() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        SerializationConfig config = mock(SerializationConfig.class);
        BeanDescription beanDesc = mock(BeanDescription.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(Shape.ARRAY);
        when(enumValues.getEnumClass()).thenReturn(ExampleEnum.class);
        when(enumValues.enums()).thenReturn(new HashSet<>());
        when(enumValues.serializedValueFor(Enum.ONE)).thenReturn("One");
        when(enumValues.serializedValueFor(Enum.TWO)).thenReturn("Two");
        when(enumValues.serializedValueFor(Enum.THREE)).thenReturn("Three");
        when(config.without(Feature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(config);
        when(beanDesc.getClassInfo()).thenReturn(ClassInfo.fromClass(ExampleEnum.class));
        when(config.introspect(beanDesc)).thenReturn(beanDesc);
        when(config.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        when(config.withSharedAttribute(any())).thenReturn(config);
        when(objectMapper.getSerializerProvider()).thenReturn(Mockito.mock(SerializerProvider.class));
        when(objectMapper.getSerializerFactory()).thenReturn(Mockito.mock(SerializerFactory.class));
        when(objectMapper.getJsonFactory()).thenReturn(Mockito.mock(JsonFactory.class));
        when(objectMapper.getSerializerProviderInstance()).thenReturn(Mockito.mock(DefaultSerializerProvider.class));

        EnumSerializer newSerializer = EnumSerializer.construct(ExampleEnum.class, config, beanDesc, format);

        assert newSerializer != null;
        assert newSerializer._values == enumValues;
        assert newSerializer._serializeAsIndex;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsShapeWrittenUsingIndex_InvalidShape() {
        EnumSerializer._isShapeWrittenUsingIndex(ExampleEnum.class, JsonFormat.Value.empty(), true);
    }
}