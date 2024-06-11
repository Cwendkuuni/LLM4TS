package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.TestUtils;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Assert;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImpl() throws IOException {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET, null);

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\"}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET | JsonParser.Feature.QUOTE_FIELD_NAMES, null);

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":test}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET, null);
        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] {CharTypes.BACKSLASH_ESCAPE};
            }
        });

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test\\");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\\\\\"}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET, null);
        jsonGenerator.setCharacterEscapes(null);

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test\\");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\\u005C\"}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET | JsonParser.Feature.QUOTE_FIELD_NAMES, null);
        jsonGenerator.setCharacterEscapes(null);

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test\\");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\\u005C\"}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET | JsonParser.Feature.ESCAPE_NON_ASCII, null);

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test\u00E9");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\\u00E9\"}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET | JsonParser.Feature.ESCAPE_NON_ASCII, null);
        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] {CharTypes.BACKSLASH_ESCAPE, CharTypes.BACKSLASH_ESCAPE};
            }
        });

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test\u00E9");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\\u00E9\\u00E9\"}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET | JsonParser.Feature.ESCAPE_NON_ASCII, null);
        jsonGenerator.setCharacterEscapes(null);

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test\u00E9");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\\u00E9\"}", result);
        } finally {
            jsonGenerator.close();
        }

        jsonGenerator = new JsonGeneratorImpl(ctxt, JsonParser.Feature.AUTO_CLOSE_TARGET | JsonParser.Feature.QUOTE_FIELD_NAMES | JsonParser.Feature.ESCAPE_NON_ASCII, null);

        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("numberField", 42);
            jsonGenerator.writeStringField("stringField", "test\u00E9");
            jsonGenerator.writeEndObject();

            String result = TestUtils.readAll(jsonGenerator.getOutput());
            Assert.assertEquals("{\"numberField\":42,\"stringField\":\"test\\u00E9\"}", result);
        } finally {
            jsonGenerator.close();
        }
    }
}