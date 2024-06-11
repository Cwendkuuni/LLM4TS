import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumValues;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() throws IOException {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializers = new SerializerProvider();
        Enum<?> en = Enum.ONE;
        enumSerializer.serialize(en, jsonGenerator, serializers);
        //assert your expected result here
    }

    @Test
    public void testSerializeAsString() throws IOException {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializers = new SerializerProvider();
        Enum<?> en = Enum.ONE;
        enumSerializer.serialize(en, jsonGenerator, serializers);
        //assert your expected result here
    }

    @Test
    public void testSerializeUsingToString() throws IOException {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializers = new SerializerProvider();
        Enum<?> en = Enum.ONE;
        serializers.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        enumSerializer.serialize(en, jsonGenerator, serializers);
        //assert your expected result here
    }

    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        EnumValues values = enumSerializer.getEnumValues();
        //assert your expected result here
    }

    @Test
    public void testSchemaWithIndexSerialization() {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = null;
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        //assert your expected result here
    }

    @Test
    public void testSchemaWithTextSerialization() {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = null;
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        //assert your expected result here
    }

    @Test
    public void testAcceptJsonFormatVisitorWithIndexSerialization() {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        //assert your expected result here
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTextSerialization() {
        EnumValues enumValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        //assert your expected result here
    }
}