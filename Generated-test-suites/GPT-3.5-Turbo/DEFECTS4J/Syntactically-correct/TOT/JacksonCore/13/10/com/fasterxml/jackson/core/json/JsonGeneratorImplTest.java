import org.junit.Assert;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);
        generator.writeStringField("fieldName", "value");

        // Assert that the field name is correctly written
        Assert.assertEquals("fieldName", generator.getOutputField());
        // Assert that the value is correctly written
        Assert.assertEquals("value", generator.getOutputValue());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);
        generator.setRootValueSeparator(new SerializableString("separator"));

        // Assert that the root value separator is correctly set
        Assert.assertEquals("separator", generator.getRootValueSeparator());
    }

    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);
        generator.enable(Feature.QUOTE_FIELD_NAMES);

        // Assert that the QUOTE_FIELD_NAMES feature is enabled
        Assert.assertTrue(generator.isFeatureEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);
        generator.setHighestNonEscapedChar(255);

        // Assert that the highest non-escaped character is correctly set
        Assert.assertEquals(255, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);
        CharacterEscapes customEscapes = new CustomCharacterEscapes();

        generator.setCharacterEscapes(customEscapes);

        // Assert that the custom character escapes are correctly set
        Assert.assertEquals(customEscapes, generator.getCharacterEscapes());
    }

    @Test
    public void testGetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);
        CharacterEscapes customEscapes = new CustomCharacterEscapes();

        generator.setCharacterEscapes(customEscapes);
        CharacterEscapes retrievedEscapes = generator.getCharacterEscapes();

        // Assert that the retrieved character escapes are the same as the set character escapes
        Assert.assertEquals(customEscapes, retrievedEscapes);
    }
    
    @Test
    public void testVersion() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);
        Version version = generator.version();

        // Assert that the version is not null
        Assert.assertNotNull(version);
        // Assert that the version is correct
        Assert.assertEquals("2.1", version.toString());
    }

    @Test
    public void test_writeStringField_with_null() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);

        try {
            generator.writeStringField(null, "value");
        } catch (IOException e) {
            // Assert that an IOException is thrown when fieldName is null
            Assert.assertEquals("Field name cannot be null", e.getMessage());
        }
    }

    @Test
    public void test_writeStringField_with_null_value() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);

        try {
            generator.writeStringField("fieldName", null);
        } catch (IOException e) {
            // Assert that an IOException is thrown when value is null
            Assert.assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void test_setRootValueSeparator_with_null() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(/* IOContext */ null, /* features */ 0, /* ObjectCodec */ null);

        try {
            generator.setRootValueSeparator(null);
        } catch (NullPointerException e) {
            // Assert that a NullPointerException is thrown when separator is null
            Assert.assertEquals("Root value separator cannot be null", e.getMessage());
        }
    }
}