
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import java.io.IOException;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize((short) 10, gen, null);
        gen.flush();
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(10, gen, null);
        gen.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(10, gen, null);
        gen.flush();
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(10L, gen, null);
        gen.flush();
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(10.5f, gen, null);
        gen.flush();
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(10.5, gen, null);
        gen.flush();
    }
}
