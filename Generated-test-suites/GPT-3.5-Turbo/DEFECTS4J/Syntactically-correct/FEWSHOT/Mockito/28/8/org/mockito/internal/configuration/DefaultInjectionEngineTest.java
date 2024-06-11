
package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.Reporter;
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

public class DefaultInjectionEngineTest {

    // Test case to cover the injectMocksOnFields method
    @Test
    public void testInjectMocksOnFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Create the injectMocksFields set
        Set<Field> injectMocksFields = new HashSet<>();
        Field testField = new Field(getClass(), "testField");
        injectMocksFields.add(testField);

        // Create the mocks set
        Set<Object> mocks = new HashSet<>();
        Object mockObj1 = new Object();
        Object mockObj2 = new Object();
        mocks.add(mockObj1);
        mocks.add(mockObj2);

        // Create the testClassInstance
        Object testClassInstance = new Object();

        // Call the injectMocksOnFields method
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify the mocks were injected into the test field
        Assert.assertEquals(mockObj1, testField.get(testClassInstance));
        Assert.assertEquals(mockObj2, testField.get(testClassInstance));
    }

    // Test case to cover the injectMockCandidate method
    @Test
    public void testInjectMockCandidate() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Set<Object> mocks = new HashSet<>();
        Object mockObj = new Object();
        mocks.add(mockObj);

        // Create the awaitingInjectionClazz
        Class<?> awaitingInjectionClazz = getClass();

        // Create the fieldInstance
        Object fieldInstance = new Object();

        // Call the injectMockCandidate method
        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Verify the mock was injected into the field instance
        Assert.assertEquals(mockObj, fieldInstance.getField());
    }

    // Test case to cover the orderedInstanceFieldsFrom method
    @Test
    public void testOrderedInstanceFieldsFrom() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Create the awaitingInjectionClazz
        Class<?> awaitingInjectionClazz = getClass();

        // Call the orderedInstanceFieldsFrom method
        Field[] fields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verify the fields are sorted correctly (with supertypes last)
        Assert.assertEquals("fieldName1", fields[0].getName());
        Assert.assertEquals("fieldName2", fields[1].getName());
        Assert.assertEquals("fieldName3", fields[2].getName());
    }

}
