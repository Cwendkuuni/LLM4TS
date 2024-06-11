package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.MockUp;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class AtomicReferenceDeserializerTest extends MockObjectTestCase {

    private static final AtomicReference<?> DEFAULT_VALUE = new AtomicReference<Object>(new Object());
    private static final JavaType FULL_TYPE = ClassUtil.simpleType(AtomicReference.class, Object.class);

    @SuppressWarnings("unchecked")
    private static final TypeDeserializer TYPE_DESERIALIZER = mock(TypeDeserializer.class);
    private static final JsonDeserializer<Object> VALUE_DESERIALIZER = mock(JsonDeserializer.class);

    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceDeserializer UNDER_TEST = new AtomicReferenceDeserializer(FULL_TYPE, new ValueInstantiator() {
        @Override
        public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
            return new Object();
        }
    }, TYPE_DESERIALIZER, VALUE_DESERIALIZER);

    @SuppressWarnings("unchecked")
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Before
    public void setup() {
        new MockUp<ValueInstantiator>() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                return new Object();
            }
        }.install();
        new MockUp<TypeDeserializer>() {
            @Override
            public TypeDeserializer createContextualInstance(DeserializationContext ctxt) {
                return TYPE_DESERIALIZER;
            }
        }.install();
        new MockUp<JsonDeserializer>() {
            @Override
            public JsonDeserializer<Object> createContextualInstance(DeserializationContext ctxt) {
                return VALUE_DESERIALIZER;
            }
        }.install();
    }

    @org.junit.Test
    public void testConstructor() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(FULL_TYPE, new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                return null;
            }
        }, TYPE_DESERIALIZER, VALUE_DESERIALIZER);
        assertNotNull(deserializer);
        assertEquals(FULL_TYPE, deserializer.getTargetType());
    }

    @org.junit.Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer1 = new AtomicReferenceDeserializer(FULL_TYPE, new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                return null;
            }
        }, TYPE_DESERIALIZER, VALUE_DESERIALIZER);
        AtomicReferenceDeserializer deserializer2 = UNDER_TEST.withResolved(TYPE_DESERIALIZER, VALUE_DESERIALIZER);
        assertEquals(deserializer1, deserializer2);
    }

    @org.junit.Test
    public void testGetNullValue() {
        assertNotNull(UNDER_TEST.getNullValue(mock(DeserializationContext.class)));
        assertEquals(DEFAULT_VALUE, UNDER_TEST.getNullValue(mock(DeserializationContext.class)));
    }

    @org.junit.Test
    public void testGetEmptyValue() {
        assertEquals(DEFAULT_VALUE, UNDER_TEST.getEmptyValue(mock(DeserializationContext.class)));
    }

    @org.junit.Test
    public void testReferenceValue() {
        Object value = new Object();
        AtomicReference<Object> result = UNDER_TEST.referenceValue(value);
        assertEquals(value, result.get());
        assertEquals(value.getClass(), result.get().getClass());
    }

    @org.junit.Test
    public void testGetReferenced() {
        AtomicReference<Object> atomicReference = new AtomicReference<>(new Object());
        Object result = UNDER_TEST.getReferenced(atomicReference);
        assertEquals(atomicReference.get(), result);
    }

    @org.junit.Test
    public void testUpdateReference() {
        AtomicReference<Object> atomicReference = new AtomicReference<>(new Object());
        Object newValue = new Object();
        AtomicReference<Object> result = UNDER_TEST.updateReference(atomicReference, newValue);
        assertEquals(newValue, result.get());
        assertEquals(newValue.getClass(), result.get().getClass());
        assertEquals(atomicReference, result);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSupportsUpdateWithFalseConfig() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(FULL_TYPE, new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                return new Object();
            }
        }, TYPE_DESERIALIZER, VALUE_DESERIALIZER) {
            @Override
            public Boolean supportsUpdate(DeserializationConfig config) {
                return Boolean.FALSE;
            }
        };
        deserializer.updateReference(DEFAULT_VALUE, new Object());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testSetReferenceNullValue() {
        UNDER_TEST.set(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetReferenceWithNullValue() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        UNDER_TEST.set(reference, null);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetReferenceWithNonAtomicReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        UNDER_TEST.set(new Object(), reference.get());
    }

    @org.junit.Test
    public void testDeserialize() throws Exception {
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"value\":{\"class\":\"java.lang.String\",\"@type\":\"java.lang.String\",\"value\":\"test\"}}";
        Object deserialized = MAPPER.readValue(ClassImposteriser.getInstance().improveClassLoading(json), AtomicReference.class);
        AtomicReference<String> expected = new AtomicReference<>("test");
        assertEquals(expected, deserialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullJson() throws Exception {
        MAPPER.readValue(null, AtomicReference.class);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeWithEmptyJson() throws Exception {
        MAPPER.readValue("", AtomicReference.class);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws Exception {
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"value\":\"test\"}";
        MAPPER.readValue(json, AtomicReference.class);
    }
}