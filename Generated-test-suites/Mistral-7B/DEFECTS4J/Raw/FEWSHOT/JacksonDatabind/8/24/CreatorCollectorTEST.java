package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Test default creator
        AnnotatedWithParams defaultCreator = createTestMethod(beanDesc, "defaultConstructor");
        collector.setDefaultCreator(defaultCreator);
        AnnotatedWithParams retrievedDefaultCreator = collector.getDefaultCreator();
        assertEquals(defaultCreator, retrievedDefaultCreator);

        // Test string creator
        AnnotatedWithParams stringCreator = createTestMethod(beanDesc, "stringCreator");
        collector.addStringCreator(stringCreator, true);
        AnnotatedWithParams retrievedStringCreator = collector.getStringCreator();
        assertEquals(stringCreator, retrievedStringCreator);

        // Test int creator
        AnnotatedWithParams intCreator = createTestMethod(beanDesc, "intCreator");
        collector.addIntCreator(intCreator, true);
        AnnotatedWithParams retrievedIntCreator = collector.getIntCreator();
        assertEquals(intCreator, retrievedIntCreator);

        // Test long creator
        AnnotatedWithParams longCreator = createTestMethod(beanDesc, "longCreator");
        collector.addLongCreator(longCreator, true);
        AnnotatedWithParams retrievedLongCreator = collector.getLongCreator();
        assertEquals(longCreator, retrievedLongCreator);

        // Test double creator
        AnnotatedWithParams doubleCreator = createTestMethod(beanDesc, "doubleCreator");
        collector.addDoubleCreator(doubleCreator, true);
        AnnotatedWithParams retrievedDoubleCreator = collector.getDoubleCreator();
        assertEquals(doubleCreator, retrievedDoubleCreator);

        // Test boolean creator
        AnnotatedWithParams booleanCreator = createTestMethod(beanDesc, "booleanCreator");
        collector.addBooleanCreator(booleanCreator, true);
        AnnotatedWithParams retrievedBooleanCreator = collector.getBooleanCreator();
        assertEquals(booleanCreator, retrievedBooleanCreator);

        // Test delegate creator with no arguments
        AnnotatedWithParams delegateCreatorNoArgs = createTestMethod(beanDesc, "delegateCreatorNoArgs");
        collector.addDelegatingCreator(delegateCreatorNoArgs, null);
        AnnotatedWithParams retrievedDelegateCreatorNoArgs = collector.getDelegateCreator();
        assertEquals(delegateCreatorNoArgs, retrievedDelegateCreatorNoArgs);

        // Test delegate creator with arguments
        AnnotatedWithParams delegateCreatorArgs = createTestMethod(beanDesc, "delegateCreatorArgs");
        AnnotatedParameter[] delegateArgs = new AnnotatedParameter[1];
        delegateArgs[0] = createTestParameter(beanDesc, "arg1");
        collector.addDelegatingCreator(delegateCreatorArgs, delegateArgs);
        AnnotatedWithParams retrievedDelegateCreatorArgs = collector.getDelegateCreator();
        assertEquals(delegateCreatorArgs, retrievedDelegateCreatorArgs);
        assertEquals(delegateArgs[0], retrievedDelegateCreatorArgs.getParameters()[0]);

        // Test property-based creator with no properties
        AnnotatedWithParams propertyCreatorNoProps = createTestMethod(beanDesc, "propertyCreatorNoProps");
        collector.addPropertyCreator(propertyCreatorNoProps, null, null);
        AnnotatedWithParams retrievedPropertyCreatorNoProps = collector.getPropertyCreator();
        assertEquals(propertyCreatorNoProps, retrievedPropertyCreatorNoProps);

        // Test property-based creator with properties
        AnnotatedWithParams propertyCreatorProps = createTestMethod(beanDesc, "propertyCreatorProps");
        AnnotatedParameter[] propertyBasedArgs = new AnnotatedParameter[2];
        propertyBasedArgs[0] = createTestParameter(beanDesc, "prop1");
        propertyBasedArgs[1] = createTestParameter(beanDesc, "prop2");
        collector.addPropertyCreator(propertyCreatorProps, null, propertyBasedArgs);
        AnnotatedWithParams retrievedPropertyCreatorProps = collector.getPropertyCreator();
        assertEquals(propertyCreatorProps, retrievedPropertyCreatorProps);
        assertEquals(propertyBasedArgs[0], retrievedPropertyCreatorProps.getParameters()[0]);
        assertEquals(propertyBasedArgs[1], retrievedPropertyCreatorProps.getParameters()[1]);
    }

    @Test
    public void testCanFixAccess() {
        BeanDescription beanDesc = createTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, false);
        AnnotatedWithParams creator = createTestMethod(beanDesc, "creatorWithAccess");
        creator.getAnnotated().setAccessible(false);
        collector.addStringCreator(creator, true);
        AnnotatedWithParams retrievedCreator = collector.getStringCreator();
        assertNull(retrievedCreator);

        collector = new CreatorCollector(beanDesc, true);
        retrievedCreator = collector.getStringCreator();
        assertEquals(creator, retrievedCreator);
    }

    private BeanDescription createTestBeanDescription() {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotation(DeserializationConfig.Feature.CAN_OVERLOAD_ACCESS_MODIFIERS.class)).thenReturn(true);
        when(ac.getRawType()).thenReturn(TestBean.class);
        BeanDescription beanDesc = new BeanDescription(ac);
        return beanDesc;
    }

    private AnnotatedWithParams createTestMethod(BeanDescription beanDesc, String name) {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getName()).thenReturn(TestBean.class.getName() + "#" + name);
        AnnotatedMethod am = mock(AnnotatedMethod.class);
        when(am.getAnnotated()).thenReturn(ac);
        when(am.getName()).thenReturn(name);
        AnnotatedWithParams creator = new AnnotatedWithParams(beanDesc, am, null, null, null, null, null, null, null);
        return creator;
    }

    private AnnotatedParameter createTestParameter(BeanDescription beanDesc, String name) {
        AnnotatedField af = mock(AnnotatedField.class);
        when(af.getName()).thenReturn(name);
        AnnotatedParameter ap = new AnnotatedParameter(beanDesc, af);
        return ap;
    }
}

class TestBean {
    // Test constructors, methods and fields here
}