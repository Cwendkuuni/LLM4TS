package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = null; // replace with appropriate type
        PropertyName wrapperName = null; // replace with appropriate type
        TypeDeserializer typeDeser = null; // replace with appropriate type
        Annotations contextAnnotations = null; // replace with appropriate type
        AnnotatedParameter param = null; // replace with appropriate type
        int index = 0; // replace with appropriate value
        Object injectableValueId = null; // replace with appropriate value
        PropertyMetadata metadata = null; // replace with appropriate type

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        assertNotNull(creatorProperty);
        // add assertions
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("test");
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        SettableBeanProperty result = creatorProperty.withName(newName);

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        SettableBeanProperty result = creatorProperty.withValueDeserializer(deser);

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        SettableBeanProperty result = creatorProperty.withNullProvider(nva);

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty.fixAccess(config);

        // add assertions
    }
    
    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty.setFallbackSetter(fallbackSetter);

        // add assertions
    }
    
    @Test
    public void testMarkAsIgnorable() {
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty.markAsIgnorable();

        // add assertions
    }
    
    @Test
    public void testIsIgnorable() {
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        boolean result = creatorProperty.isIgnorable();

        // add assertions
    }
    
    @Test
    public void testFindInjectableValue() {
        DeserializationContext context = null; // replace with appropriate type
        Object beanInstance = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        Object result = creatorProperty.findInjectableValue(context, beanInstance);

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testInject() {
        DeserializationContext context = null; // replace with appropriate type
        Object beanInstance = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty.inject(context, beanInstance);

        // add assertions
    }
    
    @Test
    public void testGetAnnotation() {
        Class<A> acls = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        A result = creatorProperty.getAnnotation(acls);

        // add assertions
    }
    
    @Test
    public void testGetMember() {
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        AnnotatedMember result = creatorProperty.getMember();

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testGetCreatorIndex() {
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        int result = creatorProperty.getCreatorIndex();

        // add assertions
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = null; // replace with appropriate type
        DeserializationContext ctxt = null; // replace with appropriate type
        Object instance = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty.deserializeAndSet(p, ctxt, instance);

        // add assertions
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = null; // replace with appropriate type
        DeserializationContext ctxt = null; // replace with appropriate type
        Object instance = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        Object result = creatorProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testSet() {
        Object instance = null; // replace with appropriate type
        Object value = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty.set(instance, value);

        // add assertions
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = null; // replace with appropriate type
        Object value = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        Object result = creatorProperty.setAndReturn(instance, value);

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testGetInjectableValueId() {
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        Object result = creatorProperty.getInjectableValueId();

        assertNotNull(result);
        // add assertions
    }
    
    @Test
    public void testToString() {
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        String result = creatorProperty.toString();

        assertNotNull(result);
        // add assertions
    }

    @Test
    public void test_verifySetter() {
        JsonParser p = null; // replace with appropriate type
        DeserializationContext ctxt = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty._verifySetter();

        // add assertions
    }

    @Test
    public void test_reportMissingSetter() {
        JsonParser p = null; // replace with appropriate type
        DeserializationContext ctxt = null; // replace with appropriate type
        // create a CreatorProperty object to test
        CreatorProperty creatorProperty = null; // replace with appropriate object

        creatorProperty._reportMissingSetter(p, ctxt);

        // add assertions
    }
}