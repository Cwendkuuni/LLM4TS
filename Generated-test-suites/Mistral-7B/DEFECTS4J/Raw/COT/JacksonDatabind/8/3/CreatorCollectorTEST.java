package com.fasterxml.jackson.databind.deser.impl; // Make sure to import necessary classes

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    

    @Test
    public void testDefaultConstructor() {
        try {
            BeanDescription beanDesc = new BeanDescription(String.class);
            CreatorCollector collector = new CreatorCollector(beanDesc, true);
            assertNull(collector.constructValueInstantiator(null));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructValueInstantiator_WhenNoSpecialCreator() {
        try {
            BeanDescription beanDesc = new BeanDescription(ArrayList.class);
            CreatorCollector collector = new CreatorCollector(beanDesc, true);
            ValueInstantiator instantiator = collector.constructValueInstantiator(null);
            assertTrue(instantiator instanceof Vanilla);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetDefaultCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method method = String.class.getDeclaredMethod("valueOf", new Class[] { String.class });
        AnnotatedWithParams creator = new AnnotatedWithParams(method, null);
        collector.setDefaultCreator(creator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(String.class, instantiator.getType());
    }

    @Test
    public void testAddStringCreator_NullCreator() {
        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        try {
            collector.addStringCreator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddStringCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method method = String.class.getDeclaredMethod("valueOf", new Class[] { String.class });
        AnnotatedWithParams creator = new AnnotatedWithParams(method, null);
        collector.addStringCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(String.class, instantiator.getType());
        assertTrue(instantiator.isStringCreator());
    }

    @Test
    public void testAddIntCreator_NullCreator() {
        BeanDescription beanDesc = new BeanDescription(Integer.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        try {
            collector.addIntCreator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddIntCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(Integer.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method method = Integer.class.getDeclaredMethod("valueOf", new Class[] { int.class });
        AnnotatedWithParams creator = new AnnotatedWithParams(method, null);
        collector.addIntCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(Integer.class, instantiator.getType());
        assertTrue(instantiator.isIntCreator());
    }

    @Test
    public void testAddLongCreator_NullCreator() {
        BeanDescription beanDesc = new BeanDescription(Long.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        try {
            collector.addLongCreator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddLongCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(Long.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method method = Long.class.getDeclaredMethod("valueOf", new Class[] { long.class });
        AnnotatedWithParams creator = new AnnotatedWithParams(method, null);
        collector.addLongCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(Long.class, instantiator.getType());
        assertTrue(instantiator.isLongCreator());
    }

    @Test
    public void testAddDoubleCreator_NullCreator() {
        BeanDescription beanDesc = new BeanDescription(Double.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        try {
            collector.addDoubleCreator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddDoubleCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(Double.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method method = Double.class.getDeclaredMethod("valueOf", new Class[] { double.class });
        AnnotatedWithParams creator = new AnnotatedWithParams(method, null);
        collector.addDoubleCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(Double.class, instantiator.getType());
        assertTrue(instantiator.isDoubleCreator());
    }

    @Test
    public void testAddBooleanCreator_NullCreator() {
        BeanDescription beanDesc = new BeanDescription(Boolean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        try {
            collector.addBooleanCreator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddBooleanCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(Boolean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method method = Boolean.class.getDeclaredMethod("valueOf", new Class[] { boolean.class });
        AnnotatedWithParams creator = new AnnotatedWithParams(method, null);
        collector.addBooleanCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(Boolean.class, instantiator.getType());
        assertTrue(instantiator.isBooleanCreator());
    }

    @Test
    public void testAddDelegatingCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class<?> targetClass = StringUtils.class;
        AnnotatedMember delegateMethod = targetClass.getDeclaredMethod("reverse", new Class[] { String.class });
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateMethod, null);

        CreatorProperty[] injectables = new CreatorProperty[] {
                new CreatorProperty(String.class, "stringCreator")
        };
        collector.addDelegatingCreator(new AnnotatedWithParams(null, null), false, injectables);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(StringUtils.class, instantiator.getType());
        assertNotNull(instantiator.getDelegateCreator());
        assertEquals(String.class, instantiator.getDelegateCreator().getType());
    }

    @Test
    public void testAddPropertyCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(MyPojo.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        CreatorProperty[] properties = new CreatorProperty[] {
                new CreatorProperty("name", String.class),
                new CreatorProperty("age", Integer.class)
        };
        collector.addPropertyCreator(new AnnotatedWithParams(null, null), false, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertEquals(MyPojo.class, instantiator.getType());
        assertArrayEquals(new CreatorProperty[] {
                new CreatorProperty("name", String.class),
                new CreatorProperty("age", Integer.class)
        }, instantiator.getPropertyBasedArgs());
    }

    @Test
    public void testVerifyNonDup() throws Exception {
        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method stringCreator = String.class.getDeclaredMethod("valueOf", new Class[] { String.class });
        AnnotatedWithParams stringCreatorAnnotation = new AnnotatedWithParams(stringCreator, null);

        Method intCreator = Integer.class.getDeclaredMethod("valueOf", new Class[] { int.class });
        AnnotatedWithParams intCreatorAnnotation = new AnnotatedWithParams(intCreator, null);

        Method booleanCreator = Boolean.class.getDeclaredMethod("valueOf", new Class[] { boolean.class });
        AnnotatedWithParams booleanCreatorAnnotation = new AnnotatedWithParams(booleanCreator, null);

        collector.addStringCreator(stringCreatorAnnotation, true);
        collector.verifyNonDup(stringCreatorAnnotation, C_STRING, true);

        // Should not add intCreator since it's already been added as stringCreator
        try {
            collector.addIntCreator(intCreatorAnnotation, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Should add booleanCreator since it's a different type
        collector.addBooleanCreator(booleanCreatorAnnotation, false);
    }

    @Test
    public void testVerifyNonDup_SameTypeButDifferentCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(String.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method stringCreator1 = String.class.getDeclaredMethod("valueOf", new Class[] { String.class });
        AnnotatedWithParams stringCreator1Annotation = new AnnotatedWithParams(stringCreator1, null);

        Method stringCreator2 = String.class.getDeclaredMethod("valueOf", new Class[] { char[].class });
        AnnotatedWithParams stringCreator2Annotation = new AnnotatedWithParams(stringCreator2, null);

        collector.addStringCreator(stringCreator1Annotation, true);
        collector.verifyNonDup(stringCreator1Annotation, C_STRING, true);

        // Should add stringCreator2 since it's a different method with the same return type
        collector.addStringCreator(stringCreator2Annotation, false);
    }

    @Test
    public void testVerifyNonDup_SameCreatorButDifferentTypes() throws Exception {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Method intCreator = Integer.class.getDeclaredMethod("valueOf", new Class[] { int.class });
        AnnotatedWithParams intCreatorAnnotation = new AnnotatedWithParams(intCreator, null);

        Method longCreator = Long.class.getDeclaredMethod("valueOf", new Class[] { long.class });
        AnnotatedWithParams longCreatorAnnotation = new AnnotatedWithParams(longCreator, null);

        collector.addIntCreator(intCreatorAnnotation, true);
        collector.verifyNonDup(longCreatorAnnotation, C_LONG, true);

        // Should not add longCreator since it's already been added as intCreator
        try {
            collector.addLongCreator(longCreatorAnnotation, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    
}

class MyPojo {
    private String name;
    private int age;

    // Getters and setters
}