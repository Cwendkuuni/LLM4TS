import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    // Test for basic functionality of CollectionDeserializer with Object elements
    @Test
    public void testCollectionDeserializerWithObjectElements() throws IOException {
        String json = "[1, 2, 3, 4, 5]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(5, collection.size());
        assertTrue(collection instanceof ArrayList);
        for (Object obj : collection) {
            assertEquals(Integer.class, obj.getClass());
            assertEquals((Integer) obj, (Integer) (int) obj);
        }
    }

    // Test for CollectionDeserializer with custom class elements
    @Test
    public void testCollectionDeserializerWithCustomClassElements() throws IOException {
        String json = "[{\"name\":\"Alice\"},{\"name\":\"Bob\"},{\"name\":\"Charlie\"}]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<MyClass> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, collection.size());
        for (MyClass obj : collection) {
            assertEquals("Alice", obj.getName());
            assertNotNull(obj.getId());
        }
    }

    // Test for CollectionDeserializer when JSON contains null elements
    @Test
    public void testCollectionDeserializerWithNullElements() throws IOException {
        String json = "[null, 1, 2, null, 5]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(5, collection.size());
        assertTrue(collection instanceof ArrayList);
        for (Object obj : collection) {
            if (obj == null) {
                assertNull(obj);
            } else {
                assertEquals(Integer.class, obj.getClass());
                assertEquals((Integer) obj, (Integer) (int) obj);
            }
        }
    }

    // Test for CollectionDeserializer when JSON contains empty array
    @Test
    public void testCollectionDeserializerWithEmptyArray() throws IOException {
        String json = "[[]]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(0, collection.size());
        assertTrue(collection instanceof ArrayList);
    }

    // Test for CollectionDeserializer when JSON does not represent valid array
    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithInvalidJson() throws IOException {
        String json = "{\"name\":\"Alice\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, CollectionDeserializer.class);
    }

    // Test for CollectionDeserializer with single value wrapped as array
    @Test
    public void testCollectionDeserializerWithSingleValueWrappedAsArray() throws IOException {
        String json = "[5]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, collection.size());
        assertTrue(collection instanceof ArrayList);
        assertEquals(Integer.class, collection.getClass());
        assertEquals(5, collection.iterator().next());
    }

    // Test for CollectionDeserializer with custom TypeDeserializer for elements
    @Test
    public void testCollectionDeserializerWithCustomTypeDeserializer() throws IOException {
        String json = "[{\"name\":\"Alice\"},{\"name\":\"Bob\"},{\"name\":\"Charlie\"}]";
        ObjectMapper mapper = new ObjectMapper();
        MyClassDeserializer myClassDeserializer = new MyClassDeserializer();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(MyClass.class, myClassDeserializer));
        Collection<MyClass> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, collection.size());
        for (MyClass obj : collection) {
            assertEquals("Alice", obj.getName());
            assertNotNull(obj.getId());
        }
    }

    // Test for CollectionDeserializer when JSON contains cyclic references
    @Test(expected = IOException.class)
    public void testCollectionDeserializerWithCyclicReferences()
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{ \"a\": { \"b\": { \"a\": this } } }]";
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_INPUT_FOR_PRIMITIVES, false);
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
    }

    // Test for CollectionDeserializer with UnresolvedForwardReference
    @Test
    public void testCollectionDeserializerWithUnresolvedForwardReference()
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{ \"$ref\": \"/A\" }, {}, { \"name\": \"Alice\" }]";
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, collection.size());
        assertTrue(collection instanceof ArrayList);
        assertFalse(collection.get(0) instanceof MapperAnnotatedClass);
        assertTrue(collection.get(1) instanceof MapperAnnotatedClass);
        assertTrue(collection.get(2) instanceof MapperAnnotatedClass);

        // Resolve the forward reference
        MapperAnnotatedClass obj1 = (MapperAnnotatedClass) collection.get(0);
        obj1.setName("Bob");
        mapper.writerWithRootValue(new ObjectWriter.DefaultPrettyPrinter()).writeValueAsString(obj1);

        // Verify the result
        Collection<Object> collection2 = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, collection2.size());
        assertTrue(collection2.get(0) instanceof MapperAnnotatedClass);
        assertEquals("Bob", ((MapperAnnotatedClass) collection2.get(0)).getName());
        assertTrue(collection2.get(1) instanceof MapperAnnotatedClass);
        assertTrue(collection2.get(2) instanceof MapperAnnotatedClass);
        assertEquals("Alice", ((MapperAnnotatedClass) collection2.get(2)).getName());
    }

    // Test for CollectionDeserializer with TypeDeserializer and JsonDeserializer
    @Test
    public void testCollectionDeserializerWithTypeDeserializerAndJsonDeserializer()
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\":\"Alice\"},{\"age\":42},{\"city\":\"New York\"}]";
        MyClassDeserializer myClassDeserializer = new MyClassDeserializer();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(MyClass.class, myClassDeserializer));
        mapper.registerDeserializer(String.class, new StringDeserializer());

        Collection<MyClass> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, collection.size());
        for (MyClass obj : collection) {
            if (obj instanceof MyClassWithStringName) {
                MyClassWithStringName myClassWithStringName = (MyClassWithStringName) obj;
                assertEquals("Alice", myClassWithStringName.getName());
                assertEquals("New York", myClassWithStringName.getCity());
            } else if (obj instanceof MyClassWithIntAge) {
                MyClassWithIntAge myClassWithIntAge = (MyClassWithIntAge) obj;
                assertEquals(42, myClassWithIntAge.getAge());
            }
        }
    }

    // Test for CollectionDeserializer when JSON contains empty object elements
    @Test
    public void testCollectionDeserializerWithEmptyObjectElements() throws IOException {
        String json = "[{},{},{}]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, collection.size());
        for (Object obj : collection) {
            assertTrue(obj instanceof Map);
            assertTrue(obj.getClass().isInstance(new HashMap<>()));
        }
    }
}

class MyClassDeserializer extends JsonDeserializer<MyClass> {
    @Override
    public MyClass deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        MyClass myClass = new MyClass();
        if (node.has("name")) {
            myClass.setName(node.get("name").asText());
        }
        if (node.has("id")) {
            myClass.setId(node.get("id").asInt());
        }
        return myClass;
    }
}

class StringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException {
        return parser.getText();
    }
}

class MapperAnnotatedClass {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JacksonStdImpl
    public static class Serializer extends JsonSerializer<MapperAnnotatedClass> {
        @Override
        public void serialize(MapperAnnotatedClass value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("name");
            gen.writeValue(value.getName());
            gen.writeFieldName("id");
            gen.writeValue(value.getId());
            gen.writeEndObject();
        }
    }

    @JacksonStdImpl
    public static class Deserializer extends JsonDeserializer<MapperAnnotatedClass> {
        @Override
        public MapperAnnotatedClass deserialize(JsonParser parser, DeserializationContext ctxt)
                throws IOException {
            MapperAnnotatedClass mapperAnnotatedClass = new MapperAnnotatedClass();
            MapperAnnotatedClass.Serializer serializer = new MapperAnnotatedClass.Serializer();
            JsonNode node = parser.getCodec().readTree(parser);
            mapperAnnotatedClass.setName(node.get("name").asText());
            mapperAnnotatedClass.setId(node.get("id").asInt());
            return mapperAnnotatedClass;
        }
    }
}

class MyClass {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class MyClassWithStringName {
    private String name;
    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

class MyClassWithIntAge {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}