
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter printer;

    @Before
    public void setup() {
        printer = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        // Arrange
        StubJsonGenerator generator = new StubJsonGenerator();

        // Act
        printer.writeRootValueSeparator(generator);

        // Assert
        Assert.assertEquals(" ", generator.getRawValue());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        // Arrange
        StubJsonGenerator generator = new StubJsonGenerator();

        // Act
        printer.writeStartObject(generator);

        // Assert
        Assert.assertEquals("{", generator.getRawValue());
        Assert.assertEquals(1, printer.getNesting());
    }
    
    // ...

    private static class StubJsonGenerator extends JsonGenerator {

        private String rawValue;

        @Override
        public void writeRaw(String raw) {
            rawValue = raw;
        }

        @Override
        public void writeRaw(String raw, int offset, int len) {
            rawValue = raw.substring(offset, offset + len);
        }

        @Override
        public void close() {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        // Other overridden methods

        public String getRawValue() {
            return rawValue;
        }
    }
}

