package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonToken;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {
    @Test
    public void testStdDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer1 = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("test.txt"), deserializer1._deserialize("test.txt", null));

        FromStringDeserializer.Std deserializer2 = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("http://example.com"), deserializer2._deserialize("http://example.com", null));

        FromStringDeserializer.Std deserializer3 = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("http://example.com"), deserializer3._deserialize("http://example.com", null));

        FromStringDeserializer.Std deserializer4 = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(String.class, deserializer4._deserialize("java.lang.String", null));

        FromStringDeserializer.Std deserializer5 = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), deserializer5._deserialize("USD", null));

        FromStringDeserializer.Std deserializer6 = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("[a-zA-Z]+"), deserializer6._deserialize("[a-zA-Z]+", null));

        FromStringDeserializer.Std deserializer7 = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(new Locale("en"), deserializer7._deserialize("en", null));

        FromStringDeserializer.Std deserializer8 = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), deserializer8._deserialize("UTF-8", null));

        FromStringDeserializer.Std deserializer9 = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("GMT"), deserializer9._deserialize("GMT", null));

        FromStringDeserializer.Std deserializer10 = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("localhost"), deserializer10._deserialize("localhost", null));

        FromStringDeserializer.Std deserializer11 = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer11._deserialize("localhost:8080", null));
        assertEquals(new InetSocketAddress("[::1]", 8080), deserializer11._deserialize("[::1]:8080", null));
    }

    @Test
    public void testDeserialize() throws IOException {
        class TestDeserializer extends FromStringDeserializer<Object> {
            TestDeserializer(Class<?> vc) {
                super(vc);
            }

            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return "test" + value;
            }
        }

        JsonParser jsonParser = new JsonParser() {
            public ObjectCodec getCodec() {
                return null;
            }

            public void setCodec(ObjectCodec c) {
            }

            public Version version() {
                return null;
            }

            public Object getInputSource() {
                return null;
            }

            public boolean isClosed() {
                return false;
            }

            public void close() throws IOException {
            }

            public JsonToken nextToken() throws IOException {
                return null;
            }

            public JsonToken nextValue() throws IOException {
                return null;
            }

            public JsonParser skipChildren() throws IOException {
                return null;
            }

            public JsonToken getCurrentToken() {
                return null;
            }

            public int getCurrentTokenId() {
                return 0;
            }

            public boolean hasCurrentToken() {
                return false;
            }

            public boolean hasTokenId(int id) {
                return false;
            }

            public String getCurrentName() {
                return null;
            }

            public JsonLocation getCurrentLocation() {
                return null;
            }

            public JsonStreamContext getParsingContext() {
                return null;
            }

            public boolean isExpectedStartArrayToken() {
                return false;
            }

            public boolean isExpectedStartObjectToken() {
                return false;
            }

            public boolean isNaN() throws IOException {
                return false;
            }

            public Number getNumberValue() throws IOException {
                return null;
            }

            public JsonParser.NumberType getNumberType() throws IOException {
                return null;
            }

            public int getIntValue() throws IOException {
                return 0;
            }

            public long getLongValue() throws IOException {
                return 0;
            }

            public BigInteger getBigIntegerValue() throws IOException {
                return null;
            }

            public float getFloatValue() throws IOException {
                return 0;
            }

            public double getDoubleValue() throws IOException {
                return 0;
            }

            public BigDecimal getDecimalValue() throws IOException {
                return null;
            }

            public String getText() throws IOException {
                return "test";
            }

            public char[] getTextCharacters() throws IOException {
                return new char[]{};
            }

            public int getTextLength() throws IOException {
                return 0;
            }

            public int getTextOffset() throws IOException {
                return 0;
            }

            public boolean hasTextCharacters() {
                return false;
            }
        };

        TestDeserializer testDeserializer = new TestDeserializer(Object.class);
        assertEquals("test", testDeserializer.deserialize(jsonParser, null));

    }
}