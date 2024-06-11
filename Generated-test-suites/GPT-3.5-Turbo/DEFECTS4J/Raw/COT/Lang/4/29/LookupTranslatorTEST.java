
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {
    private LookupTranslator translator;

    @Before
    public void setUp() {
        // Setup lookup table for testing
        CharSequence[] lookup1 = { "ab", "1" };
        CharSequence[] lookup2 = { "cde", "2" };
        CharSequence[][] lookup = { lookup1, lookup2 };
        translator = new LookupTranslator(lookup);
    }

    @Test
    public void translateWithValidInput_shouldTranslate() throws IOException {
        // Arrange
        String input = "abcde";
        StringWriter out = new StringWriter();

        // Act
        int result = translator.translate(input, 0, out);

        // Assert
        Assert.assertEquals(2, result);
        Assert.assertEquals("12", out.toString());
    }

    @Test
    public void translateWithInvalidInput_shouldNotTranslate() throws IOException {
        // Arrange
        String input = "xyz";
        StringWriter out = new StringWriter();

        // Act
        int result = translator.translate(input, 0, out);

        // Assert
        Assert.assertEquals(0, result);
        Assert.assertEquals("", out.toString());
    }

    @Test(expected = IOException.class)
    public void translateWithIOException_shouldThrowException() throws IOException {
        // Arrange
        String input = "abcde";
        StringWriter out = new StringWriter() {
            @Override
            public void write(String str) throws IOException {
                throw new IOException("Test exception");
            }
        };

        // Act
        translator.translate(input, 0, out);
    }
}