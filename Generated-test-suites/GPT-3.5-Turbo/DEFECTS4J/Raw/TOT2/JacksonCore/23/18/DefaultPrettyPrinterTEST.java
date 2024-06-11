import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockJsonGenerator generator = new MockJsonGenerator();
        
        printer.writeRootValueSeparator(generator);
        
        assertEquals(" ", generator.getOutput());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockJsonGenerator generator = new MockJsonGenerator();
        
        printer.writeStartObject(generator);
        
        assertEquals("{", generator.getOutput());
    }

    // Additional test cases for other methods can be added here

    private class MockJsonGenerator extends JsonGenerator {

        private StringBuilder output = new StringBuilder();

        @Override
        public void writeRaw(String text) throws IOException {
            output.append(text);
        }

        @Override
        public void writeRaw(char c) throws IOException {
            output.append(c);
        }

        public String getOutput() {
            return output.toString();
        }

        // Other abstract methods from JsonGenerator can be implemented here
    }
}