package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        
        assertNotNull(property);
        assertNotNull(property._annotated);
        assertEquals(0, property._creatorIndex);
        assertNull(property._fallbackSetter);
        assertFalse(property._ignorable);
    }

    @Test
    public void testWithNullProvider() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        CreatorProperty newProperty = property.withNullProvider(null);

        assertNotNull(newProperty);
        assertEquals(property._annotated, newProperty._annotated);
        assertEquals(property._creatorIndex, newProperty._creatorIndex);
        assertEquals(property._fallbackSetter, newProperty._fallbackSetter);
        assertEquals(property._ignorable, newProperty._ignorable);
        assertNotSame(property._valueDeserializer, newProperty._valueDeserializer);
        assertEquals(property._nullProvider, newProperty._nullProvider);
    }

    @Test
    public void testWithvalueDeserializer() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        CreatorProperty newProperty = property.withValueDeserializer(null);

        assertNotNull(newProperty);
        assertEquals(property._annotated, newProperty._annotated);
        assertEquals(property._creatorIndex, newProperty._creatorIndex);
        assertEquals(property._fallbackSetter, newProperty._fallbackSetter);
        assertEquals(property._ignorable, newProperty._ignorable);
        assertNotSame(property._valueDeserializer, newProperty._valueDeserializer);
        assertNull(newProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property._fallbackSetter = new SettableBeanProperty(null, null, null, null, null, null);

        property.fixAccess(null);

        assertNotNull(property._fallbackSetter);
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        SettableBeanProperty setter = new SettableBeanProperty(null, null, null, null, null, null);

        property.setFallbackSetter(setter);

        assertEquals(setter, property._fallbackSetter);
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);

        property.markAsIgnorable();

        assertTrue(property._ignorable);
    }

    @Test
    public void testIsIgnorable() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property._ignorable = true;

        assertTrue(property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);

        try {
            property.findInjectableValue(null, null);
        } catch (Exception e) {
            assertEquals("Property 'null' (type null) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        
        try {
            property.inject(null, null);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetAnnotation() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        Annotation annotation = property.getAnnotation(null);

        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property._annotated = new AnnotatedMember() {
            public void fixAccess(DeserializationConfig config) {}
            public void fixAccess(boolean force) {}
            public Class<?> getDeclaringClass() { return null; }
            public <A extends Annotation> A getAnnotation(Class<A> acls) { return null; }
            public boolean hasAnnotation(Class<?> acls) { return false; }
            public boolean hasOneOf(Class<? extends Annotation>[] annoClasses) { return false; }
        };

        assertNotNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);

        assertEquals(0, property.getCreatorIndex());
    }
}