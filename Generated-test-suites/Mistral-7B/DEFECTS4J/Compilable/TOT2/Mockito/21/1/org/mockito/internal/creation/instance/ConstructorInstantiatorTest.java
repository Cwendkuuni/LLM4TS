package org.mockito.internal.creation.instance;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Integer> intClass = Integer.class;
        Integer instance = constructorInstantiator.newInstance(intClass);
        assertNotNull(instance);
    }
}