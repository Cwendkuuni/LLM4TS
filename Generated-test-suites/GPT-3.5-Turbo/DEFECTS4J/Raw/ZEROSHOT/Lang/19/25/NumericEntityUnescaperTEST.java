import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidHexInput_ReturnsCodepoint() throws IOException {
        // Arrange
        CharSequence input = "&#x0041;";
        int index = 0;
        Writer writer = new DummyWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_ValidDecimalInput_ReturnsCodepoint() throws IOException {
        // Arrange
        CharSequence input = "&#65;";
        int index = 0;
        Writer writer = new DummyWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidHexInput_ReturnsZero() throws IOException {
        // Arrange
        CharSequence input = "&#xFG;";
        int index = 0;
        Writer writer = new DummyWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidDecimalInput_ReturnsZero() throws IOException {
        // Arrange
        CharSequence input = "&#ABC;";
        int index = 0;
        Writer writer = new DummyWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    private class DummyWriter extends Writer {
        private StringBuilder stringBuilder;

        public DummyWriter() {
            this.stringBuilder = new StringBuilder();
        }

        @Override
        public void write(char[] cbuf, int off, int len) {
            stringBuilder.append(cbuf, off, len);
        }

        @Override
        public void flush() {}

        @Override
        public void close() {}

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}