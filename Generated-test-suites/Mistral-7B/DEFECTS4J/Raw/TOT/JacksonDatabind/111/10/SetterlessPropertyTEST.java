package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.AnnotationIntrospector;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        // Given: Setterless property with a getter method for accessing and modifying a List
        Object targetObject = new Object();
        List targetList = Arrays.asList(new Integer(1), new Integer(2));
        Method getter = Mockito.spy(TargetListGetter.class.getMethod("getList", (Class[]) null));
        SetterlessProperty property = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        ) {
            {
                _getter = getter;
            }
        };

        // When: deserialize and set a value
        parser.setValue("1,2");
        property.deserializeAndSet(parser, mock(DeserializationContext.class), targetObject);

        // Then: the getter method was invoked and the list was modified
        verify(getter).invoke(targetObject);
        Assert.assertEquals(Arrays.asList(new Integer(1), new Integer(2)), getter.invoke(targetObject));
    }

    @Test
    public void testDeserializeAndSet_JsonNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        // Given: Setterless property with a getter method for accessing and modifying a Map
        Object targetObject = new Object();
        Map targetMap = new HashMap();
        Method getter = Mockito.spy(TargetMapGetter.class.getMethod("getMap", (Class[]) null));
        SetterlessProperty property = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        ) {
            {
                _getter = getter;
            }
        };

        // When: deserialize and set a null value
        parser.nextValue("null");
        property.deserializeAndSet(parser, mock(DeserializationContext.class), targetObject);

        // Then: the getter method was invoked and the map was not modified
        verify(getter, never()).invoke(targetObject);
        Assert.assertNull(getter.invoke(targetObject));
    }

    @Test
    public void testDeserializeAndSet_InvalidValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        // Given: Setterless property with a getter method for accessing and modifying a List
        Object targetObject = new Object();
        List targetList = Arrays.asList(new Integer(1), new Integer(2));
        Method getter = Mockito.spy(TargetListGetter.class.getMethod("getList", (Class[]) null));
        SetterlessProperty property = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        ) {
            {
                _getter = getter;
            }
        };

        // When: deserialize and set an invalid value
        parser.setValue("invalid");

        // Then: an exception is thrown
        try {
            property.deserializeAndSet(parser, mock(DeserializationContext.class), targetObject);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        // Then: the getter method was not invoked
        verify(getter, never()).invoke(targetObject);
        Assert.assertSame(targetObject, getter.invoke(targetObject));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        // Given: Setterless property with a getter method for accessing and modifying a List
        Object targetObject = new Object();
        List targetList = Arrays.asList(new Integer(1), new Integer(2));
        Method getter = Mockito.spy(TargetListGetter.class.getMethod("getList", (Class[]) null));
        SetterlessProperty property = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(JavaType.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        ) {
            {
                _getter = getter;
            }
        };

        // When: deserialize, set, and return a value
        parser.setValue("1,2");
        Object result = property.deserializeSetAndReturn(parser, mock(DeserializationContext.class), targetObject);

        // Then: the getter method was invoked and the list was modified
        verify(getter).invoke(targetObject);
        Assert.assertEquals(Arrays.asList(new Integer(1), new Integer(2)), getter.invoke(targetObject));
        Assert.assertEquals(targetObject, result);
    }

    @Test
    public void testSet() throws Exception {
        // This method should not be called on a setterless property
        throw new UnsupportedOperationException("Should never call `set()` on setterless property");
    }

    @Test
    public void testSetAndReturn() throws Exception {
        // This method should not be called on a setterless property
        throw new UnsupportedOperationException("Should never call `set()` on setterless property");
    }

    private static class TargetListGetter {
        public static List getList(Object obj) {
            return (List) obj.getClass().getField("list").get(obj);
        }
    }

    private static class TargetMapGetter {
        public static Map getMap(Object obj) {
            return (Map) obj.getClass().getField("map").get(obj);
        }
    }
}