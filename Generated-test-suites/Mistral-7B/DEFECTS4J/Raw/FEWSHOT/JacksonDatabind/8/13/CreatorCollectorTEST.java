package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default creator
        AnnotatedWithParams creator0 = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator0);

        // String creator
        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator1, true);

        // Int creator
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator2, true);

        // Long creator
        AnnotatedWithParams creator3 = mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator3, true);

        // Double creator
        AnnotatedWithParams creator4 = mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator4, true);

        // Boolean creator
        AnnotatedWithParams creator5 = mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator5, true);

        // Delegating creator
        JavaType delegateType = mock(JavaType.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        AnnotatedWithParams creator6 = mock(AnnotatedWithParams.class);
        collector.addDelegatingCreator(creator6, true, injectables);

        // Property-based creator
        CreatorProperty[] properties = new CreatorProperty[2];
        AnnotatedWithParams creator7 = mock(AnnotatedWithParams.class);
        collector.addPropertyCreator(creator7, true, properties);

        // Test constructValueInstantiator
        DeserializationConfig config = mock(DeserializationConfig.class);
        ValueInstantiator inst = collector.constructValueInstantiator(config);

        // Check that creators were added correctly
        assertEquals(creator0, collector.getCreator(CreatorCollector.C_DEFAULT));
        assertEquals(creator1, collector.getCreator(CreatorCollector.C_STRING));
        assertEquals(creator2, collector.getCreator(CreatorCollector.C_INT));
        assertEquals(creator3, collector.getCreator(CreatorCollector.C_LONG));
        assertEquals(creator4, collector.getCreator(CreatorCollector.C_DOUBLE));
        assertEquals(creator5, collector.getCreator(CreatorCollector.C_BOOLEAN));
        assertEquals(creator6, collector.getCreator(CreatorCollector.C_DELEGATE));
        assertEquals(creator7, collector.getCreator(CreatorCollector.C_PROPS));

        // Test that getCreator returns null for non-existent creators
        assertNull(collector.getCreator(CreatorCollector.C_NONE));

        // Test that each creator has the correct type
        assertEquals(JavaType.valueOf(String.class), collector.getCreator(CreatorCollector.C_STRING).getParameterType());
        assertEquals(JavaType.valueOf(int.class), collector.getCreator(CreatorCollector.C_INT).getParameterType());
        assertEquals(JavaType.valueOf(long.class), collector.getCreator(CreatorCollector.C_LONG).getParameterType());
        assertEquals(JavaType.valueOf(double.class), collector.getCreator(CreatorCollector.C_DOUBLE).getParameterType());
        assertEquals(JavaType.valueOf(boolean.class), collector.getCreator(CreatorCollector.C_BOOLEAN).getParameterType());

        // Test that explicit creators take precedence over automatically detected ones
        AnnotatedWithParams creator8 = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator8, false);
        assertEquals(creator2, collector.getCreator(CreatorCollector.C_INT));

        // Test that multiple creators with the same name throw an exception
        AnnotatedWithParams creator9 = mock(AnnotatedWithParams.class);
        collector.addPropertyCreator(creator9, true, properties);
        try {
            collector.addPropertyCreator(creator7, true, properties);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}