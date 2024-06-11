package org.mockito.internal.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine engine;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() {
        engine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<Field>();
        mocks = new HashSet<Object>();
        testClassInstance = new Object();
    }

    @Test
    public void testInjectMocksOnFields() {
        Field field1 = null;
        Field field2 = null;

        try {
            field1 = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
            field2 = Util.class.getDeclaredField("stripLeadingAndTrailingQuotes");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        injectMocksFields.add(field1);
        injectMocksFields.add(field2);

        Object mock1 = mock(String[].class);
        Object mock2 = mock(String.class);

        mocks.add(mock1);
        mocks.add(mock2);

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertEquals(mock1, Util.EMPTY_STRING_ARRAY);
        assertEquals(mock2, Util.stripLeadingAndTrailingQuotes);
    }

    @Test
    public void testInjectMocksOnFields_NullFields() {
        injectMocksFields = null;

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_NullMocks() {
        mocks = null;

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_NullTestClassInstance() {
        testClassInstance = null;

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }
}