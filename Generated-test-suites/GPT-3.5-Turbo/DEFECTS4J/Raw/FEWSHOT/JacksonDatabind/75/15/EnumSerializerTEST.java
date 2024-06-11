package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {
    private EnumSerializer enumSerializer;

    @Before
    public void setUp() {
        EnumValues enumValues = new EnumValues() {
            @Override
            public Enum<?>[] enums() {
                return new Enum<?>[]{
                        MyEnum.ENUM1,
                        MyEnum.ENUM2,
                        MyEnum.ENUM3,
                };
            }

            @Override
            protected Iterable<SerializableString> _values() {
                return new ArrayList<SerializableString>() {{
                    add(new MySerializableString("enum1"));
                    add(new MySerializableString("enum2"));
                    add(new MySerializableString("enum3"));
                }};
            }

            @Override
            protected Iterable<String> _names() {
                return new ArrayList<String>() {{
                    add("ENUM1");
                    add("ENUM2");
                    add("ENUM3");
                }};
            }

            @Override
            protected Iterable<String> _unquotedEnums() {
                return new ArrayList<String>() {{
                    add("enum1");
                    add("enum2");
                    add("enum3");
                }};
            }
        };
        
        enumSerializer = new EnumSerializer(enumValues, true);
    }

    @Test
    public void testSerializeWithOrdinal() throws IOException {
        MyEnum myEnum = MyEnum.ENUM1;

        JsonGenerator jsonGenerator = new MyJsonGenerator();
        SerializerProvider serializerProvider = new MySerializerProvider();

        enumSerializer.serialize(myEnum, jsonGenerator, serializerProvider);

        assertEquals("0", ((MyJsonGenerator) jsonGenerator).getLastValue());
    }

    @Test
    public void testSerializeWithString() throws IOException {
        MyEnum myEnum = MyEnum.ENUM1;

        JsonGenerator jsonGenerator = new MyJsonGenerator();
        SerializerProvider serializerProvider = new MySerializerProvider();

        enumSerializer.serialize(myEnum, jsonGenerator, serializerProvider);

        assertEquals("enum1", ((MyJsonGenerator) jsonGenerator).getLastValue());
    }

    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = enumSerializer.getEnumValues();
        assertTrue(enumValues instanceof EnumValues);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializerProvider = new MySerializerProvider();
        Type typeHint = MyEnum.ENUM1.getClass();

        assertEquals(null, enumSerializer.getSchema(serializerProvider, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitorWithIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new MyJsonFormatVisitorWrapper();
        JavaType typeHint = MyEnum.ENUM1.getClass();

        enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        assertTrue(((MyJsonFormatVisitorWrapper) jsonFormatVisitorWrapper).isVisitIntFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new MyJsonFormatVisitorWrapper();
        JavaType typeHint = MyEnum.ENUM1.getClass();

        enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        assertTrue(((MyJsonFormatVisitorWrapper) jsonFormatVisitorWrapper).isVisitStringFormat());
    }

    class MyEnum {
        static final MyEnum ENUM1 = new MyEnum();
        static final MyEnum ENUM2 = new MyEnum();
        static final MyEnum ENUM3 = new MyEnum();
    }

    class MySerializableString implements SerializableString {
        private final String value;

        MySerializableString(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public int charLength() {
            return 0;
        }

        @Override
        public char[] asQuotedChars() {
            return new char[0];
        }

        @Override
        public byte[] asUnquotedUTF8() {
            return new byte[0];
        }

        @Override
        public byte[] asQuotedUTF8() {
            return new byte[0];
        }

        @Override
        public int appendQuotedUTF8(byte[] bytes, int offset) {
            return offset;
        }

        @Override
        public int appendQuoted(char[] buffer, int offset) {
            return offset;
        }

        @Override
        public int appendUnquotedUTF8(byte[] bytes, int offset) {
            return offset;
        }

        @Override
        public int appendUnquoted(char[] buffer, int offset) {
            return offset;
        }

        @Override
        public int writeQuotedUTF8(byte[] buffer, int offset) {
            return offset;
        }

        @Override
        public int writeUnquotedUTF8(byte[] buffer, int offset) {
            return offset;
        }

        @Override
        public int putQuotedUTF8(ByteBuffer buffer) {
            return 0;
        }

        @Override
        public int putUnquotedUTF8(ByteBuffer buffer) {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o instanceof MySerializableString &&
                    ((MySerializableString) o).getValue().equals(value));
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    class MyJsonGenerator extends JsonGenerator {
        private String lastValue;

        public String getLastValue() {
            return lastValue;
        }

        @Override
        public void writeString(String text) {
            lastValue = text;
        }

        // other methods...
    }

    class MySerializerProvider extends SerializerProvider {
        // overridden methods...
    }

    class MyJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper.Base {
        private boolean visitIntFormat;
        private boolean visitStringFormat;

        public boolean isVisitIntFormat() {
            return visitIntFormat;
        }

        public boolean isVisitStringFormat() {
            return visitStringFormat;
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType convertedType) {
            visitStringFormat = true;
            return null;
        }

        @Override
        public void visitIntFormat(JsonParser.NumberType format) {
            visitIntFormat = true;
        }

        // other methods...
    }
}