package org.mockito.internal.creation.instance;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        ConstructorInstantiator ci = new ConstructorInstantiator(null);
        assertNotNull(ci);
    }

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator ci = new ConstructorInstantiator(null);
        Object obj = ci.newInstance(Object.class);
        assertNotNull(obj);
    }

    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator ci = new ConstructorInstantiator(new OuterClass());
        InnerClass innerObj = ci.newInstance(InnerClass.class);
        assertNotNull(innerObj);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_invalidOuterClassInstance() {
        ConstructorInstantiator ci = new ConstructorInstantiator("invalidOuterClassInstance");
        ci.newInstance(InnerClass.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstance_invalidNoArgConstructor() {
        ConstructorInstantiator ci = new ConstructorInstantiator(null);
        ci.newInstance(NoArgConstructorClass.class);
    }
    
    private static class OuterClass {
    }
    
    private static class InnerClass {
        public InnerClass(OuterClass outerClass) {
        }
    }
    
    private static class NoArgConstructorClass {
        public NoArgConstructorClass(int param) {
        }
    }
}
