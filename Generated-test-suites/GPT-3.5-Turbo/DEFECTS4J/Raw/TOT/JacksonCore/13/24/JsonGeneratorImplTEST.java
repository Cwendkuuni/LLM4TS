
import org.junit.Assert;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        try {
            generator.writeStringField("name", "John");
            // Verify that the field name and value are properly written
            String expected = "\"name\":\"John\"";
            Assert.assertEquals(expected, generator.toString());
        } catch (IOException e) {
            Assert.fail("Unexpected exception");
        }
        
        try {
            generator.writeStringField("name", null);
            // Verify that the field name is written but the value is null
            String expected = "\"name\":null";
            Assert.assertEquals(expected, generator.toString());
        } catch (IOException e) {
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testWriteFieldName() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        try {
            generator.writeFieldName("age");
            // Verify that the field name is properly written
            String expected = "\"age\":";
            Assert.assertEquals(expected, generator.toString());
        } catch (IOException e) {
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testWriteString() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        try {
            generator.writeString("John");
            // Verify that the string value is properly written
            String expected = "\"John\"";
            Assert.assertEquals(expected, generator.toString());
        } catch (IOException e) {
            Assert.fail("Unexpected exception");
        }
        
        try {
            generator.writeString(null);
            // Verify that a null value is written as "null"
            String expected = "null";
            Assert.assertEquals(expected, generator.toString());
        } catch (IOException e) {
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testEnableAndDisableFeature() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        // Verify that QUOTE_FIELD_NAMES feature is disabled by default
        Assert.assertTrue(generator._cfgUnqNames);
    
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        // Verify that QUOTE_FIELD_NAMES feature is enabled after calling enable()
        Assert.assertFalse(generator._cfgUnqNames);
    
        generator.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        // Verify that QUOTE_FIELD_NAMES feature is disabled after calling disable()
        Assert.assertTrue(generator._cfgUnqNames);
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        CharacterEscapes escapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[128];
            }
    
            @Override
            public SerializableString getEscapeSequence(int c) {
                return null;
            }
        };
        
        generator.setCharacterEscapes(escapes);
        // Verify that the character escapes are properly set
        Assert.assertEquals(escapes, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        generator.setRootValueSeparator(new SerializedString(","));
        // Verify that the root value separator is properly set
        Assert.assertEquals(",", generator._rootValueSeparator);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        generator.setHighestNonEscapedChar(255);
        // Verify that the highest non-escaped character is properly set
        Assert.assertEquals(255, generator.getHighestEscapedChar());
    }
}
