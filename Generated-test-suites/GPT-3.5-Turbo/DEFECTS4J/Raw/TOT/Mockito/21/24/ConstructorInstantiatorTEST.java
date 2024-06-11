import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class ConstructorInstantiatorTest {

    // Test case for noArgConstructor
    @Test
    public void testNoArgConstructor() {
        Class<TestClass> cls = TestClass.class;
        TestClass instance = ConstructorInstantiator.noArgConstructor(cls);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
    }

    // Test case for withOuterClass
    @Test
    public void testWithOuterClass() {
        Class<TestClass> cls = TestClass.class;
        TestClass outerInstance = mock(TestClass.class);
        when(outerInstance.getClass()).thenReturn(TestOuterClass.class);
        TestClass instance = ConstructorInstantiator.withOuterClass(cls);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
        verify(outerInstance).getClass();
    }

    // Test case for newInstance with outerClassInstance=null
    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Class<TestClass> cls = TestClass.class;
        TestClass instance = ConstructorInstantiator.newInstance(cls);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
    }

    // Test case for newInstance with outerClassInstance not null
    @Test
    public void testNewInstanceWithNonNullOuterClassInstance() {
        Class<TestClass> cls = TestClass.class;
        TestClass outerInstance = mock(TestClass.class);
        TestClass instance = ConstructorInstantiator.newInstance(cls);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
    }

    // Test case for newInstance with exception in withOuterClass
    @Test(expected = InstantationException.class)
    public void testNewInstanceWithExceptionInWithOuterClass() {
        Class<TestClass> cls = TestClass.class;
        TestClass outerInstance = mock(TestClass.class);
        when(outerInstance.getClass()).thenThrow(NoSuchMethodException.class);
        TestClass instance = ConstructorInstantiator.newInstance(cls);
    }

    // Test case for newInstance with exception in noArgConstructor
    @Test(expected = InstantationException.class)
    public void testNewInstanceWithExceptionInNoArgConstructor() {
        Class<TestClass> cls = TestClass.class;
        TestClass instance = ConstructorInstantiator.newInstance(cls);
    }

    // Test case for paramsException
    @Test(expected = InstantationException.class)
    public void testParamsException() {
        Class<TestClass> cls = TestClass.class;
        InstantationException exception = ConstructorInstantiator.paramsException(cls, new Exception());
        assertNotNull(exception);
        assertEquals("Unable to create mock instance of 'TestClass'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }
}