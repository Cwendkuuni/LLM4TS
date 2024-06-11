package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.testng.annotations.*;

import static org.junit.Assert.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValueInstantiatorTest
{
    private ObjectMapper mapper;

    @BeforeClass
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSimpleInstantiator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(String.class);
        JsonNode node = mapper.readTree("\"abc\"");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertEquals("abc", created);
    }

    @Test
    public void testEmptyStringAsNull() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(String.class);
        JsonNode node = mapper.readTree("{}");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertNull(created);
    }

    @Test
    public void testEmptyInstantiator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(Object.class);
        // should fail with no-args constructor
        JsonNode node = mapper.readTree("{}");
        try {
            inst.createUsingDefault(mapper.getDeserializationContext());
            fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            assertTrue(e.getMessage().startsWith("no default no-arguments constructor found"));
        }
    }

    @Test
    public void testStringCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyString.class);

        JsonNode node = mapper.readTree("\"abc\"");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertEquals("abc", created);
    }

    @Test
    public void testInvalidStringCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyString.class);

        JsonNode node = mapper.readTree("123");
        try {
            inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
            fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            assertTrue(e.getMessage().startsWith("no String-argument constructor/factory method to deserialize from Number value (123)"));
        }
    }

    @Test
    public void testIntCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyInt.class);

        JsonNode node = mapper.readTree("42");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertEquals(42, created);
    }

    @Test
    public void testInvalidIntCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyInt.class);

        JsonNode node = mapper.readTree("\"abc\"");
        try {
            inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
            fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            assertTrue(e.getMessage().startsWith("no int/Int-argument constructor/factory method to deserialize from String value (\"abc\")"));
        }
    }

    @Test
    public void testLongCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyLong.class);

        JsonNode node = mapper.readTree("42l");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertEquals(42L, created);
    }

    @Test
    public void testInvalidLongCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyLong.class);

        JsonNode node = mapper.readTree("\"abc\"");
        try {
            inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
            fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            assertTrue(e.getMessage().startsWith("no long/Long-argument constructor/factory method to deserialize from String value (\"abc\")"));
        }
    }

    @Test
    public void testDoubleCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyDouble.class);

        JsonNode node = mapper.readTree("42.0");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertEquals(42.0, created);
    }

    @Test
    public void testInvalidDoubleCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyDouble.class);

        JsonNode node = mapper.readTree("\"abc\"");
        try {
            inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
            fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            assertTrue(e.getMessage().startsWith("no double/Double-argument constructor/factory method to deserialize from String value (\"abc\")"));
        }
    }

    @Test
    public void testBooleanCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyBoolean.class);

        JsonNode node = mapper.readTree("true");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertEquals(true, created);
    }

    @Test
    public void testInvalidBooleanCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyBoolean.class);

        JsonNode node = mapper.readTree("42");
        try {
            inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
            fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            assertTrue(e.getMessage().startsWith("no boolean/Boolean-argument constructor/factory method to deserialize from Number value (42)"));
        }
    }

    @Test
    public void testDelegateCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyDelegate.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return mapper.findValueDeserializer(MyDelegate.class.getName());
            }
        };

        JsonNode node = mapper.readTree("{\"value\": 123}");
        Object created = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        assertEquals(123, created.getClass().getMethod("getValue").invoke(created));
    }

    @Test
    public void testMissingDelegateCreator() throws Exception {
        ValueInstantiator inst = new ValueInstantiator.Base(MyDelegate.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return null;
            }
        };

        JsonNode node = mapper.readTree("{\"value\": 123}");
        try {
            inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
            fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            assertTrue(e.getMessage().startsWith("no delegate creator specified"));
        }
    }

    static class MyString extends String {
        public MyString() {}

        @JsonCreator
        public MyString(@JsonProperty("value") String value) {
            this(value);
        }
    }

    static class MyInt extends Number implements StdScalarValue {
        public MyInt() {}

        @JsonCreator
        public MyInt(@JsonProperty("value") int value) {
            this(value);
        }

        @Override
        public int intValue() {
            return this.value;
        }

        private int value;
    }

    static class MyLong extends Number implements StdScalarValue {
        public MyLong() {}

        @JsonCreator
        public MyLong(@JsonProperty("value") long value) {
            this(value);
        }

        @Override
        public long longValue() {
            return this.value;
        }

        private long value;
    }

    static class MyDouble extends Number implements StdScalarValue {
        public MyDouble() {}

        @JsonCreator
        public MyDouble(@JsonProperty("value") double value) {
            this(value);
        }

        @Override
        public double doubleValue() {
            return this.value;
        }

        private double value;
    }

    static class MyBoolean extends Boolean {
        public MyBoolean() {}

        @JsonCreator
        public MyBoolean(@JsonProperty("value") String value) {
            this(value.equals("true"));
        }
    }

    static class MyDelegate {
        private int value;

        @JsonDeserialize(using = MyDelegateDeserializer.class)
        public static MyDelegate fromJson(JsonNode node) throws IOException {
            return new MyDelegate();
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    static class MyDelegateDeserializer extends StdDeserializer<MyDelegate> {
        public MyDelegateDeserializer() {
            this(null);
        }

        public MyDelegateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public MyDelegate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            Object id = ctxt.readValueAsArray(NodeType.class, p);
            MyDelegate result = new MyDelegate();
            PropertyName idName = new PropertyName("value");
            AnnotatedMember member = findAnnotatedMember(result, idName);
            BeanPropertyPropertyNode propNode = BeanPropertyPropertyNode.construct(member, result, ctxt, p);
            propNode.setValue(ctxt.handleValueReference(node.get("value")));
            return result;
        }
    }
}