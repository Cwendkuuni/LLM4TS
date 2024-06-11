package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.core.IOContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;

public class JsonGeneratorImplTest {

    @Mock
    private IOContext iOContext;
    @Mock
    private ObjectCodec objectCodec;
    
    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonGeneratorImpl = new JsonGeneratorImpl(iOContext, 1, objectCodec);
    }

    @Test
    public void testEnable() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        jsonGeneratorImpl._checkStdFeatureChanges(1, Feature.QUOTE_FIELD_NAMES.getMask());
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGeneratorImpl._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        jsonGeneratorImpl._maximumNonEscapedChar = 127;
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGeneratorImpl._characterEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        jsonGeneratorImpl._characterEscapes = characterEscapes;
        assertEquals(characterEscapes, jsonGeneratorImpl.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = new DefaultPrettyPrinter().DEFAULT_ROOT_VALUE_SEPARATOR;
        jsonGeneratorImpl.setRootValueSeparator(sep);
        assertEquals(sep, jsonGeneratorImpl._rootValueSeparator);
    }

}