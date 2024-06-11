
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;

public class JsonGeneratorImplTest {

    // Mock dependencies for JsonGeneratorImpl
    private IOContext ioContext = mock(IOContext.class);
    private ObjectCodec codec = mock(ObjectCodec.class);

    @Test
    public void testEnableQUOTE_FIELD_NAMES() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        boolean quoteFieldNames = generator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(quoteFieldNames);

        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        quoteFieldNames = generator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertTrue(quoteFieldNames);
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        CharacterEscapes escapes = mock(CharacterEscapes.class);

        generator.setCharacterEscapes(escapes);
        CharacterEscapes returnedEscapes = generator.getCharacterEscapes();

        assertEquals(escapes, returnedEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        SerializableString sep = mock(SerializableString.class);

        generator.setRootValueSeparator(sep);
        SerializableString returnedSep = generator._rootValueSeparator;

        assertEquals(sep, returnedSep);
    }

    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl generator = spy(new JsonGeneratorImpl(ioContext, 0, codec));
        String fieldName = "testField";
        String value = "testValue";

        generator.writeStringField(fieldName, value);

        verify(generator).writeFieldName(fieldName);
        verify(generator).writeString(value);
    }
}
