
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new BigDecimal("123.45"), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new BigInteger("123456789"), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new Integer(123), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new Long(123456789), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new Double(12.34), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new Float(12.34), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new Byte("1"), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new Short("1"), generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }

    @Test
    public void testSerializeUnknownNumberType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        try {
            generator = new ObjectMapper().getFactory().createGenerator(System.out);
            serializer.serialize(new TestNumber() {
                @Override
                public String toString() {
                    return "10";
                }
            }, generator, provider);
            generator.flush();
        } catch (IOException e) {
            assertTrue(false);
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    assertTrue(false);
                }
            }
        }
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null;
        JsonNode schema = serializer.getSchema(provider, null); // Mocked typeHint
        assertTrue(schema.isObject());
        assertEquals("number", schema.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = null; // Mocked visitor
        JavaType typeHint = null; // Mocked typeHint
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            // Check that the visit methods were called
            // assertTrue(v2.visitIntFormatCalled());
            // assertTrue(v2.visitFloatFormatCalled());
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    // Test class to represent an unknown number type
    private abstract class TestNumber extends Number {
        // Implement abstract methods of Number
        public abstract int intValue();

        public abstract long longValue();

        public abstract float floatValue();

        public abstract double doubleValue();
    }
}
