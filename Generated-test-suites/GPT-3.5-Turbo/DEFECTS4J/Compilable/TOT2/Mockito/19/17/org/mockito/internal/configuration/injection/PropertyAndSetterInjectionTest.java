package org.mockito.internal.configuration.injection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection() {
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredFields()[0];
        Set<Object> mockCandidates = new HashSet<>();
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, propertyAndSetterInjection, mockCandidates);

        assertTrue(result);
    }

}