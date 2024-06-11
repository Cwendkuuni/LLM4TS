package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.MemberNameResolver;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for CreatorCollector.
 */
public class CreatorCollectorTest {

    

    @Test
    public void testCreatorCollector() throws Exception {
        Class<CreatorCollector> clazz = CreatorCollector.class;
        BeanDescription beanDesc = createMockBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Test default creator
        Method defaultCreatorMethod = clazz.getMethod("setDefaultCreator", AnnotatedWithParams.class);
        AnnotatedWithParams defaultCreator = createMockCreator();
        collector.setDefaultCreator(defaultCreator);
        ValueInstantiator valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test string creator
        Method addStringCreatorMethod = clazz.getMethod("addStringCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams stringCreator = createMockCreator();
        collector.addStringCreator(stringCreator, false);
        valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test int creator
        Method addIntCreatorMethod = clazz.getMethod("addIntCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams intCreator = createMockCreator();
        collector.addIntCreator(intCreator, false);
        valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test long creator
        Method addLongCreatorMethod = clazz.getMethod("addLongCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams longCreator = createMockCreator();
        collector.addLongCreator(longCreator, false);
        valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test double creator
        Method addDoubleCreatorMethod = clazz.getMethod("addDoubleCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams doubleCreator = createMockCreator();
        collector.addDoubleCreator(doubleCreator, false);
        valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test boolean creator
        Method addBooleanCreatorMethod = clazz.getMethod("addBooleanCreator", AnnotatedWithParams.class, boolean.class);
        AnnotatedWithParams booleanCreator = createMockCreator();
        collector.addBooleanCreator(booleanCreator, false);
        valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test delegating creator
        Method addDelegatingCreatorMethod = clazz.getMethod("addDelegatingCreator", AnnotatedWithParams.class, boolean.class, CreatorProperty[].class);
        CreatorProperty[] delegateArgs = new CreatorProperty[1];
        CreatorProperty delegateArg = createMockCreatorProperty();
        delegateArgs[0] = delegateArg;
        AnnotatedWithParams delegatingCreator = createMockCreator();
        collector.addDelegatingCreator(delegatingCreator, false, delegateArgs);
        valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test property-based creator
        Method addPropertyCreatorMethod = clazz.getMethod("addPropertyCreator", AnnotatedWithParams.class, boolean.class, CreatorProperty[].class);
        CreatorProperty[] propertyBasedArgs = new CreatorProperty[2];
        CreatorProperty propertyBasedArg1 = createMockCreatorProperty();
        CreatorProperty propertyBasedArg2 = createMockCreatorProperty();
        propertyBasedArgs[0] = propertyBasedArg1;
        propertyBasedArgs[1] = propertyBasedArg2;
        AnnotatedWithParams propertyCreator = createMockCreator();
        collector.addPropertyCreator(propertyCreator, false, propertyBasedArgs);
        valueInstantiator = collector.constructValueInstantiator(createMockDeserializationConfig());
        assertNotNull(valueInstantiator);

        // Test invalid creator
        Method addInvalidCreatorMethod = clazz.getMethod("setDefaultCreator", AnnotatedWithParams.class);
        AnnotatedWithParams invalidCreator = createMockCreatorWithInvalidAccess();
        try {
            collector.setDefaultCreator(invalidCreator);
            fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // expected
        }

        // Test null deserialization config
        Method constructValueInstantiatorMethod = clazz.getMethod("constructValueInstantiator", DeserializationConfig.class);
        try {
            collector.constructValueInstantiator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private BeanDescription createMockBeanDescription() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        when(beanDesc.getType()).thenReturn(Integer.class);
        when(beanDesc.getBindingsForBeanType()).thenReturn(createMockTypeBindings());
        when(beanDesc.getName()).thenReturn("mockBean");
        return beanDesc;
    }

    private TypeBindings createMockTypeBindings() {
        TypeBindings typeBindings = mock(TypeBindings.class);
        when(typeBindings.resolveType(any(JavaType.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        return typeBindings;
    }

    private DeserializationConfig createMockDeserializationConfig() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        return config;
    }

    private DeserializationContext createMockDeserializationContext() {
        DeserializationContext context = mock(DeserializationContext.class);
        return context;
    }

    private AnnotatedWithParams createMockCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.getAnnotated()).thenReturn(createMockAnnotatedMember());
        return creator;
    }

    private AnnotatedMember createMockAnnotatedMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        return member;
    }

    private CreatorProperty createMockCreatorProperty() {
        CreatorProperty creatorProperty = mock(CreatorProperty.class);
        when(creatorProperty.getName()).thenReturn("mockProperty");
        return creatorProperty;
    }

    private AnnotatedWithParams createMockCreatorWithInvalidAccess() {
        AnnotatedWithParams creator = createMockCreator();
        doThrow(IllegalAccessException.class).when(creator).getAnnotated();
        return creator;
    }

    
}