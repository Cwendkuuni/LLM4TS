import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] expectedTypes = new Class<?>[] {
            File.class,
            URL.class,
            URI.class,
            Class.class,
            JavaType.class,
            Currency.class,
            Pattern.class,
            Locale.class,
            Charset.class,
            TimeZone.class,
            InetAddress.class,
            InetSocketAddress.class,
        };

        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }
    
    @Test
    public void testFindDeserializer() {
        assertNull(FromStringDeserializer.findDeserializer(Object.class));
        assertNotNull(FromStringDeserializer.findDeserializer(File.class));
        assertNotNull(FromStringDeserializer.findDeserializer(URL.class));
        assertNotNull(FromStringDeserializer.findDeserializer(URI.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Class.class));
        assertNotNull(FromStringDeserializer.findDeserializer(JavaType.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Currency.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Pattern.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Locale.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Charset.class));
        assertNotNull(FromStringDeserializer.findDeserializer(TimeZone.class));
        assertNotNull(FromStringDeserializer.findDeserializer(InetAddress.class));
        assertNotNull(FromStringDeserializer.findDeserializer(InetSocketAddress.class));
    }
    
    @Test
    public void testDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException
            {
                return value;
            }
        };

        try {
            deserializer.deserialize(null, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Unexpected exception message", "No current token", e.getMessage());
        }

        String input = "testValue";
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            assertEquals("Unexpected deserialized value", input, deserializer.deserialize(new JsonParser(null), context));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
    
    @Test
    public void testDeserializeWithEmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException
            {
                return value;
            }

            @Override
            protected Object _deserializeFromEmptyString() throws IOException {
                return "empty";
            }
        };


        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        try {
            assertNull("Unexpected deserialized value", deserializer.deserialize(parser, context));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
        
        try {
            assertEquals("Unexpected deserialized value", "empty", deserializer.deserialize(parser, context));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
    
    @Test
    public void testDeserializeEmbedded() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0) {
            @Override
            protected Object _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException
            {
                return ob;
            }
        };

        try {
            deserializer.deserializeEmbedded(null, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Unexpected exception message", "Cannot deserialize embedded object of type null", e.getMessage());
        }

        Object input = new Object();
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            assertEquals("Unexpected deserialized value", input, deserializer.deserializeEmbedded(input, context));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
    
    @Test
    public void testDeserializeFromInvalidString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException
            {
                throw new IllegalArgumentException("Invalid value: " + value);
            }
        };

        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected exception message", "not a valid textual representation, problem: Invalid value: null", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
        
        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected exception message", "not a valid textual representation, problem: Invalid value: test", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

}