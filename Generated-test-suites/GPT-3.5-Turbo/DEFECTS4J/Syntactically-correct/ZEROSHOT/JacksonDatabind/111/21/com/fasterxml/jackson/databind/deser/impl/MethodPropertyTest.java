import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {
    
    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);
        
        assertEquals(nva, newMethodProperty.getNullValueProvider());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new ObjectMapper().getJsonFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        methodProperty.deserializeAndSet(p, ctxt, instance);
        
        // Verify that the instance is modified as expected
        // TODO: Add assertion here
        
        // TODO: Complete the test method
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new ObjectMapper().getJsonFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        // Verify that the instance is modified as expected
        // TODO: Add assertion here
        
        assertEquals(instance, result);
        assertTrue(result instanceof Object);
        
        try {
            result = result.toString();
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
            assertEquals("null", e.getMessage());
        }
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.set(instance, value);
        
        // Verify that the instance is modified as expected
        // TODO: Add assertion here
        
        // TODO: Complete the test method
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        Object result = methodProperty.setAndReturn(instance, value);
        
        // Verify that the instance is modified as expected
        // TODO: Add assertion here
        
        assertEquals(instance, result);
        assertTrue(result instanceof Object);
        
        try {
            result = result.toString();
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
            assertEquals("null", e.getMessage());
        }
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        
        methodProperty.fixAccess(config);
        
        // TODO: Complete the test method
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        
        Annotation result = methodProperty.getAnnotation(acls);
        
        // Verify that the result is correct as expected
        assertEquals(null, result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = methodProperty.getMember();
        
        // Verify that the result is correct as expected
        assertEquals(null, result);
    }

    @Test
    public void testWithNullValueProvider() {
        NullValueProvider nva = new NullValueProvider();
        MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);
        
        assertEquals(nva, newMethodProperty.getNullValueProvider());
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName();
        MethodProperty newMethodProperty = methodProperty.withName(newName);
        
        assertEquals(newName, newMethodProperty.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(deser);
        
        assertEquals(deser, newMethodProperty.getValueDeserializer());
    }
}