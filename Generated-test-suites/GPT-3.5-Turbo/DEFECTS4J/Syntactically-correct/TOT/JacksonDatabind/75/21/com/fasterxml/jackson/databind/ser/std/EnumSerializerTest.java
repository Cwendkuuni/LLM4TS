import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;

    @Before
    public void setup() {
        EnumValues enumValues = mock(EnumValues.class);
        when(enumValues.getEnumClass()).thenReturn(MyEnum.class);
        when(enumValues.serializedValueFor(any(Enum.class))).thenReturn("VALUE1");
        when(enumValues.values()).thenReturn(new LinkedHashSet<>());

        enumSerializer = new EnumSerializer(enumValues, false);
    }

    @Test
    public void testSerialize_WhenSerializeAsIndexFalse_ShouldWriteString() {
        // Arrange
        Enum myEnum = MyEnum.VALUE1;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        // Act
        enumSerializer.serialize(myEnum, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeString("VALUE1");
    }

    @Test
    public void testSerialize_WhenSerializeAsIndexTrue_ShouldWriteNumber() {
        // Arrange
        Enum myEnum = MyEnum.VALUE1;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        // Act
        enumSerializer.serialize(myEnum, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(anyInt());
    }

    @Test
    public void testSerialize_WhenWriteEnumsUsingToString_ShouldWriteString() {
        // Arrange
        Enum myEnum = MyEnum.VALUE1;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        // Act
        enumSerializer.serialize(myEnum, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeString(myEnum.toString());
    }

    @Test
    public void testCreateContextual_WhenSerializeAsIndexChanged_ShouldReturnNewEnumSerializer() {
        // Arrange
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        Type type = MyEnum.class;
        JavaType javaType = mock(JavaType.class);
        when(beanProperty.getType()).thenReturn(javaType);
        when(javaType.getRawClass()).thenReturn(MyEnum.class);

        // Act
        JsonSerializer<?> result = enumSerializer.createContextual(serializerProvider, beanProperty);

        // Assert
        assertTrue(result instanceof EnumSerializer);
        assertNotSame(enumSerializer, result);
        assertEquals(enumSerializer.getEnumValues(), ((EnumSerializer) result).getEnumValues());
        assertFalse(((EnumSerializer) result)._serializeAsIndex);
    }

    @Test
    public void testCreateContextual_WhenSerializeAsIndexNotChanged_ShouldReturnCurrentEnumSerializer() {
        // Arrange
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        Type type = MyEnum.class;
        JavaType javaType = mock(JavaType.class);
        when(beanProperty.getType()).thenReturn(javaType);
        when(javaType.getRawClass()).thenReturn(MyEnum.class);

        // Act
        JsonSerializer<?> result = enumSerializer.createContextual(serializerProvider, beanProperty);

        // Assert
        assertSame(enumSerializer, result);
    }

    @Test
    public void testGetSchema_WhenSerializeAsIndexTrue_ShouldReturnSchemaNodeWithTypeInteger() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = MyEnum.class;

        // Act
        JsonNode result = enumSerializer.getSchema(provider, typeHint);

        // Assert
        assertNotNull(result);
        assertEquals("integer", result.get("type").textValue());
    }

    @Test
    public void testGetSchema_WhenSerializeAsIndexFalse_ShouldReturnSchemaNodeWithTypeStringAndEnumArray() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = MyEnum.class;
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        BeanDescription beanDescription = mock(BeanDescription.class);

        // Act
        JsonNode result = enumSerializer.getSchema(provider, typeHint);

        // Assert
        assertNotNull(result);
        assertEquals("string", result.get("type").textValue());
        assertNotNull(result.get("enum"));
        assertEquals(0, result.get("enum").size());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndexTrue_ShouldVisitIntFormat() {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));

        // Act
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor).expectIntFormat(typeHint, JsonParser.NumberType.INT);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndexFalse_ShouldVisitStringFormatAndSetEnumTypes() {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));
        when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);
        Enum myEnum1 = MyEnum.VALUE1;
        Enum myEnum2 = MyEnum.VALUE2;
        LinkedHashSet<SerializableString> values = new LinkedHashSet<>();
        values.add(mock(SerializableString.class));
        EnumValues enumValues = mock(EnumValues.class);
        when(enumValues.enums()).thenReturn(new Enum[] { myEnum1, myEnum2 });
        when(enumValues.values()).thenReturn(values);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        // Act
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(stringVisitor).enumTypes(anySet());
    }
}

enum MyEnum {
    VALUE1,
    VALUE2
}