import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.FixedSpaceIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.NopIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.SerializableString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Separators;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class DefaultPrettyPrinterTest {
    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Before
    public void setup() {
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(SerializedString.class));
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeStartObject(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(Character.class));
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.beforeObjectEntries(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(Character.class));
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(SerializedString.class));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(Character.class));
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeEndObject(jsonGenerator, 0);

        verify(jsonGenerator, times(1)).writeRaw(any(Character.class));
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeStartArray(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(Character.class));
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.beforeArrayValues(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(Character.class));
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);

        verify(jsonGenerator, times(1)).writeRaw(any(SerializedString.class));
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        defaultPrettyPrinter.writeEndArray(jsonGenerator, 0);

        verify(jsonGenerator, times(1)).writeRaw(any(Character.class));
    }
}