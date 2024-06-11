package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

@SuppressWarnings("serial")
public class EnumSerializerTest {
    private EnumSerializer enumSerializer;
    private EnumValues enumValues;

    @Before
    public void setup() {
        enumValues = mock(EnumValues.class);
        enumSerializer = new EnumSerializer(enumValues, true);
    }

    @Test
    public void testConstructorWithEnumValuesAndSerializeAsIndex() {
        EnumSerializer serializer = new EnumSerializer(enumValues, true);
        assertEquals(enumValues, serializer.getEnumValues());
        assertTrue(serializer._serializeAsIndex);

        serializer = new EnumSerializer(enumValues, false);
        assertEquals(enumValues, serializer.getEnumValues());
        assertFalse(serializer._serializeAsIndex);
    }

    @Test
    public void testConstruct() {
        JavaType javaType = mock(JavaType.class);
        BeanDescription beanDescription = mock(BeanDescription.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        EnumSerializer.construct(javaType.getRawClass(), objectMapper.getSerConfig(),
                beanDescription, format);

        // verify construction of serializer
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        assertNull(enumSerializer.createContextual(serializers, null));

        when(property.getType()).thenReturn(mock(JavaType.class));
        assertNull(enumSerializer.createContextual(serializers, property));

        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(property.getValueFormat()).thenReturn(format);
        assertEquals(enumSerializer, enumSerializer.createContextual(serializers, property));
    }

    @Test
    public void testSerialize() throws IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        enumSerializer.serialize(en, gen, serializers);

        // verify serialization
    }

    @Test
    public void testGetSchemaWithSerializeAsIndex() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        enumSerializer.getSchema(provider, typeHint);

        // verify schema
    }

    @Test
    public void testGetSchemaWithoutSerializeAsIndex() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        enumSerializer._serializeAsIndex = false;

        enumSerializer.getSchema(provider, typeHint);

        // verify schema
    }

    @Test
    public void testAcceptJsonFormatVisitorWithSerializeAsIndex() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // verify visitor
    }

    @Test
    public void testAcceptJsonFormatVisitorWithoutSerializeAsIndex() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        enumSerializer._serializeAsIndex = false;
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // verify visitor
    }

    @Test
    public void test_serializeAsIndex() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        assertTrue(enumSerializer._serializeAsIndex(serializers));

        enumSerializer._serializeAsIndex = null;
        enumSerializer._serializeAsIndex(mock(SerializerProvider.class));

        // verify serialization choice
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_isShapeWrittenUsingIndex() {
        Class<?> enumClass = mock(Class.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);

        // verify shape writing using index
    }
}