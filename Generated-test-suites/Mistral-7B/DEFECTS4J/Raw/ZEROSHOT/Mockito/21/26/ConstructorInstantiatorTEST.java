package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClass() {
        // Test case for newInstance method when outerClassInstance is null
        Class<Integer> intClass = Integer.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(intClass));
        assertInstanceOf(NoArgsConstructorException.class, exception.getCause());
    }

    @Test
    public void testNewInstanceWithNullClass() {
        // Test case for newInstance method when Class parameter is null
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(null));
        assertInstanceOf(NullPointerException.class, exception.getCause());
    }

    @Test
    public void testNewInstanceWithClassWithoutNoArgConstructor() {
        // Test case for newInstance method when Class does not have a no-arg constructor
        Class<Person> personClass = new Class<Person>() {
            public Person() {
                this("John Doe");
            }

            public Person(String name) {
                this.name = name;
            }

            private String name;
        };

        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(personClass));
        assertInstanceOf(InstantiationException.class, exception);
        String expectedMessage = "Unable to create mock instance of 'personClass'.\nPlease ensure it has parameter-less constructor.";
        assertInstanceOf(NoArgsConstructorException.class, exception.getCause());
        assertInstanceOf(NoSuchMethodException.class, exception.getCause().getCause());
        assertInstanceOf(InvocationTargetException.class, exception.getCause().getCause().getCause());
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testNewInstanceWithClassWithNoArgConstructor() {
        // Test case for newInstance method when Class has a no-arg constructor
        Class<Integer> intClass = Integer.class;
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Integer actualResult = constructorInstantiator.newInstance(intClass);
        assertNotNull(actualResult);
        assertInstanceOf(Integer.class, actualResult);
    }

    @Test
    public void testNewInstanceWithClassWithOuterClassConstructor() {
        // Test case for withOuterClass method when Class has a constructor with the outer class as parameter
        class OuterClass {
            private final InnerClass innerClass;

            public OuterClass() {
                this.innerClass = new InnerClass();
            }
        }

        class InnerClass {
            public InnerClass() {
            }
        }

        Object outerClassInstance = mock(OuterClass.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<InnerClass> innerClass = InnerClass.class;
        InnerClass actualResult = constructorInstantiator.newInstance(innerClass);
        assertNotNull(actualResult);
        assertInstanceOf(InnerClass.class, actualResult);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassWithOuterClassConstructor_ExceptionThrown() {
        // Test case for withOuterClass method when Class has a constructor with the outer class as parameter and the constructor throws an exception
        class OuterClass {
            public OuterClass() {
            }

            public OuterClass(String arg) {
                throw new RuntimeException("Test Exception");
            }
        }

        class InnerClass {
            public InnerClass() {
            }
        }

        Object outerClassInstance = mock(OuterClass.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<InnerClass> innerClass = InnerClass.class;
        constructorInstantiator.newInstance(innerClass);
    }

    @Test
    public void testParamsException() {
        // Test case for paramsException method
        Class<Person> personClass = new Class<Person>() {
            public Person() {
                this("John Doe");
            }

            public Person(String name) {
                this.name = name;
            }

            private String name;
        };

        Object outerClassInstance = mock(Object.class);
        InstantiationException expectedException = new InstantiationException("Unable to create mock instance of 'personClass'.\nPlease ensure it has parameter-less constructor.", new Throwable("Test Error"));
        InstantiationException actualException = ConstructorInstantiator.paramsException(personClass, new Throwable("Test Error"));
        assertInstanceOf(InstantiationException.class, actualException);
        assertInstanceOf(expectedException.getCause(), actualException.getCause());
    }
}