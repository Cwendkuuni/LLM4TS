package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstructor() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        assertNull(collector.getDefaultCreator());
        assertFalse(collector.hasNonDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithStringCreator() {
        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getStringCreator());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithIntCreator() {
        BeanDescription beanDesc = new BeanDescription(Integer.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getIntCreator());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithLongCreator() {
        BeanDescription beanDesc = new BeanDescription(Long.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getLongCreator());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription(Double.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getDoubleCreator());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription(Boolean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getBooleanCreator());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithDelegateCreator() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[0];
        collector.addDelegatingCreator(creator, false, injectables);

        JavaType delegateType = TypeBindings.TypeFactory.unresolved(String.class);
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getDelegateCreator());
        assertEquals(delegateType, instantiator.getDelegateType());
        assertArrayEquals(new CreatorProperty[0], instantiator.getDelegateArgs());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getPropertyBasedArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithPropertyBasedCreator() {
        BeanDescription beanDesc = new BeanDescription(PropertyBasedCreatorTestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("prop1", Integer.TYPE, null);
        properties[1] = new CreatorProperty("prop2", String.class, null);
        collector.addPropertyCreator(creator, false, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(creator, instantiator.getPropertyBasedCreator());
        assertArrayEquals(properties, instantiator.getPropertyBasedArgs());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getIncompleteParameter());
    }

    @Test
    public void testCreatorCollectorWithIncompleteParameter() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        collector.addIncompleteParameter(parameter);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(parameter, instantiator.getIncompleteParameter());
        assertNull(collector.getDefaultCreator());
        assertNull(collector.getDelegateType());
        assertNull(collector.getDelegateArgs());
        assertNull(collector.getPropertyBasedArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateStringCreator() {
        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator1, false);
        collector.addStringCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateIntCreator() {
        BeanDescription beanDesc = new BeanDescription(Integer.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator1, false);
        collector.addIntCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateLongCreator() {
        BeanDescription beanDesc = new BeanDescription(Long.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator1, false);
        collector.addLongCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription(Double.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator1, false);
        collector.addDoubleCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription(Boolean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator1, false);
        collector.addBooleanCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateDelegateCreator() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addDelegatingCreator(creator1, false, new CreatorProperty[0]);
        collector.addDelegatingCreator(creator2, false, new CreatorProperty[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicatePropertyBasedCreator() {
        BeanDescription beanDesc = new BeanDescription(PropertyBasedCreatorTestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties1 = new CreatorProperty[1];
        properties1[0] = new CreatorProperty("prop1", Integer.TYPE, null);

        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties2 = new CreatorProperty[1];
        properties2[0] = new CreatorProperty("prop1", Integer.TYPE, null);

        collector.addPropertyCreator(creator1, false, properties1);
        collector.addPropertyCreator(creator2, false, properties2);
    }
}

class PropertyBasedCreatorTestBean {
    // Add properties here if needed
}