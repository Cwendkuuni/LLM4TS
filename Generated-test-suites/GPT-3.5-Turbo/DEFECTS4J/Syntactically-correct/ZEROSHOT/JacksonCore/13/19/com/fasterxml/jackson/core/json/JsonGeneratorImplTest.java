package com.fasterxml.jackson.core.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    private class DummyObjectCodec extends ObjectCodec {

        @Override
        public <T> T readValue(JsonParser p, Class<T> valueType) throws IOException {
            return null;
        }

        @Override
        public <T> T readValue(JsonParser p, TypeReference<T> valueTypeRef) throws IOException {
            return null;
        }

        @Override
        public <T> T readValue(JsonParser p, ResolvedType valueType) throws IOException {
            return null;
        }

        @Override
        public void writeValue(JsonGenerator g, Object value) throws IOException {
        }

        @Override
        public <T extends TreeNode> T readTree(JsonParser p) throws IOException {
            return null;
        }

        @Override
        public void writeTree(JsonGenerator g, TreeNode tree) throws IOException {
        }

        @Override
        public TreeNode createArrayNode() {
            return null;
        }

        @Override
        public TreeNode createObjectNode() {
            return null;
        }

        @Override
        public JsonParser treeAsTokens(TreeNode n) {
            return null;
        }

        @Override
        public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonProcessingException {
            return null;
        }

        @Override
        public void writeValue(JsonGenerator g, TreeNode n) throws IOException {
        }

    }

    private class DummyIOContext extends IOContext {

        public DummyIOContext(Object srcRef) {
            super(srcRef, false);
        }

    }
    
    private class DummyJsonObject extends JsonGeneratorImpl {

        public DummyJsonObject(IOContext ctxt, int features, ObjectCodec codec) {
            super(ctxt, features, codec);
        }

        @Override
        public JsonGenerator enable(Feature f) {
            return this;
        }

        @Override
        protected void _checkStdFeatureChanges(int newFeatureFlags, int changedFeatures) {
        }

        @Override
        public JsonGenerator setHighestNonEscapedChar(int charCode) {
            return this;
        }

        @Override
        public int getHighestEscapedChar() {
            return 0;
        }

        @Override
        public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
            return this;
        }

        @Override
        public CharacterEscapes getCharacterEscapes() {
            return null;
        }

        @Override
        public JsonGenerator setRootValueSeparator(SerializableString sep) {
            return this;
        }

        @Override
        public JsonGeneratorImpl.Feature[] features() {
            return null;
        }

        @Override
        public void writeFieldName(String name) throws IOException {
        }

        @Override
        public void writeString(String text) throws IOException {
        }
    }

    private final IOContext _ioContext = new DummyIOContext(new Object());
    private final ObjectCodec _codec = new DummyObjectCodec();

    @Test
    public void testJsonGeneratorImpl() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        Assert.assertNotNull(generator);
    }

    @Test
    public void testEnable() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        JsonGenerator result = generator.enable(Feature.QUOTE_FIELD_NAMES);
        Assert.assertEquals(generator, result);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        generator._checkStdFeatureChanges(0, 0);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        JsonGenerator result = generator.setHighestNonEscapedChar(0);
        Assert.assertEquals(generator, result);
    }

    @Test
    public void testGetHighestEscapedChar() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        int result = generator.getHighestEscapedChar();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetCharacterEscapes() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        JsonGenerator result = generator.setCharacterEscapes(null);
        Assert.assertEquals(generator, result);
    }

    @Test
    public void testGetCharacterEscapes() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        CharacterEscapes result = generator.getCharacterEscapes();
        Assert.assertNull(result);
    }

    @Test
    public void testSetRootValueSeparator() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        JsonGenerator result = generator.setRootValueSeparator(null);
        Assert.assertEquals(generator, result);
    }

    @Test
    public void testFeatures() {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        JsonGeneratorImpl.Feature[] result = generator.features();
        Assert.assertNull(result);
    }

    @Test
    public void testWriteFieldName() throws IOException {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        generator.writeFieldName(null);
    }

    @Test
    public void testWriteString() throws IOException {
        DummyJsonObject generator = new DummyJsonObject(_ioContext, 0, _codec);
        generator.writeString(null);
    }
}