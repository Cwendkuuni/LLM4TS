package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToByteArray;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdClassIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSubtypeResolverTest {

    @JsonTypeName("Foo")
    static class Foo {
        int x;
    }

    @JsonTypeName("Bar")
    @JsonSubTypes({ @JsonSubTypes.Type(value = Bar1.class, name = "Bar1"),
            @JsonSubTypes.Type(value = Bar2.class, name = "Bar2") })
    static class Bar extends Foo {
        String y;

        public Bar() {}

        public Bar(int x, String y) {
            this.x = x;
            this.y = y;
        }

        @JsonTypeName("Bar1")
        static class Bar1 extends Bar {
            long z;
        }

        @JsonTypeName("Bar2")
        static class Bar2 extends Bar {
            short z;
        }
    }

    static class TestObject {
        Foo f;

        TestObject(Foo f) {
            this.f = f;
        }
    }

    static class TestObjectSerializer extends StdSerializer<TestObject> {
        TestObjectSerializer() {
            this(null);
        }

        TestObjectSerializer(Class<TestObject> t) {
            super(t);
        }

        @Override
        public void serialize(TestObject testObject, JsonGenerator jsonGenerator, SerializerProvider serializers)
                throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("f");
            serializers.findValueSerializer(Foo.class).serialize(testObject.f, jsonGenerator, serializers);
            jsonGenerator.writeEndObject();
        }
    }

    @Test
    public void testCollectionAndResolveSubtypesByClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(INDENT_OUTPUT, true);
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        mapper.setSubtypeResolver(subtypeResolver);

        MapperConfig<?> config = mapper.getConfig();
        JavaType javaType = mapper.getTypeFactory().constructType(TestObject.class);
        ObjectNode rootNode = mapper.readValue(TestObject.class.getResourceAsStream("/testObject.json"), javaType);
        TestObject testObject = mapper.readValue(rootNode, javaType);
        assertEquals(new Foo(), testObject.f);

        mapper.writerWithView(StdClassIdResolver.instance).writeValueAsBytes(new TestObject(new Bar1(1, "foo1")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(mapper.writerWithView(StdClassIdResolver.instance)
                .writeValueAsBytes(new TestObject(new Bar1(1, "foo1"))));
        TestObject testObject2 = mapper.readValue(inputStream, javaType);
        assertEquals(new Bar1(1, "foo1"), testObject2.f);

        mapper.writerWithView(StdClassIdResolver.instance).writeValueAsBytes(new TestObject(new Bar2(1, "foo2")));
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(mapper.writerWithView(StdClassIdResolver.instance)
                .writeValueAsBytes(new TestObject(new Bar2(1, "foo2"))));
        TestObject testObject3 = mapper.readValue(inputStream2, javaType);
        assertEquals(new Bar2(1, "foo2"), testObject3.f);

        mapper.writerWithView(StdClassIdResolver.instance).writeValueAsBytes(new TestObject(new Foo()));
        ByteArrayInputStream inputStream3 = new ByteArrayInputStream(mapper.writerWithView(StdClassIdResolver.instance)
                .writeValueAsBytes(new TestObject(new Foo())));
        TestObject testObject4 = mapper.readValue(inputStream3, javaType);
        assertEquals(new Foo(), testObject4.f);

        mapper.writerWithView(StdClassIdResolver.instance).writeValueAsBytes(new TestObject(null));
        ByteArrayInputStream inputStream4 = new ByteArrayInputStream(mapper.writerWithView(StdClassIdResolver.instance)
                .writeValueAsBytes(new TestObject(null)));
        TestObject testObject5 = mapper.readValue(inputStream4, javaType);
        assertNull(testObject5.f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, new TestObject(new Foo()));
        byte[] bytes = outputStream.toByteArray();
        TestObject testObject6 = mapper.readValue(new ByteArrayInputStream(bytes), javaType);
        assertEquals(new Foo(), testObject6.f);

    }

    @Test
    public void testCollectionAndResolveSubtypesByTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(INDENT_OUTPUT, true);
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        mapper.setSubtypeResolver(subtypeResolver);

        MapperConfig<?> config = mapper.getConfig();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("f", Object.class.getName());
        JsonNode node = mapper.treeToNodes(new ObjectNode(ObjectMapper.Factory.instance.getObjectMapper().getDefaultSerializer()),
                rootNode);
        ObjectNode objectNode = (ObjectNode) node.get(0);
        objectNode.put("f", new Bar1(1, "foo1").getClass().getName());
        rootNode.set("o", objectNode);
        ObjectNode rootNode2 = mapper.createObjectNode();
        rootNode2.put("f", Object.class.getName());
        JsonNode node2 = mapper.treeToNodes(new ObjectNode(ObjectMapper.Factory.instance.getObjectMapper().getDefaultSerializer()),
                rootNode2);
        ObjectNode objectNode2 = (ObjectNode) node2.get(0);
        objectNode2.put("f", new Bar2(1, "foo2").getClass().getName());
        rootNode2.set("o2", objectNode2);
        ObjectNode rootNode3 = mapper.createObjectNode();
        rootNode3.put("f", Object.class.getName());
        JsonNode node3 = mapper.treeToNodes(new ObjectNode(ObjectMapper.Factory.instance.getObjectMapper().getDefaultSerializer()),
                rootNode3);
        ObjectNode objectNode3 = (ObjectNode) node3.get(0);
        objectNode3.put("f", Foo.class.getName());
        rootNode3.set("o3", objectNode3);

        ObjectNode rootNode4 = mapper.createObjectNode();
        rootNode4.put("f", Object.class.getName());
        JsonNode node4 = mapper.treeToNodes(new ObjectNode(ObjectMapper.Factory.instance.getObjectMapper().getDefaultSerializer()),
                rootNode4);
        ObjectNode objectNode4 = (ObjectNode) node4.get(0);
        objectNode4.put("f", null);
        rootNode4.set("o4", objectNode4);

        ObjectNode rootNode5 = mapper.createObjectNode();
        rootNode5.put("f", new Foo().getClass().getName());
        JsonNode node5 = mapper.treeToNodes(new ObjectNode(ObjectMapper.Factory.instance.getObjectMapper().getDefaultSerializer()),
                rootNode5);
        ObjectNode objectNode5 = (ObjectNode) node5.get(0);
        rootNode5.set("o5", objectNode5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, rootNode);
        byte[] bytes1 = outputStream.toByteArray();
        ObjectNode rootNode6 = mapper.readValue(new ByteArrayInputStream(bytes1), ObjectNode.class);
        TestObject testObject1 = mapper.treeToValue(rootNode6.get("o"), TestObject.class);
        assertEquals(new Bar1(1, "foo1"), testObject1.f);

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        mapper.writeValue(outputStream2, rootNode2);
        byte[] bytes2 = outputStream2.toByteArray();
        ObjectNode rootNode7 = mapper.readValue(new ByteArrayInputStream(bytes2), ObjectNode.class);
        TestObject testObject2 = mapper.treeToValue(rootNode7.get("o2"), TestObject.class);
        assertEquals(new Bar2(1, "foo2"), testObject2.f);

        ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        mapper.writeValue(outputStream3, rootNode3);
        byte[] bytes3 = outputStream3.toByteArray();
        ObjectNode rootNode8 = mapper.readValue(new ByteArrayInputStream(bytes3), ObjectNode.class);
        TestObject testObject3 = mapper.treeToValue(rootNode8.get("o3"), TestObject.class);
        assertEquals(new Foo(), testObject3.f);

        ByteArrayOutputStream outputStream4 = new ByteArrayOutputStream();
        mapper.writeValue(outputStream4, rootNode4);
        byte[] bytes4 = outputStream4.toByteArray();
        ObjectNode rootNode9 = mapper.readValue(new ByteArrayInputStream(bytes4), ObjectNode.class);
        TestObject testObject4 = mapper.treeToValue(rootNode9.get("o4"), TestObject.class);
        assertNull(testObject4.f);

        ByteArrayOutputStream outputStream5 = new ByteArrayOutputStream();
        mapper.writeValue(outputStream5, rootNode5);
        byte[] bytes5 = outputStream5.toByteArray();
        ObjectNode rootNode10 = mapper.readValue(new ByteArrayInputStream(bytes5), ObjectNode.class);
        TestObject testObject5 = mapper.treeToValue(rootNode10.get("o5"), TestObject.class);
        assertEquals(new Foo(), testObject5.f);

    }

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> rawBase = Foo.class;
        subtypeResolver.registerSubtypes(new NamedType(Bar1.class));
        List<NamedType> registeredSubtypes = new ArrayList<>(subtypeResolver._registeredSubtypes);
        assertTrue(registeredSubtypes.contains(new NamedType(Bar1.class)));
        assertFalse(registeredSubtypes.contains(new NamedType(Bar2.class)));

        subtypeResolver.registerSubtypes(new NamedType(Bar2.class));
        registeredSubtypes = new ArrayList<>(subtypeResolver._registeredSubtypes);
        assertTrue(registeredSubtypes.contains(new NamedType(Bar1.class)));
        assertTrue(registeredSubtypes.contains(new NamedType(Bar2.class)));

        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        registeredSubtypes = new ArrayList<>(subtypeResolver._registeredSubtypes);
        assertTrue(registeredSubtypes.contains(new NamedType(Bar1.class)));
        assertTrue(registeredSubtypes.contains(new NamedType(Bar2.class)));
        assertTrue(registeredSubtypes.contains(new NamedType(Foo.class)));
    }

    @Test
    public void testRegisterSubtypesClasses() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> rawBase = Foo.class;
        subtypeResolver.registerSubtypes(Foo.class.getPackage().getName() + ".Bar1");
        List<NamedType> registeredSubtypes = new ArrayList<>(subtypeResolver._registeredSubtypes);
        assertTrue(registeredSubtypes.contains(new NamedType(Class.forName(rawBase.getPackage().getName() + ".Bar1"))));
        assertFalse(registeredSubtypes.contains(new NamedType(Class.forName(rawBase.getPackage().getName() + ".Bar2"))));

        subtypeResolver.registerSubtypes(Foo.class.getPackage().getName() + ".Bar2");
        registeredSubtypes = new ArrayList<>(subtypeResolver._registeredSubtypes);
        assertTrue(registeredSubtypes.contains(new NamedType(Class.forName(rawBase.getPackage().getName() + ".Bar1"))));
        assertTrue(registeredSubtypes.contains(new NamedType(Class.forName(rawBase.getPackage().getName() + ".Bar2"))));

        subtypeResolver.registerSubtypes(Foo.class);
        registeredSubtypes = new ArrayList<>(subtypeResolver._registeredSubtypes);
        assertTrue(registeredSubtypes.contains(new NamedType(Class.forName(rawBase.getPackage().getName() + ".Bar1"))));
        assertTrue(registeredSubtypes.contains(new NamedType(Class.forName(rawBase.getPackage().getName() + ".Bar2"))));
        assertTrue(registeredSubtypes.contains(new NamedType(rawBase)));
    }

}