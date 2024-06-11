import static org.junit.Assert.*;
import org.junit.*;

public class JsonGeneratorImplTest {

    @Test
    public void testEnable() {
        // Create JsonGeneratorImpl object
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl();

        // Enable Feature
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);

        // Verify if the feature is enabled
        assertTrue(jsonGeneratorImpl._cfgUnqNames == false);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        // Create JsonGeneratorImpl object
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl();

        // Check standard feature changes
        jsonGeneratorImpl._checkStdFeatureChanges(1, 1);

        // Verify if the standard feature changes are checked
        assertTrue(jsonGeneratorImpl._cfgUnqNames == false);
    }

    @Test
    public void test_setHighestNonEscapedChar() {
        // Create JsonGeneratorImpl object
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl();

        // Set highest non escaped char
        jsonGeneratorImpl.setHighestNonEscapedChar(128);

        // Verify if the highest non escaped char is set
        assertTrue(jsonGeneratorImpl._maximumNonEscapedChar == 128);
    }

    @Test
    public void test_getHighestEscapedChar() {
        // Create JsonGeneratorImpl object
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl();

        // Set highest non escaped char
        jsonGeneratorImpl.setHighestNonEscapedChar(120);

        // Get highest escaped char
        int highestEscapedChar = jsonGeneratorImpl.getHighestEscapedChar();

        // Verify if the highest escaped char is fetched correctly
        assertEquals(120, highestEscapedChar);
    }

    @Test
    public void test_setCharacterEscapes() {
        // Create JsonGeneratorImpl object
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl();

        // Set character escapes
        jsonGeneratorImpl.setCharacterEscapes(new CharacterEscapes());

        // Verify if the character escapes are set
        assertNotNull(jsonGeneratorImpl._characterEscapes);
    }

    @Test
    public void test_getCharacterEscapes() {
        // Create JsonGeneratorImpl object
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl();

        // Set character escapes
        jsonGeneratorImpl.setCharacterEscapes(new CharacterEscapes());

        // Get character escapes
        CharacterEscapes characterEscapes = jsonGeneratorImpl.getCharacterEscapes();

        // Verify if the character escapes are fetched correctly
        assertNotNull(characterEscapes);
    }

    @Test
    public void test_setRootValueSeparator() {
        // Create JsonGeneratorImpl object
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl();

        // Set root value separator
        jsonGeneratorImpl.setRootValueSeparator(new SerializableString());

        // Verify if the root value separator is set
        assertNotNull(jsonGeneratorImpl._rootValueSeparator);
    }

}