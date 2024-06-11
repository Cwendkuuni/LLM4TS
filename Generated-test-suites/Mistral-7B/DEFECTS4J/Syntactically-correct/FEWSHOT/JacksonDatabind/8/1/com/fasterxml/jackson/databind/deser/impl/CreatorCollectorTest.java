package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstruction() {
        BeanDescription beanDesc = Mockito.mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        assertEquals(beanDesc, collector._beanDesc);
        assertTrue(collector._canFixAccess);
        assertNull(collector._creators);
        assertNull(collector._explicitCreators);
        assertFalse(collector._hasNonDefaultCreator);
        assertNull(collector._delegateArgs);
        assertNull(collector._propertyBasedArgs);
        assertNull(collector._incompleteParameter);
    }

    @Test
    public void testCreatorCollectorSetters() {
        BeanDescription beanDesc = Mockito.mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creatorDefault = Mockito.mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creatorDefault);
        assertEquals(creatorDefault, collector._creators[CreatorCollector.C_DEFAULT]);

        AnnotatedWithParams creatorString = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creatorString, true);
        assertEquals(creatorString, collector._creators[CreatorCollector.C_STRING]);

        AnnotatedWithParams creatorInt = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(creatorInt, true);
        assertEquals(creatorInt, collector._creators[CreatorCollector.C_INT]);

        AnnotatedWithParams creatorLong = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(creatorLong, true);
        assertEquals(creatorLong, collector._creators[CreatorCollector.C_LONG]);

        AnnotatedWithParams creatorDouble = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creatorDouble, true);
        assertEquals(creatorDouble, collector._creators[CreatorCollector.C_DOUBLE]);

        AnnotatedWithParams creatorBoolean = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creatorBoolean, true);
        assertEquals(creatorBoolean, collector._creators[CreatorCollector.C_BOOLEAN]);

        CreatorProperty[] delegateArgs = new CreatorProperty[1];
        AnnotatedWithParams creatorDelegate = Mockito.mock(AnnotatedWithParams.class);
        collector.addDelegatingCreator(creatorDelegate, true, delegateArgs);
        assertEquals(creatorDelegate, collector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(delegateArgs, collector._delegateArgs);

        CreatorProperty[] propertyBasedArgs = new CreatorProperty[1];
        AnnotatedWithParams creatorProperty = Mockito.mock(AnnotatedWithParams.class);
        collector.addPropertyCreator(creatorProperty, true, propertyBasedArgs);
        assertEquals(creatorProperty, collector._creators[CreatorCollector.C_PROPS]);
        assertEquals(propertyBasedArgs, collector._propertyBasedArgs);

        AnnotatedParameter incompleteParameter = Mockito.mock(AnnotatedParameter.class);
        collector.addIncompeteParameter(incompleteParameter);
        assertEquals(incompleteParameter, collector._incompleteParameter);
    }

    @Test
    public void testCreatorCollectorConstructValueInstantiator() {
        BeanDescription beanDesc = Mockito.mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        ValueInstantiator inst = collector.constructValueInstantiator(config);

        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);

        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertEquals(beanDesc, stdInst._beanDesc);
        assertTrue(stdInst._config.isCreatorAnnotationIntrospectorEnabled());
        assertEquals(config, stdInst._config);

        assertNull(stdInst._beanDesc.getType().getRawClass()); // Verify not initialized
    }
}