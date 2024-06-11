
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        Integer number = 42;
        TestObject testObject = new TestObject(number);

        // Serialize the number
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
            String json = generator.getOutputTarget().toString();

            // Verify the JSON output
            assertEquals(number.toString(), json);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred");
        }

    }

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short number = 42;
        TestObject testObject = new TestObject(number);

        // Serialize the number
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
            String json = generator.getOutputTarget().toString();

            // Verify the JSON output
            assertEquals(number.toString(), json);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred");
        }

    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Integer number = 42;
        TestObject testObject = new TestObject(number);

        // Serialize the number
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
            String json = generator.getOutputTarget().toString();

            // Verify the JSON output
            assertEquals(number.toString(), json);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred");
        }

    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new NumberSerializers.LongSerializer();
        Long number = 42L;
        TestObject testObject = new TestObject(number);

        // Serialize the number
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
            String json = generator.getOutputTarget().toString();

            // Verify the JSON output
            assertEquals(number.toString(), json);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred");
        }

    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float number = 42.0f;
        TestObject testObject = new TestObject(number);

        // Serialize the number
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
            String json = generator.getOutputTarget().toString();

            // Verify the JSON output
            assertEquals(number.toString(), json);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred");
        }

    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
        Double number = 42.0;
        TestObject testObject = new TestObject(number);

        // Serialize the number
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
            String json = generator.getOutputTarget().toString();

            // Verify the JSON output
            assertEquals(number.toString(), json);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred");
        }

    }
}

class TestObject {
    private Object value;

    public TestObject(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
