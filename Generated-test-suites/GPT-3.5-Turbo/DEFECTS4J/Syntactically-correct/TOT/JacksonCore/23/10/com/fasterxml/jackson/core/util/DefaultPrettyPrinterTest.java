import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;

public class DefaultPrettyPrinterTest {

    @Mock
    private JsonGenerator jsonGenerator;

    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        defaultPrettyPrinter.writeStartObject(jsonGenerator);

        verify(jsonGenerator).writeRaw('{');
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        defaultPrettyPrinter.beforeObjectEntries(jsonGenerator);

        verify(jsonGenerator).writeRaw(System.lineSeparator());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(DefaultPrettyPrinter.DEFAULT_SEPARATORS.getObjectFieldValueSeparator());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(DefaultPrettyPrinter.DEFAULT_SEPARATORS.getObjectEntrySeparator());
    }
    
    @Test
    public void testWriteEndObjectWithNrOfEntriesGreaterThanZero() throws IOException {
        defaultPrettyPrinter.writeEndObject(jsonGenerator, 2);

        verify(jsonGenerator).writeRaw('}');
    }
    
    @Test
    public void testWriteEndObjectWithNrOfEntriesEqualsZero() throws IOException {
        defaultPrettyPrinter.writeEndObject(jsonGenerator, 0);

        verify(jsonGenerator).writeRaw(' ');
    }

    @Test
    public void testWriteStartArray() throws IOException {
        defaultPrettyPrinter.writeStartArray(jsonGenerator);

        verify(jsonGenerator).writeRaw('[');
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        defaultPrettyPrinter.beforeArrayValues(jsonGenerator);

        verify(jsonGenerator).writeRaw(System.lineSeparator());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(DefaultPrettyPrinter.DEFAULT_SEPARATORS.getArrayValueSeparator());
    }

    @Test
    public void testWriteEndArrayWithNrOfValuesGreaterThanZero() throws IOException {
        defaultPrettyPrinter.writeEndArray(jsonGenerator, 2);

        verify(jsonGenerator).writeRaw(']');
    }
    
    @Test
    public void testWriteEndArrayWithNrOfValuesEqualsZero() throws IOException {
        defaultPrettyPrinter.writeEndArray(jsonGenerator, 0);

        verify(jsonGenerator).writeRaw(' ');
    }
}