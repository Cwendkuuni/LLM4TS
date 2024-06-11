import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import java.io.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

        Number number = 123;
        String expectedOutput = "123";

        serializer.serialize(number, jsonGenerator, mapper.getSerializerProvider());
        String actualOutput = jsonGenerator.getOutputBuffer().toString();

        assertEquals(expectedOutput, actualOutput);
    }
}