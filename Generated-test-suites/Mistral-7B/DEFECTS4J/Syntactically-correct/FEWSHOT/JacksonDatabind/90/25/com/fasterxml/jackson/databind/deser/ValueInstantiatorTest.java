package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind. JaneDo.ValueInstantiator.Base.getValueClass;
import static com.fasterxml.jackson.databind.JaneDo.ValueInstantiator.Base.getValueTypeDesc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator.Base {
        @Override
        public boolean canCreateUsingDefault() {
            return true;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    @Test
    public void testValueClass() {
        Class<?> valueClass = new TestValueInstantiator(String.class).getValueClass();
        assertEquals(String.class, valueClass);
    }

    @Test
    public void testValueTypeDesc() {
        String valueTypeDesc = new TestValueInstantiator(String.class).getValueTypeDesc();
        assertEquals("java.lang.String", valueTypeDesc);

        valueTypeDesc = new TestValueInstantiator(Integer.TYPE).getValueTypeDesc();
        assertEquals("int", valueTypeDesc);

        valueTypeDesc = new TestValueInstantiator(Integer.class).getValueTypeDesc();
        assertEquals("java.lang.Integer", valueTypeDesc);
    }

    @Test
    public void testCanInstantiate() {
        TestValueInstantiator tvi = new TestValueInstantiator(String.class);
        assertTrue(tvi.canInstantiate());

        TestValueInstantiator intTvi = new TestValueInstantiator(int[].class);
        assertFalse(intTvi.canInstantiate());

        TestValueInstantiator stringIntTvi = new TestValueInstantiator(String[].class);
        assertTrue(stringIntTvi.canInstantiate());
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        TestValueInstantiator tvi = new TestValueInstantiator(TestValueInstantiator.class);
        assertFalse(tvi.canCreateFromObjectWith());

        TestValueInstantiator pojoTvi = new TestValueInstantiator(TestPojo.class);
        SettableBeanProperty[] props = new SettableBeanProperty[]{
            ObjectCreatorTest.createProperty("name", String.class),
            ObjectCreatorTest.createProperty("age", Integer.TYPE),
            ObjectCreatorTest.createProperty("booleanValue", Boolean.TYPE)
        };
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);
        node.put("booleanValue", true);
        ObjectNode pojoNode = ObjectMapper.getDefaultInstance().treeToObject(node, TestPojo.class);

        Object created = tvi.createFromObjectWith(ObjectMapper.getDefaultInstance().getDeserializationContext(), props);
        TestPojo instance = (TestPojo) created;
        assertEquals("John Doe", instance.getName());
        assertEquals(30, instance.getAge());
        assertTrue(instance.isBooleanValue());
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        TestValueInstantiator tvi = new TestValueInstantiator(TestValueInstantiator.class);
        assertFalse(tvi.canCreateUsingDelegate());

        TestValueInstantiator delegateTvi = new TestValueInstantiator(TestDelegate.class);
        TestValueInstantiator tviWithDelegate = new TestValueInstantiator(TestValueInstantiatorWithDelegate.class);
        assertSame(delegateTvi, tviWithDelegate.getDelegateCreator());

        TestValueInstantiator created = tviWithDelegate.createUsingDelegate(ObjectMapper.getDefaultInstance().getDeserializationContext(), delegateTvi);
        TestValueInstantiator instance = (TestValueInstantiator) created;
        assertSame(delegateTvi, instance.getDelegateCreator());
    }

    static class TestPojo {
        private String name;
        private int age;
        private boolean booleanValue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isBooleanValue() {
            return booleanValue;
        }

        public void setBooleanValue(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }
    }

    static class TestDelegate {
        public TestValueInstantiator create() {
            return new TestValueInstantiator(String.class);
        }
    }

    static class TestValueInstantiatorWithDelegate extends ValueInstantiator.Base {
        @Override
        public boolean canCreateUsingDelegate() {
            return true;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return delegate.getClass() == TestValueInstantiator.class ? new TestValueInstantiator(String.class) : null;
        }
    }
}

class ObjectCreatorTest {
    public static SettableBeanProperty createProperty(String name, Class<?> propertyType) {
        SettableBeanProperty p = new SettableBeanProperty();
        p.setName(name);
        p.setPropertyType(propertyType);
        return p;
    }
}