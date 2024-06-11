import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.java.TypeUtils;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.throwException;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_WhenClassHasNoArgConstructor_ThenInstanceIsCreated() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<DummyClass1> dummyClass1 = DummyClass1.class;
        DummyClass1 instance = constructorInstantiator.newInstance(dummyClass1);
        assertInstanceOf(DummyClass1.class, instance);
    }

    @Test
    public void testNewInstance_WhenClassHasOuterInstanceConstructor_ThenInstanceIsCreated() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass2> dummyClass2 = DummyClass2.class;
        DummyClass2 instance = constructorInstantiator.newInstance(dummyClass2);
        assertInstanceOf(DummyClass2.class, instance);
        assertInstanceOf(outerInstance.getClass(), instance.getOuterClass());
    }

    @Test
    public void testNewInstance_WhenClassHasNoArgConstructorButThrowsException_ThenInstantiationExceptionIsThrown() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<DummyClass3> dummyClass3 = DummyClass3.class;
        throwException(InstantiationException.class, () -> constructorInstantiator.newInstance(dummyClass3));
    }

    @Test
    public void testNewInstance_WhenClassHasOuterInstanceConstructorButThrowsException_ThenInstantiationExceptionIsThrown() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass4> dummyClass4 = DummyClass4.class;
        throwException(InstantiationException.class, () -> constructorInstantiator.newInstance(dummyClass4));
    }

    @Test
    public void testWithOuterClass_WhenClassHasNoArgConstructor_ThenInstanceIsCreated() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass1> dummyClass1 = DummyClass1.class;
        DummyClass1 instance = constructorInstantiator.withOuterClass(dummyClass1);
        assertInstanceOf(DummyClass1.class, instance);
        assertInstanceOf(outerInstance.getClass(), instance.getOuterClass());
    }

    @Test
    public void testWithOuterClass_WhenClassHasOuterInstanceConstructor_ThenInstanceIsCreated() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass2> dummyClass2 = DummyClass2.class;
        DummyClass2 instance = constructorInstantiator.withOuterClass(dummyClass2);
        assertInstanceOf(DummyClass2.class, instance);
        assertInstanceOf(outerInstance.getClass(), instance.getOuterClass());
    }

    @Test
    public void testWithOuterClass_WhenClassHasNoArgConstructorButThrowsException_ThenInstantiationExceptionIsThrown() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass3> dummyClass3 = DummyClass3.class;
        throwException(InstantiationException.class, () -> constructorInstantiator.withOuterClass(dummyClass3));
    }

    @Test
    public void testWithOuterClass_WhenClassHasOuterInstanceConstructorButThrowsException_ThenInstantiationExceptionIsThrown() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass4> dummyClass4 = DummyClass4.class;
        throwException(InstantiationException.class, () -> constructorInstantiator.withOuterClass(dummyClass4));
    }

    @Test
    public void testGetDeclaredConstructor_WhenConstructorExists_ThenConstructorIsReturned() {
        Class<DummyClass5> dummyClass5 = DummyClass5.class;
        Constructor<DummyClass5> constructor = TypeUtils.getDeclaredConstructor(dummyClass5, Object.class);
        assertNotNull(constructor);
    }

    @Test
    public void testGetDeclaredConstructor_WhenConstructorDoesNotExist_ThenNullIsReturned() {
        Class<DummyClass6> dummyClass6 = DummyClass6.class;
        Constructor<DummyClass6> constructor = TypeUtils.getDeclaredConstructor(dummyClass6, Object.class);
        assertNull(constructor);
    }

    @Test
    public void testNewInstance_WhenClassHasDeclaredConstructorWithParamsButNoArgConstructorThrowsException_ThenInstantiationExceptionIsThrown() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass7> dummyClass7 = DummyClass7.class;
        throwException(InstantiationException.class, () -> constructorInstantiator.newInstance(dummyClass7));
    }

    @Test
    public void testNewInstance_WhenClassHasDeclaredConstructorWithParamsAndOuterInstanceConstructorExists_ThenInstanceIsCreated() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass8> dummyClass8 = DummyClass8.class;
        DummyClass8 instance = constructorInstantiator.newInstance(dummyClass8, "test");
        assertInstanceOf(DummyClass8.class, instance);
        assertInstanceOf(outerInstance.getClass(), instance.getOuterClass());
    }

    @Test
    public void testNewInstance_WhenClassHasDeclaredConstructorWithParamsButNoArgConstructorThrowsExceptionAndOuterConstructorAlsoThrowsException_ThenInstantiationExceptionIsThrown() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass9> dummyClass9 = DummyClass9.class;
        throwException(InstantiationException.class, () -> constructorInstantiator.newInstance(dummyClass9, "test"));
    }

    @Test
    public void testNewInstance_WhenClassHasDeclaredConstructorWithParamsAndOuterInstanceConstructorExists_ThenInstanceIsCreated() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Class<DummyClass10> dummyClass10 = DummyClass10.class;
        DummyClass10 instance = constructorInstantiator.newInstance(dummyClass10, "test1", "test2");
        assertInstanceOf(DummyClass10.class, instance);
        assertInstanceOf(outerInstance.getClass(), instance.getOuterClass());
    }

    private static class DummyClass1 {}

    private static class DummyClass2 {
        private final Object outerClass;

        public DummyClass2(Object outerClass) {
            this.outerClass = outerClass;
        }

        public Object getOuterClass() {
            return outerClass;
        }
    }

    private static class DummyClass3 {
        public DummyClass3() {}

        private static class InnerClass {
            public InnerClass() {}
        }
    }

    private static class DummyClass4 {
        private final Object outerClass;

        public DummyClass4(Object outerClass) {
            this.outerClass = outerClass;
        }

        public Object getOuterClass() {
            return outerClass;
        }

        private void innerMethod() {}
    }

    private static class DummyClass5 {
        private DummyClass5() {}
    }

    private static class DummyClass6 {
        private DummyClass6() {}

        public static void staticMethod() {}
    }

    private static class DummyClass7 {
        private DummyClass7() throws InstantiationException {
            throw new InstantiationException("Test exception");
        }
    }

    private static class DummyClass8 {
        private final Object outerClass;

        public DummyClass8(Object outerClass, String param) {
            this.outerClass = outerClass;
        }

        public void doSomething() {}

        public Object getOuterClass() {
            return outerClass;
        }
    }

    private static class DummyClass9 {
        private DummyClass9(String param) throws InstantiationException {
            throw new InstantiationException("Test exception");
        }

        public void doSomething() {}
    }

    private static class DummyClass10 {
        private final Object outerClass;

        public DummyClass10(Object outerClass, String param1, String param2) {
            this.outerClass = outerClass;
        }

        public void doSomething() {}

        public Object getOuterClass() {
            return outerClass;
        }
    }
}