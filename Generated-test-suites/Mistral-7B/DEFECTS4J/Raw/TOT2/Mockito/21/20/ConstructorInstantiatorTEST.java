package org.mockito.internal.creation.instance.test;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.mockito.exceptions.*;

@RunWith(JUnit4.class)
public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private Class<TestClass> cls;
    private Constructor<TestClass> constructor;

    @Before
    public void setup() {
        outerClassInstance = Mockito.mock(Object.class);
        cls = Mockito.mock(Class.class);
        constructor = Mockito.mock(Constructor.class);
        when(cls.getDeclaredConstructor(outerClassInstance.getClass())).thenReturn(constructor);
    }

    @Test
    public void testNewInstanceWithArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        TestClass result = constructorInstantiator.newInstance(cls);

        verify(constructor).newInstance(outerClassInstance);
        assertNotNull(result);
    }

    @Test(expected = InstantiationException.class)
    public void testParamsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        constructorInstantiator.newInstance(cls);
    }

    @Test
    public void testParamsExceptionMessage() {
        InstantiationException exception = Mockito.mock(InstantiationException.class);

        String actualMessage = ConstructorInstantiator.paramsException(cls, exception).getMessage();

        String expectedMessage = "Unable to create mock instance of 'TestClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testWithOuterClass() {
        TestClass result = ConstructorInstantiator.withOuterClass(cls, outerClassInstance);

        verify(constructor).newInstance(outerClassInstance);
        assertNotNull(result);
    }

    @Test
    public void testNoArgConstructor() {
        TestClass result = ConstructorInstantiator.noArgConstructor(cls);

        verifyNoInteractions(cls);
        assertNotNull(result);
    }
}