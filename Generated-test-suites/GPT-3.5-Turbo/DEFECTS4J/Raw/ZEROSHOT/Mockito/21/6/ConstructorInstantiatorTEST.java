import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<TestObject> cls = TestObject.class;
        TestObject result = constructorInstantiator.newInstance(cls);
        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }

    @Test
    public void testNewInstance_withNonNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new OuterClass());
        Class<TestObject> cls = TestObject.class;
        TestObject result = constructorInstantiator.newInstance(cls);
        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<TestObjectWithParams> cls = TestObjectWithParams.class;
        constructorInstantiator.newInstance(cls);
    }

    public static class TestObject {

        public TestObject() {}

    }

    public static class TestObjectWithParams {

        public TestObjectWithParams(String param) {}

    }

    public class OuterClass {

    }
}