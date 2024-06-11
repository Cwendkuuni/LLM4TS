package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstructor() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        assertNull(collector._creators[CreatorCollector.C_DEFAULT]);
        assertEquals(false, collector._hasNonDefaultCreator);
        assertNull(collector._delegateArgs);
        assertNull(collector._propertyBasedArgs);
        assertNull(collector._incompleteParameter);
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_DEFAULT]);
        assertTrue(collector._hasNonDefaultCreator);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, false);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_STRING]);
        assertTrue(collector._hasNonDefaultCreator);
    }

    @Test
    public void testAddStringCreatorExplicit() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, true);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_STRING]);
        assertTrue(collector._hasNonDefaultCreator);
        assertTrue((collector._explicitCreators & (1 << CreatorCollector.C_STRING)) != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateStringCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator1, false);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator2, false);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator, false);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_INT]);
        assertTrue(collector._hasNonDefaultCreator);
    }

    @Test
    public void testAddIntCreatorExplicit() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator, true);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_INT]);
        assertTrue(collector._hasNonDefaultCreator);
        assertTrue((collector._explicitCreators & (1 << CreatorCollector.C_INT)) != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateIntCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator1, false);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator2, false);
    }

    // Test other creator types similarly...

    @Test
    public void testAddDelegateCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        collector.addDelegatingCreator(creator, false, injectables);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_DELEGATE]);
        assertTrue(collector._hasNonDefaultCreator);
        assertEquals(injectables, collector._delegateArgs);
    }

    @Test
    public void testAddDelegateCreatorExplicit() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        collector.addDelegatingCreator(creator, true, injectables);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_DELEGATE]);
        assertTrue(collector._hasNonDefaultCreator);
        assertEquals(injectables, collector._delegateArgs);
        assertTrue((collector._explicitCreators & (1 << CreatorCollector.C_DELEGATE)) != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDelegateCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables1 = new CreatorProperty[1];
        injectables1[0] = mock(CreatorProperty.class);
        collector.addDelegatingCreator(creator1, false, injectables1);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables2 = new CreatorProperty[1];
        injectables2[0] = mock(CreatorProperty.class);
        collector.addDelegatingCreator(creator2, false, injectables2);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = mock(CreatorProperty.class);
        collector.addPropertyCreator(creator, false, properties);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_PROPS]);
        assertTrue(collector._hasNonDefaultCreator);
        assertEquals(properties, collector._propertyBasedArgs);
    }

    @Test
    public void testAddPropertyCreatorExplicit() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = mock(CreatorProperty.class);
        collector.addPropertyCreator(creator, true, properties);
        AnnotatedWithParams expectedCreator = _fixAccess(creator);
        assertEquals(expectedCreator, collector._creators[CreatorCollector.C_PROPS]);
        assertTrue(collector._hasNonDefaultCreator);
        assertEquals(properties, collector._propertyBasedArgs);
        assertTrue((collector._explicitCreators & (1 << CreatorCollector.C_PROPS)) != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatePropertyCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties1 = new CreatorProperty[1];
        properties1[0] = mock(CreatorProperty.class);
        collector.addPropertyCreator(creator1, false, properties1);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties2 = new CreatorProperty[1];
        properties2[0] = mock(CreatorProperty.class);
        collector.addPropertyCreator(creator2, false, properties2);
    }

    // Test getters

    @Test
    public void testHasDefaultCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        assertFalse(collector.hasDefaultCreator());
        collector.setDefaultCreator(mock(AnnotatedWithParams.class));
        assertTrue(collector.hasDefaultCreator());
    }

    // Test helper methods

    private AnnotatedWithParams _fixAccess(AnnotatedWithParams member) {
        if (member != null) {
            ClassUtil.checkAndFixAccess((Member) member.getAnnotated());
        }
        return member;
    }
}