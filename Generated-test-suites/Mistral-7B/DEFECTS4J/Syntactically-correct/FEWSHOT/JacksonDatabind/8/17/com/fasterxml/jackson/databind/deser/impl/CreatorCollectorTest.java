package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for CreatorCollector class
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default creator
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(defaultCreator);

        // String creator
        AnnotatedWithParams stringCreator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator, true);

        // Int creator
        AnnotatedWithParams intCreator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(intCreator, true);

        // Long creator
        AnnotatedWithParams longCreator = mock(AnnotatedWithParams.class);
        collector.addLongCreator(longCreator, true);

        // Double creator
        AnnotatedWithParams doubleCreator = mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(doubleCreator, true);

        // Boolean creator
        AnnotatedWithParams booleanCreator = mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(booleanCreator, true);

        // Delegating creator
        AnnotatedWithParams delegatingCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        collector.addDelegatingCreator(delegatingCreator, true, injectables);

        // Property-based creator
        AnnotatedWithParams propertyCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mock(CreatorProperty.class);
        properties[1] = mock(CreatorProperty.class);
        collector.addPropertyCreator(propertyCreator, true, properties);

        // Verify number of creators
        assertEquals(8, collector._creators.length);
        assertNull(collector._delegateArgs);
        assertNull(collector._propertyBasedArgs);
        assertNull(collector._incompleteParameter);

        // Verify creators
        AnnotatedWithParams[] creators = collector._creators;
        assertEquals(defaultCreator, creators[CreatorCollector.C_DEFAULT]);
        assertEquals(stringCreator, creators[CreatorCollector.C_STRING]);
        assertEquals(intCreator, creators[CreatorCollector.C_INT]);
        assertEquals(longCreator, creators[CreatorCollector.C_LONG]);
        assertEquals(doubleCreator, creators[CreatorCollector.C_DOUBLE]);
        assertEquals(booleanCreator, creators[CreatorCollector.C_BOOLEAN]);
        assertEquals(delegatingCreator, creators[CreatorCollector.C_DELEGATE]);
        assertNull(creators[CreatorCollector.C_PROPS]);

        // Verify explicit creators
        assertEquals(0b11110111, collector._explicitCreators);

        // Verify no non-default creator
        assertFalse(collector._hasNonDefaultCreator);
    }

    @Test
    public void testCreatorCollectorWithNonDefaultCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default creator
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(defaultCreator);

        // Non-default creator
        AnnotatedWithParams nonDefaultCreator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(nonDefaultCreator, false);

        // Verify number of creators
        assertEquals(8, collector._creators.length);

        // Verify creators
        AnnotatedWithParams[] creators = collector._creators;
        assertEquals(defaultCreator, creators[CreatorCollector.C_DEFAULT]);
        assertNull(creators[CreatorCollector.C_STRING]);
        assertNull(creators[CreatorCollector.C_INT]);
        assertEquals(nonDefaultCreator, creators[CreatorCollector.C_INT]);
        assertNull(creators[CreatorCollector.C_LONG]);
        assertNull(creators[CreatorCollector.C_DOUBLE]);
        assertNull(creators[CreatorCollector.C_BOOLEAN]);
        assertNull(creators[CreatorCollector.C_DELEGATE]);
        assertNull(creators[CreatorCollector.C_PROPS]);

        // Verify explicit creators
        assertEquals(0b10110111, collector._explicitCreators);

        // Verify non-default creator
        assertTrue(collector._hasNonDefaultCreator);
    }
}