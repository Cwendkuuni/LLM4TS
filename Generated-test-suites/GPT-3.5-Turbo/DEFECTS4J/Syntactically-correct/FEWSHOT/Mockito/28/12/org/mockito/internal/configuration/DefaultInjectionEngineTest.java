
package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private Field field1;
    private Field field2;
    private Field field3;
    private Field field4;
    private Object testClassInstance;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;

    @Before
    public void setUp() throws NoSuchFieldException {
        defaultInjectionEngine = new DefaultInjectionEngine();
        field1 = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        field2 = DefaultInjectionEngine.class.getDeclaredField("supertypesLast");
        field3 = DefaultInjectionEngine.class.getDeclaredField("injectMocksFields");
        field4 = DefaultInjectionEngine.class.getDeclaredField("mocks");

        testClassInstance = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        injectMocksFields.add(DefaultInjectionEngine.class.getDeclaredField("injectMocksFields"));
        mocks = new HashSet<>();
        mocks.add(new Object());
    }

    @Test
    public void testInjectMocksOnFields() {
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals("mockCandidateFilter", testClassInstance.getField(field1.getName()));
        Assert.assertEquals("supertypesLast", testClassInstance.getField(field2.getName()));
        Assert.assertEquals("injectMocksFields", testClassInstance.getField(field3.getName()));
        Assert.assertEquals("mocks", testClassInstance.getField(field4.getName()));
    }

    @Test
    public void testInjectMocksOnFieldsWithNoMocks() {
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, new HashSet<>(), testClassInstance);

        Assert.assertNull(testClassInstance.getField(field1.getName()));
        Assert.assertNull(testClassInstance.getField(field2.getName()));
        Assert.assertNull(testClassInstance.getField(field3.getName()));
        Assert.assertNull(testClassInstance.getField(field4.getName()));
    }

}
