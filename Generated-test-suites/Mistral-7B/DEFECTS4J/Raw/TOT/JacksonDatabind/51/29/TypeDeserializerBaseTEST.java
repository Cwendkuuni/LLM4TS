package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase testee;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        testee = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws Exception {
        String typeId = "com.example.MyClass";
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}");
        JsonDeserializer<Object> deserializer = testee.findDeserializer(mapper.getDeserializationContext(), typeId);
        assertEquals(MyClassDeserializer.class, deserializer.getClass());
        parser.close();
    }

    @Test
    public void testFindDeserializerWithMissingTypeId() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("{}");
        JsonDeserializer<Object> deserializer = testee.findDeserializer(mapper.getDeserializationContext(), null);
        assertNull(deserializer);
        parser.close();
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        String unknownTypeId = "unknown.type.id";
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"" + unknownTypeId + "\"}");
        JsonDeserializer<Object> deserializer = testee.findDeserializer(mapper.getDeserializationContext(), unknownTypeId);
        assertNull(deserializer);
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws Exception {
        String invalidTypeId = "com.example.MyClass[";
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"" + invalidTypeId + "\"}");
        testee.findDeserializer(mapper.getDeserializationContext(), invalidTypeId);
        parser.close();
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> deserializer = testee.findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws Exception {
        TypeDeserializerBase testeeWithDefaultImpl = new TypeDeserializerBaseForTesting() {
            @Override
            public JsonDeserializer<Object> findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return new MyClassDeserializer();
            }
        };
        JsonDeserializer<Object> deserializer = testeeWithDefaultImpl.findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertEquals(MyClassDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeIdWithStaticTypeIds() throws Exception {
        TypeDeserializerBase testeeWithStaticTypeIds = new TypeDeserializerBaseForTesting() {
            @Override
            protected JavaType handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                assertEquals("expected type id: " + typeId, MyClass.class, idResolver.typeFromId(ctxt, typeId));
                return MyClass.class;
            }
        };
        String typeId = "com.example.MyClass";
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}");
        JavaType resultType = testeeWithStaticTypeIds.handleUnknownTypeId(mapper.getDeserializationContext(), typeId, null, null);
        assertEquals(MyClass.class, resultType);
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithMissingTypeIdResolver() throws Exception {
        TypeDeserializerBase testee = new TypeDeserializerBaseForTesting() {
            @Override
            protected JavaType handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                throw new IOException("Unknown type id: " + typeId);
            }
        };
        String typeId = "com.example.MyClass";
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}");
        testee.handleUnknownTypeId(mapper.getDeserializationContext(), typeId, null, null);
        parser.close();
    }

    @Test
    public void testHandleUnknownTypeIdWithNullBaseType() throws Exception {
        TypeDeserializerBase testee = new TypeDeserializerBaseForTesting() {
            @Override
            protected JavaType handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return idResolver.typeFromId(ctxt, typeId);
            }
        };
        String typeId = "com.example.MyClass";
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}");
        JavaType resultType = testee.handleUnknownTypeId(mapper.getDeserializationContext(), typeId, null, null);
        assertNull(resultType);
        parser.close();
    }
}

class MyClassDeserializer extends StdDeserializer<MyClass> {
    public MyClassDeserializer() {
        this(null);
    }

    protected MyClassDeserializer(Class<MyClass> c) {
        super(c);
    }

    @Override
    public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new MyClass();
    }
}

class MyClass {
}

class TypeDeserializerBaseForTesting extends TypeDeserializerBase {
    // Empty constructor and fields to match the base class
}