package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }
    
    @Test
    public void testWithOuterClass() {
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClass);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
        assertEquals(outerClass, myClass.getOuterClassInstance());
    }
    
    @Test(expected = InstantationException.class)
    public void testWithInvalidOuterClass() {
        InvalidOuterClass invalidOuterClass = new InvalidOuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(invalidOuterClass);
        constructorInstantiator.newInstance(MyClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testWithClassWithoutNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(ClassWithoutNoArgConstructor.class);
    }
    
    public class MyClass {
        private Object outerClassInstance;
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    public class OuterClass {}
    
    public class InvalidOuterClass {}
    
    public class ClassWithoutNoArgConstructor {
        public ClassWithoutNoArgConstructor(Object arg) {}
    }
}