import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator mockedGenerator = Mockito.mock(JsonGenerator.class);
        printer.writeRootValueSeparator(mockedGenerator);
        Mockito.verify(mockedGenerator).writeRaw(new SerializedString(" "));
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator mockedGenerator = Mockito.mock(JsonGenerator.class);
        printer.writeStartObject(mockedGenerator);
        Mockito.verify(mockedGenerator).writeRaw('{');
    }

    // More test cases for other methods can be added here

}