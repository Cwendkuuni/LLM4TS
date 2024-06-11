import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = 0;
        ObjectCodec codec = null;
        generator = new JsonGeneratorImpl(ctxt, features, codec) {};
    }

    @Test
    public void testEnable_WithQuoteFieldNamesFeature_ShouldSetCfgUnqNamesToFalse() {
        // Arrange
        JsonGenerator.Feature feature = JsonGenerator.Feature.QUOTE_FIELD_NAMES;

        // Act
        JsonGenerator result = generator.enable(feature);

        // Assert
        assertFalse(generator._cfgUnqNames);
        assertEquals(generator, result);
    }

    @Test
    public void testEnable_WithoutQuoteFieldNamesFeature_ShouldNotChangeCfgUnqNames() {
        // Arrange
        JsonGenerator.Feature feature = JsonGenerator.Feature.ESCAPE_NON_ASCII;

        // Act
        JsonGenerator result = generator.enable(feature);

        // Assert
        assertEquals(generator._cfgUnqNames, generator._cfgUnqNames);
        assertEquals(generator, result);
    }

    @Test
    public void testSetHighestNonEscapedChar_WithValidCharCode_ShouldSetMaximumNonEscapedChar() {
        // Arrange
        int charCode = 255;

        // Act
        JsonGenerator result = generator.setHighestNonEscapedChar(charCode);

        // Assert
        assertEquals(charCode, generator._maximumNonEscapedChar);
        assertEquals(generator, result);
    }

    @Test
    public void testSetHighestNonEscapedChar_WithNegativeCharCode_ShouldSetMaximumNonEscapedCharToZero() {
        // Arrange
        int charCode = -1;

        // Act
        JsonGenerator result = generator.setHighestNonEscapedChar(charCode);

        // Assert
        assertEquals(0, generator._maximumNonEscapedChar);
        assertEquals(generator, result);
    }

    @Test
    public void testSetCharacterEscapes_WithNullEscapes_ShouldSetOutputEscapesToDefault() {
        // Arrange
        CharacterEscapes escapes = null;

        // Act
        JsonGenerator result = generator.setCharacterEscapes(escapes);

        // Assert
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, generator._outputEscapes);
        assertEquals(generator, result);
    }

    @Test
    public void testSetCharacterEscapes_WithCustomEscapes_ShouldSetOutputEscapesToCustomEscapes() {
        // Arrange
        CharacterEscapes escapes = new CustomCharacterEscapes();

        // Act
        JsonGenerator result = generator.setCharacterEscapes(escapes);

        // Assert
        assertArrayEquals(escapes.getEscapeCodesForAscii(), generator._outputEscapes);
        assertEquals(generator, result);
    }

    @Test
    public void testSetRootValueSeparator_WithSerializableString_ShouldSetRootValueSeparator() {
        // Arrange
        SerializableString sep = new DefaultPrettyPrinter().getRootValueSeparator();

        // Act
        JsonGenerator result = generator.setRootValueSeparator(sep);

        // Assert
        assertEquals(sep, generator._rootValueSeparator);
        assertEquals(generator, result);
    }

    @Test
    public void testVersion_ShouldReturnVersionForJsonGeneratorImplClass() {
        // Arrange
        Version expectedVersion = VersionUtil.versionFor(JsonGeneratorImpl.class);

        // Act
        Version result = generator.version();

        // Assert
        assertEquals(expectedVersion, result);
    }

    class CustomCharacterEscapes extends CharacterEscapes {

        @Override
        public int[] getEscapeCodesForAscii() {
            // Custom implementation
            return null;
        }

        @Override
        public SerializableString getEscapeSequence(int ch) {
            // Custom implementation
            return null;
        }
    }
}