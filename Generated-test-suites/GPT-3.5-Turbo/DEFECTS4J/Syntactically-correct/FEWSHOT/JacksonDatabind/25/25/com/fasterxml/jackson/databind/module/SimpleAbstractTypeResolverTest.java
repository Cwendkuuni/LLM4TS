package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractType1.class, ConcreteType1.class);
        resolver.addMapping(AbstractType2.class, ConcreteType2.class);
        resolver.addMapping(AbstractType3.class, ConcreteType3.class);
        
        assertNull(resolver.addMapping(AbstractType1.class, AbstractType1.class)); // check that mapping from class to itself is not added
        assertNull(resolver.addMapping(ConcreteType1.class, AbstractType1.class)); // check that mapping from subType to superType is not added
        assertNull(resolver.addMapping(ConcreteType1.class, ConcreteType1.class)); // check that mapping from class to itself is not added
        assertNull(resolver.addMapping(AbstractType1.class, ConcreteType2.class)); // check that mapping from incompatible types is not added
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractType1.class, ConcreteType1.class);
        resolver.addMapping(AbstractType2.class, ConcreteType2.class);
        resolver.addMapping(AbstractType3.class, ConcreteType3.class);
        
        JavaType type1 = resolver.findTypeMapping(deserializationConfig, new JavaType(Type1.class)); // replace deserializationConfig with the actual DeserializationConfig instance
        assertEquals(new JavaType(ConcreteType1.class), type1);
        
        JavaType type2 = resolver.findTypeMapping(deserializationConfig, new JavaType(Type2.class)); // replace deserializationConfig with the actual DeserializationConfig instance
        assertEquals(new JavaType(ConcreteType2.class), type2);
        
        JavaType type3 = resolver.findTypeMapping(deserializationConfig, new JavaType(Type3.class)); // replace deserializationConfig with the actual DeserializationConfig instance
        assertEquals(new JavaType(ConcreteType3.class), type3);
        
        JavaType type4 = resolver.findTypeMapping(deserializationConfig, new JavaType(Type4.class)); // replace deserializationConfig with the actual DeserializationConfig instance
        assertNull(type4);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type = resolver.resolveAbstractType(deserializationConfig, new JavaType(AbstractType1.class)); // replace deserializationConfig with the actual DeserializationConfig instance
        assertNull(type);
    }
    
    // Concrete class hierarchies for testing
    static abstract class AbstractType1 {}
    static class ConcreteType1 extends AbstractType1 {}
    
    static abstract class AbstractType2 {}
    static class ConcreteType2 extends AbstractType2 {}
    
    static abstract class AbstractType3 {}
    static class ConcreteType3 extends AbstractType3 {}
    
    static class Type1 extends AbstractType1 {}
    static class Type2 extends AbstractType2 {}
    static class Type3 extends AbstractType3 {}
    static class Type4 {}
}