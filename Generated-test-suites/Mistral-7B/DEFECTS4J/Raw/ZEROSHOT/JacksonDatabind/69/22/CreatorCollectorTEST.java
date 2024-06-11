package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedWithParams;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.DeserializerPropertyName;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreatorCollectorTest {

    private CreatorCollector collector;
    private AnnotatedClass annotatedClass;
    private DeserializationContext context;
    private AnnotationIntrospector introspector;
    private MapperConfig<?> config;

    @Before
    public void setUp() throws Exception {
        introspector = Mockito.mock(AnnotationIntrospector.class);
        config = Mockito.mock(MapperConfig.class);
        context = Mockito.mock(DeserializationContext.class);
        annotatedClass = Mockito.mock(AnnotatedClass.class);

        collector = new CreatorCollector(new BeanDescription("TestBean", null), config);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        Method defaultConstructor = Mockito.mock(Method.class);
        AnnotatedMember defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        defaultCreator.setAnnotated(defaultConstructor);
        BeanPropertyDefinition propertyDefinition = Mockito.mock(BeanPropertyDefinition.class);

        Mockito.when(introspector.findCreator(annotatedClass)).thenReturn(defaultCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(propertyDefinition);
        Mockito.when(defaultCreator.getParameterType(0)).thenReturn(int.class);
        Mockito.when(defaultConstructor.getName()).thenReturn("constructorWithNoArguments");

        collector.setDefaultCreator(defaultCreator);

        assertTrue(collector.hasDefaultCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureDefaultCreator(ArgumentMatchers.eq(defaultCreator));
    }

    @Test
    public void testStringCreator() throws Exception {
        String stringValue = "test";
        Method stringCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember stringCreator = Mockito.mock(AnnotatedWithParams.class);
        stringCreator.setAnnotated(stringCreatorMethod);
        DeserializerPropertyName stringPropertyName = Mockito.mock(DeserializerPropertyName.class);
        Mockito.when(introspector.findValueDeserializer(annotatedClass, "field1")).thenReturn(stringPropertyName);
        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(stringCreator);
        Mockito.when(stringCreatorMethod.getName()).thenReturn("stringCreator");

        collector.addStringCreator(stringCreator, false);

        assertTrue(collector.hasDefaultCreator());
        assertTrue(collector.hasStringCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureStringCreator(ArgumentMatchers.eq(stringCreator), ArgumentMatchers.eq(stringPropertyName));
    }

    @Test
    public void testIntCreator() throws Exception {
        int intValue = 1;
        Method intCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember intCreator = Mockito.mock(AnnotatedWithParams.class);
        intCreator.setAnnotated(intCreatorMethod);
        BeanPropertyDefinition intPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);

        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(intCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(intPropertyDefinition);
        Mockito.when(intCreator.getParameterType(0)).thenReturn(int.class);
        Mockito.when(intCreatorMethod.getName()).thenReturn("intCreator");

        collector.addIntCreator(intCreator, false);

        assertTrue(collector.hasDefaultCreator());
        assertFalse(collector.hasStringCreator());
        assertTrue(collector.hasIntCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureIntCreator(ArgumentMatchers.eq(intCreator), ArgumentMatchers.eq(intPropertyDefinition));
    }

    @Test
    public void testLongCreator() throws Exception {
        long longValue = 1L;
        Method longCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember longCreator = Mockito.mock(AnnotatedWithParams.class);
        longCreator.setAnnotated(longCreatorMethod);
        BeanPropertyDefinition longPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);

        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(longCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(longPropertyDefinition);
        Mockito.when(longCreator.getParameterType(0)).thenReturn(long.class);
        Mockito.when(longCreatorMethod.getName()).thenReturn("longCreator");

        collector.addLongCreator(longCreator, false);

        assertTrue(collector.hasDefaultCreator());
        assertFalse(collector.hasStringCreator());
        assertFalse(collector.hasIntCreator());
        assertTrue(collector.hasLongCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureLongCreator(ArgumentMatchers.eq(longCreator), ArgumentMatchers.eq(longPropertyDefinition));
    }

    @Test
    public void testDoubleCreator() throws Exception {
        double doubleValue = 1.5;
        Method doubleCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        doubleCreator.setAnnotated(doubleCreatorMethod);
        BeanPropertyDefinition doublePropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);

        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(doubleCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(doublePropertyDefinition);
        Mockito.when(doubleCreator.getParameterType(0)).thenReturn(double.class);
        Mockito.when(doubleCreatorMethod.getName()).thenReturn("doubleCreator");

        collector.addDoubleCreator(doubleCreator, false);

        assertTrue(collector.hasDefaultCreator());
        assertFalse(collector.hasStringCreator());
        assertFalse(collector.hasIntCreator());
        assertFalse(collector.hasLongCreator());
        assertTrue(collector.hasDoubleCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureDoubleCreator(ArgumentMatchers.eq(doubleCreator), ArgumentMatchers.eq(doublePropertyDefinition));
    }

    @Test
    public void testBooleanCreator() throws Exception {
        boolean booleanValue = true;
        Method booleanCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        booleanCreator.setAnnotated(booleanCreatorMethod);
        BeanPropertyDefinition booleanPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);

        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(booleanCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(booleanPropertyDefinition);
        Mockito.when(booleanCreator.getParameterType(0)).thenReturn(boolean.class);
        Mockito.when(booleanCreatorMethod.getName()).thenReturn("booleanCreator");

        collector.addBooleanCreator(booleanCreator, false);

        assertTrue(collector.hasDefaultCreator());
        assertFalse(collector.hasStringCreator());
        assertFalse(collector.hasIntCreator());
        assertFalse(collector.hasLongCreator());
        assertFalse(collector.hasDoubleCreator());
        assertTrue(collector.hasBooleanCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureBooleanCreator(ArgumentMatchers.eq(booleanCreator), ArgumentMatchers.eq(booleanPropertyDefinition));
    }

    @Test
    public void testDelegateCreator() throws Exception {
        JavaType delegateType = Mockito.mock(JavaType.class);
        Method delegateCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember delegateCreator = Mockito.mock(AnnotatedWithParams.class);
        delegateCreator.setAnnotated(delegateCreatorMethod);
        List<SettableBeanProperty> delegateArgs = new ArrayList<>();
        SettableBeanProperty delegateArg = Mockito.mock(SettableBeanProperty.class);
        delegateArgs.add(delegateArg);

        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(delegateCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(null);
        Mockito.when(delegateCreator.getParameterType(0)).thenReturn(delegateType);
        Mockito.when(delegateCreatorMethod.getName()).thenReturn("delegateCreator");
        Mockito.when(delegateArg.getName()).thenReturn("delegateArg");

        collector.addDelegatingCreator(delegateCreator, false, delegateArgs.toArray(new SettableBeanProperty[0]));

        assertTrue(collector.hasDefaultCreator());
        assertFalse(collector.hasStringCreator());
        assertFalse(collector.hasIntCreator());
        assertFalse(collector.hasLongCreator());
        assertFalse(collector.hasDoubleCreator());
        assertFalse(collector.hasBooleanCreator());
        assertTrue(collector.hasDelegateCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureDelegateCreator(ArgumentMatchers.eq(delegateCreator), ArgumentMatchers.eq(delegateType), delegateArgs.toArray(new SettableBeanProperty[0]));
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        JavaType arrayDelegateType = Mockito.mock(JavaType.class);
        Method arrayDelegateCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember arrayDelegateCreator = Mockito.mock(AnnotatedWithParams.class);
        arrayDelegateCreator.setAnnotated(arrayDelegateCreatorMethod);
        List<SettableBeanProperty> arrayDelegateArgs = new ArrayList<>();
        SettableBeanProperty arrayDelegateArg = Mockito.mock(SettableBeanProperty.class);
        arrayDelegateArgs.add(arrayDelegateArg);

        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(arrayDelegateCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(null);
        Mockito.when(delegateCreator.getParameterType(0)).thenReturn(delegateType);
        Mockito.when(delegateCreatorMethod.getName()).thenReturn("arrayDelegateCreator");
        Mockito.when(arrayDelegateArg.getName()).thenReturn("arrayDelegateArg");

        collector.addArrayDelegateCreator(arrayDelegateCreator, false, arrayDelegateArgs.toArray(new SettableBeanProperty[0]));

        assertTrue(collector.hasDefaultCreator());
        assertFalse(collector.hasStringCreator());
        assertFalse(collector.hasIntCreator());
        assertFalse(collector.hasLongCreator());
        assertFalse(collector.hasDoubleCreator());
        assertFalse(collector.hasBooleanCreator());
        assertFalse(collector.hasDelegateCreator());
        assertTrue(collector.hasArrayDelegateCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configureArrayDelegateCreator(ArgumentMatchers.eq(arrayDelegateCreator), ArgumentMatchers.eq(arrayDelegateType), arrayDelegateArgs.toArray(new SettableBeanProperty[0]));
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        List<CreatorProperty> creatorProperties = Arrays.asList(
                Mockito.mock(CreatorProperty.class),
                Mockito.mock(CreatorProperty.class)
        );
        Method propertyBasedCreatorMethod = Mockito.mock(Method.class);
        AnnotatedMember propertyBasedCreator = Mockito.mock(AnnotatedWithParams.class);
        propertyBasedCreator.setAnnotated(propertyBasedCreatorMethod);

        Mockito.when(introspector.findCreator(annotatedClass, "field1")).thenReturn(propertyBasedCreator);
        Mockito.when(introspector.findBeanProperty(annotatedClass, "field1")).thenReturn(null);
        Mockito.when(propertyBasedCreator.getParameterType(0)).thenReturn(void.class);
        Mockito.when(propertyBasedCreatorMethod.getName()).thenReturn("propertyBasedCreator");

        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        SettableBeanProperty propertyBasedArg1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propertyBasedArg2 = Mockito.mock(SettableBeanProperty.class);
        propertyBasedArgs[0] = propertyBasedArg1;
        propertyBasedArgs[1] = propertyBasedArg2;

        collector.addPropertyCreator(propertyBasedCreator, false, propertyBasedArgs);

        assertTrue(collector.hasDefaultCreator());
        assertFalse(collector.hasStringCreator());
        assertFalse(collector.hasIntCreator());
        assertFalse(collector.hasLongCreator());
        assertFalse(collector.hasDoubleCreator());
        assertFalse(collector.hasBooleanCreator());
        assertFalse(collector.hasDelegateCreator());
        assertFalse(collector.hasArrayDelegateCreator());
        assertTrue(collector.hasPropertyBasedCreator());
        assertTrue(collector.constructValueInstantiator(context) instanceof StdValueInstantiator);
        Mockito.verify(context).configurePropertyBasedCreator(ArgumentMatchers.eq(propertyBasedCreator), propertyBasedArgs);
    }
}