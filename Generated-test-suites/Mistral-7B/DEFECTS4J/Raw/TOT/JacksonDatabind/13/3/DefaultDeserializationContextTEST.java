package com.example.jackson.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;

@JsonAutoDetect
@JsonIdentityInfo(generator = ObjectIdGenerator.PropertyGenerator.class)
public class DefaultDeserializationContextTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonDeserialize(using = MyIdResolver.class)
    static class MyClass {
        private int id;
        private String name;
        private MyClass reference;

        public MyClass() {}

        public MyClass(int id, String name, MyClass reference) {
            this.id = id;
            this.name = name;
            this.reference = reference;
        }

        @JsonIdentityReference(alwaysAsProperty = true)
        public MyClass getReference() {
            return reference;
        }

        @JsonIdentityInfo(alwaysAsProperty = true)
        public int getId() {
            return id;
        }
    }

    static class MyIdResolver extends SimpleObjectIdResolver {

        @Override
        public ObjectId generateId(DeserializationContext ctxt, ObjectBeingDeserialized obj) {
            if (obj instanceof MyClass) {
                MyClass myClass = (MyClass) obj;
                return new MyClassId(ctxt, myClass.getId());
            }
            return super.generateId(ctxt, obj);
        }
    }

    static class MyClassId extends Id {

        private final int id;

        public MyClassId(DeserializationContext ctxt, int id) {
            super(ctxt, IdKey.CLASS_ID_PROPERTY);
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    @org.junit.Test
    public void testFindObjectId() throws IOException {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

        ObjectReader reader = MAPPER.readerFor(MyClass.class);

        DeserializationContext ctxt = reader.getContext();

        MyClass obj1 = new MyClass(1, "obj1", null);
        MyClass obj2 = new MyClass(2, "obj2", obj1);

        JsonParser parser = new JsonFactory(MAPPER.getFactory()).createParser("[]");
        ctxt.handleUnknownProperty("root", parser, null);

        ReadableObjectId id1 = ctxt.findObjectId(obj1, ObjectIdGenerator.PropertyGenerator.instance(), null);
        assertNotNull(id1);
        assertEquals(MyClass.class, id1.getObjectType());
        assertEquals(1, id1.getId());

        ReadableObjectId id2 = ctxt.findObjectId(obj2, ObjectIdGenerator.PropertyGenerator.instance(), null);
        assertNotNull(id2);
        assertEquals(MyClass.class, id2.getObjectType());
        assertEquals(1, id2.getId());
        assertEquals(obj1, id2.getObject());
    }

    @org.junit.Test
    public void testFindObjectId_ObjectIdGenerator() throws IOException {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

        ObjectReader reader = MAPPER.readerFor(MyClass.class);

        DeserializationContext ctxt = reader.getContext();

        MyClass obj1 = new MyClass(1, "obj1", null);
        MyClass obj2 = new MyClass(2, "obj2", obj1);

        JsonParser parser = new JsonFactory(MAPPER.getFactory()).createParser("[]");
        ctxt.handleUnknownProperty("root", parser, null);

        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectId generateId(DeserializationContext ctxt, ObjectBeingDeserialized obj) {
                if (obj instanceof MyClass) {
                    MyClass myClass = (MyClass) obj;
                    return new MyClassId(ctxt, myClass.getId());
                }
                return null;
            }

            @Override
            public IdKey key(Object id) {
                return null;
            }
        };

        ReadableObjectId id1 = ctxt.findObjectId(obj1, generator, null);
        assertNotNull(id1);
        assertEquals(MyClass.class, id1.getObjectType());
        assertEquals(1, id1.getId());

        ReadableObjectId id2 = ctxt.findObjectId(obj2, generator, null);
        assertNotNull(id2);
        assertEquals(MyClass.class, id2.getObjectType());
        assertEquals(1, id2.getId());
        assertEquals(obj1, id2.getObject());
    }

    @org.junit.Test
    public void testFindObjectId_ObjectIdResolver() throws IOException {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

        ObjectReader reader = MAPPER.readerFor(MyClass.class);

        DeserializationContext ctxt = reader.getContext();

        MyClass obj1 = new MyClass(1, "obj1", null);
        MyClass obj2 = new MyClass(2, "obj2", obj1);

        JsonParser parser = new JsonFactory(MAPPER.getFactory()).createParser("[]");
        ctxt.handleUnknownProperty("root", parser, null);

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId idFromObject(DeserializationContext ctxt, ObjectBeingDeserialized obj) {
                if (obj instanceof MyClass) {
                    MyClass myClass = (MyClass) obj;
                    return new MyClassId(ctxt, myClass.getId());
                }
                return null;
            }

            @Override
            public ObjectId idFromString(DeserializationContext ctxt, String id) {
                return null;
            }

            @Override
            public ObjectId idFromValueId(DeserializationContext ctxt, ValueId valueId) {
                return null;
            }
        };

        ReadableObjectId id1 = ctxt.findObjectId(obj1, resolver, null);
        assertNotNull(id1);
        assertEquals(MyClass.class, id1.getObjectType());
        assertEquals(1, id1.getId());

        ReadableObjectId id2 = ctxt.findObjectId(obj2, resolver, null);
        assertNotNull(id2);
        assertEquals(MyClass.class, id2.getObjectType());
        assertEquals(1, id2.getId());
        assertEquals(obj1, id2.getObject());
    }

    @org.junit.Test
    public void testFindObjectId_Null() throws IOException {
        ObjectReader reader = MAPPER.readerFor(MyClass.class);
        DeserializationContext ctxt = reader.getContext();

        assertNull(ctxt.findObjectId(null, ObjectIdGenerator.PropertyGenerator.instance(), null));
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws IOException {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);

        ObjectReader reader = MAPPER.readerFor(MyClass.class);

        DeserializationContext ctxt = reader.getContext();

        List<MyClass> list = Arrays.asList(new MyClass(1, "obj1", null), new MyClass(2, "obj2", null));

        JsonParser parser = new JsonFactory(MAPPER.getFactory()).createParser("[" + ValueIntrospector.getEmptyValueInstance(MyClass.class).getValueAsString() + "]");
        ctxt.handleUnknownProperty("root", parser, null);

        for (MyClass obj : list) {
            ctxt.findObjectId(obj, ObjectIdGenerator.PropertyGenerator.instance(), null);
        }

        assertDoesNotThrow(() -> ctxt.checkUnresolvedObjectId());

        ctxt.handleUnknownProperty("notFound", parser, null);
        assertThrows(UnresolvedForwardReference.class, () -> ctxt.checkUnresolvedObjectId());
    }

    @org.junit.Test
    public void testDeserializerInstance() throws IOException {
        ObjectReader reader = MAPPER.readerFor(MyClass.class);
        DeserializationContext ctxt = reader.getContext();

        AnnotatedClass annotatedClass = ValueIntrospector.findAnnotated(MyClass.class, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);

        JsonDeserializer<MyClass> deserializer = ctxt.deserializerInstance(annotatedClass, MyClassDeserializer.class);
        assertNotNull(deserializer);

        TypeDeserializer typeDeserializer = ctxt.deserializerInstance(annotatedClass, MyClassTypeDeserializer.class);
        assertNotNull(typeDeserializer);

        KeyDeserializer keyDeserializer = ctxt.keyDeserializerInstance(annotatedClass, String.class);
        assertNotNull(keyDeserializer);

        TypeSerializer typeSerializer = ctxt.serializerInstance(MyClass.class, null);
        assertNotNull(typeSerializer);

        NameTransformer nameTransformer = ctxt.nameTransformerInstance(AnnotatedClass.ClassAnnotations.forSerialization.get(annotatedClass));
        assertNotNull(nameTransformer);
    }

    static class MyClassDeserializer extends JsonDeserializer<MyClass> {}

    static class MyClassTypeDeserializer extends JsonDeserializer<MyClass> {}

}