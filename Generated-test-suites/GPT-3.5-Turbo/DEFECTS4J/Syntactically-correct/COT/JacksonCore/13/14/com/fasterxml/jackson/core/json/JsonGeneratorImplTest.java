import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        generator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec());
    }

    @Test
    public void testEnable() {
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        generator._checkStdFeatureChanges(0, Feature.QUOTE_FIELD_NAMES.mask);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        generator.setHighestNonEscapedChar(255);
        assertEquals(255, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes escapes = new CharacterEscapes();
        generator.setCharacterEscapes(escapes);
        assertEquals(escapes, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString separator = new SerializableString();
        generator.setRootValueSeparator(separator);
        assertEquals(separator, generator._rootValueSeparator);
    }

}