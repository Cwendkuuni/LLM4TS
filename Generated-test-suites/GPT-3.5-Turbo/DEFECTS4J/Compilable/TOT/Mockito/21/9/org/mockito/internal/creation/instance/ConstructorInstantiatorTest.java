package org.mockito.internal.creation.instance;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

import static org.junit.Assert.assertEquals;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testNewInstance_withNullOuterClassInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        String testString = "test";
        
        String result = instantiator.newInstance(String.class);
        
        assertEquals(testString, result);
    }
    
    @Test
    public void testNewInstance_withNullOuterClassInstance_withOuterClassConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        OuterClass outer = new OuterClass();
        
        OuterClass result = instantiator.newInstance(OuterClass.class);
        
        assertEquals(outer, result);
    }
    
    @Test
    public void testNewInstance_withNonNullOuterClassInstance_noArgConstructor() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        String testString = "test";
        
        String result = instantiator.newInstance(String.class);
        
        assertEquals(testString, result);
    }
    
    @Test
    public void testNewInstance_withNonNullOuterClassInstance_withOuterClassConstructor() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        OuterClass result = instantiator.newInstance(OuterClass.class);
        
        assertEquals(outerClassInstance, result);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withInvalidOuterClassInstance_withOuterClassConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("invalidInstance");
        
        instantiator.newInstance(OuterClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withNoArgConstructorAndInvalidClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        instantiator.newInstance(InvalidClass.class);
    }
    
    private class OuterClass {}
    
    private class InvalidClass {}
    
}