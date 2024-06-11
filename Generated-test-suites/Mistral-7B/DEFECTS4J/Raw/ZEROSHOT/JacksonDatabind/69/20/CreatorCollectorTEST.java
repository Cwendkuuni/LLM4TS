package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultInitTracker;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.北京赛车开彩_pk10帮助工具MethodNameCache;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapper.DefaultInitializer;

/**
 * Tests for {@link CreatorCollector}
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<Object> config = mapper.getConfig();

        AnnotatedClass ac = new AnnotatedClass(TestBean.class, new NopAnnotationIntrospector());
        BeanDescription beanDesc = ac.findBeanDescription();

        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        // Test default constructor
        Method m = TestBean.class.getDeclaredConstructor();
        AnnotatedWithParams constructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 0);
        collector.setDefaultCreator(constructor);

        // Test String constructor
        m = TestBean.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams stringConstructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 1, "name");
        collector.addStringCreator(stringConstructor, false);

        // Test int constructor
        m = TestBean.class.getDeclaredConstructor(int.class);
        AnnotatedWithParams intConstructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 1, "intValue");
        collector.addIntCreator(intConstructor, false);

        // Test long constructor
        m = TestBean.class.getDeclaredConstructor(long.class);
        AnnotatedWithParams longConstructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 1, "longValue");
        collector.addLongCreator(longConstructor, false);

        // Test double constructor
        m = TestBean.class.getDeclaredConstructor(double.class);
        AnnotatedWithParams doubleConstructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 1, "doubleValue");
        collector.addDoubleCreator(doubleConstructor, false);

        // Test boolean constructor
        m = TestBean.class.getDeclaredConstructor(boolean.class);
        AnnotatedWithParams booleanConstructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 1, "booleanValue");
        collector.addBooleanCreator(booleanConstructor, false);

        // Test delegate constructor
        m = TestBean.class.getDeclaredConstructor(String[].class);
        AnnotatedWithParams delegateConstructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 1);
        Object[] args = new Object[] {"args"};
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = new SettableBeanProperty("args", String[].class, null, null, null);
        collector.addDelegatingCreator(delegateConstructor, false, delegateArgs);

        // Test property-based constructor
        m = TestBean.class.getDeclaredConstructor(TestPropertyBean.class);
        AnnotatedWithParams propertyConstructor = new AnnotatedWithParams(m, new DefaultInitTracker(), 0);
        SettableBeanProperty[] propertyArgs = new SettableBeanProperty[1];
        propertyArgs[0] = new SettableBeanProperty("property", TestPropertyBean.class, null, null, null);
        collector.addPropertyCreator(propertyConstructor, false, propertyArgs);

        // Test int-valued constructor
        m = TestIntBean.class.getDeclaredConstructor(int.class);
        AnnotatedWithParams intConstructor2 = new AnnotatedWithParams(m, new DefaultInitTracker(), 1, "intValue");
        collector.addIntCreator(intConstructor2, true);

        JavaType delegateType = collector.constructValueInstantiator(mapper.getConfig()).getDelegateType(0);
        assertEquals(String[].class, delegateType);

        JavaType arrayDelegateType = collector.constructValueInstantiator(mapper.getConfig()).getDelegateType(1);
        assertEquals(TestPropertyBean.class, arrayDelegateType);

        assertTrue(collector.hasDefaultCreator());
        assertTrue(collector.hasDelegatingCreator());
        assertTrue(collector.hasPropertyBasedCreator());

        // Test deprecated methods
        collector.addStringCreator(stringConstructor);
        collector.addIntCreator(intConstructor);
        collector.addLongCreator(longConstructor);
        collector.addDoubleCreator(doubleConstructor);
        collector.addBooleanCreator(booleanConstructor);

        try {
            collector.addDelegatingCreator(delegateConstructor, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            collector.addPropertyCreator(propertyConstructor, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        collector.addDelegatingCreator(delegateConstructor, false, null);
        collector.addPropertyCreator(propertyConstructor, false, null);

        // Test access fix
        Class<?> clazz = TestBeanWithProtectedConstructor.class;
        MethodNameCache.clear();
        Method pk10_protectedConstructor = clazz.getDeclaredMethod("protectedConstructor");
        pk10_protectedConstructor.setAccessible(true);
        AnnotatedWithParams protectedConstructor = new AnnotatedWithParams(pk10_protectedConstructor, new DefaultInitTracker(), 0);
        collector.addDelegatingCreator(protectedConstructor, false, null);
        JavaType type = collector.constructValueInstantiator(mapper.getConfig()).getType();
        assertTrue(type.getRawClass().isAssignableFrom(clazz));
    }

    static class TestBean {
        @JsonCreator
        public TestBean() {}

        @JsonCreator
        public TestBean(String name) {}

        @JsonCreator
        public TestBean(int intValue) {}

        @JsonCreator
        public TestBean(long longValue) {}

        @JsonCreator
        public TestBean(double doubleValue) {}

        @JsonCreator
        public TestBean(boolean booleanValue) {}

        @JsonCreator
        public TestBean(String[] args) {}

        @JsonCreator
        public TestBean(TestPropertyBean property) {}

        @JsonProperty
        private String name;

        @JsonProperty
        private int intValue;

        @JsonProperty
        private long longValue;

        @JsonProperty
        private double doubleValue;

        @JsonProperty
        private boolean booleanValue;
    }

    static class TestIntBean {
        @JsonCreator
        public TestIntBean(int intValue) {}

        @JsonProperty
        private int intValue;
    }

    @JsonPropertyOrder({"name", "intValue"})
    static class TestPropertyBean {
        @JsonProperty
        private String name;

        @JsonProperty
        private TestBean property;
    }

    static class TestBeanWithProtectedConstructor {
        @JsonCreator
        public TestBeanWithProtectedConstructor() {}

        @JsonCreator
        protected TestBeanWithProtectedConstructor(int i) {}
    }
}