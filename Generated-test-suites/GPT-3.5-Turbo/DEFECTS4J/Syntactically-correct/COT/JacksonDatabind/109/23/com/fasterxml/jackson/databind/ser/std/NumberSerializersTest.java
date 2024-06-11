
import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testSerializeShort() {
        ShortSerializer serializer = new ShortSerializer();
        Short value = (short) 10;
        try {
            String expected = "10";
            assertEquals(expected, serializer.serialize(value, null, null));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeInteger() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Integer value = 100;
        try {
            String expected = "100";
            assertEquals(expected, serializer.serialize(value, null, null));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeIntLike() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        Number value = 200;
        try {
            String expected = "200";
            assertEquals(expected, serializer.serialize(value, null, null));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeLong() {
        LongSerializer serializer = new LongSerializer(Long.class);
        Long value = 1000L;
        try {
            String expected = "1000";
            assertEquals(expected, serializer.serialize(value, null, null));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeFloat() {
        FloatSerializer serializer = FloatSerializer.instance;
        Float value = 10.5f;
        try {
            String expected = "10.5";
            assertEquals(expected, serializer.serialize(value, null, null));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeDouble() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = 3.14159;
        try {
            String expected = "3.14159";
            assertEquals(expected, serializer.serialize(value, null, null));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetSchema() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        SerializerProvider provider = null;
        Type typeHint = null;
        try {
            assertNotNull(serializer.getSchema(provider, typeHint));
        } catch (Exception e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("JsonMappingException occurred");
        }
    }

    @Test
    public void testCreateContextual() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        SerializerProvider prov = null;
        BeanProperty property = null;
        try {
            assertNull(serializer.createContextual(prov, property));
        } catch (Exception e) {
            fail("An exception occurred");
        }
    }
}
