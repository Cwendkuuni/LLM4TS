
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumSerializerTest {

    @Test
    public void testSerializeWithSerializeAsIndexTrue() throws Exception {
        EnumValues enumValues = getEnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        assertEquals(true, getField(enumSerializer, "_serializeAsIndex"));
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        enumSerializer.serialize(MockEnum.VALUE1, generator, provider);
        
        Mockito.verify(generator).writeNumber(MockEnum.VALUE1.ordinal());
    }

    @Test
    public void testSerializeWithSerializeAsIndexFalse() throws Exception {
        EnumValues enumValues = getEnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        assertEquals(false, getField(enumSerializer, "_serializeAsIndex"));
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        enumSerializer.serialize(MockEnum.VALUE1, generator, provider);
        
        Mockito.verify(generator).writeString(enumValues.serializedValueFor(MockEnum.VALUE1));
    }

    @Test
    public void testSerializeWithSerializeAsIndexNull() throws Exception {
        EnumValues enumValues = getEnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);
        assertNull(getField(enumSerializer, "_serializeAsIndex"));
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        enumSerializer.serialize(MockEnum.VALUE1, generator, provider);
        
        Mockito.verify(generator).writeString(enumValues.serializedValueFor(MockEnum.VALUE1));
    }

    @Test
    public void testCreateContextualWithSameSerializeAsIndex() throws Exception {
        EnumValues enumValues = getEnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);
        Mockito.when(format.getShape()).thenReturn(JsonFormat.Shape.ARRAY);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Mockito.when(property.getType()).thenReturn(Mockito.mock(JavaType.class));
        Mockito.when(property.getType().getRawClass()).thenReturn(MockEnum.class);

        ContextualSerializer contextualSerializer = enumSerializer.createContextual(provider, property);

        assertEquals(enumSerializer, contextualSerializer);
    }

    @Test
    public void testCreateContextualWithDifferentSerializeAsIndex() throws Exception {
        EnumValues enumValues = getEnumValues();
        EnumSerializer originalEnumSerializer = new EnumSerializer(enumValues, true);
        EnumSerializer newEnumSerializer = new EnumSerializer(enumValues, false);
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);
        Mockito.when(format.getShape()).thenReturn(JsonFormat.Shape.ARRAY);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Mockito.when(property.getType()).thenReturn(Mockito.mock(JavaType.class));
        Mockito.when(property.getType().getRawClass()).thenReturn(MockEnum.class);

        ContextualSerializer contextualSerializer = originalEnumSerializer.createContextual(provider, property);

        assertEquals(newEnumSerializer, contextualSerializer);
    }

    @Test
    public void testGetSchemaWithSerializeAsIndexTrue() throws Exception {
        EnumValues enumValues = getEnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonNode schemaNode = enumSerializer.getSchema(Mockito.mock(SerializerProvider.class), Mockito.mock(Type.class));
        assertTrue(schemaNode.isInt());
    }

    @Test
    public void testGetSchemaWithSerializeAsIndexFalse() throws Exception {
        EnumValues enumValues = getEnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonNode schemaNode = enumSerializer.getSchema(Mockito.mock(SerializerProvider.class), Mockito.mock(Type.class));
        assertTrue(schemaNode.isTextual());
    }

    private EnumValues getEnumValues() {
        EnumValues enumValues = new EnumValues(MockEnum.class, new String[]{ "VALUE1", "VALUE2" });
        return enumValues;
    }

    private <T> T getField(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }
}
