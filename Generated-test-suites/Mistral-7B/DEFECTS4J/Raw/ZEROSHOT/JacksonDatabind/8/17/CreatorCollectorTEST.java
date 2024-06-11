package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedCreatorProperty;
import com.fasterxml.jackson.databind.introspect.NestedPropertyMIterator;
import com.fasterxml.jackson.databind.introspect. VisibleAnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(TestBean.class), ObjectMapper.getDefaultInstance().getConfig(), null);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        // Test default constructor
        Method method = TestBean.class.getDeclaredConstructor();
        AnnotatedWithParams constructor = new AnnotatedWithParams(method, new PropertyName("defaultConstructor"), null, null, null);
        collector.setDefaultCreator(constructor);

        // Test string constructor
        method = TestBean.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams stringConstructor = new AnnotatedWithParams(method, new PropertyName("stringConstructor"), null, null, new Object[]{"arg1"});
        collector.addStringCreator(stringConstructor, true);

        // Test int constructor
        method = TestBean.class.getDeclaredConstructor(int.class);
        AnnotatedWithParams intConstructor = new AnnotatedWithParams(method, new PropertyName("intConstructor"), null, null, new Object[]{1});
        collector.addIntCreator(intConstructor, true);

        // Test long constructor
        method = TestBean.class.getDeclaredConstructor(long.class);
        AnnotatedWithParams longConstructor = new AnnotatedWithParams(method, new PropertyName("longConstructor"), null, null, new Object[]{1L});
        collector.addLongCreator(longConstructor, true);

        // Test double constructor
        method = TestBean.class.getDeclaredConstructor(double.class);
        AnnotatedWithParams doubleConstructor = new AnnotatedWithParams(method, new PropertyName("doubleConstructor"), null, null, new Object[]{1.0});
        collector.addDoubleCreator(doubleConstructor, true);

        // Test boolean constructor
        method = TestBean.class.getDeclaredConstructor(boolean.class);
        AnnotatedWithParams booleanConstructor = new AnnotatedWithParams(method, new PropertyName("booleanConstructor"), null, null, new Object[]{true});
        collector.addBooleanCreator(booleanConstructor, true);

        // Test delegating constructor
        method = TestBean.class.getDeclaredConstructor(TestDelegate.class);
        AnnotatedWithParams delegatingConstructor = new AnnotatedWithParams(method, new PropertyName("delegatingConstructor"), null, null, null);
        AnnotatedMember member = new NestedCreatorProperty(TestDelegate.class, "delegateProperty", null, null, null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty(member, null, null, null, null);
        collector.addDelegatingCreator(delegatingConstructor, true, injectables);

        // Test property-based constructor
        Class<?> clazz = TestBeanWithProperties.class;
        BeanDescription beanDescWithProperties = new BeanDescription(new AnnotatedClass(clazz), ObjectMapper.getDefaultInstance().getConfig(), null);
        CreatorCollector collectorWithProperties = new CreatorCollector(beanDescWithProperties, false);
        NestedPropertyMIterator it = beanDescWithProperties.findProperties();
        while (it.hasNext()) {
            BeanPropertyDefinition propDef = it.next();
            Method getter = propDef.getMember();
            PropertyName propertyName = new PropertyName(getName(getter));
            AnnotatedWithParams propertyConstructor = new AnnotatedWithParams(getter, propertyName, null, null, null);
            collectorWithProperties.addPropertyCreator(propertyConstructor, true, new CreatorProperty[1]);
        }

        ValueInstantiator instantiator = collector.constructValueInstantiator(ObjectMapper.getDefaultInstance().getConfig());

        // Test default constructor
        assertNotNull(instantiator.createUsingDefault(null));

        // Test string constructor
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        TestBean bean = mapper.readValue(mapper.writeValueAsString("arg1"), TestBean.class);
        assertNotNull(bean);
        assertEquals("arg1", bean.getArg1());

        // Test int constructor
        bean = mapper.readValue(mapper.writeValueAsString("1"), TestBean.class);
        assertNotNull(bean);
        assertEquals(1, bean.getIntArg());

        // Test long constructor
        bean = mapper.readValue(mapper.writeValueAsString("1"), TestBean.class);
        assertNotNull(bean);
        assertEquals(1L, bean.getLongArg());

        // Test double constructor
        bean = mapper.readValue(mapper.writeValueAsString("1.0"), TestBean.class);
        assertNotNull(bean);
        assertEquals(1.0, bean.getDoubleArg(), 0.001);

        // Test boolean constructor
        bean = mapper.readValue(mapper.writeValueAsString("true"), TestBean.class);
        assertNotNull(bean);
        assertTrue(bean.isBooleanArg());

        // Test delegating constructor
        TestDelegate delegate = new TestDelegate();
        TestBean delegatingBean = mapper.readValue(mapper.writeValueAsString(delegate), TestBean.class);
        assertNotNull(delegatingBean);
        assertEquals(delegate, delegatingBean.getDelegate());

        // Test property-based constructor
        TestBeanWithProperties beanWithProperties = mapper.readValue(mapper.writeValueAsString("{ \"arg1\": \"value1\", \"arg2\": 1 }"), TestBeanWithProperties.class);
        assertNotNull(beanWithProperties);
        assertEquals("value1", beanWithProperties.getArg1());
        assertEquals(1, beanWithProperties.getIntArg());

        // Test that no other creators are used
        StdValueInstantiator stdInst = (StdValueInstantiator) instantiator;
        assertNull(stdInst.getDefaultValueFactory());
    }

    private String getName(Member member) {
        if (member instanceof Method) {
            Method method = (Method) member;
            JsonCreator creatorAnnotation = method.getAnnotation(JsonCreator.class);
            return creatorAnnotation != null ? creatorAnnotation.name() : method.getName();
        }
        PropertyName name = ((PropertyName) member).getBaseName();
        return name != null ? name.getShortName() : member.getName();
    }
}

class TestBean {
    private String arg1;

    public String getArg1() { return arg1; }

    public TestBean() {
        this("default");
    }

    public TestBean(String arg1) {
        this.arg1 = arg1;
    }
}

class TestBeanWithProperties {
    private String arg1;
    private int arg2;

    public String getArg1() { return arg1; }

    public int getIntArg() { return arg2; }

    public TestBeanWithProperties() {
        this("default");
    }

    public TestBeanWithProperties(String arg1, int arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}

class TestBeanDelegate {
    private TestDelegate delegate;

    public TestDelegate getDelegate() { return delegate; }

    public TestBeanDelegate() {
        this(new TestDelegate());
    }

    public TestBeanDelegate(TestDelegate delegate) {
        this.delegate = delegate;
    }
}

class TestDelegate {
    private String delegateProperty;

    public String getDelegateProperty() { return delegateProperty; }

    public TestDelegate() {
        this(null);
    }

    @JsonProperty("delegate")
    public TestDelegate(String delegateProperty) {
        this.delegateProperty = delegateProperty;
    }
}