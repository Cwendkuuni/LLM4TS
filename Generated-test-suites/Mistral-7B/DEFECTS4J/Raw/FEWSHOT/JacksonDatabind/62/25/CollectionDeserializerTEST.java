package org.apache.commons.cli.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException {
        JavaType javaType = new TypeFactory().constructCollectionType(List.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("[]");
        List<String> list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Collections.emptyList(), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\"]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Collections.singletonList("foo"), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[1, 2, 3]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList(1, 2, 3), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, 1.5, 4.5, true]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList("foo", null, 1.5, 4.5, true), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, 1, 2, 3, 4, 5]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList("foo", null, 1, 2, 3, 4, 5), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\"]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Collections.singletonList("foo"), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[null]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Collections.emptyList(), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[null, null, null]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Collections.emptyList(), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[null, null, null, null]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(Collections.singletonList(Collections.emptyList()), list);

        jsonParser = new ObjectMapper().getFactory().createParser("[1, 2, [3, 4], 5]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        List<Object> list2 = (List<Object>) list.get(2);
        assertEquals(Arrays.asList(new Integer(3), new Integer(4)), list2);

        jsonParser = new ObjectMapper().getFactory().createParser("[{\"name\": \"John\", \"age\": 30}, {\"name\": \"Jane\", \"age\": 25}]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        List<Object> listOfMaps = (List<Object>) list;
        assertEquals(2, listOfMaps.size());
        ObjectNode firstMap = (ObjectNode) listOfMaps.get(0);
        assertEquals("John", firstMap.get("name").asText());
        assertEquals(30, firstMap.get("age").asInt());
        ObjectNode secondMap = (ObjectNode) listOfMaps.get(1);
        assertEquals("Jane", secondMap.get("name").asText());
        assertEquals(25, secondMap.get("age").asInt());

        jsonParser = new ObjectMapper().getFactory().createParser("[1, 2, [3], [4, 5]]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        List<Object> list3 = (List<Object>) list.get(2);
        List<Object> list4 = (List<Object>) list.get(3);
        assertEquals(1, list3.size());
        assertEquals(new Integer(3), list3.get(0));
        assertEquals(2, list4.size());
        assertEquals(new Integer(4), list4.get(0));
        assertEquals(new Integer(5), list4.get(1));

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, [1], [1, 2, 3]]");
        list = collectionDeserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        List<Object> list5 = (List<Object>) list.get(2);
        List<Object> list6 = (List<Object>) list.get(3);
        assertEquals(1, list5.size());
        assertEquals(new Integer(1), list5.get(0));
        assertEquals(3, list6.size());
        assertEquals(new Integer(1), list6.get(0));
        assertEquals(new Integer(2), list6.get(1));
        assertEquals(new Integer(3), list6.get(2));
    }

    @Test
    public void testCollectionDeserializerContextual() throws IOException {
        JavaType javaType = new TypeFactory().constructCollectionType(LinkedHashSet.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("\"foo\"");
        LinkedHashSet<String> linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Collections.singleton("foo"), linkedHashSet);

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", \"bar\"]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Arrays.asList("foo", "bar"), new ArrayList<>(linkedHashSet));

        jsonParser = new ObjectMapper().getFactory().createParser("[1, 2, 3]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Collections.emptySet(), linkedHashSet);

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, 1.5, 4.5, true]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Arrays.asList("foo"), new ArrayList<>(linkedHashSet));

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, 1, 2, 3, 4, 5]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Arrays.asList("foo"), new ArrayList<>(linkedHashSet));

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, [3, 4], 5]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        LinkedHashSet<Object> linkedHashSet2 = (LinkedHashSet<Object>) linkedHashSet;
        LinkedHashSet<Object> linkedHashSet3 = new LinkedHashSet<>();
        linkedHashSet3.add(new Integer(3));
        linkedHashSet3.add(new Integer(4));
        assertEquals(linkedHashSet3, linkedHashSet2);

        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, null, null]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Collections.emptySet(), linkedHashSet);

        jsonParser = new ObjectMapper().getFactory().createParser("[null]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Collections.emptySet(), linkedHashSet);

        jsonParser = new ObjectMapper().getFactory().createParser("[null, null, null, null]");
        linkedHashSet = collectionDeserializer.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        assertEquals(Collections.emptySet(), linkedHashSet);

        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        JsonDeserializer<Integer> integerDeserializer = new SimpleIntDeserializer();
        TypeDeserializer intTypeDeserializer = new IntTypeDeserializer();
        jsonParser = new ObjectMapper().getFactory().createParser("[\"foo\", null, 1, 2, 3, 4, 5]");
        CollectionDeserializer collectionDeserializerWithContext = new CollectionDeserializer(javaType, stringDeserializer, intTypeDeserializer, null, null, null)
                .withResolved(integerDeserializer, stringDeserializer, intTypeDeserializer);
        linkedHashSet = collectionDeserializerWithContext.createContextual(new DeserializationContext(), null).deserialize(jsonParser, new DeserializationContext(), new LinkedHashSet<>());
        LinkedHashSet<Object> linkedHashSet4 = (LinkedHashSet<Object>) linkedHashSet;
        LinkedHashSet<Object> linkedHashSet5 = new LinkedHashSet<>();
        linkedHashSet5.add("foo");
        linkedHashSet5.add(new Integer(1));
        linkedHashSet5.add(new Integer(2));
        linkedHashSet5.add(new Integer(3));
        linkedHashSet5.add(new Integer(4));
        linkedHashSet5.add(new Integer(5));
        assertEquals(linkedHashSet5, linkedHashSet4);
    }
}

class StringDeserializer extends StdDeserializer<String> {
    public StringDeserializer() {
        this(null);
    }

    public StringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return jp.getValueAsString();
    }
}

class SimpleIntDeserializer extends StdDeserializer<Integer> {
    public SimpleIntDeserializer() {
        this(null);
    }

    public SimpleIntDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return jp.getValueAsInt();
    }
}

class IntTypeDeserializer extends StdTypeDeserializer<Integer> {
    public IntTypeDeserializer() {
        this(null);
    }

    public IntTypeDeserializer(Class<Integer> vc) {
        super(vc);
    }
}