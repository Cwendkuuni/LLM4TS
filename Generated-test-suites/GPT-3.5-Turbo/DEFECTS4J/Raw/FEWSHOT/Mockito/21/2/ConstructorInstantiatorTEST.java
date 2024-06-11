package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testConstructorInstantiatorWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        MyClass myClass = instantiator.newInstance(MyClass.class);
        
        assertNotNull(myClass);
        assertEquals(outerClassInstance, myClass.getOuterClassInstance());
    }
    
    @Test
    public void testConstructorInstantiatorWithoutOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        MyClass myClass = instantiator.newInstance(MyClass.class);
        
        assertNotNull(myClass);
        // Assert that the outerClassInstance is null in MyClass
        assertNull(myClass.getOuterClassInstance());
    }
    
    @Test
    public void testConstructorInstantiatorWithCorrectType() {
        Object outerClassInstance = new MyOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        MyOuterClass myClass = instantiator.newInstance(MyOuterClass.class);
        
        assertNotNull(myClass);
        assertEquals(outerClassInstance, myClass.getOuterClassInstance());
    }
    
    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithIncorrectType() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        
        instantiator.newInstance(MyOuterClass.class);
    }

}

class MyClass {
    private Object outerClassInstance;

    public Object getOuterClassInstance() {
        return outerClassInstance;
    }

    public MyClass(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }
}

class MyOuterClass {
    private Object outerClassInstance;

    public Object getOuterClassInstance() {
        return outerClassInstance;
    }

    public MyOuterClass(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }
}