import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.ConstructorInstantiator.InstantiationException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ConstructorInstantiatorTest {

    ConstructorInstantiator constructorInstantiator;

    @Before
    public void setup() {
        constructorInstantiator = new ConstructorInstantiator(new Object());
    }

    @Test
    public void testConstructorInstantiatorWithValidArguments() {
        assertNotNull(constructorInstantiator);
    }

    @Test
    public void testNewInstanceWithValidClass() throws InstantiationException {
        Class<String> stringClass = String.class;
        String instance = constructorInstantiator.newInstance(stringClass);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass_NullOuterClassInstance() {
        assertNull(constructorInstantiator.newInstance(String.class));
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ClassDoesNotHaveConstructorWithSingleArg() throws InstantiationException {
        constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<Object[]> arrayClass = Object[].class;
        constructorInstantiator.withOuterClass(arrayClass);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ConstructorThrowsException() throws InstantiationException {
        constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<String> stringClass = String.class;
        try {
            constructorInstantiator.withOuterClass(stringClass);
        } catch (InstantiationException e) {
            throw new InstantiationException("Unexpected error", e);
        }
        fail("Expected InstantiationException");
    }

    @Test
    public void testParamsException_NoSuchConstructor() {
        Class<Object[]> arrayClass = Object[].class;
        InstantiationException exception = ConstructorInstantiator.paramsException(arrayClass, new NoSuchMethodException(""));
        assertNotNull(exception);
    }

    @Test
    public void testParamsException_ConstructorThrowsException() {
        Class<Object[]> arrayClass = Object[].class;
        Exception constructorException = new RuntimeException("Constructor exception");
        try {
            new Object[1][0].getClass().getDeclaredConstructor(Object.class);
        } catch (Exception e) {
            InstantiationException exception = ConstructorInstantiator.paramsException(arrayClass, constructorException);
            assertNotNull(exception);
        }
    }

    @Test
    public void testNewInstance_NoArgConstructorDoesNotExist() throws InstantiationException {
        Class<Object> objectClass = Object.class;
        constructorInstantiator = new ConstructorInstantiator(new Object());
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(objectClass));
    }

    @Test
    public void testNewInstance_NoArgConstructorThrowsException() throws InstantiationException {
        Class<Object> objectClass = Object.class;
        try {
            constructorInstantiator = new ConstructorInstantiator(new Object());
            constructorInstantiator.newInstance(objectClass);
        } catch (InstantiationException e) {
            throw new InstantiationException("Unexpected error", e);
        }
        fail("Expected InstantiationException");
    }

}