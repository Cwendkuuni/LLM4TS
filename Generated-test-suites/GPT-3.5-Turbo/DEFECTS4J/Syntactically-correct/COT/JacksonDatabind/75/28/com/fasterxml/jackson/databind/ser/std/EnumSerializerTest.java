import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private EnumValues enumValues;
    private Class enumClass;

    @Before
    public void setup() {
        enumClass = TestEnum.class;
        enumValues = EnumValues.constructFromName(null, enumClass);
        enumSerializer = new EnumSerializer(enumValues, null);
    }

    @Test
    public void testGetEnumValues() {
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testSerializeByIndex() throws IOException {
        TestEnum testEnum = TestEnum.VAL1;

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new DefaultSerializerProvider.Builder().build();
        enumSerializer.serialize(testEnum, jsonGenerator, serializerProvider);

        jsonGenerator.flush();
    }

    @Test
    public void testSerializeByToString() throws IOException {
        TestEnum testEnum = TestEnum.VAL1;

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider =
                new DefaultSerializerProvider.Builder().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING).build();
        enumSerializer.serialize(testEnum, jsonGenerator, serializerProvider);

        jsonGenerator.flush();
    }

    @Test
    public void testSerializeByValue() throws IOException {
        TestEnum testEnum = TestEnum.VAL1;

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider =
                new DefaultSerializerProvider.Builder().build();
        enumSerializer.serialize(testEnum, jsonGenerator, serializerProvider);

        jsonGenerator.flush();
    }

    @Test
    public void testGetSchema() {
        JsonSerializer jsonSerializer = new JsonSerializer() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            public JsonSerializer createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }
        };

        TypeBindings typeBindings = new TypeBindings(TypeFactory.defaultInstance());
        FilterProvider filterProvider = new SimpleFilterProvider();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withOverrides(format);
        format = format.withFeature(JsonFormat.Feature.WRITE_ENUMS_USING_INDEX, true);

        jsonSerializer.createContextual(null, null); // test createContextual method
        jsonSerializer.getSchema(null, null); // test getSchema method
        jsonSerializer.acceptJsonFormatVisitor(null, null); // test acceptJsonFormatVisitor method
    }

    /**
     * Mock enum for testing.
     */
    private enum TestEnum {
        VAL1,
        VAL2,
        VAL3;
    }
}