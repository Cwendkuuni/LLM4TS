
package org.mockito.internal.configuration;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        DefaultInjectionEngine engine = new DefaultInjectionEngine();

        // Setup
        Set<Field> injectMocksFields = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = new Object();

        // Test empty injectMocksFields and mocks set
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertEquals(0, injectMocksFields.size());
        assertEquals(0, mocks.size());

        // Test non-empty injectMocksFields and mocks set
        Field field1 = getField("field1");
        Field field2 = getField("field2");
        Field field3 = getField("field3");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertEquals(0, injectMocksFields.size());
        assertEquals(0, mocks.size());
        assertNotNull(field1.get(testClassInstance));
        assertNotNull(field2.get(testClassInstance));
        assertNotNull(field3.get(testClassInstance));
        assertSame(mock1, field1.get(testClassInstance));
        assertSame(mock2, field2.get(testClassInstance));

        // Test initialization exception
        Field field4 = getField("field4");
        injectMocksFields.add(field4);
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertTrue(field4.get(testClassInstance) instanceof Reporter);

        // Test fields in class hierarchy
        injectMocksFields.clear();
        mocks.clear();
        Field field5 = getField("field5");
        Field field6 = getField("field6");
        Field field7 = getField("field7");
        injectMocksFields.add(field5);
        injectMocksFields.add(field6);
        injectMocksFields.add(field7);
        Object mock3 = mock(Object.class);
        Object mock4 = mock(Object.class);
        Object mock5 = mock(Object.class);
        Object mock6 = mock(Object.class);
        mocks.add(mock3);
        mocks.add(mock4);
        mocks.add(mock5);
        mocks.add(mock6);
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertEquals(0, injectMocksFields.size());
        assertEquals(0, mocks.size());
        assertNotNull(field5.get(testClassInstance));
        assertNotNull(field6.get(testClassInstance));
        assertNotNull(field7.get(testClassInstance));
        assertSame(mock3, field5.get(testClassInstance));
        assertSame(mock4, field6.get(testClassInstance));
        assertSame(mock5, field7.get(testClassInstance));
    }

    private Field getField(String name) {
        try {
            return DefaultInjectionEngine.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail("Failed to get field: " + name);
        }
        return null;
    }
}
