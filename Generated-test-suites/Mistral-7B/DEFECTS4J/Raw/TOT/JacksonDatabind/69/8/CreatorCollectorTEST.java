package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.core.base.ClassIdResolverBase;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class CreatorCollectorTest {

    private ObjectMapper mapper;
    private CreatorCollector collector;
    private MapperFactory factory;

    @BeforeClass
    public void setup() throws Exception {
        factory = mock(MapperFactory.class);
        mapper = new ObjectMapper();
        mapper.setFactory(factory);
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();

        collector = new CreatorCollector(
                new BeanDescription(null, null, null, null, null, null, introspector), factory);
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);
        when(ac.findAnnotation(CreatorProperty.class)).thenReturn(null);
        when(ac.findConstructor(null)).thenReturn(mock(AnnotatedConstructor.class));

        collector.setDefaultCreator(new AnnotatedWithParams(ac, null));

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
    }

    @Test
    public void testStringConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);
        when(ac.findConstructor(new Class[] {String.class})).thenReturn(mock(AnnotatedConstructor.class));

        collector.addStringCreator(new AnnotatedWithParams(ac, null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertEquals(stdInst.getConstructor(String.class), ac.findConstructor(new Class[] {String.class}));
    }

    @Test
    public void testIntConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);
        when(ac.findConstructor(new Class[] {int.class})).thenReturn(mock(AnnotatedConstructor.class));

        collector.addIntCreator(new AnnotatedWithParams(ac, null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertEquals(stdInst.getConstructor(int.class), ac.findConstructor(new Class[] {int.class}));
    }

    @Test
    public void testLongConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);
        when(ac.findConstructor(new Class[] {long.class})).thenReturn(mock(AnnotatedConstructor.class));

        collector.addLongCreator(new AnnotatedWithParams(ac, null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertEquals(stdInst.getConstructor(long.class), ac.findConstructor(new Class[] {long.class}));
    }

    @Test
    public void testDoubleConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);
        when(ac.findConstructor(new Class[] {double.class})).thenReturn(mock(AnnotatedConstructor.class));

        collector.addDoubleCreator(new AnnotatedWithParams(ac, null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertEquals(stdInst.getConstructor(double.class), ac.findConstructor(new Class[] {double.class}));
    }

    @Test
    public void testBooleanConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);
        when(ac.findConstructor(new Class[] {boolean.class})).thenReturn(mock(AnnotatedConstructor.class));

        collector.addBooleanCreator(new AnnotatedWithParams(ac, null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertEquals(stdInst.getConstructor(boolean.class), ac.findConstructor(new Class[] {boolean.class}));
    }

    @Test
    public void testDelegateConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);
        when(ac.findAnnotation(CreatorProperty.class)).thenReturn(null);
        when(ac.findConstructor(null)).thenReturn(mock(AnnotatedConstructor.class));
        when(ac.getRawClass()).thenReturn(List.class);

        SettableBeanProperty[] args = new SettableBeanProperty[1];
        SettableBeanProperty arg = mock(SettableBeanProperty.class);
        args[0] = arg;

        collector.addDelegatingCreator(new AnnotatedWithParams(ac, null), false, args);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        Constructor<?> constructor = stdInst.getDelegateCreator();
        assertEquals(constructor.getParameterTypes()[0], List.class);
        verify(arg).getValue();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testDuplicateConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);

        // add int constructor
        when(ac.findConstructor(new Class[] {int.class})).thenReturn(mock(AnnotatedConstructor.class));
        collector.addIntCreator(new AnnotatedWithParams(ac, null), false);

        // try to add another int constructor
        when(ac.findConstructor(new Class[] {int.class})).thenReturn(mock(AnnotatedConstructor.class));
        collector.addIntCreator(new AnnotatedWithParams(ac, null), false);
    }

    @Test
    public void testPropertyBasedConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);

        BeanPropertyDefinition p1 = mock(BeanPropertyDefinition.class);
        BeanPropertyDefinition p2 = mock(BeanPropertyDefinition.class);
        SettableBeanProperty[] args = {p1, p2};

        when(ac.findProperty(p1.getName())).thenReturn(p1);
        when(ac.findProperty(p2.getName())).thenReturn(p2);

        collector.addPropertyCreator(new AnnotatedWithParams(ac, null), false, args);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        verify(p1).setValue(any());
        verify(p2).setValue(any());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDuplicatePropertyBasedConstructor() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);

        BeanPropertyDefinition p1 = mock(BeanPropertyDefinition.class);
        BeanPropertyDefinition p2 = mock(BeanPropertyDefinition.class);
        SettableBeanProperty[] args = {p1, p2};

        when(ac.findProperty(p1.getName())).thenReturn(p1);
        when(ac.findProperty(p2.getName())).thenReturn(p2);

        collector.addPropertyCreator(new AnnotatedWithParams(ac, null), false, args);

        // try to add another property creator with same property names
        collector.addPropertyCreator(new AnnotatedWithParams(ac, null), false, args);
    }

    @Test
    public void testDupNameButDifferentTypes() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);

        BeanPropertyDefinition p1 = mock(BeanPropertyDefinition.class);
        BeanPropertyDefinition p2 = mock(BeanPropertyDefinition.class);
        SettableBeanProperty[] args = {p1, p2};

        when(ac.findProperty(p1.getName())).thenReturn(p1);
        when(ac.findProperty(p2.getName())).thenReturn(p2);
        when(p1.getParameterType()).thenReturn(Integer.TYPE);
        when(p2.getParameterType()).thenReturn(Double.TYPE);

        collector.addPropertyCreator(new AnnotatedWithParams(ac, null), false, args);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        verify(p1, times(1)).setValue(anyInt());
        verify(p2, times(1)).setValue(anyDouble());
    }

    @Test
    public void testNoCreators() throws Exception {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotations()).thenReturn(new Annotation[0]);

        collector = new CreatorCollector(
                new BeanDescription(null, null, null, null, null, null, null), factory);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        assertNull(inst);
    }

    static class AnnotatedWithParams implements AnnotatedMember {
        private final AnnotatedMember member;

        public AnnotatedWithParams(AnnotatedMember member) {
            this.member = member;
        }

        @Override
        public Member getMember() {
            return member.getMember();
        }

        @Override
        public AnnotatedParameter[] findParameters() {
            return new AnnotatedParameter[0];
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getAnnotations(Class<? extends Annotation> annotationType) {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations(Class<? extends Annotation> annotationType) {
            return new Annotation[0];
        }

        @Override
        public Class<?> getRawType() {
            return member.getRawType();
        }

        @Override
        public ClassIdResolver getClassIdResolver() {
            return new ClassIdResolverBase() {
                @Override
                public String idFromClass(Class<?> c) {
                    return "";
                }
            };
        }
    }
}