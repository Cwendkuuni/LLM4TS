package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    private Object outerClassInstance;
    private ConstructorInstantiator constructorInstantiator;
    
    @Before
    public void setup() {
        outerClassInstance = new Object(); // Creating a mock or a real instance for testing
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }
    
    @Test
    public void testNewInstanceWithOuterClass() {
        Class<TestClass> cls = TestClass.class;
        TestClass instance = constructorInstantiator.newInstance(cls);
        
        assertNotNull(instance);
        assertEquals(outerClassInstance, instance.getOuterClassInstance());
    }
    
    @Test
    public void testNewInstanceWithOuterClass_invalidOuterClassInstance() {
        Class<TestClass> cls = TestClass.class;
        outerClassInstance = new String(); // Using a wrong type of outer class instance
        
        try {
            constructorInstantiator.newInstance(cls);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'TestClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testNewInstanceWithOuterClass_missingParameterlessConstructor() {
        Class<TestClass> cls = TestClass.class;
        
        try {
            constructorInstantiator.newInstance(cls);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'TestClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testNewInstanceWithNoArgConstructor() {
        Class<TestClassNoArgConstructor> cls = TestClassNoArgConstructor.class;
        TestClassNoArgConstructor instance = constructorInstantiator.newInstance(cls);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testNewInstanceWithNoArgConstructor_classWithNoParamConstructor() {
        Class<TestClassNoParamConstructor> cls = TestClassNoParamConstructor.class;
        TestClassNoParamConstructor instance = constructorInstantiator.newInstance(cls);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testNewInstanceWithNoArgConstructor_missingParameterlessConstructor() {
        Class<TestClassWithParamConstructor> cls = TestClassWithParamConstructor.class;
        
        try {
            constructorInstantiator.newInstance(cls);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'TestClassWithParamConstructor'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    private static class TestClass {
        private Object outerClassInstance;
        
        public TestClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class TestClassNoArgConstructor {
        public TestClassNoArgConstructor() {
            
        }
    }
    
    private static class TestClassNoParamConstructor {
        
    }
    
    private static class TestClassWithParamConstructor {
        public TestClassWithParamConstructor(Object param) {
            
        }
    }
}