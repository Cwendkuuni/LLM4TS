
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdKeySerializerTest {

    private static final String DEFAULT_EXPECTED_RESULT = "\"test\"";
    private static final String DATE_EXPECTED_RESULT = "\"2022-01-01\"";
    private static final String NULL_EXPECTED_RESULT = "\"null\"";

    private StdKeySerializer serializer;
    private StringWriter writer;
    private JsonGenerator generator;
    private SerializerProvider provider;

    @Before
    public void setUp() throws IOException {
        serializer = new StdKeySerializer();
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        provider = new ObjectMapper().getSerializerProvider();
    }

    @Test
    public void serialize_StringValue_ShouldWriteString() throws IOException {
        String value = "test";

        serializer.serialize(value, generator, provider);

        generator.flush();
        assertEquals(DEFAULT_EXPECTED_RESULT, writer.toString());
    }

    @Test
    public void serialize_DateValue_ShouldSerializeAsDate() throws IOException {
        Date value = new Date(1640995200000L); // 2022-01-01

        serializer.serialize(value, generator, provider);

        generator.flush();
        assertEquals(DATE_EXPECTED_RESULT, writer.toString());
    }

    @Test
    public void serialize_NullValue_ShouldWriteNull() throws IOException {
        String value = null;

        serializer.serialize(value, generator, provider);

        generator.flush();
        assertEquals(NULL_EXPECTED_RESULT, writer.toString());
    }

    @Test
    public void getSchema_ShouldReturnStringSchemaNode() throws IOException {
        assertNotNull(serializer.getSchema(provider, null));
    }

    @Test
    public void acceptJsonFormatVisitor_ShouldExpectStringFormat() throws IOException {
        TestJsonFormatVisitorWrapper visitor = new TestJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);

        assertEquals(TestJsonFormatVisitorWrapper.STRING_FORMAT_EXPECTED, visitor.getVisitedFormat());
    }

    private static class TestJsonFormatVisitorWrapper implements com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper {
        public static final String STRING_FORMAT_EXPECTED = "Expected String format";
        private String visitedFormat;

        void setVisitedFormat(String visitedFormat) {
            this.visitedFormat = visitedFormat;
        }

        String getVisitedFormat() {
            return visitedFormat;
        }

        @Override
        public boolean expectsStringFormat() {
            return true;
        }

        @Override
        public com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor expectStringFormat(com.fasterxml.jackson.databind.JavaType convertedType) {
            setVisitedFormat(STRING_FORMAT_EXPECTED);
            return new JsonStringFormatVisitor() {
                @Override
                public void format(JsonValueFormat format) {

                }

                @Override
                public void enumTypes(JavaType enumType) {

                }

                @Override
                public void pattern(String pattern) {

                }

                @Override
                public void minLength(Integer minLength) {

                }

                @Override
                public void maxLength(Integer maxLength) {

                }
            };
        }
    }
}
