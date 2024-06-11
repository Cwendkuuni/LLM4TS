package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;

@SuppressWarnings("unchecked")
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization and deserialization of a simple Collection
        List<Integer> list = Arrays.asList(1, 2, 3);

        String json = mapper.writeValueAsString(list);
        List<Integer> deserializedList = mapper.readValue(json, Collection.class);
        assertEquals(list, deserializedList);

        // Test serialization and deserialization of a Collection with nested objects
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        static class MyClass {
            private int id;

            public MyClass() {}

            public MyClass(int id) {
                this.id = id;
            }

            public int getId() {
                return id;
            }
        }

        List<MyClass> myList = Arrays.asList(new MyClass(1), new MyClass(2), new MyClass(3));

        json = mapper.writeValueAsString(myList);
        List<MyClass> deserializedMyList = mapper.readValue(json, Collection.class);
        assertEquals(myList, deserializedMyList);

        // Test deserialization of a Collection with a custom deserializer
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return value.toUpperCase();
            }
        };

        @JsonDeserialize(using = stringDeserializer.getClass())
        static class MyClassWithCustomDeserializer {
            private String name;

            public String getName() {
                return name;
            }
        }

        List<MyClassWithCustomDeserializer> myListWithCustomDeserializer = Arrays.asList(
                new MyClassWithCustomDeserializer(), new MyClassWithCustomDeserializer());

        json = mapper.writeValueAsString(myListWithCustomDeserializer);
        List<MyClassWithCustomDeserializer> deserializedMyListWithCustomDeserializer = mapper.readValue(json, Collection.class);

        List<String> expectedNames = Arrays.asList("LIST", "LIST");
        List<String> actualNames = new ArrayList<>();
        for (MyClassWithCustomDeserializer myClass : deserializedMyListWithCustomDeserializer) {
            actualNames.add(myClass.getName());
        }

        assertEquals(expectedNames, actualNames);

        // Test deserialization of a Collection with a type deserializer
        @JsonDeserialize(using = MyClassWithCustomTypeDeserializer.class)
        static class MyClassWithCustomTypeDeserializer extends StdDeserializer<MyClass> {
            @Override
            public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String idStr = p.getValueAsString();
                return new MyClass(Integer.parseInt(idStr));
            }
        }

        List<MyClass> myListWithCustomTypeDeserializer = Arrays.asList(new MyClass(1), new MyClass(2), new MyClass(3));

        json = mapper.writeValueAsString(myListWithCustomTypeDeserializer);
        List<MyClass> deserializedMyListWithCustomTypeDeserializer = mapper.readValue(json, new TypeReference<Collection<MyClass>>() {});

        assertEquals(myListWithCustomTypeDeserializer, deserializedMyListWithCustomTypeDeserializer);
    }
}

class MyClass {
    private int id;

    public MyClass() {}

    public MyClass(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}