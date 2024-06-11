import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testDefaultConstructor() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertNull(deserializer._valueDeserializer);
        assertNull(deserializer._delegateDeserializer);
    }

    @Test
    public void testCustomConstructor() {
        JavaType collectionType = new TypeFactory().constructCollectionType(List.class, String.class);
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<String> valueDeserializer = new StdStringDeserializer();
        JsonDeserializer<Object> delegateDeserializer = new StdArrayDeserializer(Object[].class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, valueInstantiator, delegateDeserializer, valueDeserializer, null, null);
        assertEquals(collectionType, deserializer._containerType);
        assertEquals(valueDeserializer, deserializer._valueDeserializer);
        assertEquals(delegateDeserializer, deserializer._delegateDeserializer);
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertFalse(deserializer.isCachable());
    }

    @Test
    public void testCreateContextualNull() {
        DeserializationContext context = new TestContext();
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertNull(deserializer.createContextual(context, null));
    }

    @Test
    public void testCreateContextualValueDeserializer() {
        DeserializationContext context = new TestContext();
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<String> valueDeserializer = new StdStringDeserializer();
        JsonDeserializer<Object> delegateDeserializer = new StdArrayDeserializer(Object[].class);
        StringCollectionDeserializer expected = new StringCollectionDeserializer(null, null, delegateDeserializer, valueDeserializer, null, null);
        assertEquals(expected, deserializer.createContextual(context, new AnnotatedMethod("testMethod", String[].class)));
    }

    @Test
    public void testCreateContextualDelegateDeserializer() {
        DeserializationContext context = new TestContext();
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = new StdArrayDeserializer(Object[].class);
        JsonDeserializer<String> valueDeserializer = new StdStringDeserializer();
        StringCollectionDeserializer expected = new StringCollectionDeserializer(null, valueInstantiator, delegateDeserializer, null, null, null);
        assertEquals(expected, deserializer.createContextual(context, new AnnotatedMethod("testMethodWithDelegateDeserializer", String[].class)));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser p = factory.createParser("[{}]");
        deserializer.deserialize(p, new TestContext(), null);
    }

    @Test
    public void testDeserializeSingleValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser p = factory.createParser("[\"test\"]");
        Collection<String> result = deserializer.deserialize(p, new TestContext(), null);
        assertEquals(1, result.size());
        assertEquals("test", result.iterator().next());
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser p = factory.createParser("[null]");
        Collection<String> result = deserializer.deserialize(p, new TestContext(), null);
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser p = factory.createParser("[1]");
        deserializer.deserialize(p, new TestContext(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedToken() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser p = factory.createParser("[true]");
        deserializer.deserialize(p, new TestContext(), null);
    }

    @Test
    public void testDeserializeUsingCustomDeserializer() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser p = factory.createParser("[1, 2, 3]");
        JsonDeserializer<String> customDeserializer = new StdNumberDeserializer();
        Collection<String> result = deserializer.deserializeUsingCustom(p, new TestContext(), null, customDeserializer);
        assertEquals(3, result.size());
        assertEquals("1", result.iterator().next());
        assertEquals("2", result.iterator().next());
        assertEquals("3", result.iterator().next());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        TypeDeserializer typeDeserializer = new StdTypeDeserializer(String[].class);
        Collection<String> result = deserializer.deserializeWithType(factory.createParser("[\"test1\", \"test2\"]"), new TestContext(), typeDeserializer);
        assertEquals(2, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectFactory getFactory() {
            return new StandardSerializer.Base() {
                @Override
                public Object createValueInstance(Type type) {
                    return new ArrayList<>();
                }
            };
        }
    }
}