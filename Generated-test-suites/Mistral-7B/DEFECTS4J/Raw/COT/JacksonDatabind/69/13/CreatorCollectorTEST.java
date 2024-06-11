package com.example; // replace with your test package name

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameter;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.JUnit4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithMembers;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(JUnit4.class)
public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    private MapperConfig<?> config;

    @Before
    public void setup() {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        beanDescription = new BeanDescription(annotatedClass);
        config = mock(MapperConfig.class);
        creatorCollector = new CreatorCollector(beanDescription, config);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(defaultCreator);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams stringCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator, false);
        assertTrue(creatorCollector.hasCreatorForType(AnnotatedType.class, String.class));
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams intCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(intCreator, false);
        assertTrue(creatorCollector.hasCreatorForType(AnnotatedType.class, int.class));
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams longCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(longCreator, false);
        assertTrue(creatorCollector.hasCreatorForType(AnnotatedType.class, long.class));
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams doubleCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(doubleCreator, false);
        assertTrue(creatorCollector.hasCreatorForType(AnnotatedType.class, double.class));
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams booleanCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(booleanCreator, false);
        assertTrue(creatorCollector.hasCreatorForType(AnnotatedType.class, boolean.class));
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams delegatingCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = mock(SettableBeanProperty.class);
        creatorCollector.addDelegatingCreator(delegatingCreator, false, injectables);
        assertArrayEquals(new SettableBeanProperty[1], creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams propertyCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = mock(SettableBeanProperty.class);
        properties[1] = mock(SettableBeanProperty.class);
        creatorCollector.addPropertyCreator(propertyCreator, false, properties);
        assertArrayEquals(new SettableBeanProperty[2], creatorCollector._propertyBasedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        creatorCollector.addStringCreator(creator, false);
    }

    @Test
    public void testConstructValueInstantiatorWithDefaultCreator() {
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(defaultCreator);
        StdValueInstantiator inst = creatorCollector.constructValueInstantiator(null);
        assertNotNull(inst);
    }

    @Test
    public void testConstructValueInstantiatorWithCustomCreators() {
        AnnotatedWithParams stringCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator, false);
        AnnotatedWithParams intCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(intCreator, false);
        StdValueInstantiator inst = creatorCollector.constructValueInstantiator(null);
        assertTrue(inst.canCreateUsingString());
        assertTrue(inst.canCreateUsingInt());
    }

    @Test
    public void testConstructValueInstantiatorWithDelegateCreator() {
        AnnotatedWithParams delegatingCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = mock(SettableBeanProperty.class);
        creatorCollector.addDelegatingCreator(delegatingCreator, false, injectables);
        JavaType delegateType = creatorCollector._computeDelegateType(delegatingCreator, injectables);
        assertNotNull(delegateType);
    }

    @Test
    public void testConstructValueInstantiatorWithPropertyBasedCreator() {
        AnnotatedWithParams propertyCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = mock(SettableBeanProperty.class);
        properties[1] = mock(SettableBeanProperty.class);
        creatorCollector.addPropertyCreator(propertyCreator, false, properties);
        assertEquals(2, creatorCollector._propertyBasedArgs.length);
    }

    @Test
    public void testConstructValueInstantiatorWithNoCustomCreator() {
        String typeName = "MyClass";
        BeanDescription beanDescription = mock(BeanDescription.class);
        when(beanDescription.getType()).thenReturn(JavaType.construct(typeName, Collections.emptyList()));
        creatorCollector = new CreatorCollector(beanDescription, config);
        StdValueInstantiator inst = creatorCollector.constructValueInstantiator(null);
        assertNull(inst);
    }

    @Test
    public void testConstructValueInstantiatorWithPrivateCreator() throws IllegalAccessException {
        String typeName = "MyClass";
        BeanDescription beanDescription = mock(BeanDescription.class);
        when(beanDescription.getType()).thenReturn(JavaType.construct(typeName, Collections.emptyList()));
        Object target = new Object() {
            @JsonCreator
            private MyClass constructor(String s) {
                return new MyClass();
            }
        };
        AnnotatedMethod constructorMethod = mock(AnnotatedMethod.class);
        when(constructorMethod.getAnnotated()).thenReturn(new AnnotatedElement() {
            @Override
            public AnnotatedType<AnnotatedClass> getAnnotatedAnnotation(Class<? extends Annotation> annotationType) {
                if (CreatorProperty.class.equals(annotationType)) {
                    return (AnnotatedType<AnnotatedClass>) mock(AnnotatedType.class);
                }
                return null;
            }

            @Override
            public AnnotatedParameter[] getParameters() {
                return new AnnotatedParameter[1];
            }

            @Override
            public int getModifiers() {
                return Modifier.PRIVATE;
            }
        });
        when(beanDescription.findConstructor(any(Class[].class))).thenReturn(constructorMethod);
        creatorCollector = new CreatorCollector(beanDescription, config);
        StdValueInstantiator inst = creatorCollector.constructValueInstantiator(null);
        assertNotNull(inst);
    }
}