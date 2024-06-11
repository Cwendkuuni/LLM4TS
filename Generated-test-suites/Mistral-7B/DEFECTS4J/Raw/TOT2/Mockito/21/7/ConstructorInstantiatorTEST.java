import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.support.annotation.MockStatic;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@MockStatic(Constructor.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        Object result = instantiator.newInstance(String.class);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testNewInstanceWithClassHavingConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        Object outerClassInstance = mock(Object.class);
        Object result = instantiator.newInstance(MyClassWithConstructor.class);
        assertEquals(MyClassWithConstructor.class, result.getClass());
    }

    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        MyClassWithConstructor outerClassInstance = mock(MyClassWithConstructor.class);
        MyClassWithConstructor result = instantiator.withOuterClass(MyClassWithConstructor.class);
        assertEquals(outerClassInstance, result);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(MyClassWithConstructor.class);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        instantiator.newInstance(null);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNonInstantiableClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        try {
            instantiator.newInstance(NonInstantiableClass.class);
        } catch (Exception e) {
            throw new InstantiationException("Unexpected exception", e);
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithUnsupportedOuterClassType() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        try {
            instantiator.newInstance(UnsupportedOuterClass.class);
        } catch (Exception e) {
            throw new InstantiationException("Unexpected exception", e);
        }
    }

    @Test
    public void testParamsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        try {
            instantiator.newInstance(IllegalArgumentExceptionClass.class);
        } catch (Exception e) {
            ConstructorInstantiator.paramsException(IllegalArgumentExceptionClass.class, e);
        }
    }

}