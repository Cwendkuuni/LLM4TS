package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.collections.ListUtil.newArrayList;
import static org.mockito.internal.util.reflection.ReflectionUtils.newInstance;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestUtils;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection() {
        Class<?> testClass = TestUtils.getTestClass(TestClassWithInjectMocks.class);
        Field injectMocksField = TestClassWithInjectMocks.class.getDeclaredField("injectMocksField");

        // Test with empty mocks
        Set<Object> mockCandidates = new HashSet<>();
        assertFalse(injectionStrategy.processInjection(injectMocksField, newObject(), mockCandidates));

        // Test with no mock candidates
        mockCandidates.add(newObjectWithMockitoException());
        assertFalse(injectionStrategy.processInjection(injectMocksField, newObject(), mockCandidates));

        // Test with one mock candidate
        mockCandidates.clear();
        mockCandidates.add(newObject());
        assertTrue(injectionStrategy.processInjection(injectMocksField, newObject(), mockCandidates));

        // Test with multiple mock candidates
        mockCandidates.clear();
        mockCandidates.add(newObject());
        mockCandidates.add(newObjectWithSameType());
        assertTrue(injectionStrategy.processInjection(injectMocksField, newObject(), mockCandidates));

        // Test with multiple mock candidates and multiple inheritance
        mockCandidates.clear();
        mockCandidates.add(newObjectWithMockitoExceptionInBaseClass());
        mockCandidates.add(newObjectWithSameTypeInBaseClass());
        assertTrue(injectionStrategy.processInjection(injectMocksField, newObjectWithBaseClass(), mockCandidates));

        // Test with multiple mock candidates and same name
        mockCandidates.clear();
        mockCandidates.add(newObjectWithSameName());
        mockCandidates.add(newObjectWithSameNameDifferentType());
        assertTrue(injectionStrategy.processInjection(injectMocksField, newObject(), mockCandidates));
    }

    private Object newObject() {
        return newObjectWithMocks(newObject(), new Object[]{});
    }

    private Object newObjectWithMockitoException() {
        return newObjectWithMocks(new MockitoException(""), new Object[]{});
    }

    private Object newObjectWithSameType() {
        return newObjectWithMocks(new ObjectType(), new Object[]{});
    }

    private Object newObjectWithSameTypeInBaseClass() {
        return newObjectWithMocks(new ObjectTypeInBaseClass(), new Object[]{});
    }

    private Object newObjectWithBaseClass() {
        return newObjectWithMocks(new BaseClass(), new Object[]{});
    }

    private Object newObjectWithSameName() {
        return newObjectWithMocks(new ObjectWithSameName(), new Object[]{});
    }

    private Object newObjectWithSameNameDifferentType() {
        return newObjectWithMocks(new ObjectWithSameNameDifferentType(), new Object[]{});
    }

    private Object newObjectWithMocks(Object obj, Object[] mocks) {
        Field field = ReflectionUtils.getField(TestClassWithInjectMocks.class, "injectMocksField");
        FieldInitializationReport report = new FieldInitializer(obj, field).initialize();
        for (Object mock : mocks) {
            ReflectionUtils.setFieldValue(obj, field, mock);
        }
        return obj;
    }

    private static class ObjectType {
    }

    private static class ObjectTypeInBaseClass extends ObjectType {
    }

    private static class BaseClass {
    }

    private static class ObjectWithSameName extends ObjectType {
    }

    private static class ObjectWithSameNameDifferentType extends ObjectType {
    }

    private static class TestClassWithInjectMocks {
        @InjectMocks
        private static Field injectMocksField;
    }
}