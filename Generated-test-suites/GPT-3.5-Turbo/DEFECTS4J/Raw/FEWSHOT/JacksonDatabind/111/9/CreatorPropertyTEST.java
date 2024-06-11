package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorPropertyTest {
    
    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType(String.class);
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 1;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, 
                contextAnnotations, param, index, injectableValueId, metadata);
        
        assertEquals(name, property.getFullName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property.getMember());
        assertEquals(index, property.getCreatorIndex());
        assertEquals(injectableValueId, property.getInjectableValueId());
        assertEquals(metadata, property.getMetadata());
    }
    
    @Test
    public void testWithDeserialier() {
        CreatorProperty property = new CreatorProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        SettableBeanProperty result = property.withValueDeserializer(deser);
        
        assertEquals(property, result);
        assertEquals(deser, result.getValueDeserializer());
    }
    
    @Test
    public void testWithNullProvider() {
        CreatorProperty property = new CreatorProperty();
        NullValueProvider nva = new NullValueProvider();
        
        SettableBeanProperty result = property.withNullProvider(nva);
        
        assertEquals(property, result);
        assertEquals(nva, result.getNullProvider());
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        CreatorProperty property = new CreatorProperty();
        
        property.fixAccess(config);
        
        // No assertion, we'll just check that no exception is thrown
    }
    
    @Test
    public void testSetFallbackSetter() {
        CreatorProperty property = new CreatorProperty();
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();
        
        property.setFallbackSetter(fallbackSetter);
        
        assertEquals(fallbackSetter, property.getFallbackSetter());
    }
    
    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty property = new CreatorProperty();
        
        property.markAsIgnorable();
        
        assertTrue(property.isIgnorable());
    }
    
    @Test
    public void testIsIgnorable() {
        CreatorProperty property = new CreatorProperty();
        property.markAsIgnorable();
        
        assertTrue(property.isIgnorable());
    }
    
    @Test
    public void testFindInjectableValue() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext();
        CreatorProperty property = new CreatorProperty();
        Object beanInstance = new Object();
        
        assertNull(property.findInjectableValue(context, beanInstance));
    }
    
    @Test
    public void testInject() throws IOException {
        DeserializationContext context = new DeserializationContext();
        CreatorProperty property = new CreatorProperty();
        Object beanInstance = new Object();
        
        property.inject(context, beanInstance);
        
        // No assertion, we'll just check that no exception is thrown
    }
    
    @Test
    public void testGetAnnotation() {
        CreatorProperty property = new CreatorProperty();
        Class<A> acls = A.class;
        
        assertNull(property.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        CreatorProperty property = new CreatorProperty();
        AnnotatedMember expected = new AnnotatedMember();
        
        property.setMember(expected);
        
        AnnotatedMember result = property.getMember();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testToString() {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType(String.class);
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 1;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();
        
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, 
                contextAnnotations, param, index, injectableValueId, metadata);
        
        String expected = "[creator property, name 'testName'; inject id 'java.lang.Object']";
        String result = property.toString();
        
        assertEquals(expected, result);
    }
    
    @Test(expected = IOException.class)
    public void testSetFailsWithoutFallbackSetter() throws IOException {
        CreatorProperty property = new CreatorProperty();
        
        property.set(new Object(), new Object());
    }
    
    @Test(expected = IOException.class)
    public void testSetAndReturnFailsWithoutFallbackSetter() throws IOException {
        CreatorProperty property = new CreatorProperty();
        
        property.setAndReturn(new Object(), new Object());
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeAndSetFailsWithoutFallbackSetter() throws IOException {
        CreatorProperty property = new CreatorProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        property.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnFailsWithoutFallbackSetter() throws IOException {
        CreatorProperty property = new CreatorProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        property.deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test
    public void testGetInjectableValueId() {
        CreatorProperty property = new CreatorProperty();
        Object expected = new Object();
        
        property.setInjectableValueId(expected);
        
        Object result = property.getInjectableValueId();
        
        assertEquals(expected, result);
    }
    
    @Test(expected = IOException.class)
    public void testVerifySetterFailsWithoutFallbackSetter() throws IOException {
        CreatorProperty property = new CreatorProperty();
        
        property.verifySetter();
    }
    
    @Test(expected = IOException.class)
    public void testReportMissingSetterWithNullParserAndContext() throws IOException {
        CreatorProperty property = new CreatorProperty();
        
        property.reportMissingSetter(null, null);
    }
    
    @Test
    public void testGetterSetter() {
        CreatorProperty property = new CreatorProperty();
        AnnotatedParameter param = new AnnotatedParameter();
        
        property.setMember(param);
        
        AnnotatedMember expected = param;
        AnnotatedMember result = property.getMember();
        
        assertEquals(expected, result);
    }

}