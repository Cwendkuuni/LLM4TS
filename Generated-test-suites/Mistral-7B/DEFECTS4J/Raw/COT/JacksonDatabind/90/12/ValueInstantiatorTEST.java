package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

public class ValueInstantiatorTest {

    

    @Test
    public void testValueInstantiator_getValueClass() {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", valueInstantiator.getValueClass().getName());

        ValueInstantiator valueInstantiatorInt = new ValueInstantiator.Base(int.class);
        assertEquals("int", valueInstantiatorInt.getValueClass().getName());
    }

    @Test
    public void testValueInstantiator_getValueTypeDesc() {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());

        ValueInstantiator valueInstantiatorInt = new ValueInstantiator.Base(int.class);
        assertEquals("int", valueInstantiatorInt.getValueTypeDesc());
    }

    @Test
    public void testValueInstantiator_canInstantiate_default() {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testValueInstantiator_canInstantiate_false() {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(int[].class);
        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test(expected = AssertionError.class)
    public void testValueInstantiator_canCreateFromString_default() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String value = "value";
        Object result = valueInstantiator.createFromString(ctxt, value);
    }

    @Test
    public void testValueInstantiator_canCreateFromString_true() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                assertEquals("value", value);
                return "string";
            }
        };
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String value = "value";
        Object result = valueInstantiator.createFromString(ctxt, value);
        assertEquals("string", result);
    }

    @Test(expected = AssertionError.class)
    public void testValueInstantiator_createFromObjectWith_null() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(MyClass.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SettableBeanProperty[] props = new SettableBeanProperty[] {
                mapper2.findBeanProperty("prop1", MyClass.class),
                mapper2.findBeanProperty("prop2", MyClass.class)
        };
        Object result = valueInstantiator.createFromObjectWith(ctxt, props);
    }

    @Test
    public void testValueInstantiator_createFromObjectWith_success() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] props) throws IOException {
                Object[] args = new Object[2];
                args[0] = "prop1Value";
                args[1] = 1;
                return new MyClass(args);
            }
        };
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SettableBeanProperty[] props = new SettableBeanProperty[] {
                mapper2.findBeanProperty("prop1", MyClass.class),
                mapper2.findBeanProperty("prop2", MyClass.class)
        };
        Object result = valueInstantiator.createFromObjectWith(ctxt, props);
        assertEquals(MyClass.class, result.getClass());
        MyClass myClass = (MyClass) result;
        assertEquals("prop1Value", myClass.getProp1());
        assertEquals(1, myClass.getProp2());
    }

    @JsonDeserialize(using = MyClassDeserializer.class)
    static class MyClass {
        private String prop1;
        private int prop2;

        public MyClass(Object... args) {
            this.prop1 = (String) args[0];
            this.prop2 = (int) args[1];
        }

        public String getProp1() {
            return prop1;
        }

        public int getProp2() {
            return prop2;
        }

        @JsonCreator
        public static MyClass create(@JsonProperty("prop1") String prop1,
                @JsonProperty("prop2") int prop2) {
            return new MyClass(prop1, prop2);
        }
    }

    @JsonDeserialize(using = SnakeCaseStrategy.class)
    static class MyClassDeserializer extends StdValueInstantiator {
        @Override
        public boolean canCreateUsingObject() {
            return true;
        }

        @Override
        public Object createUsingObject(DeserializationContext ctxt, Object bean) throws IOException {
            return new MyClass((String) ctxt.readValueAs(String.class, ctxt.getParser()),
                    ctxt.readValueAs(int.class, ctxt.getParser()));
        }
    }

    
}