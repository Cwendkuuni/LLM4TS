package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ValueInstantiatorImpl;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.CollectionReferringAccumulator;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.CollectionReferring;

/**
 * Unit tests for {@link CollectionDeserializer}.
 */
@JacksonStdImpl
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException {
        // Test deserializing empty collection
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[]");
        CollectionDeserializer deserializer = createCollectionDeserializer(factory, Object[].class);
        List<Object> result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Collections.emptyList(), result);

        // Test deserializing collection with single element
        parser.nextValue(); // consume [
        parser.setValue("foo");
        result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList("foo"), result);

        // Test deserializing collection with multiple elements
        parser.nextValue(); // consume ["foo", "bar"]
        result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList("foo", "bar"), result);

        // Test deserializing collection with null element
        parser.nextValue(); // consume ["null"]
        result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertNull(result.get(0));

        // Test deserializing collection with non-null element and type deserializer
        factory = new JsonFactory();
        factory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        parser = factory.createParser("[" +
                "{\"name\":\"foo\", \"value\":1}" +
                ",\"bar\"]");
        JsonDeserializer<MyPojo> pojoDeserializer = new MyPojoDeserializer();
        TypeDeserializer pojoTypeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return pojoDeserializer.deserialize(p, ctxt);
            }
        };
        deserializer = createCollectionDeserializer(factory, MyPojo[].class, pojoDeserializer, pojoTypeDeserializer);
        result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList(new MyPojo(), "bar"), result);

        // Test handling unresolved forward references
        factory = new JsonFactory();
        JsonParser parser1 = factory.createParser("[{ \"$ref\": \"/MyRef\" }]");
        CollectionDeserializer deserializer1 = createCollectionDeserializer(factory, Object[].class);
        TestDeserializationContext context = new TestDeserializationContext();
        context.addValueInstantiator(new ValueInstantiatorImpl(MyRef.class, new MyRefDeserializer()));
        TestDeserializationContext.TestRef ref = context.addTestRef();
        result = deserializer1.createContextual(context, null);
        assertNotNull(result);
        deserializer1.handleUnresolvedReference(ref.getId(), null);
        assertEquals(Collections.singletonList(new MyRef()), result);

        // Test handling unresolved forward references with multiple elements
        JsonParser parser2 = factory.createParser("[{ \"$ref\": \"/MyRef1\" }, { \"$ref\": \"/MyRef2\" }]");
        CollectionDeserializer deserializer2 = createCollectionDeserializer(factory, Object[].class);
        context = new TestDeserializationContext();
        context.addValueInstantiator(new ValueInstantiatorImpl(MyRef1.class, new MyRef1Deserializer()));
        context.addValueInstantiator(new ValueInstantiatorImpl(MyRef2.class, new MyRef2Deserializer()));
        TestDeserializationContext.TestRef ref1 = context.addTestRef();
        TestDeserializationContext.TestRef ref2 = context.addTestRef();
        result = deserializer2.createContextual(context, null);
        assertNotNull(result);
        deserializer2.handleUnresolvedReference(ref1.getId(), null);
        deserializer2.handleUnresolvedReference(ref2.getId(), null);
        assertEquals(Arrays.asList(new MyRef1(), new MyRef2()), result);
    }

    @Test
    public void testCollectionDeserializerWithCustomValueInstantiator() throws IOException {
        // Test deserializing collection with custom value instantiator
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2.5, true]");
        CollectionDeserializer deserializer = createCollectionDeserializer(factory, Object[].class, null, null,
                new TestValueInstantiator());
        List<Object> result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList(1, 2.5, true), result);
    }

    private CollectionDeserializer createCollectionDeserializer(JsonFactory factory,
            Class<Object> collectionType, JsonDeserializer<Object> valueDeserializer,
            TypeDeserializer valueTypeDeserializer, ValueInstantiator valueInstantiator) throws IOException {
        JavaType collectionTypeJava = factory.getTypeFactory().constructType(collectionType);
        return new CollectionDeserializer(collectionTypeJava, valueDeserializer, valueTypeDeserializer,
                valueInstantiator);
    }

    static class MyPojo {
        String name;
        int value;
    }

    static class MyPojoDeserializer extends StdDeserializer<MyPojo> {
        @Override
        public MyPojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            MyPojo pojo = new MyPojo();
            pojo.name = p.getText();
            p.nextToken(); // consume ':'
            pojo.value = p.getIntValue();
            return pojo;
        }
    }

    static class MyRef {
    }

    static class MyRefDeserializer extends StdDeserializer<MyRef> {
        @Override
        public MyRef deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyRef();
        }
    }

    static class MyRef1 {
    }

    static class MyRef1Deserializer extends StdDeserializer<MyRef1> {
        @Override
        public MyRef1 deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyRef1();
        }
    }

    static class MyRef2 {
    }

    static class MyRef2Deserializer extends StdDeserializer<MyRef2> {
        @Override
        public MyRef2 deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyRef2();
        }
    }

    static class TestValueInstantiator extends ValueInstantiator {
        @Override
        public Object createFromString(DeserializationContext ctxt, String contentAsString) throws IOException {
            return contentAsString;
        }
    }

    static class TestDeserializationContext extends DeserializationContext {
        private final List<ValueInstantiator> _instantiators = new ArrayList<>();
        private int _idCount = 0;

        TestDeserializationContext() {
            super(null, null, null, null, null, null, null, null, null, null, null);
        }

        void addValueInstantiator(ValueInstantiator instantiator) {
            _instantiators.add(instantiator);
        }

        TestRef addTestRef() {
            TestRef ref = new TestRef(_idCount++, this);
            _instantiators.get(_instantiators.size() - 1).getValueInstantiator().afterDeserialization(_idCount, ref, null);
            return ref;
        }

        static class TestRef extends Referring {
            private final int _id;
            private final DeserializationContext _context;

            TestRef(int id, DeserializationContext context) {
                super(null, null, null);
                _id = id;
                _context = context;
            }

            int getId() {
                return _id;
            }
        }
    }
}