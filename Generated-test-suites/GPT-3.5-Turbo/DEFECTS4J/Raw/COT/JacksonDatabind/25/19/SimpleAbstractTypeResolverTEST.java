

package com.fasterxml.jackson.databind.module;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping_sameClass() {
        SimpleAbstractTypeResolver testResolver = new SimpleAbstractTypeResolver();
        try{
            testResolver.addMapping(String.class, String.class);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException e){
            String expectedMessage = "Can not add mapping from class to itself";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testAddMapping_notSubtype() {
        SimpleAbstractTypeResolver testResolver = new SimpleAbstractTypeResolver();
        try{
            testResolver.addMapping(AbstractList.class, ArrayList.class);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException e){
            String expectedMessage = "Can not add mapping from class "+AbstractList.class.getName()
                    +" to "+ArrayList.class.getName()+", as latter is not a subtype of former";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testAddMapping_notAbstract() {
        SimpleAbstractTypeResolver testResolver = new SimpleAbstractTypeResolver();
        try{
            testResolver.addMapping(List.class, ArrayList.class);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException e){
            String expectedMessage = "Can not add mapping from class "+List.class.getName()
                    +" since it is not abstract";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping_present() {
        SimpleAbstractTypeResolver testResolver = new SimpleAbstractTypeResolver();
        testResolver.addMapping(Collection.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig();
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(Collection.class, Object.class);
        JavaType expectedOutputType = TypeFactory.defaultInstance().constructCollectionType(LinkedList.class, Object.class);
        JavaType outputType = testResolver.findTypeMapping(config, inputType);
        assertEquals(expectedOutputType, outputType);
    }

    @Test
    public void testFindTypeMapping_notPresent() {
        SimpleAbstractTypeResolver testResolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(List.class, Object.class);
        JavaType outputType = testResolver.findTypeMapping(config, inputType);
        assertNull(outputType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver testResolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(List.class, Object.class);
        JavaType outputType = testResolver.resolveAbstractType(config, inputType);
        assertNull(outputType);
    }
}

