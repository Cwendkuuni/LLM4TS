package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase testee = new AbstractTypeDeserializerBase() {
            @Override
            protected JavaType handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
                return new ClassType(String.class);
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer ts = testee.forProperty(null);

        JsonToken token = parser.nextToken();
        assertNull("Expected null token", token);

        assertEquals("String", ts.handleMissingTypeId(ctxt, "test").getName());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase testee = new AbstractTypeDeserializerBase() {
            @Override
            protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                if (typeId.equals("String")) {
                    return new StringDeserializer();
                }
                return null;
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"String\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer ts = testee.forProperty(null);

        ts.deserialize(parser, ctxt);

        verify(parser).nextToken(JsonToken.START_OBJECT);
        verify(parser).nextFieldName();
        verify(parser).nextValue();
        verify(parser).nextToken(JsonToken.END_OBJECT);
    }

    @Test
    public void testFindDeserializerWithNativeTypeId() throws IOException {
        TypeDeserializerBase testee = new AbstractTypeDeserializerBase() {
            @Override
            protected Object deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return "OK";
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"TypeDeserializerBase\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer ts = testee.forProperty(null);

        assertEquals("OK", ts.deserializeWithNativeTypeId(parser, ctxt));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase testee = new AbstractTypeDeserializerBase() {
            @Override
            protected JavaType handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
                return new ClassType(String.class);
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"UnknownType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer ts = testee.forProperty(null);

        try {
            ts.deserializeWithNativeTypeId(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id (expected JavaScript type id) 'UnknownType' for polymorphic type handling", e.getMessage());
        }
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase testee = new AbstractTypeDeserializerBase() {
            @Override
            protected JavaType baseType() {
                return new ClassType(String.class);
            }

            @Override
            protected JsonDeserializer<Object> findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return new StringDeserializer();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer ts = testee.forProperty(null);

        assertEquals("OK", ts.findDefaultImplDeserializer(ctxt).deserialize(parser, ctxt));
    }
}

class AbstractTypeDeserializerBase extends TypeDeserializerBase {
    protected AbstractTypeDeserializerBase() {
        super(null, null, null, false, null);
    }

    protected AbstractTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return new AbstractTypeDeserializerBase(this, prop);
    }
}

class StringDeserializer extends StdDeserializer<String> {
    public StringDeserializer() {
        this(null);
    }

    public StringDeserializer(Class<String> t) {
        super(t);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}