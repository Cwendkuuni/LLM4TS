package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.DeserializationConfigParser;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ValueInstantiatorTest {

    @SuppressWarnings("unchecked")
    private static class MyValueInstantiator extends ValueInstantiator {

        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new MyIntInstance(value);
        }

        @Override
        public JavaType getDelegateType(DeserializationConfig config) {
            return MyLongDelegate.getClass();
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyValueInstantiatorWithArrayDelegate extends ValueInstantiator {

        @Override
        public boolean canCreateUsingArrayDelegate() {
            return true;
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return new MyIntArrayInstance((int[]) delegate);
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return int[].class;
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyValueInstantiatorWithDefaultCreator extends ValueInstantiator {

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            return MyDefaultCreator.class.getDeclaredMethod("newInstance", (Class[]) null).getAnnotations();
        }

        @JsonCreator
        private static class MyDefaultCreator {
            public static MyDefaultInstance newInstance() {
                return new MyDefaultInstance();
            }
        }

        private static class MyDefaultInstance {
            // empty class
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyValueInstantiatorWithObjectCreator extends ValueInstantiator {

        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            MyObjectInstance obj = (MyObjectInstance) args[0];
            return new MyObjectInstanceWithValue(obj.value);
        }

        @JsonDeserialize(using = MyObjectDeserializer.class)
        private static class MyObjectInstance {
            @JsonProperty("value")
            public int value;
        }

        private static class MyObjectDeserializer extends StdDeserializer<MyObjectInstance> {
            public MyObjectDeserializer() {
                this(null);
            }

            protected MyObjectDeserializer(Class<?> vc) {
                super(vc);
            }

            @Override
            public MyObjectInstance deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new MyObjectInstance();
            }
        }

        private static class MyObjectInstanceWithValue {
            public final int value;

            public MyObjectInstanceWithValue(int value) {
                this.value = value;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyValueInstantiatorWithCreatorProperty extends ValueInstantiator {

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[] { new BeanPropertyDefinition(null, "value", int.class) };
        }

        @JsonCreator
        private static class MyCreatorPropertyInstance {
            public int value;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            MyCreatorPropertyInstance obj = (MyCreatorPropertyInstance) args[0];
            return new MyValueInstanceWithCreatorProperty(obj.value);
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyValueInstantiatorWithStringCreator extends ValueInstantiator {

        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            int num = Integer.parseInt(value);
            return new MyIntInstance(num);
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyValueInstantiatorWithBooleanCreator extends ValueInstantiator {

        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return value ? new MyBooleanInstance(true) : new MyBooleanInstance(false);
        }
    }

    @JsonSubTypes(value = { @Type(value = MyIntType.class, name = "INT"),
            @Type(value = MyLongType.class, name = "LONG"),
            @Type(value = MyBooleanType.class, name = "BOOLEAN") })
    private static class MyValueInstance {
        protected final int value;

        protected MyValueInstance(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static class MyIntInstance extends MyValueInstance {
        public MyIntInstance(int value) {
            super(value);
        }
    }

    private static class MyLongDelegate {
        public Long value;
    }

    private static class MyLongInstance extends MyValueInstance {
        public MyLongInstance(long value) {
            super((int) value);
        }
    }

    private static class MyBooleanType {
    }

    private static class MyBooleanInstance extends MyValueInstance {
        public MyBooleanInstance(boolean value) {
            super(value ? 1 : 0);
        }
    }

    @BeforeClass
    public static void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new NopAnnotationIntrospector());
        DeserializationConfig config = mapper.getConfig();
        DeserializationConfigParser parser = new DeserializationConfigParser(config);
        parser.setClassInfo(MyValueInstance.class, new StdValueInstantiator.StdClassInfo(MyValueInstantiator.class));
        config.setRootValueFactory(new StdDeserializer.NullValueFactory(MyValueInstance.class));
    }

    @Test
    public void testCreateInstanceWithDefault() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateUsingDefault());
        assertNull(inst.getDefaultCreator());
        assertNull(inst.createUsingDefault(null));

        MyValueInstantiatorWithDefaultCreator instDefault = new MyValueInstantiatorWithDefaultCreator(MyValueInstance.class);
        assertTrue(instDefault.canCreateUsingDefault());
        assertNotNull(instDefault.getDefaultCreator());
        assertNotNull(instDefault.createUsingDefault(null));
    }

    @Test
    public void testCreateInstanceWithDelegate() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateUsingDelegate());
        assertNull(inst.getDelegateType(null));
        assertNull(inst.createUsingDelegate(null, null));

        MyValueInstantiatorWithDelegate instDelegate = new MyValueInstantiatorWithDelegate(MyValueInstance.class);
        assertTrue(instDelegate.canCreateUsingDelegate());
        assertEquals(MyLongDelegate.class, instDelegate.getDelegateType(null));
        assertNotNull(instDelegate.createUsingDelegate(null, new MyLongDelegate()));
    }

    @Test
    public void testCreateInstanceWithArrayDelegate() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateUsingArrayDelegate());
        assertNull(inst.getArrayDelegateType(null));
        assertNull(inst.createUsingArrayDelegate(null, null));

        MyValueInstantiatorWithArrayDelegate instArrayDelegate = new MyValueInstantiatorWithArrayDelegate(MyValueInstance.class);
        assertTrue(instArrayDelegate.canCreateUsingArrayDelegate());
        assertEquals(int[].class, instArrayDelegate.getArrayDelegateType(null));
        assertNotNull(instArrayDelegate.createUsingArrayDelegate(null, new Object[] { new int[] { 1, 2 } }));
    }

    @Test
    public void testCreateInstanceWithString() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateFromString());
        assertNull(inst.createFromString(null, null));

        MyValueInstantiatorWithStringCreator instString = new MyValueInstantiatorWithStringCreator(MyValueInstance.class);
        assertTrue(instString.canCreateFromString());
        assertEquals(MyIntInstance.class, instString.getValueClass());
        assertNotNull(instString.createFromString(null, "1"));
    }

    @Test
    public void testCreateInstanceWithInt() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateFromInt());
        assertNull(inst.createFromInt(null, 1));

        MyValueInstantiatorWithIntCreator instInt = new MyValueInstantiatorWithIntCreator(MyValueInstance.class);
        assertTrue(instInt.canCreateFromInt());
        assertEquals(MyIntInstance.class, instInt.getValueClass());
        assertNotNull(instInt.createFromInt(null, 1));
    }

    @Test
    public void testCreateInstanceWithLong() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateFromLong());
        assertNull(inst.createFromLong(null, 1L));

        MyValueInstantiatorWithLongCreator instLong = new MyValueInstantiatorWithLongCreator(MyValueInstance.class);
        assertTrue(instLong.canCreateFromLong());
        assertEquals(MyLongInstance.class, instLong.getValueClass());
        assertNotNull(instLong.createFromLong(null, 1L));
    }

    @Test
    public void testCreateInstanceWithBoolean() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateFromBoolean());
        assertNull(inst.createFromBoolean(null, false));

        MyValueInstantiatorWithBooleanCreator instBoolean = new MyValueInstantiatorWithBooleanCreator(MyValueInstance.class);
        assertTrue(instBoolean.canCreateFromBoolean());
        assertEquals(MyBooleanInstance.class, instBoolean.getValueClass());
        assertNotNull(instBoolean.createFromBoolean(null, true));
    }

    @Test
    public void testCreateInstanceWithObject() throws Exception {
        MyValueInstantiator inst = new MyValueInstantiator(MyValueInstance.class);
        assertFalse(inst.canCreateFromObjectWith());
        assertNull(inst.getFromObjectArguments(null));
        assertNull(inst.createFromObjectWith(null, null, null));

        MyValueInstantiatorWithObjectCreator instObject = new MyValueInstantiatorWithObjectCreator(MyValueInstance.class);
        assertTrue(instObject.canCreateFromObjectWith());
        assertNotNull(instObject.getFromObjectArguments(null));
        assertEquals(1, instObject.getFromObjectArguments(null).length);
        BeanPropertyDefinition prop = instObject.getFromObjectArguments(null)[0];
        assertEquals(int.class, prop.getType());
        assertEquals("value", prop.getName());

        AnnotatedClass ac = instObject.getValueClass().getAnnotation(JsonSubTypes.class);
        assertEquals(3, ac.getMemberValues().size());
        assertEquals("INT", ac.getMemberValues().get(0).getName());
        assertEquals(MyIntType.class, ac.getMemberValues().get(0).getType());
        assertEquals("LONG", ac.getMemberValues().get(1).getName());
        assertEquals(MyLongType.class, ac.getMemberValues().get(1).getType());
        assertEquals("BOOLEAN", ac.getMemberValues().get(2).getName());
        assertEquals(MyBooleanType.class, ac.getMemberValues().get(2).getType());

        ObjectMapper mapper = new ObjectMapper();
        MyObjectInstance obj = mapper.readValue(new ObjectMapper().writeValueAsString(new int[] { 1, 2 }), MyObjectInstance[].class)[0];
        assertNotNull(instObject.createFromObjectWith(null, new Object[] { obj }));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateInstanceWithInvalidObject() throws Exception {
        MyValueInstantiatorWithObjectCreator instObject = new MyValueInstantiatorWithObjectCreator(MyValueInstance.class);
        ObjectMapper mapper = new ObjectMapper();
        MyObjectInstanceWithCreatorProperty obj = mapper.readValue(new ObjectMapper().writeValueAsString(new MyCreatorPropertyInstance()), MyObjectInstanceWithCreatorProperty.class);
        instObject.createFromObjectWith(null, new Object[] { obj });
    }
}