package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass myClass = instantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    @Test
    public void testNoArgConstructorError() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(MyClassWithArgs.class);
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClassWithArgs'. Please ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClass() {
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
        MyClass myClass = instantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
        assertSame(outerClass, myClass.getOuterClassInstance());
    }

    @Test
    public void testWithOuterClassError() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("invalid outer class instance");
        try {
            instantiator.newInstance(MyClass.class);
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'. Please ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    public class MyClass {
        private OuterClass outerClassInstance;

        public MyClass() {
        }

        public void setOuterClassInstance(OuterClass outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public OuterClass getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    public class MyClassWithArgs {
        private String arg;

        public MyClassWithArgs(String arg) {
            this.arg = arg;
        }

        public String getArg() {
            return arg;
        }
    }

    public class OuterClass {
    }
}
