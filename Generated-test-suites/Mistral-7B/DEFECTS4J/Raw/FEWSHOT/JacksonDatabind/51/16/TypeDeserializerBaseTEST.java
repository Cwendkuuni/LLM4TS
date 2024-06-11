package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = new FooTypeDeserializer(mapper.getTypeFactory().constructType(Foo.class), mapper.getTypeFactory(), "", false, null);
        DeserializationContext ctxt = mapper.getDeserializationContext().withBehavior(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test with null typeId
        JsonDeserializer<Object> nullDeserializer = deserializer._findDeserializer(ctxt, null);
        assertNull(nullDeserializer);

        // Test with known typeId
        String knownTypeId = "com.fasterxml.jackson.databind.jsontype.impl.FooTypeDeserializer";
        JsonDeserializer<Object> knownDeserializer = deserializer._findDeserializer(ctxt, knownTypeId);
        assertNotNull(knownDeserializer);
        assertEquals(FooDeserializer.class, knownDeserializer.getClass());

        // Test with unknown typeId
        String unknownTypeId = "com.fasterxml.jackson.databind.jsontype.impl.BarTypeDeserializer";
        JsonDeserializer<Object> unknownDeserializer = deserializer._findDeserializer(ctxt, unknownTypeId);
        assertNull(unknownDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = new FooTypeDeserializer(mapper.getTypeFactory().constructType(Foo.class), mapper.getTypeFactory(), "", false, null);
        DeserializationContext ctxt = mapper.getDeserializationContext().withBehavior(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test when defaultImpl is null
        JsonDeserializer<Object> nullDeserializer = deserializer._findDefaultImplDeserializer(ctxt);
        assertNull(nullDeserializer);

        // Test when defaultImpl is not null
        Foo defaultImpl = new Foo();
        JavaType defaultImplType = mapper.getTypeFactory().constructType(Foo.class);
        deserializer = new FooTypeDeserializer(mapper.getTypeFactory().constructType(Foo.class), mapper.getTypeFactory(), "", false, defaultImplType);
        JsonDeserializer<Object> defaultImplDeserializer = deserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(defaultImplDeserializer);
        assertEquals(NullifyingDeserializer.instance.getClass(), defaultImplDeserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = new FooTypeDeserializer(mapper.getTypeFactory().constructType(Foo.class), mapper.getTypeFactory(), "", false, null);
        DeserializationContext ctxt = mapper.getDeserializationContext().withBehavior(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String unknownTypeId = "com.fasterxml.jackson.databind.jsontype.impl.BarTypeDeserializer";
        JavaType actual = deserializer._handleUnknownTypeId(ctxt, unknownTypeId, mapper.getTypeIdResolver(), mapper.getTypeFactory().constructType(Foo.class));
        assertNull(actual);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    static class Foo {
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

    static class FooDeserializer extends StdDeserializer<Foo> {
        public FooDeserializer() { this(null); }

        public FooDeserializer(Class<Foo> c) { super(c); }

        @Override
        public Foo deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            Foo foo = new Foo();
            foo.setName(p.getValueAsString());
            foo.setId(p.nextIntValue());
            return foo;
        }
    }
}

class FooTypeDeserializer extends TypeDeserializerBase<Foo> {
    protected FooTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return new FooTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.PROPERTY;
    }
}