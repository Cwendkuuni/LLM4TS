package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConstructorInstantiatorTest {
    @Test
    public void testNewInstance_withNullOuterClassInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        StringObject result = instantiator.newInstance(StringObject.class);

        assertNotNull(result);
        assertNull(result.value);
    }

    @Test
    public void testNewInstance_withNullOuterClassInstance_withOuterClassConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        OuterClassObject result = instantiator.newInstance(OuterClassObject.class);

        assertNotNull(result);
        assertNull(result.value);
    }

    @Test
    public void testNewInstance_withNonNullOuterClassInstance_noArgConstructor() {
        OuterClassObject outer = new OuterClassObject(null);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outer);
        StringObject result = instantiator.newInstance(StringObject.class);

        assertNotNull(result);
        assertNotNull(result.value);
        assertEquals(outer, result.value.outer);
    }

    @Test
    public void testNewInstance_withNonNullOuterClassInstance_withOuterClassConstructor() {
        OuterClassObject outer = new OuterClassObject(null);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outer);
        OuterClassObject result = instantiator.newInstance(OuterClassObject.class);

        assertNotNull(result);
        assertNotNull(result.value);
        assertEquals(outer, result.value.outer);
    }
}