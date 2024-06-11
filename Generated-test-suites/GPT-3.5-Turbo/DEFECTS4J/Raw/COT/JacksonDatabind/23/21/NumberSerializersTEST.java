import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(allDeserializers.containsKey(Integer.class.getName()), true);
        assertEquals(allDeserializers.containsKey(Integer.TYPE.getName()), true);
        assertEquals(allDeserializers.containsKey(Long.class.getName()), true);
        assertEquals(allDeserializers.containsKey(Long.TYPE.getName()), true);
        assertEquals(allDeserializers.containsKey(Byte.class.getName()), true);
        assertEquals(allDeserializers.containsKey(Byte.TYPE.getName()), true);
        assertEquals(allDeserializers.containsKey(Short.class.getName()), true);
        assertEquals(allDeserializers.containsKey(Short.TYPE.getName()), true);
        assertEquals(allDeserializers.containsKey(Float.class.getName()), true);
        assertEquals(allDeserializers.containsKey(Float.TYPE.getName()), true);
        assertEquals(allDeserializers.containsKey(Double.class.getName()), true);
        assertEquals(allDeserializers.containsKey(Double.TYPE.getName()), true);
    }

    @Test
    public void testBaseGetSchema() {
        Base base = new Base() {};
        SerializerProvider provider = new DefaultSerializerProvider();
        Type typeHint = null;
        JsonNode schemaNode = base.getSchema(provider, typeHint);
        assertEquals(schemaNode.isObject(), true);
        assertEquals(schemaNode.get("type").textValue(), "number");
        assertEquals(schemaNode.get("required").booleanValue(), true);
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() {
        Base base = new Base() {};
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.BaseFormatVisitorImpl(null);
        JavaType typeHint = null;
        base.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(visitor instanceof JsonIntegerFormatVisitor, true);
        assertEquals(visitor.getProvider(), null);
    }

    @Test
    public void testBaseCreateContextual() {
        Base base = new Base() {};
        SerializerProvider prov = new DefaultSerializerProvider();
        BeanProperty property = null;
        try {
            base.createContextual(prov, property);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testShortSerializerSerialize() {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            shortSerializer.serialize(10, gen, provider);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testIntegerSerializerSerialize() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            integerSerializer.serialize(10, gen, provider);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testIntegerSerializerSerializeWithType() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider();
        TypeSerializer typeSer = null;
        try {
            integerSerializer.serializeWithType(10, gen, provider, typeSer);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testIntLikeSerializerSerialize() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            intLikeSerializer.serialize(10, gen, provider);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLongSerializerSerialize() {
        LongSerializer longSerializer = new LongSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            longSerializer.serialize(10L, gen, provider);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testFloatSerializerSerialize() {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            floatSerializer.serialize(10.0f, gen, provider);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDoubleSerializerSerialize() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            doubleSerializer.serialize(10.0, gen, provider);
        }
        catch(Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

}