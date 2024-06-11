package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ctxt, features, codec);
        Assert.assertEquals(0, jsonGeneratorImpl.getFeatures());
        Assert.assertEquals(ctxt, jsonGeneratorImpl.getIOContext());
        Assert.assertEquals(codec, jsonGeneratorImpl.getCodec());
    }

    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ctxt, features, codec);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        Assert.assertFalse(jsonGeneratorImpl.getCfgUnqNames());
    }

    @Test
    public void testCheckStdFeatureChanges() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ctxt, features, codec);
        jsonGeneratorImpl._checkStdFeatureChanges(10, 10);
        Assert.assertFalse(jsonGeneratorImpl.getCfgUnqNames());
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ctxt, features, codec);
        jsonGeneratorImpl.setHighestNonEscapedChar(10);
        Assert.assertEquals(10, jsonGeneratorImpl.getHighestEscapedChar());
        jsonGeneratorImpl.setHighestNonEscapedChar(-1);
        Assert.assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ctxt, features, codec);
        CharacterEscapes esc = new CharacterEscapes();
        jsonGeneratorImpl.setCharacterEscapes(esc);
        Assert.assertEquals(esc, jsonGeneratorImpl.getCharacterEscapes());
        Assert.assertEquals(esc.getEscapeCodesForAscii(), jsonGeneratorImpl.getOutputEscapes());
        jsonGeneratorImpl.setCharacterEscapes(null);
        Assert.assertNull(jsonGeneratorImpl.getCharacterEscapes());
        Assert.assertEquals(JsonGeneratorImpl.sOutputEscapes, jsonGeneratorImpl.getOutputEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ctxt, features, codec);
        SerializableString sep = new DefaultPrettyPrinter().getDefaultRootValueSeparator();
        jsonGeneratorImpl.setRootValueSeparator(sep);
        Assert.assertEquals(sep, jsonGeneratorImpl.getRootValueSeparator());
    }

}