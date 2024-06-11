package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    private ConstructorInstantiator constructorInstantiator;
    
    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }
    
    @Test
    public void testNewInstance_noArgConstructor() {
        Class<TestClass> cls = TestClass.class;
        TestClass result = constructorInstantiator.newInstance(cls);
        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }
    
    @Test
    public void testNewInstance_withOuterClass() {
        OuterClass outerClassInstance = new OuterClass();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        
        Class<TestClass> cls = TestClass.class;
        TestClass result = constructorInstantiator.newInstance(cls);
        assertNotNull(result);
        assertEquals(cls, result.getClass());
        assertEquals(outerClassInstance, result.getOuterClassInstance());
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_wrongType() {
        constructorInstantiator = new ConstructorInstantiator("string");
        Class<TestClass> cls = TestClass.class;
        constructorInstantiator.newInstance(cls);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_noConstructor() {
        constructorInstantiator = new ConstructorInstantiator(null);
        Class<OuterClass> cls = OuterClass.class;
        constructorInstantiator.newInstance(cls);
    }
    
    private static class TestClass {
        private Object outerClassInstance;
        
        public TestClass() {
        }
        
        public TestClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
        
        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
    
    private static class OuterClass {
        
    }
}