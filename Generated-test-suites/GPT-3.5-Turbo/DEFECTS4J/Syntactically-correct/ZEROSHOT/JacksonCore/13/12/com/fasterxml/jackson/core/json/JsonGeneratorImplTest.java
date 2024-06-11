import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class JsonGeneratorImplTest {

    @Test
    public void testEnableQuoteFieldNames() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorImpl._checkStdFeatureChanges(0, 0);
        assertTrue(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorImpl.setCharacterEscapes(null);
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorImpl.setRootValueSeparator(null);
        assertNull(jsonGeneratorImpl._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        assertNotNull(jsonGeneratorImpl.version());
    }

    @Test
    public void testWriteStringField() {
        try {
            JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
            jsonGeneratorImpl.writeStringField("fieldName", "value");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}