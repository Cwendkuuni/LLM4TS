package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameter;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.AnnotatedWithParams;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedTypeImpl;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for CreatorCollector class.
 */
public class CreatorCollectorTest {

    private CreatorCollector collector;
    private BeanDescription beanDesc;
    private DeserializationConfig config;

    @Before
    public void setUp() throws Exception {
        beanDesc = new BeanDescription(new BeanDescAnnotatedClass());
        config = new DeserializationConfig(new DeserializationFeature[0]);
        collector = new CreatorCollector(beanDesc, true);
    }

    @Test
    public void testSetDefaultCreator() {
        Method defaultMethod = new BeanClass().getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams defaultAnnotation = new AnnotatedWithParams(new JacksonAnnotation(DefaultConstruct.class), defaultMethod);
        collector.setDefaultCreator(defaultAnnotation);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultCreatorTwice() {
        Method defaultMethod = new BeanClass().getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams defaultAnnotation = new AnnotatedWithParams(new JacksonAnnotation(DefaultConstruct.class), defaultMethod);
        collector.setDefaultCreator(defaultAnnotation);
        collector.setDefaultCreator(defaultAnnotation);
    }

    @Test
    public void testAddStringCreator() {
        Method stringMethod = new BeanClass().getDeclaredMethod("stringConstructor", String.class);
        AnnotatedWithParams stringAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), stringMethod,
                new AnnotatedTypeImpl(String.class));
        collector.addStringCreator(stringAnnotation, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStringCreatorTwice() {
        Method stringMethod = new BeanClass().getDeclaredMethod("stringConstructor", String.class);
        AnnotatedWithParams stringAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), stringMethod,
                new AnnotatedTypeImpl(String.class));
        collector.addStringCreator(stringAnnotation, false);
        collector.addStringCreator(stringAnnotation, false);
    }

    @Test
    public void testAddIntCreator() {
        Method intMethod = new BeanClass().getDeclaredMethod("intConstructor", int.class);
        AnnotatedWithParams intAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), intMethod,
                new AnnotatedTypeImpl(Integer.class));
        collector.addIntCreator(intAnnotation, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIntCreatorTwice() {
        Method intMethod = new BeanClass().getDeclaredMethod("intConstructor", int.class);
        AnnotatedWithParams intAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), intMethod,
                new AnnotatedTypeImpl(Integer.class));
        collector.addIntCreator(intAnnotation, false);
        collector.addIntCreator(intAnnotation, false);
    }

    @Test
    public void testAddLongCreator() {
        Method longMethod = new BeanClass().getDeclaredMethod("longConstructor", long.class);
        AnnotatedWithParams longAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), longMethod,
                new AnnotatedTypeImpl(Long.class));
        collector.addLongCreator(longAnnotation, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLongCreatorTwice() {
        Method longMethod = new BeanClass().getDeclaredMethod("longConstructor", long.class);
        AnnotatedWithParams longAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), longMethod,
                new AnnotatedTypeImpl(Long.class));
        collector.addLongCreator(longAnnotation, false);
        collector.addLongCreator(longAnnotation, false);
    }

    @Test
    public void testAddDoubleCreator() {
        Method doubleMethod = new BeanClass().getDeclaredMethod("doubleConstructor", double.class);
        AnnotatedWithParams doubleAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), doubleMethod,
                new AnnotatedTypeImpl(Double.class));
        collector.addDoubleCreator(doubleAnnotation, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingDouble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDoubleCreatorTwice() {
        Method doubleMethod = new BeanClass().getDeclaredMethod("doubleConstructor", double.class);
        AnnotatedWithParams doubleAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), doubleMethod,
                new AnnotatedTypeImpl(Double.class));
        collector.addDoubleCreator(doubleAnnotation, false);
        collector.addDoubleCreator(doubleAnnotation, false);
    }

    @Test
    public void testAddBooleanCreator() {
        Method booleanMethod = new BeanClass().getDeclaredMethod("booleanConstructor", boolean.class);
        AnnotatedWithParams booleanAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), booleanMethod,
                new AnnotatedTypeImpl(Boolean.class));
        collector.addBooleanCreator(booleanAnnotation, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBooleanCreatorTwice() {
        Method booleanMethod = new BeanClass().getDeclaredMethod("booleanConstructor", boolean.class);
        AnnotatedWithParams booleanAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), booleanMethod,
                new AnnotatedTypeImpl(Boolean.class));
        collector.addBooleanCreator(booleanAnnotation, false);
        collector.addBooleanCreator(booleanAnnotation, false);
    }

    @Test
    public void testAddDelegatingCreator() {
        Class<? extends CreatorProperty> creatorClass = CreatorProperty.class;
        Method delegateMethod = new BeanClass().getDeclaredMethod("delegateConstructor", CreatorProperty[].class);
        Object[] delegateArgs = new Object[] { new CreatorProperty(new AnnotatedElement() {
            @Override
            public AnnotatedType<CreatorProperty> getAnnotatedType() {
                return new AnnotatedTypeImpl(creatorClass);
            }
        }, "a") };
        AnnotatedWithParams delegateAnnotation = new AnnotatedWithParams(new JacksonAnnotation(Creator.class), delegateMethod,
                new AnnotatedTypeImpl(new Class[] { CreatorProperty[].class }));
        collector.addDelegatingCreator(delegateAnnotation, false, (CreatorProperty[]) delegateArgs);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingDelegate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDelegatingCreatorTwice() {
        Class<? extends CreatorProperty> creatorClass = CreatorProperty.class;
        Method delegateMethod = new BeanClass().getDeclaredMethod("delegateConstructor", CreatorProperty[].class);
        Object[] delegateArgs = new Object[] { new CreatorProperty(new AnnotatedElement() {
            @Override
            public AnnotatedType<CreatorProperty> getAnnotatedType() {
                return new AnnotatedTypeImpl(creatorClass);
            }
        }, "a") };
        AnnotatedWithParams delegateAnnotation = new AnnotatedWithParams(new JacksonAnnotation(Creator.class), delegateMethod,
                new AnnotatedTypeImpl(new Class[] { CreatorProperty[].class }));
        collector.addDelegatingCreator(delegateAnnotation, false, (CreatorProperty[]) delegateArgs);
        collector.addDelegatingCreator(delegateAnnotation, false, (CreatorProperty[]) delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        Class<? extends CreatorProperty> creatorClass = CreatorProperty.class;
        Method propertyMethod = new BeanClass().getDeclaredMethod("propertyConstructor", CreatorProperty[].class);
        Object[] propertyArgs = new Object[] { new CreatorProperty(new AnnotatedElement() {
            @Override
            public AnnotatedType<CreatorProperty> getAnnotatedType() {
                return new AnnotatedTypeImpl(creatorClass);
            }
        }, "a"), new CreatorProperty(new AnnotatedElement() {
            @Override
            public AnnotatedType<CreatorProperty> getAnnotatedType() {
                return new AnnotatedTypeImpl(creatorClass);
            }
        }, "b") };
        AnnotatedWithParams propertyAnnotation = new AnnotatedWithParams(new JacksonAnnotation(Creator.class), propertyMethod,
                new AnnotatedTypeImpl(new Class[] { CreatorProperty[].class }));
        collector.addPropertyCreator(propertyAnnotation, false, (CreatorProperty[]) propertyArgs);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingProperties());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyCreatorTwice() {
        Class<? extends CreatorProperty> creatorClass = CreatorProperty.class;
        Method propertyMethod = new BeanClass().getDeclaredMethod("propertyConstructor", CreatorProperty[].class);
        Object[] propertyArgs = new Object[] { new CreatorProperty(new AnnotatedElement() {
            @Override
            public AnnotatedType<CreatorProperty> getAnnotatedType() {
                return new AnnotatedTypeImpl(creatorClass);
            }
        }, "a"), new CreatorProperty(new AnnotatedElement() {
            @Override
            public AnnotatedType<CreatorProperty> getAnnotatedType() {
                return new AnnotatedTypeImpl(creatorClass);
            }
        }, "b") };
        AnnotatedWithParams propertyAnnotation = new AnnotatedWithParams(new JacksonAnnotation(Creator.class), propertyMethod,
                new AnnotatedTypeImpl(new Class[] { CreatorProperty[].class }));
        collector.addPropertyCreator(propertyAnnotation, false, (CreatorProperty[]) propertyArgs);
        collector.addPropertyCreator(propertyAnnotation, false, (CreatorProperty[]) propertyArgs);
    }

    @Test
    public void testAddIncompleteParameter() {
        Method incompleteMethod = new BeanClass().getDeclaredMethod("incompleteConstructor", AnnotatedParameter[].class);
        AnnotatedParameter incompleteParam = new AnnotatedParameter(new AnnotatedElement() {
            @Override
            public AnnotatedType<AnnotatedParameter> getAnnotatedType() {
                return new AnnotatedTypeImpl(AnnotatedParameter[].class);
            }
        }, 0, incompleteMethod);
        collector.addIncompleteParameter(incompleteParam);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingIncompleteParameter());
    }

    @Test
    public void testConstructValueInstantiator() throws Exception {
        Method defaultMethod = new BeanClass().getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams defaultAnnotation = new AnnotatedWithParams(new JacksonAnnotation(DefaultConstruct.class), defaultMethod);
        Method stringMethod = new BeanClass().getDeclaredMethod("stringConstructor", String.class);
        AnnotatedWithParams stringAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), stringMethod,
                new AnnotatedTypeImpl(String.class));
        Method intMethod = new BeanClass().getDeclaredMethod("intConstructor", int.class);
        AnnotatedWithParams intAnnotation = new AnnotatedWithParams(new JacksonAnnotation(ValueConstructor.class), intMethod,
                new AnnotedTypeImpl(Integer.class));
        collector.setDefaultCreator(defaultAnnotation);
        collector.addStringCreator(stringAnnotation, false);
        collector.addIntCreator(intAnnotation, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertTrue(instantiator instanceof StdValueInstantiator);
        assertTrue(instantiator.canCreateUsingDefault());
        assertTrue(instantiator.canCreateUsingString());
        assertTrue(instantiator.canCreateUsingInt());
    }

}

class BeanClass {
    private BeanClass() {}

    public void defaultConstructor() {}

    public void stringConstructor(String str) {}

    public void intConstructor(int i) {}

    public void longConstructor(long l) {}

    public void doubleConstructor(double d) {}

    public void booleanConstructor(boolean b) {}

    public void delegateConstructor(CreatorProperty[] args) {}

    public void propertyConstructor(CreatorProperty[] args) {}

    public void incompleteConstructor(AnnotatedParameter[] args) {}
}

class DefaultConstruct {
    @jackson.annotation.JacksonAnnotation
    public @interface DefaultConstruct {
    }
}

class ValueConstructor {
    @jackson.annotation.JacksonAnnotation
    public @interface ValueConstructor {
    }
}