package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class CreatorCollectorTest extends BaseTest {

    private CreatorCollector collector;
    private AnnotatedMethod defaultMethod;
    private AnnotatedMethod stringMethod;
    private AnnotatedMethod intMethod;
    private AnnotatedMethod longMethod;
    private AnnotatedMethod doubleMethod;
    private AnnotatedMethod booleanMethod;
    private AnnotatedMethod delegateMethod;
    private AnnotatedMethod propertyBasedMethod;
    private AnnotatedMethod arrayDelegateMethod;
    private AnnotatedConstructor intArrayConstructor;
    
    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();

        BeanDescription beanDesc = new BeanDescription(Object.class);
        collector = new CreatorCollector(beanDesc, config);

        Class<?> type = Object.class;
        Class<?> inputType = int[].class;

        defaultMethod = Mockito.mock(AnnotatedMethod.class);
        StringMethod defaultAnnotation = new StringMethod();
        defaultMethod.setAnnotations(new JacksonAnnotation[] { defaultAnnotation });
        defaultMethod.setParameterAnnotations(new Annotation[] { defaultAnnotation });
        defaultMethod.setParameterTypes(new Class<?>[0]);
        defaultMethod.setDeclaringClass(Object.class);
        defaultMethod.setName("defaultConstructor");
        collector.setDefaultCreator(new AnnotatedWithParams(defaultMethod));

        stringMethod = Mockito.mock(AnnotatedMethod.class);
        StringMethod stringAnnotation = new StringMethod();
        stringMethod.setAnnotations(new JacksonAnnotation[] { stringAnnotation });
        stringMethod.setParameterAnnotations(new Annotation[] { stringAnnotation });
        stringMethod.setParameterTypes(new Class<?>[] { String.class });
        stringMethod.setDeclaringClass(Object.class);
        stringMethod.setName("stringCreator");
        int index = 0;
        collector.addStringCreator(new AnnotatedWithParams(stringMethod), false);
        collector.addStringCreator(new AnnotatedWithParams(stringMethod), true);
        assertEquals("String creator should be set twice", 2, collector.getNumStringCreators());

        intMethod = Mockito.mock(AnnotatedMethod.class);
        IntConstructor intAnnotation = new IntConstructor();
        intMethod.setAnnotations(new JacksonAnnotation[] { intAnnotation });
        intMethod.setParameterAnnotations(new Annotation[] { intAnnotation });
        intMethod.setParameterTypes(new Class<?>[] { int.class });
        intMethod.setDeclaringClass(Object.class);
        intMethod.setName("intCreator");
        collector.addIntCreator(new AnnotatedWithParams(intMethod), false);
        collector.addIntCreator(new AnnotatedWithParams(intMethod), true);
        assertEquals("Int creator should be set twice", 2, collector.getNumIntCreators());

        longMethod = Mockito.mock(AnnotatedMethod.class);
        LongConstructor longAnnotation = new LongConstructor();
        longMethod.setAnnotations(new JacksonAnnotation[] { longAnnotation });
        longMethod.setParameterAnnotations(new Annotation[] { longAnnotation });
        longMethod.setParameterTypes(new Class<?>[] { long.class });
        longMethod.setDeclaringClass(Object.class);
        longMethod.setName("longCreator");
        collector.addLongCreator(new AnnotatedWithParams(longMethod), false);
        collector.addLongCreator(new AnnotatedWithParams(longMethod), true);
        assertEquals("Long creator should be set twice", 2, collector.getNumLongCreators());

        doubleMethod = Mockito.mock(AnnotatedMethod.class);
        DoubleConstructor doubleAnnotation = new DoubleConstructor();
        doubleMethod.setAnnotations(new JacksonAnnotation[] { doubleAnnotation });
        doubleMethod.setParameterAnnotations(new Annotation[] { doubleAnnotation });
        doubleMethod.setParameterTypes(new Class<?>[] { double.class });
        doubleMethod.setDeclaringClass(Object.class);
        doubleMethod.setName("doubleCreator");
        collector.addDoubleCreator(new AnnotatedWithParams(doubleMethod), false);
        collector.addDoubleCreator(new AnnotatedWithParams(doubleMethod), true);
        assertEquals("Double creator should be set twice", 2, collector.getNumDoubleCreators());

        booleanMethod = Mockito.mock(AnnotatedMethod.class);
        BooleanConstructor booleanAnnotation = new BooleanConstructor();
        booleanMethod.setAnnotations(new JacksonAnnotation[] { booleanAnnotation });
        booleanMethod.setParameterAnnotations(new Annotation[] { booleanAnnotation });
        booleanMethod.setParameterTypes(new Class<?>[] { boolean.class });
        booleanMethod.setDeclaringClass(Object.class);
        booleanMethod.setName("booleanCreator");
        collector.addBooleanCreator(new AnnotatedWithParams(booleanMethod), false);
        collector.addBooleanCreator(new AnnotatedWithParams(booleanMethod), true);
        assertEquals("Boolean creator should be set twice", 2, collector.getNumBooleanCreators());

        Class<?> delegateType = int[].class;
        delegateMethod = Mockito.mock(AnnotatedMethod.class);
        DelegatingConstructor delegateAnnotation = new DelegatingConstructor();
        delegateMethod.setAnnotations(new JacksonAnnotation[] { delegateAnnotation });
        delegateMethod.setParameterAnnotations(new Annotation[] { delegateAnnotation });
        delegateMethod.setParameterTypes(new Class<?>[] { Object[].class });
        delegateMethod.setDeclaringClass(Object.class);
        delegateMethod.setName("delegateCreator");
        int[] delegateArgs = new int[] { 1, 2, 3 };
        SettableBeanProperty[] delegateArgsProperty = new SettableBeanProperty[] {
                new SettableBeanProperty(Object[].class, "arg1"),
                new SettableBeanProperty(Object[].class, "arg2"),
                new SettableBeanProperty(Object[].class, "arg3")
        };
        collector.addDelegatingCreator(new AnnotatedWithParams(delegateMethod), false, delegateArgsProperty);
        collector.addDelegatingCreator(new AnnotatedWithParams(delegateMethod), true, delegateArgsProperty);
        assertEquals("Delegate creator should be set twice", 2, collector.getNumDelegatingCreators());

        propertyBasedMethod = Mockito.mock(AnnotatedMethod.class);
        PropertyBasedConstructor propertyBasedAnnotation = new PropertyBasedConstructor();
        propertyBasedMethod.setAnnotations(new JacksonAnnotation[] { propertyBasedAnnotation });
        propertyBasedMethod.setParameterAnnotations(new Annotation[] { propertyBasedAnnotation });
        propertyBasedMethod.setParameterTypes(new Class<?>[0]);
        propertyBasedMethod.setDeclaringClass(Object.class);
        propertyBasedMethod.setName("propertyBasedCreator");
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] {
                new SettableBeanProperty(String.class, "prop1"),
                new SettableBeanProperty(Integer.class, "prop2")
        };
        collector.addPropertyCreator(new AnnotatedWithParams(propertyBasedMethod), false, propertyBasedArgs);
        collector.addPropertyCreator(new AnnotatedWithParams(propertyBasedMethod), true, propertyBasedArgs);
        assertEquals("Property based creator should be set twice", 2, collector.getNumPropertyBasedCreators());

        Class<?> arrayDelegateType = int[].class;
        arrayDelegateMethod = Mockito.mock(AnnotatedMethod.class);
        ArrayDelegateConstructor arrayDelegateAnnotation = new ArrayDelegateConstructor();
        arrayDelegateMethod.setAnnotations(new JacksonAnnotation[] { arrayDelegateAnnotation });
        arrayDelegateMethod.setParameterAnnotations(new Annotation[] { arrayDelegateAnnotation });
        arrayDelegateMethod.setParameterTypes(new Class<?>[] { Object[].class, int[].class });
        arrayDelegateMethod.setDeclaringClass(Object.class);
        arrayDelegateMethod.setName("arrayDelegateCreator");
        int[] arrayDelegateArgs = new int[] { 1, 2, 3 };
        SettableBeanProperty[] arrayDelegateArgsProperty = new SettableBeanProperty[] {
                new SettableBeanProperty(Object[].class, "delegate"),
                new SettableBeanProperty(int[].class, "args")
        };
        collector.addArrayDelegateCreator(new AnnotatedWithParams(arrayDelegateMethod), false, arrayDelegateArgsProperty);
        collector.addArrayDelegateCreator(new AnnotatedWithParams(arrayDelegateMethod), true, arrayDelegateArgsProperty);
        assertEquals("Array delegate creator should be set twice", 2, collector.getNumArrayDelegateCreators());

        intArrayConstructor = Mockito.mock(AnnotatedConstructor.class);
        IntArrayConstructor intArrayAnnotation = new IntArrayConstructor();
        intArrayConstructor.setAnnotations(new JacksonAnnotation[] { intArrayAnnotation });
        intArrayConstructor.setParameterAnnotations(new Annotation[] { intArrayAnnotation });
        intArrayConstructor.setParameterTypes(new Class<?>[] { int[].class });
        intArrayConstructor.setDeclaringClass(Object.class);
        intArrayConstructor.setName("intArrayConstructor");
        int[] intArrayArgs = new int[] { 1, 2, 3 };
        SettableBeanProperty[] intArrayDelegateArgs = new SettableBeanProperty[] {
                new SettableBeanProperty(int[].class, "args")
        };
        collector.addIntArrayCreator(new AnnotatedWithParams(intArrayConstructor), false, intArrayDelegateArgs);
        collector.addIntArrayCreator(new AnnotatedWithParams(intArrayConstructor), true, intArrayDelegateArgs);
        assertEquals("Int array constructor should be set twice", 2, collector.getNumIntArrayCreators());
    }

    @Test
    public void testConstructValueInstantiator() throws Exception {
        Assert.assertNotNull(collector.constructValueInstantiator(getTestContext().getDeserializationConfig()));
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiatorWithNoCreators() throws Exception {
        collector.setDefaultCreator(null);
        try {
            collector.constructValueInstantiator(getTestContext().getDeserializationConfig());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("No creators found", e.getMessage());
        }
    }

    @Test
    public void testGetters() throws Exception {
        assertEquals(0, collector.getNumDefaultCreators());
        assertEquals(2, collector.getNumStringCreators());
        assertEquals(2, collector.getNumIntCreators());
        assertEquals(2, collector.getNumLongCreators());
        assertEquals(2, collector.getNumDoubleCreators());
        assertEquals(2, collector.getNumBooleanCreators());
        assertEquals(2, collector.getNumDelegateCreators());
        assertEquals(2, collector.getNumPropertyBasedCreators());
        assertEquals(2, collector.getNumArrayDelegateCreators());
        assertEquals(2, collector.getNumIntArrayCreators());

        assertEquals(delegateType, collector.getDelegateType());
        assertEquals(delegateType, collector.getArrayDelegateType());

        assertNull(collector.getDefaultBeanDescription());
        assertNotNull(collector.getBeanDescription());

        assertTrue(collector.hasDefaultCreator());
        assertTrue(collector.hasDelegatingCreator());
        assertTrue(collector.hasPropertyBasedCreator());

        assertFalse(collector.hasIntArrayCreator());
        assertTrue(collector.hasDelegatingCreator());

        assertFalse(collector.isCanFixAccess());
        assertTrue(collector.isForceAccess());

        List<AnnotatedWithParams> creators = Arrays.asList(collector.getDefaultCreator(),
                collector.getStringCreator(0), collector.getIntCreator(0),
                collector.getLongCreator(0), collector.getDoubleCreator(0),
                collector.getBooleanCreator(0), collector.getDelegatingCreator(0),
                collector.getPropertyBasedCreator(0), collector.getArrayDelegateCreator(0),
                collector.getIntArrayCreator(0));
        assertEquals(11, creators.size());
        assertEquals(defaultMethod, collector.getDefaultCreator().getAnnotatedMember());
        assertEquals(stringMethod, collector.getStringCreator(0).getAnnotatedMember());
        assertEquals(intMethod, collector.getIntCreator(0).getAnnotatedMember());
        assertEquals(longMethod, collector.getLongCreator(0).getAnnotatedMember());
        assertEquals(doubleMethod, collector.getDoubleCreator(0).getAnnotatedMember());
        assertEquals(booleanMethod, collector.getBooleanCreator(0).getAnnotatedMember());
        assertEquals(delegateMethod, collector.getDelegatingCreator(0).getAnnotatedMember());
        assertEquals(propertyBasedMethod, collector.getPropertyBasedCreator(0).getAnnotatedMember());
        assertEquals(delegateMethod, collector.getArrayDelegateCreator(0).getAnnotatedMember());
        assertEquals(intArrayConstructor, collector.getIntArrayCreator(0).getAnnotatedMember());

        assertNull(collector.getIncompleteParameter());

        assertEquals(null, collector.getDelegateType(0));
        assertEquals(int[].class, collector.getDelegateType(1));

        Method m = collector.getClass().getDeclaredMethod("verifyNonDup", AnnotatedWithParams.class, int.class, boolean.class);
        m.setAccessible(true);
        m.invoke(collector, new AnnotatedWithParams(delegateMethod), C_DELEGATE, false);
        m.invoke(collector, new AnnotatedWithParams(propertyBasedMethod), C_PROPS, false);
        m.invoke(collector, new AnnotatedWithParams(delegateMethod), C_DELEGATE, true);
        m.invoke(collector, new AnnotatedWithParams(propertyBasedMethod), C_PROPS, true);
        assertEquals(3, collector.getNumDelegatingCreators());
        assertEquals(3, collector.getNumPropertyBasedCreators());
    }
}

class StringMethod extends JacksonAnnotation {}
class IntConstructor extends JacksonAnnotation {}
class IntArrayConstructor extends JacksonAnnotation {}
class LongConstructor extends JacksonAnnotation {}
class DoubleConstructor extends JacksonAnnotation {}
class BooleanConstructor extends JacksonAnnotation {}
class DelegatingConstructor extends JacksonAnnotation {}
class ArrayDelegateConstructor extends JacksonAnnotation {}