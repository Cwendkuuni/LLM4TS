import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AsPropertyTypeDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        String json = "{\"_type\":\"type\",\"field1\":\"test\",\"field2\":42}";

        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(SimpleType.constructUnsafe(Object.class), null, "_type", true, null);
        JsonParser parser = objectMapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedFromObject(parser, objectMapper.getDeserializationContext());

        assertEquals("{\"_type\":\"type\",\"field1\":\"test\",\"field2\":42}", result);
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        String json = "{\"field1\":\"test\",\"field2\":42}";

        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(SimpleType.constructUnsafe(Object.class), null, "_type", true, null);
        JsonParser parser = objectMapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedForId(parser, objectMapper.getDeserializationContext(), null);

        assertEquals("{\"field1\":\"test\",\"field2\":42}", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        String json = "{\"field1\":\"test\",\"field2\":42}";

        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(SimpleType.constructUnsafe(Object.class), null, "_type", true, null);
        JsonParser parser = objectMapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, objectMapper.getDeserializationContext(), null);

        assertEquals("{\"field1\":\"test\",\"field2\":42}", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        String json = "{\"field1\":\"test\",\"field2\":42}";

        TypeDeserializerBase deserializer = new AsPropertyTypeDeserializer(SimpleType.constructUnsafe(Object.class), null, "_type", true, null);
        JsonParser parser = objectMapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedFromAny(parser, objectMapper.getDeserializationContext());

        assertEquals("{\"field1\":\"test\",\"field2\":42}", result);
    }
}