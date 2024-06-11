import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        String expected = "Expected content";
        String inputContent = "Input content";
        String outputContent = "";

        InputStream input = new ByteArrayInputStream(inputContent.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, output);

        outputContent = output.toString();

        Assertions.assertEquals(expected, outputContent);
    }
}