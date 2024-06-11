package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

/**
 * Basic deserializer that can take JSON "Array" structure and construct a
 * {@link java.util.Collection} instance, with typed contents.
 */
@JacksonStdImpl
public class CollectionDeserializerTest {

    @JsonFormat(features = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    static class TestBean {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    @Test
    public void testCollectionDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ValueDeserializer deserializer = mapper.findValueDeserializer(javaType, null);
        assertEquals(List.class, deserializer.getDeserializationType());

        // Test empty array
        String emptyJson = "[]";
        List<String> emptyList = deserializer.deserialize(mapper.readTree(emptyJson), null, javaType);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        // Test single value
        String singleJson = "[\"foo\"]";
        List<String> singleList = deserializer.deserialize(mapper.readTree(singleJson), null, javaType);
        assertNotNull(singleList);
        assertEquals(1, singleList.size());
        assertEquals("foo", singleList.get(0));

        // Test multi-value
        String multiJson = "[\"foo\", \"bar\", \"baz\"]";
        List<String> multiList = deserializer.deserialize(mapper.readTree(multiJson), null, javaType);
        assertNotNull(multiList);
        assertEquals(3, multiList.size());
        assertEquals("foo", multiList.get(0));
        assertEquals("bar", multiList.get(1));
        assertEquals("baz", multiList.get(2));

        // Test deserializing to a bean with a list field
        TestBean testBean = new TestBean();
        String beanJson = "[\"foo\", \"bar\", \"baz\"]";
        ObjectNode beanNode = mapper.readValue(beanJson, ObjectNode.class);
        deserializer.deserializeAndBind(beanNode, testBean, null, javaType);
        assertEquals(3, testBean.getList().size());
        assertEquals("foo", testBean.getList().get(0));
        assertEquals("bar", testBean.getList().get(1));
        assertEquals("baz", testBean.getList().get(2));
    }

    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ValueDeserializer collectionDeserializer = mapper.findValueDeserializer(javaType, null);

        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class, null);
        ValueDeserializer delegateDeserializer = new StdDelegatingDeserializer<>(stringDeserializer);
        ValueDeserializer deserializer = new CollectionDeserializer.Builder<List<String>>(
                javaType,
                null,
                null,
                null,
                null,
                null
        ).withDelegateDeserializer(delegateDeserializer).build();

        assertEquals(List.class, deserializer.getDeserializationType());

        // Test empty array
        String emptyJson = "[]";
        List<String> emptyList = deserializer.deserialize(mapper.readTree(emptyJson), null, javaType);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        // Test single value
        String singleJson = "[\"foo\"]";
        List<String> singleList = deserializer.deserialize(mapper.readTree(singleJson), null, javaType);
        assertNotNull(singleList);
        assertEquals(1, singleList.size());
        assertEquals("foo", singleList.get(0));

        // Test multi-value
        String multiJson = "[\"foo\", \"bar\", \"baz\"]";
        List<String> multiList = deserializer.deserialize(mapper.readTree(multiJson), null, javaType);
        assertNotNull(multiList);
        assertEquals(3, multiList.size());
        assertEquals("foo", multiList.get(0));
        assertEquals("bar", multiList.get(1));
        assertEquals("baz", multiList.get(2));
    }

    @Test
    public void testCollectionDeserializerWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ValueDeserializer deserializer = mapper.findValueDeserializer(javaType, null);

        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        deserializer = new CollectionDeserializer.Builder<List<String>>(
                javaType,
                stringDeserializer,
                null,
                null,
                null,
                null
        ).build();

        assertEquals(List.class, deserializer.getDeserializationType());

        // Test empty array
        String emptyJson = "[]";
        List<String> emptyList = deserializer.deserialize(mapper.readTree(emptyJson), null, javaType);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        // Test single value
        String singleJson = "[\"foo\"]";
        List<String> singleList = deserializer.deserialize(mapper.readTree(singleJson), null, javaType);
        assertNotNull(singleList);
        assertEquals(1, singleList.size());
        assertEquals("foo", singleList.get(0));

        // Test multi-value
        String multiJson = "[\"foo\", \"bar\", \"baz\"]";
        List<String> multiList = deserializer.deserialize(mapper.readTree(multiJson), null, javaType);
        assertNotNull(multiList);
        assertEquals(3, multiList.size());
        assertEquals("foo", multiList.get(0));
        assertEquals("bar", multiList.get(1));
        assertEquals("baz", multiList.get(2));
    }

    @Test
    public void testCollectionDeserializerWithValueDeserializerAndTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ValueDeserializer deserializer = mapper.findValueDeserializer(javaType, null);

        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeDeserializer stringTypeDeserializer = new StringTypeDeserializer();
        deserializer = new CollectionDeserializer.Builder<List<String>>(
                javaType,
                stringDeserializer,
                stringTypeDeserializer,
                null,
                null,
                null
        ).build();

        assertEquals(List.class, deserializer.getDeserializationType());

        // Test empty array
        String emptyJson = "[]";
        List<String> emptyList = deserializer.deserialize(mapper.readTree(emptyJson), null, javaType);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        // Test single value
        String singleJson = "[\"foo\"]";
        List<String> singleList = deserializer.deserialize(mapper.readTree(singleJson), null, javaType);
        assertNotNull(singleList);
        assertEquals(1, singleList.size());
        assertEquals("foo", singleList.get(0));

        // Test multi-value
        String multiJson = "[{\"id\":1,\"name\":\"foo\"},{\"id\":2,\"name\":\"bar\"},{\"id\":3,\"name\":\"baz\"}]";
        List<Map<String, Object>> multiList = deserializer.deserialize(mapper.readTree(multiJson), null, javaType);
        assertNotNull(multiList);
        assertEquals(3, multiList.size());
        assertEquals(1, multiList.get(0).size());
        assertEquals("foo", multiList.get(0).get("name"));
        assertEquals(2, multiList.get(1).size());
        assertEquals("bar", multiList.get(1).get("name"));
        assertEquals(3, multiList.get(2).size());
        assertEquals("baz", multiList.get(2).get("name"));
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class StringTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}