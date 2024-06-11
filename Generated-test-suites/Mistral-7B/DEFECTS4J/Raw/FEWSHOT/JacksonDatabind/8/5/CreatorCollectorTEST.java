package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default creator
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(defaultCreator);
        assertTrue(collector.hasDefaultCreator());

        // String creator
        AnnotatedWithParams stringCreator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator, false);
        assertFalse(collector.hasDefaultCreator());

        // Int creator
        AnnotatedWithParams intCreator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(intCreator, false);
        assertFalse(collector.hasDefaultCreator());

        // Long creator
        AnnotatedWithParams longCreator = mock(AnnotatedWithParams.class);
        collector.addLongCreator(longCreator, false);
        assertFalse(collector.hasDefaultCreator());

        // Double creator
        AnnotatedWithParams doubleCreator = mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(doubleCreator, false);
        assertFalse(collector.hasDefaultCreator());

        // Boolean creator
        AnnotatedWithParams booleanCreator = mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(booleanCreator, false);
        assertFalse(collector.hasDefaultCreator());

        // Delegating creator
        AnnotatedWithParams delegatingCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        collector.addDelegatingCreator(delegatingCreator, false, injectables);
        assertFalse(collector.hasDefaultCreator());

        // Property-based creator
        AnnotatedWithParams propertyCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mock(CreatorProperty.class);
        properties[1] = mock(CreatorProperty.class);
        collector.addPropertyCreator(propertyCreator, false, properties);
        assertFalse(collector.hasDefaultCreator());

        // Verify no duplicates
        AnnotatedWithParams duplicateStringCreator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(duplicateStringCreator, false);
        assertEquals(stringCreator, collector._creators[CreatorCollector.C_STRING]);
        try {
            collector.addStringCreator(duplicateStringCreator, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Verify explicit creators
        AnnotatedWithParams explicitStringCreator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(explicitStringCreator, true);
        assertTrue((collector._explicitCreators & (1 << CreatorCollector.C_STRING)) != 0);

        // Verify no vanilla creator when no non-default creator
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertTrue(inst instanceof StdValueInstantiator);

        // Verify vanilla creator when no non-default creator for a specific type
        beanDesc = mock(BeanDescription.class);
        collector = new CreatorCollector(beanDesc, true);
        JavaType type = mock(JavaType.class);
        when(type.getRawClass()).thenReturn(Collection.class);
        collector.setDefaultCreator(null);
        inst = collector.constructValueInstantiator(null);
        assertTrue(inst instanceof CreatorCollector.Vanilla);

        // Verify non-vanilla creator when non-default creator for a specific type
        collector = new CreatorCollector(beanDesc, true);
        when(beanDesc.getType()).thenReturn(type);
        AnnotatedWithParams nonDefaultCreator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(null);
        collector.addStringCreator(nonDefaultCreator, false);
        inst = collector.constructValueInstantiator(null);
        assertFalse(inst instanceof CreatorCollector.Vanilla);
    }
}