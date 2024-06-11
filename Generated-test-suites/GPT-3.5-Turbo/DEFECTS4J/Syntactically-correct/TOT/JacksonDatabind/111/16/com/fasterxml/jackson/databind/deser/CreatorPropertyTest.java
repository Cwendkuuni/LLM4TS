package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {
    
    @Test
    public void testFindInjectableValue_NullInjectableValueId_ShouldThrowException() {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        DeserializationContext context = createDeserializationContext();
        Object beanInstance = new Object();
        
        try {
            creatorProperty.findInjectableValue(context, beanInstance);
        } catch (JsonMappingException e) {
            String expectedMessage = "Property 'null' (type " + creatorProperty.getClass().getName() 
                + ") has no injectable value id configured";
            assertEquals(expectedMessage, e.getMessage());
            return;
        }
        
        // Fail test if exception is not thrown
        throw new AssertionError("Expected JsonMappingException not thrown");
    }
    
    @Test
    public void testFindInjectableValue_NonNullInjectableValueId_ShouldReturnInjectableValue() {
        Object injectableValueId = new Object();
        CreatorProperty creatorProperty = createCreatorProperty(injectableValueId);
        DeserializationContext context = createDeserializationContext();
        Object beanInstance = new Object();
        
        Object result = creatorProperty.findInjectableValue(context, beanInstance);
        
        assertEquals(injectableValueId, result);
    }
    
    @Test
    public void testGetAnnotation_NullAnnotated_ShouldReturnNull() {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        Class<Annotation> acls = Annotation.class;
        
        Annotation result = creatorProperty.getAnnotation(acls);
        
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_NonNullAnnotated_ShouldReturnAnnotation() {
        TestAnnotation annotation = new TestAnnotation();
        CreatorProperty creatorProperty = createCreatorPropertyWithAnnotation(annotation);
        Class<TestAnnotation> acls = TestAnnotation.class;
        
        TestAnnotation result = creatorProperty.getAnnotation(acls);
        
        assertEquals(annotation, result);
    }
    
    @Test
    public void testDeserializeAndSet_ShouldSetInjectedValue() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        SettableBeanProperty fallbackSetter = createFallbackSetter();
        creatorProperty.setFallbackSetter(fallbackSetter);
        
        Object instance = new Object();
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("test");
        DeserializationContext context = createDeserializationContext();
        
        creatorProperty.deserializeAndSet(jsonParser, context, instance);
        
        assertEquals("test", fallbackSetter.get(instance));
    }
    
    @Test
    public void testDeserializeSetAndReturn_ShouldSetAndReturnValue() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        SettableBeanProperty fallbackSetter = createFallbackSetter();
        creatorProperty.setFallbackSetter(fallbackSetter);
        
        Object instance = new Object();
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("test");
        DeserializationContext context = createDeserializationContext();
        
        Object result = creatorProperty.deserializeSetAndReturn(jsonParser, context, instance);
        
        assertEquals("test", fallbackSetter.get(instance));
        assertEquals(instance, result);
    }
    
    @Test
    public void testSet_ShouldSetValue() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        SettableBeanProperty fallbackSetter = createFallbackSetter();
        creatorProperty.setFallbackSetter(fallbackSetter);
        
        Object instance = new Object();
        Object value = "test";
        
        creatorProperty.set(instance, value);
        
        assertEquals(value, fallbackSetter.get(instance));
    }
    
    @Test
    public void testSetAndReturn_ShouldSetAndReturnValue() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        SettableBeanProperty fallbackSetter = createFallbackSetter();
        creatorProperty.setFallbackSetter(fallbackSetter);
        
        Object instance = new Object();
        Object value = "test";
        
        Object result = creatorProperty.setAndReturn(instance, value);
        
        assertEquals(value, fallbackSetter.get(instance));
        assertEquals(instance, result);
    }
    
    private CreatorProperty createCreatorProperty(Object injectableValueId) {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null);
        return new CreatorProperty(null, null, null, null, null, annotatedParameter, 0, injectableValueId, null);
    }
    
    private CreatorProperty createCreatorPropertyWithAnnotation(TestAnnotation annotation) {
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null) {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                if (acls == TestAnnotation.class) {
                    return (A) annotation;
                }
                return null;
            }
        };
        return new CreatorProperty(null, null, null, null, null, annotatedParameter, 0, null, null);
    }
    
    private SettableBeanProperty createFallbackSetter() {
        return new SettableBeanProperty(null, null, null, null, null, null) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                // do nothing
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                // do nothing
                return instance;
            }

            @Override
            public Object getInjectableValueId() {
                return null;
            }
        };
    }
    
    private DeserializationContext createDeserializationContext() {
        return new DeserializationContext(null, null);
    }
    
    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}