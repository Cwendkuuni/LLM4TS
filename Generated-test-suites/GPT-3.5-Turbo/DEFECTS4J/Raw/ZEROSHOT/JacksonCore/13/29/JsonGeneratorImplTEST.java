package com.fasterxml.jackson.core.json;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testEnable() {
        // Arrange
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);

        // Act
        JsonGenerator result = generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);

        // Assert
        Assert.assertEquals(generator, result);
        Assert.assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        // Arrange
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator._cfgUnqNames = true;

        // Act
        generator._checkStdFeatureChanges(0, 0);

        // Assert
        Assert.assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        // Arrange
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);

        // Act
        JsonGenerator result = generator.setHighestNonEscapedChar(127);

        // Assert
        Assert.assertEquals(generator, result);
        Assert.assertEquals(127, generator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        // Arrange
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator._maximumNonEscapedChar = 127;

        // Act
        int result = generator.getHighestEscapedChar();

        // Assert
        Assert.assertEquals(127, result);
    }

    @Test
    public void testSetCharacterEscapes() {
        // Arrange
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        CharacterEscapes esc = new CharacterEscapes();

        // Act
        JsonGenerator result = generator.setCharacterEscapes(esc);

        // Assert
        Assert.assertEquals(generator, result);
        Assert.assertArrayEquals(esc.getEscapeCodesForAscii(), generator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        // Arrange
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        CharacterEscapes esc = new CharacterEscapes();
        generator._characterEscapes = esc;

        // Act
        CharacterEscapes result = generator.getCharacterEscapes();

        // Assert
        Assert.assertEquals(esc, result);
    }

    @Test
    public void testSetRootValueSeparator() {
        // Arrange
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        SerializableString sep = new DefaultPrettyPrinter().DEFAULT_ROOT_VALUE_SEPARATOR;

        // Act
        JsonGenerator result = generator.setRootValueSeparator(sep);

        // Assert
        Assert.assertEquals(generator, result);
        Assert.assertEquals(sep, generator._rootValueSeparator);
    }

}