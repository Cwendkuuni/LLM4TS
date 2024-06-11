import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    private AsWrapperTypeDeserializer deserializer;
    private TestTypeIdResolver typeIdResolver;
    private JavaType javaType;
    private BeanDeserializer baseDeserializer;
    private TokenBuffer tokenBuffer;

    @Before
    public void setUp() {
        typeIdResolver = new TestTypeIdResolver();
        javaType = new JavaType(null, Object.class, null, null);
        baseDeserializer = new BeanDeserializer(javaType, null, null, null, null);
        deserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver,
                "typePropertyName", true, null);
        tokenBuffer = new TokenBuffer(null, false);
    }

    @Test
    public void testDeserializeTypedFromObject() throws JsonParseException,
            JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser();
        DeserializationContext deserializationContext = createDeserializationContext();

        Object result = deserializer.deserializeTypedFromObject(jsonParser,
                deserializationContext);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws JsonParseException,
            JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser();
        DeserializationContext deserializationContext = createDeserializationContext();

        Object result = deserializer.deserializeTypedFromArray(jsonParser,
                deserializationContext);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws JsonParseException,
            JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser();
        DeserializationContext deserializationContext = createDeserializationContext();

        Object result = deserializer.deserializeTypedFromScalar(jsonParser,
                deserializationContext);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws JsonParseException,
            JsonProcessingException, IOException {
        JsonParser jsonParser = createJsonParser();
        DeserializationContext deserializationContext = createDeserializationContext();

        Object result = deserializer.deserializeTypedFromAny(jsonParser,
                deserializationContext);

        assertNotNull(result);
    }

    private JsonParser createJsonParser() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.getFactory().createParser("{\"key\":\"value\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DeserializationContext createDeserializationContext() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonDeserializer<Object> obj = objectMapper
                .deserializerProviderInstance()
                .findValueDeserializer(
                        objectMapper.deserializationConfig(), javaType,
                        baseDeserializer);

        return new DeserializationContext(objectMapper.getFactory(),
                objectMapper.getDeserializationConfig(),
                objectMapper.getSerializerFactory()).withDeserializer(obj);
    }
}