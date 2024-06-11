package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGeneratorType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class DefaultDeserializationContextTest extends ObjectMapperTestBase {

    private DefaultDeserializationContext ctx;
    private DefaultSerializerProvider serializerProvider;

    @BeforeClass
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ctx = mapper.getDeserializationContext();
        serializerProvider = mapper.getSerializerProvider();
    }

    @Test
    public void testFindObjectId() throws IOException {
        // Test finding object id for simple object
        MyClass1 obj1 = new MyClass1(1);
        ObjectNode node1 = mapper.valueToTree(obj1);

        ObjectIdGenerator generator = ctx.constructorArgs()[1];
        ObjectIdResolver resolver = ctx.constructorArgs()[2];
        ObjectId id = generator.generateId(ctx, MyClass1.class, obj1);
        ReadableObjectId roid = ctx.findObjectId(id, generator, resolver);

        Assert.assertNotNull(roid);
        Assert.assertEquals(obj1.getId(), roid.getId());
        Assert.assertSame(resolver, roid.getResolver());

        // Test finding using SimpleObjectIdResolver
        MyClass1 obj2 = new MyClass1(2);
        ObjectNode node2 = mapper.valueToTree(obj2);
        ObjectId id2 = generator.generateId(ctx, MyClass1.class, obj2);
        ReadableObjectId roid2 = ctx.findObjectId(id2);

        Assert.assertNotNull(roid2);
        Assert.assertEquals(obj2.getId(), roid2.getId());
        Assert.assertNull(roid2.getResolver());

        // Test finding using custom ObjectIdResolver
        MyClass2 obj3 = new MyClass2();
        ObjectNode node3 = mapper.valueToTree(obj3);
        ObjectId id3 = generator.generateId(ctx, MyClass2.class, obj3);
        MyCustomObjectIdResolver resolver1 = new MyCustomObjectIdResolver();
        ReadableObjectId roid3 = ctx.findObjectId(id3, generator, resolver1);

        Assert.assertNotNull(roid3);
        Assert.assertEquals(obj3.getId(), roid3.getId());
        Assert.assertSame(resolver1, roid3.getResolver());

        // Test not finding object id
        ObjectNode node4 = mapper.valueToTree(new Object());
        ObjectId id4 = generator.generateId(ctx, Object.class, new Object());
        ReadableObjectId roid4 = ctx.findObjectId(id4);

        Assert.assertNull(roid4);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws IOException {
        MyClass1 obj1 = new MyClass1(1);
        ObjectNode node1 = mapper.valueToTree(obj1);
        ObjectId id1 = ctx.findObjectId(obj1, null, null).getId();

        // Test with resolved object ids
        List<ObjectNode> nodes = new ArrayList<>();
        nodes.add(node1);
        ObjectMapper mapper2 = new ObjectMapper();
        DefaultDeserializationContext ctx2 = mapper2.getDeserializationContext();
        ctx2.handleUnknownId(_ -> {
            if (id1.equals(_.getId())) {
                return new MyClass1(2);
            }
            throw new UnresolvedForwardReference("test");
        });
        MyClass1 obj2 = mapper2.readValue(nodes.stream().map(ObjectNode::toString).collect(Collectors.joining("\n")), MyClass1.class);

        Assert.assertEquals(obj1.getId(), obj2.getId());

        // Test with unresolved object ids
        List<ObjectNode> nodes2 = new ArrayList<>();
        nodes2.add(node1);
        ObjectMapper mapper3 = new ObjectMapper();
        DefaultDeserializationContext ctx3 = mapper3.getDeserializationContext();
        ctx3.handleUnknownId(_ -> {
            throw new UnresolvedForwardReference("test");
        });
        try {
            MyClass1 obj3 = mapper3.readValue(nodes2.stream().map(ObjectNode::toString).collect(Collectors.joining("\n")), MyClass1.class);
            Assert.fail();
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() throws IOException {
        // Test deserializer instance for simple class
        MyClass1 obj1 = new MyClass1(1);
        ObjectNode node1 = mapper.valueToTree(obj1);
        JsonDeserializer<MyClass1> deser1 = ctx.deserializerInstance(new AnnotatedClass(MyClass1.class), null);
        MyClass1 deserialized1 = mapper.readTree(node1, deser1.getClass(), deser1);
        Assert.assertEquals(deserialized1, obj1);

        // Test deserializer instance for custom class
        MyClass2 obj2 = new MyClass2();
        ObjectNode node2 = mapper.valueToTree(obj2);
        JsonDeserializer<MyClass2> deser2 = ctx.deserializerInstance(new AnnotatedClass(MyClass2.class), null);
        MyClass2 deserialized2 = mapper.readTree(node2, deser2.getClass(), deser2);
        Assert.assertEquals(deserialized2, obj2);

        // Test deserializer instance for null deserializer definition
        ObjectNode node3 = mapper.valueToTree(new Object());
        JsonDeserializer<Object> deser3 = ctx.deserializerInstance(new AnnotatedClass(Object.class), null);
        Object deserialized3 = mapper.readTree(node3, deser3.getClass(), deser3);
        Assert.assertNull(deserialized3);

        // Test deserializer instance for non-JsonDeserializer class
        MyCustomDeserializer deser4 = new MyCustomDeserializer();
        ObjectNode node4 = mapper.valueToTree(deser4);
        JsonDeserializer<MyCustomDeserializer> deser5 = ctx.deserializerInstance(new AnnotatedClass(MyCustomDeserializer.class), null);
        MyCustomDeserializer deserialized4 = mapper.readTree(node4, deser5.getClass(), deser5);
        Assert.assertEquals(deserialized4, deser4);
    }

    @Test
    public void testKeyDeserializerInstance() throws IOException {
        // Test key deserializer instance for simple class
        MyClass1 obj1 = new MyClass1(1);
        ObjectNode node1 = mapper.valueToTree(obj1);
        KeyDeserializer deser1 = ctx.keyDeserializerInstance(new AnnotatedClass(MyClass1.class), null);
        Object deserialized1 = mapper.readTree(node1, deser1.getClass(), deser1);
        Assert.assertEquals(deserialized1, obj1.getId().toString());

        // Test key deserializer instance for custom class
        MyClass2 obj2 = new MyClass2();
        ObjectNode node2 = mapper.valueToTree(obj2);
        KeyDeserializer deser2 = ctx.keyDeserializerInstance(new AnnotatedClass(MyClass2.class), null);
        Object deserialized2 = mapper.readTree(node2, deser2.getClass(), deser2);
        Assert.assertEquals(deserialized2, obj2.getId().toString());

        // Test key deserializer instance for null key deserializer definition
        ObjectNode node3 = mapper.valueToTree(new Object());
        KeyDeserializer deser3 = ctx.keyDeserializerInstance(new AnnotatedClass(Object.class), null);
        Object deserialized3 = mapper.readTree(node3, deser3.getClass(), deser3);
        Assert.assertNull(deserialized3);

        // Test key deserializer instance for non-KeyDeserializer class
        MyCustomKeyDeserializer deser4 = new MyCustomKeyDeserializer();
        ObjectNode node4 = mapper.valueToTree(deser4);
        KeyDeserializer deser5 = ctx.keyDeserializerInstance(new AnnotatedClass(MyCustomKeyDeserializer.class), null);
        Object deserialized4 = mapper.readTree(node4, deser5.getClass(), deser5);
        Assert.assertEquals(deserialized4, deser4.getId().toString());
    }

    static class MyClass1 {
        private int id;

        public MyClass1(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @JsonIdentityInfo(generator = ObjectIdGenerators.IntGenerator.class, property = "id")
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        public static class Serializer extends StandardSerializer<MyClass1> {
            public Serializer() {
                this(null);
            }

            public Serializer(Class<MyClass1> t) {
                this(null);
            }

            public Serializer(Class<MyClass1> t, BeanDescription beanDesc) {
                this(null);
            }

            public Serializer(Class<MyClass1> t, BeanDescription beanDesc, JsonSerializer<Object> valueSerializer) {
                this(null);
            }

            @Override
            public void serialize(MyClass1 value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
                jgen.writeNumber(value.getId());
            }
        }
    }

    static class MyClass2 {
        private int id;

        public MyClass2() {
            this(0);
        }

        public MyClass2(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @JsonIdentityInfo(generator = ObjectIdGenerators.IntGenerator.class, property = "id")
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        @ObjectIdGenerator(JsonIdentityInfo.AsString.class)
        public static class Serializer extends StandardSerializer<MyClass2> {
            public Serializer() {
                this(null);
            }

            public Serializer(Class<MyClass2> t) {
                this(null);
            }

            public Serializer(Class<MyClass2> t, BeanDescription beanDesc) {
                this(null);
            }

            public Serializer(Class<MyClass2> t, BeanDescription beanDesc, JsonSerializer<Object> valueSerializer) {
                this(null);
            }

            @Override
            public void serialize(MyClass2 value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
                jgen.writeString(String.valueOf(value.getId()));
            }
        }
    }

    static class MyCustomObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId resolveId(DeserializationContext ctxt, ObjectId id) {
            return new ReadableObjectId(new ObjectIdGenerator.IdKey(MyClass1.class, id), null);
        }
    }

    static class MyCustomKeyDeserializer extends StdKeyDeserializer {
        private String id;

        public MyCustomKeyDeserializer() {
            this(null);
        }

        public MyCustomKeyDeserializer(Class<MyCustomKeyDeserializer> t) {
            this(null);
        }

        public MyCustomKeyDeserializer(Class<MyCustomKeyDeserializer> t, BeanDescription beanDesc) {
            this(null);
        }

        public MyCustomKeyDeserializer(Class<MyCustomKeyDeserializer> t, BeanDescription beanDesc, JsonDeserializer<Object> valueDeserializer) {
            this(null);
        }

        @Override
        public Object deserializeKey(MapperConfig config, DeserializationContext ctxt, JsonParser p, Object id) throws IOException {
            String idStr = p.getText();
            this.id = idStr;
            return idStr;
        }

        public int getId() {
            return Integer.parseInt(id);
        }
    }

    static class MyCustomDeserializer extends JsonDeserializer<MyCustomDeserializer> {
        private Object id;

        public MyCustomDeserializer() {
            this(null);
        }

        public MyCustomDeserializer(Class<?> c) {
            this(null);
        }

        public MyCustomDeserializer(Class<?> c, BeanDescription beanDesc) {
            this(null);
        }

        public MyCustomDeserializer(Class<?> c, BeanDescription beanDesc, JsonDeserializer<Object> valueDeserializer) {
            this(null);
        }

        @Override
        public MyCustomDeserializer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String idStr = p.getText();
            this.id = idStr;
            return this;
        }

        public Object getId() {
            return id;
        }
    }
}