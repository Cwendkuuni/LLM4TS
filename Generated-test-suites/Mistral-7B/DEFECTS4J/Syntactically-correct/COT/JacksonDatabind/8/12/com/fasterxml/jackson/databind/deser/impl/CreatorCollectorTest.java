package com.example.junit;

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {
    

    @Test
    public void testConstructValueInstantiator() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        ValueInstantiator inst = cc.constructValueInstantiator(getTestConfig());

        assertNotNull(inst);
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestDefaultCreator();
        cc.setDefaultCreator(creator);

        AnnotatedWithParams actual = cc.getDefaultCreator();
        assertEquals(creator, actual);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestStringCreator();
        cc.addStringCreator(creator, false);

        AnnotatedWithParams actual = cc.getCreator(CreatorCollector.C_STRING);
        assertEquals(creator, actual);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestIntCreator();
        cc.addIntCreator(creator, false);

        AnnotatedWithParams actual = cc.getCreator(CreatorCollector.C_INT);
        assertEquals(creator, actual);
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestLongCreator();
        cc.addLongCreator(creator, false);

        AnnotatedWithParams actual = cc.getCreator(CreatorCollector.C_LONG);
        assertEquals(creator, actual);
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestDoubleCreator();
        cc.addDoubleCreator(creator, false);

        AnnotatedWithParams actual = cc.getCreator(CreatorCollector.C_DOUBLE);
        assertEquals(creator, actual);
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestBooleanCreator();
        cc.addBooleanCreator(creator, false);

        AnnotatedWithParams actual = cc.getCreator(CreatorCollector.C_BOOLEAN);
        assertEquals(creator, actual);
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestDelegatingCreator();
        AnnotatedParameter[] injectables = new AnnotatedParameter[1];
        injectables[0] = getTestInjectableParameter();

        cc.addDelegatingCreator(creator, false, injectables);

        CreatorProperty[] actual = cc.getPropertyBasedArgs();
        assertEquals(injectables.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(injectables[i], actual[i]);
        }
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestPropertyCreator();
        AnnotatedParameter[] properties = new AnnotatedParameter[2];
        properties[0] = getTestInjectableParameter();
        properties[1] = getTestInjectableParameter();

        cc.addPropertyCreator(creator, false, properties);

        CreatorProperty[] actual = cc.getPropertyBasedArgs();
        assertEquals(properties.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(properties[i], actual[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestDefaultCreator();
        cc.addStringCreator(creator, false);
        cc.addStringCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExplicitDuplicateCreator() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestStringCreator();
        int typeIndex = CreatorCollector.C_STRING;
        cc.addStringCreator(creator, true);
        cc.addStringCreator(creator, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDifferentTypeCreator() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator1 = getTestIntCreator();
        AnnotatedWithParams creator2 = getTestStringCreator();
        cc.addIntCreator(creator1, false);
        cc.addStringCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSameCreatorDifferentIndex() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestStringCreator();
        cc.addStringCreator(creator, false);
        cc.addIntCreator(creator, false);
    }

    @Test
    public void testHasDefaultCreator() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = getTestDefaultCreator();
        cc.setDefaultCreator(creator);

        assertTrue(cc.hasDefaultCreator());

        cc.setDefaultCreator(null);

        assertFalse(cc.hasDefaultCreator());
    }

    @Test
    public void testFixAccess() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedMember member = getTestMember();

        AnnotatedWithParams actual = cc._fixAccess((AnnotatedElement) member);

        assertSame(member, actual.getAnnotated());
    }

    @Test
    public void testVerifyNonDup() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator1 = getTestStringCreator();
        AnnotatedWithParams creator2 = getTestStringCreator();

        cc.verifyNonDup(creator1, CreatorCollector.C_STRING, false);
        cc.verifyNonDup(creator2, CreatorCollector.C_STRING, false);
    }

    @Test
    public void testVerifyNonDupWithExplicit() throws Exception {
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator1 = getTestStringCreator();
        AnnotatedWithParams creator2 = getTestStringCreator();

        cc.verifyNonDup(creator1, CreatorCollector.C_STRING, true);
        cc.verifyNonDup(creator2, CreatorCollector.C_STRING, true);
    }

    private BeanDescription getTestBeanDescription() {
        Class<?> clazz = getClass();
        BeanDescription beanDesc = ClassIntrospector.findBeanDescription(clazz, new NopAnnotationIntrospector());
        return beanDesc;
    }

    private DeserializationConfig getTestConfig() {
        return new DeserializationConfig();
    }

    private AnnotatedWithParams getTestDefaultCreator() {
        Class<?> clazz = getClass();
        AnnotatedMember member = ClassIntrospector.findConstructor(clazz, 0);
        return new AnnotatedWithParams(member, null);
    }

    private AnnotatedWithParams getTestStringCreator() {
        Class<?> clazz = getClass();
        Constructor<?> constructor = clazz.getConstructor(String.class);
        return new AnnotatedWithParams(constructor, new Annotation[0]);
    }

    private AnnotatedWithParams getTestIntCreator() {
        Class<?> clazz = getClass();
        Constructor<?> constructor = clazz.getConstructor(int.class);
        return new AnnotatedWithParams(constructor, new Annotation[0]);
    }

    private AnnotatedWithParams getTestLongCreator() {
        Class<?> clazz = getClass();
        Constructor<?> constructor = clazz.getConstructor(long.class);
        return new AnnotatedWithParams(constructor, new Annotation[0]);
    }

    private AnnotatedWithParams getTestDoubleCreator() {
        Class<?> clazz = getClass();
        Constructor<?> constructor = clazz.getConstructor(double.class);
        return new AnnotatedWithParams(constructor, new Annotation[0]);
    }

    private AnnotatedWithParams getTestBooleanCreator() {
        Class<?> clazz = getClass();
        Constructor<?> constructor = clazz.getConstructor(boolean.class);
        return new AnnotatedWithParams(constructor, new Annotation[0]);
    }

    private AnnotatedWithParams getTestDelegatingCreator() {
        Class<?> clazz = getClass();
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
        return new AnnotatedWithParams(constructor, new Annotation[0]);
    }

    private AnnotatedParameter getTestInjectableParameter() {
        Class<?> clazz = getClass();
        BeanPropertyDefinition property = ClassIntrospector.findProperty(clazz, "injectableProperty");
        return new AnnotatedParameter(property, new Annotation[0]);
    }

    private AnnotatedMember getTestMember() {
        Class<?> clazz = getClass();
        Field field = clazz.getField("testField");
        return new AnnotatedMember(field, new Annotation[0]);
    }

    
}