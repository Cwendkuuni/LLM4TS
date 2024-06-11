package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedName;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test for {@link CreatorCollector} class.
 */
public class CreatorCollectorTest {

    private static class TestBean {
        private TestBean() {}

        @JsonCreator
        public static TestBean create(int i, String s) {
            return new TestBean();
        }

        @JsonProperty
        public void setI(int i) {}

        @JsonProperty
        public void setS(String s) {}

        public int getI() {
            return 0;
        }

        public String getS() {
            return "";
        }
    }

    private static class TestBean2 {
        private TestBean2() {}

        @JsonProperty
        public void setI1(int i1) {}

        @JsonProperty
        public void setI2(int i2) {}

        @JsonCreator
        public static TestBean2 create(@JsonProperty("i1") int i1, @JsonProperty("i2") int i2) {
            return new TestBean2();
        }

        public int getI1() {
            return 0;
        }

        public int getI2() {
            return 0;
        }
    }

    private DeserializationConfig config;
    private BeanDescription beanDesc;
    private CreatorCollector collector;

    @Before
    public void setUp() throws Exception {
        config = new DeserializationConfig();
        beanDesc = new BeanDescription(TestBean.class, config);
        collector = new CreatorCollector(beanDesc, true);
    }

    @Test
    public void testDefaultCreator() {
        Method defaultCtor = TestBean.class.getDeclaredConstructor();
        collector.setDefaultCreator(new AnnotatedWithParams(defaultCtor));

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean.class, inst.getType());
    }

    @Test
    public void testStringCreator() throws Exception {
        Method stringCtor = TestBean.class.getDeclaredMethod("create", String.class);
        collector.addStringCreator(new AnnotatedWithParams(stringCtor, true));

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean.class, inst.getType());
    }

    @Test
    public void testIntCreator() throws Exception {
        Method intCtor = TestBean.class.getDeclaredConstructor(int.class);
        collector.addIntCreator(new AnnotatedWithParams(intCtor, true));

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean.class, inst.getType());
    }

    @Test
    public void testLongCreator() throws Exception {
        Method longCtor = TestBean.class.getDeclaredConstructor(long.class);
        collector.addLongCreator(new AnnotatedWithParams(longCtor, true));

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean.class, inst.getType());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        Method doubleCtor = TestBean.class.getDeclaredConstructor(double.class);
        collector.addDoubleCreator(new AnnotatedWithParams(doubleCtor, true));

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean.class, inst.getType());
    }

    @Test
    public void testBooleanCreator() throws Exception {
        Method booleanCtor = TestBean.class.getDeclaredConstructor(boolean.class);
        collector.addBooleanCreator(new AnnotatedWithParams(booleanCtor, true));

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean.class, inst.getType());
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        Class<TestBean2> testBean2Class = TestBean2.class;
        AnnotatedClass ac = Introspector.getAnnotatedClass(testBean2Class);
        AnnotatedConstructor ac2 = ac.findConstructor(new AnnotatedParameter[0]);

        CreatorProperty[] injectables = new CreatorProperty[0];
        collector.addDelegatingCreator(new AnnotatedWithParams(ac2), true, injectables);

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean2.class, inst.getType());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        Method setI1 = TestBean2.class.getDeclaredMethod("setI1", int.class);
        Method setI2 = TestBean2.class.getDeclaredMethod("setI2", int.class);
        AnnotatedField i1Fld = Introspector.findAnnotated(TestBean2.class, "i1");
        AnnotatedField i2Fld = Introspector.findAnnotated(TestBean2.class, "i2");

        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty(setI1, i1Fld, null);
        properties[1] = new CreatorProperty(setI2, i2Fld, null);

        collector.addPropertyCreator(new AnnotatedWithParams(TestBean2.class.getDeclaredConstructor()), false, properties);

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean2.class, inst.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        Method intCtor = TestBean.class.getDeclaredConstructor(int.class);
        collector.addIntCreator(new AnnotatedWithParams(intCtor, true));

        Method intCtor2 = TestBean.class.getDeclaredConstructor(int.class);
        collector.addIntCreator(new AnnotatedWithParams(intCtor2, true));

        ValueInstantiator inst = collector.constructValueInstantiator(config);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() throws Exception {
        Method setI1 = TestBean2.class.getDeclaredMethod("setI1", int.class);
        Method setI2 = TestBean2.class.getDeclaredMethod("setI2", int.class);
        AnnotatedField i1Fld = Introspector.findAnnotated(TestBean2.class, "i1");
        AnnotatedField i2Fld = Introspector.findAnnotated(TestBean2.class, "i2");

        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty(setI1, i1Fld, null);
        properties[1] = new CreatorProperty(setI2, i2Fld, null);

        collector.addPropertyCreator(new AnnotatedWithParams(TestBean2.class.getDeclaredConstructor()), false, properties);

        CreatorProperty[] properties2 = new CreatorProperty[2];
        properties2[0] = new CreatorProperty(setI1, i1Fld, null);
        properties2[1] = new CreatorProperty(setI2, i2Fld, null);

        try {
            collector.addPropertyCreator(new AnnotatedWithParams(TestBean2.class.getDeclaredConstructor()), false, properties2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testVanillaCreator() throws Exception {
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        assertEquals(TestBean.class, inst.getType());
        inst = inst.getInstance(config);
        assertNotNull(inst);

        JavaType type = collector.getType();
        assertEquals(TestBean.class, type.getRawClass());

        ValueDeserializer deserializer = inst.createValueDeserializer(config, type, null);
        assertTrue(deserializer instanceof StdValueInstantiator);
    }

    private static class AnnotatedWithParams implements AnnotatedMember {
        private final AnnotatedMember member;
        private final JavaType type;

        public AnnotatedWithParams(AnnotatedMember member) {
            this.member = member;
            this.type = null;
        }

        public AnnotatedWithParams(AnnotatedMember member, JavaType type) {
            this.member = member;
            this.type = type;
        }

        @Override
        public AnnotatedClass getAnnotated() {
            return Introspector.findAnnotated(member.getAnnotated().getType(), member.getName());
        }

        @Override
        public AnnotatedConstructor getDeclaredConstructor() {
            return (AnnotatedConstructor) member;
        }

        @Override
        public AnnotatedField getDeclaredField() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotatedMethod getDeclaredMethod(String name, Class<?>... parameterTypes) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotatedMethod getMethod(String name, Class<?>... parameterTypes) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotatedParameter[] getParameters() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotationIntrospector introspect() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NestedName getNestedName() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public String toString() {
            return member.toString();
        }

        @Override
        public JavaType getGenericParameterType(int i) {
            return type;
        }
    }
}

class CreatorProperty {
    private final AnnotatedMethod method;
    private final AnnotatedField field;

    public CreatorProperty(AnnotatedMethod method) {
        this.method = method;
        this.field = null;
    }

    public CreatorProperty(AnnotatedField field) {
        this.field = field;
        this.method = null;
    }

    public AnnotatedMethod getMethod() {
        return method;
    }

    public AnnotatedField getField() {
        return field;
    }

    public String getName() {
        return method != null ? method.getName() : field.getName();
    }
}

class AnnotatedWithParams implements AnnotatedMember {
    // ...
    private JavaType type;

    // ...

    public JavaType getGenericParameterType(int i) {
        return type == null ? null : type.getParameterTypes()[i];
    }
}