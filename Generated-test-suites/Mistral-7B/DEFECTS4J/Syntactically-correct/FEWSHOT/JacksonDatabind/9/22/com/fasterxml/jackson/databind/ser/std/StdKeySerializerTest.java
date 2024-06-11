package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.SerializationTest.getTestInstance;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.json.JsonSerializer;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.jsonschema.SchemaPrinter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = mapper.getSerializerProvider().findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        Date date = new Date();
        keySerializer.serialize(date, gen, new DefaultSerializerProvider());
        gen.flush();
    }

    @Test
    public void testSerializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = mapper.getSerializerProvider().findValueSerializer(String.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        String str = "test";
        keySerializer.serialize(str, gen, new DefaultSerializerProvider());
        gen.flush();
    }

    @Test
    public void testSerializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = mapper.getSerializerProvider().findValueSerializer(Object.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        Object obj = new Object() {
            @Override
            public String toString() {
                return "test_obj";
            }
        };
        keySerializer.serialize(obj, gen, new DefaultSerializerProvider());
        gen.flush();
    }

    @Test
    public void testGetSchema() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonNode schema = keySerializer.getSchema(provider, Object.class);
        JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator();
        SchemaPrinter schemaPrinter = schemaGenerator.printPretty(schema);
        System.out.println(schemaPrinter);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFormatVisitor();
        keySerializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testSerializeMapKeyWithStringValues() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> keySerializer = mapper.getSerializerProvider().findValueSerializer(Object.class);

        Map<String, Integer> map = new java.util.HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        JsonNode tree = mapper.readTree(mapper.writeValueAsString(map));
        JsonNode root = tree.get("_");

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        keySerializer.serialize(root, gen, new DefaultSerializerProvider());
        gen.flush();
    }

    @Test
    public void testSerializeMapKeyWithDateValues() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> keySerializer = mapper.getSerializerProvider().findValueSerializer(Object.class);

        Map<String, Date> map = new java.util.HashMap<>();
        map.put("one", new Date());
        map.put("two", new Date());
        map.put("three", new Date());

        JsonNode tree = mapper.readTree(mapper.writeValueAsString(map));
        JsonNode root = tree.get("_");

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        keySerializer.serialize(root, gen, new DefaultSerializerProvider());
        gen.flush();
    }

    @Test
    public void testSerializeMapKeyWithCustomClassValues() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> keySerializer = mapper.getSerializerProvider().findValueSerializer(Object.class);

        Map<String, MyCustomClass> map = new java.util.HashMap<>();
        map.put("one", getTestInstance(MyCustomClass.class));
        map.put("two", getTestInstance(MyCustomClass.class));
        map.put("three", getTestInstance(MyCustomClass.class));

        JsonNode tree = mapper.readTree(mapper.writeValueAsString(map));
        JsonNode root = tree.get("_");

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        keySerializer.serialize(root, gen, new DefaultSerializerProvider());
        gen.flush();
    }
}

class MyCustomClass {
    private int id;

    public MyCustomClass() {
    }

    public MyCustomClass(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MyCustomClass{" +
                "id=" + id +
                '}';
    }
}