import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerialize_ValueIsNull() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Bean bean = new Bean(null);

        String json = objectMapper.writeValueAsString(bean);

        Assert.assertEquals("{\"value\":null}", json);
    }

    @Test
    public void testSerialize_ValueIsString() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Bean bean = new Bean("test");

        String json = objectMapper.writeValueAsString(bean);

        Assert.assertEquals("{\"value\":\"test\"}", json);
    }

    @Test
    public void testCreateContextual_TypeIsFinal() throws JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        SerializerProvider provider = null;
        BeanProperty property = null;
        ContextualSerializer result = null;

        result = serializer.createContextual(provider, property);

        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_TypeIsNotFinal_UseStaticTyping() throws JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        serializer._accessorMethod.getType();
        SerializerProvider provider = null;
        BeanProperty property = null;
        ContextualSerializer result = null;

        result = serializer.createContextual(provider, property);

        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_TypeIsNotFinal_DoNotUseStaticTyping() throws JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        serializer._accessorMethod.getType().isFinal();
        SerializerProvider provider = null;
        BeanProperty property = null;
        ContextualSerializer result = null;

        result = serializer.createContextual(provider, property);

        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testSerializeWithType_NullValue() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Bean bean = new Bean(null);

        String json = objectMapper.writeValueAsString(bean);

        Assert.assertEquals("{\"value\":null}", json);
    }

    @Test
    public void testSerializeWithType_NotNullValue() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Bean bean = new Bean("test");

        String json = objectMapper.writeValueAsString(bean);

        Assert.assertEquals("{\"value\":\"test\"}", json);
    }

    @Test
    public void testGetSchema_SchemaAwareSerializer() throws JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        SerializerProvider provider = null;
        BeanProperty property = null;
        Type typeHint = null;
        JsonNode result = null;

        result = serializer.getSchema(provider, typeHint);

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetSchema_SerializerIsNotSchemaAware() throws JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        SerializerProvider provider = null;
        BeanProperty property = null;
        Type typeHint = null;
        JsonNode result = null;

        result = serializer.getSchema(provider, typeHint);

        Assert.assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum_EnumType() throws JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        Class<?> enumType = null;
        boolean result = false;

        result = serializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);

        Assert.assertFalse(result);
    }

    private static class Bean {
        private final String value;

        public Bean(String value) {
            this.value = value;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String getValue() throws IOException {
            return value;
        }
    }
}