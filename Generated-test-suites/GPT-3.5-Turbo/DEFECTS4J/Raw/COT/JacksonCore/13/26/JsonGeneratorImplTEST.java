
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        IOContext ctxt = mock(IOContext.class);
        generator = new JsonGeneratorImpl(ctxt, 0, null) {
            @Override
            public void writeFieldName(String name) {
                // Do nothing
            }
            @Override
            public void writeString(String text) {
                // Do nothing
            }
        };
    }

    @Test
    public void testEnableQuoteFieldNames() {
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertEquals(false, generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        generator.setHighestNonEscapedChar(100);
        assertEquals(100, generator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        int result = generator.getHighestEscapedChar();
        assertEquals(127, result);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = mock(CharacterEscapes.class);
        generator.setCharacterEscapes(esc);
        assertEquals(esc, generator._characterEscapes);
        assertEquals(null, generator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes esc = generator.getCharacterEscapes();
        assertNull(esc);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = mock(SerializableString.class);
        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        Version version = generator.version();
        assertEquals(2.1, version.getMajorVersion(), 0);
    }

}
