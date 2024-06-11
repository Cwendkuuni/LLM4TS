package com.fasterxml.jackson.databind.deser; 
 
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
 
public class CreatorPropertyTest {
 
    private CreatorProperty creatorProperty;
 
    @Before
    public void setUp() {
        creatorProperty = new CreatorProperty(
                new PropertyName("name"), JavaType.INT_TYPE,
                null, null, null, null, 0, null, null);
    }
 
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = creatorProperty.withName(newName);
        assertEquals(newName, result.getFullName());
    }
 
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty result = creatorProperty.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }
 
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty result = creatorProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullProvider());
    }
 
    @SuppressWarnings("deprecation")
    @Test
    public void testFixAccess() {
        creatorProperty.fixAccess(null);
    }
 
    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = null;
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, creatorProperty.getFallbackSetter());
    }
 
    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }
 
    @Test
    public void testFindInjectableValue() throws Exception {
        DeserializationContext context = null;
        Object beanInstance = null;
        creatorProperty.findInjectableValue(context, beanInstance);
    }
 
    @Test
    public void testInject() throws Exception {
        DeserializationContext context = null;
        Object beanInstance = null;
        creatorProperty.inject(context, beanInstance);
    }
 
    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        assertNull(creatorProperty.getAnnotation(acls));
    }
 
    @Test
    public void testGetMember() {
        assertNull(creatorProperty.getMember());
    }
 
    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }
 
    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        creatorProperty.deserializeAndSet(p, ctxt, instance);
    }
 
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        Object result = creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
    }
 
    @Test
    public void testSet() throws Exception {
        Object instance = null;
        Object value = null;
        creatorProperty.set(instance, value);
    }
 
    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = null;
        Object value = null;
        Object result = creatorProperty.setAndReturn(instance, value);
        assertNull(result);
    }
 
    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }
 
    @Test
    public void testToString() {
        assertEquals("[creator property, name 'name'; inject id 'null']", creatorProperty.toString());
    }
 
    @Test(expected = IOException.class)
    public void testVerifySetterWithIOException() throws Exception {
        creatorProperty.verifySetter(null, null);
    }
 
    @Test
    public void testVerifySetterWithJsonParserAndDeserializationContext() {
        creatorProperty.verifySetter(new JsonParser(null) {
        }, null);
    }
 
    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetter() throws Exception {
        creatorProperty.reportMissingSetter(null, null);
    }
 
    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetterWithJsonParser() throws Exception {
        creatorProperty.reportMissingSetter(new JsonParser(null) {
        }, null);
    }
 
}