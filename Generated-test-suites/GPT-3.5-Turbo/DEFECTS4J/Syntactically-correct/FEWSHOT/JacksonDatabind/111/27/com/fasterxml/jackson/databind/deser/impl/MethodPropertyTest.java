import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;

public class MethodPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, methodProperty.getPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getMember());
    }
    
    @Test
    public void testWithNullProvider() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        SettableBeanProperty newMethodProperty = methodProperty.withNullProvider(nva);
        
        assertEquals(methodProperty.getType(), newMethodProperty.getType());
        assertEquals(deser, newMethodProperty.getValueDeserializer());
        assertEquals(nva, newMethodProperty.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.fixAccess(config);
        
        verifyZeroInteractions(config);
        //TODO: verify _annotated.fixAccess() is called
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonToken jsonToken = JsonToken.START_OBJECT;
        
        when(p.hasToken(jsonToken)).thenReturn(false);
        
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.deserializeAndSet(p, ctxt, instance);
        
        verify(p).hasToken(jsonToken);
        //TODO: verify other method invocations and assertions
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonToken jsonToken = JsonToken.START_OBJECT;
        
        when(p.hasToken(jsonToken)).thenReturn(false);
        
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        verify(p).hasToken(jsonToken);
        //TODO: verify other method invocations and assertions
    }
    
    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.set(instance, value);
        
        //TODO: verify method invocations and assertions
    }
    
    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.setAndReturn(instance, value);
        
        //TODO: verify method invocations and assertions
    }
    
    //TODO: Add more test cases to comprehensively test all methods in the Class
    
    @Test
    public void testReadResolve() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        MethodProperty resolvedProperty = methodProperty.readResolve();
        
        //TODO: verify method invocations and assertions
        //assertEquals(methodProperty, resolvedProperty);
    }
}