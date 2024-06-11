import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testConstructor() throws IOException {
        IOContext ctxt = new IOContext();
        ObjectCodec codec = new ObjectMapper().getFactory().getCodec(); // For ObjectMapper, make sure it is imported
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, new int[]{}, codec);
        Assertions.assertNotNull(jsonGenerator);
    }
}