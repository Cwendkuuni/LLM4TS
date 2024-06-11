import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class MethodPropertyTest {
    
    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        Annotations annotations = mapper.getDeserializationConfig().getAnnotationIntrospector().getAllAnnotations(
                    mapper.constructType(MethodProperty.class)
                ).getAllAnnotations();
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(new MethodProperty());
        methodProperty = new MethodProperty(new BeanPropertyDefinition(), mapper.constructType(MethodProperty.class), null,
                annotations, annotatedMethod);
    }

    @Test
    public void testGetAnnotation() {
        Class<JsonDeserialize> annotationClass = JsonDeserialize.class;
        JsonDeserialize annotation = methodProperty.getAnnotation(annotationClass);
        assertNotNull(annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = methodProperty.getMember();
        assertNotNull(annotatedMember);
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object instance = new Object();
        try {
            methodProperty.deserializeAndSet(parser, context, instance);
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object instance = new Object();
        try {
            methodProperty.deserializeSetAndReturn(parser, context, instance);
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        try {
            methodProperty.set(instance, value);
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        try {
            methodProperty.setAndReturn(instance, value);
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
}