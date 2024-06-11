package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ValueInstantiatorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultCreator() throws Exception {
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(String.class);
        assertEquals("", instantiator.createUsingDefault(mapper.getDeserializationContext().disabledContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingCreator() throws Exception {
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(new TypeReference<Foo>() {}.getType());
        instantiator.createUsingDefault(mapper.getDeserializationContext().disabledContext());
    }

    @Test
    public void testStringCreator() throws Exception {
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(String.class);
        assertEquals("test", instantiator.createFromString(mapper.getDeserializationContext(), "test"));
    }

    @Test
    public void testIntCreator() throws Exception {
        ValueInstantiator instantiator = new IntValueInstantiator(mapper.getTypeFactory());
        assertEquals(42, instantiator.createFromInt(mapper.getDeserializationContext(), 42));
    }

    @Test
    public void testLongCreator() throws Exception {
        ValueInstantiator instantiator = new LongValueInstantiator(mapper.getTypeFactory());
        assertEquals(42L, instantiator.createFromLong(mapper.getDeserializationContext(), 42L));
    }

    @Test
    public void testDoubleCreator() throws Exception {
        ValueInstantiator instantiator = new DoubleValueInstantiator(mapper.getTypeFactory());
        assertEquals(3.14, instantiator.createFromDouble(mapper.getDeserializationContext(), 3.14), 0.001);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        ValueInstantiator instantiator = new BooleanValueInstantiator(mapper.getTypeFactory());
        assertTrue(instantiator.createFromBoolean(mapper.getDeserializationContext(), true));
        assertFalse(instantiator.createFromBoolean(mapper.getDeserializationContext(), false));
    }

    @Test
    public void testObjectCreator() throws Exception {
        JavaType valueType = mapper.getTypeFactory().constructType(Foo.class);
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(valueType);

        Object input = new Object() {
            @JsonProperty("a")
            public int a = 1;

            @JsonProperty("b")
            public String b = "test";
        };

        Foo result = (Foo) instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { input });
        assertEquals(1, result.getIntField());
        assertEquals("test", result.getStringField());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        JavaType valueType = mapper.getTypeFactory().constructType(FooDelegate.class);
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(valueType);

        FooDelegate delegate = new FooDelegate("test");
        Foo result = (Foo) instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
        assertEquals("test", result.getStringField());
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        JavaType valueType = mapper.getTypeFactory().constructType(FooArrayDelegate.class);
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(valueType);

        Object[] input = new Object[] { new Object() {
            @JsonProperty("a")
            public int a = 1;
        } };

        Foo[] result = (Foo[]) instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), input);
        assertEquals(1, result[0].getIntField());
    }

    @Test(expected = JsonMappingException.class)
    public void testNoCreator() throws Exception {
        JavaType valueType = mapper.getTypeFactory().constructType(NoCreator.class);
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(valueType);
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]);
    }

    @Test
    public void testTypeDeserializerCreator() throws Exception {
        JavaType valueType = mapper.getTypeFactory().constructType(ValueTypeDeserializer.class);
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueDeserializer(valueType);

        ValueTypeDeserializer result = (ValueTypeDeserializer) instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertEquals(ValueType.class, result.getValueType());
    }

    private static class Foo {
        private int intField;
        private String stringField;

        public Foo(int intField, String stringField) {
            this.intField = intField;
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public String getStringField() {
            return stringField;
        }
    }

    private static class FooDelegate {
        private String stringField;

        public FooDelegate(String stringField) {
            this.stringField = stringField;
        }

        public String getStringField() {
            return stringField;
        }
    }

    private static class FooArrayDelegate {
        private Object[] values;

        public FooArrayDelegate(Object[] values) {
            this.values = values;
        }

        public Foo createInstance(DeserializationContext ctxt) throws IOException {
            return mapper.readValue(ctxt.getTextFactory().getText(ctxt.getParser().getValueAsString()), Foo.class);
        }
    }

    private static class NoCreator {}

    private static class ValueTypeDeserializer extends ValueInstantiator {
        private TypeDeserializer _valueTypeDeserializer;

        public ValueTypeDeserializer() {
            _valueTypeDeserializer = new StdValueDeserializer();
        }

        @Override
        public JavaType getDelegateType(DeserializationConfig config) {
            return _valueTypeDeserializer.getType(config, ValueType.class);
        }
    }

    private static class StdValueDeserializer extends ValueInstantiator {
        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return null;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[0];
        }
    }

    private static class ValueType {
        // no op
    }

    private static class IntValueInstantiator extends ValueInstantiator {
        private JavaType _intType;

        public IntValueInstantiator(TypeFactory typeFactory) {
            _intType = typeFactory.constructType(Integer.class);
        }

        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return value;
        }
    }

    private static class LongValueInstantiator extends ValueInstantiator {
        private JavaType _longType;

        public LongValueInstantiator(TypeFactory typeFactory) {
            _longType = typeFactory.constructType(Long.class);
        }

        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return value;
        }
    }

    private static class DoubleValueInstantiator extends ValueInstantiator {
        private JavaType _doubleType;

        public DoubleValueInstantiator(TypeFactory typeFactory) {
            _doubleType = typeFactory.constructType(Double.class);
        }

        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return value;
        }
    }

    private static class BooleanValueInstantiator extends ValueInstantiator {
        private JavaType _booleanType;

        public BooleanValueInstantiator(TypeFactory typeFactory) {
            _booleanType = typeFactory.constructType(Boolean.class);
        }

        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return value;
        }
    }
}