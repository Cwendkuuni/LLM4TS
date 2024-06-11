package org.mockito.internal.configuration.DefaultInjectionEngine;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidate;
import org.mockito.internal.util.reflection.FieldUtils;
import org.powermock.reflect.whitebox.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() throws IllegalAccessException, InstantiationException {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<SomeClass> testClass = SomeClass.class;
        Set<Field> injectMocksFields = new HashSet<Field>(Arrays.asList(testClass.getDeclaredFields()));
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = testClass.newInstance();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object fieldInstance = FieldUtils.getFieldValue(testClassInstance, field);
            Assert.assertNotNull(fieldInstance);
        }
    }

    @Test(expected = MockitoException.class)
    public void testFailWhenCanNotInitializeFieldAnnotatedWithInjectMocks() throws InstantiationException, IllegalAccessException {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<SomeClass> testClass = SomeClass.class;
        Set<Field> injectMocksFields = new HashSet<Field>(Arrays.asList(testClass.getDeclaredFields()));
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = testClass.newInstance();

        Field injectMocksFieldWithException = new FieldUtils().getFieldByName(testClass, "injectMocksFieldWithException", true);
        injectMocksFields.add(injectMocksFieldWithException);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testEmptySetOfMocks() throws InstantiationException, IllegalAccessException {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<SomeClass> testClass = SomeClass.class;
        Set<Field> injectMocksFields = new HashSet<Field>(Arrays.asList(testClass.getDeclaredFields()));
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = testClass.newInstance();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidateByType() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = SomeClassWithMockableFields.class;
        Set<Object> mocks = new HashSet<Object>();
        Object fieldInstance = new SomeMockImplementation();

        MockCandidate mockCandidate = injectionEngine.new MockCandidate(SomeMockImplementation.class, null, fieldInstance);

        Assert.assertTrue(mockCandidate.isMockable());
        Assert.assertFalse(mockCandidate.shouldBeIgnored());

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        MockCandidate filteredMockCandidate = mockCandidateFilter.filterCandidate(mocks, new MockCandidate("fieldName", SomeMockImplementation.class), fieldInstance);

        Assert.assertNotNull(filteredMockCandidate);
        Assert.assertEquals(SomeMockImplementation.class, filteredMockCandidate.getType());
        Assert.assertEquals("fieldName", filteredMockCandidate.getName());
    }

    @Test
    public void testInjectMockCandidateByName() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = SomeClassWithMockableFields.class;
        Set<Object> mocks = new HashSet<Object>();
        Object fieldInstance = new SomeMockImplementation();

        MockCandidate mockCandidate = new MockCandidate("fieldName", SomeMockImplementation.class, null, fieldInstance);

        Assert.assertTrue(mockCandidate.isMockable());
        Assert.assertFalse(mockCandidate.shouldBeIgnored());

        MockCandidate filteredMockCandidate = mockCandidateFilter.filterCandidate(mocks, mockCandidate, fieldInstance);

        Assert.assertNotNull(filteredMockCandidate);
        Assert.assertEquals(SomeMockImplementation.class, filteredMockCandidate.getType());
        Assert.assertEquals("fieldName", filteredMockCandidate.getName());
    }

    @Test
    public void testInjectMockCandidateWhenMultipleCandidatesByType() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = SomeClassWithMockableFields.class;
        Set<Object> mocks = new HashSet<Object>(Arrays.asList(new SomeMockImplementation(), new AnotherMockImplementation()));
        Object fieldInstance = new SomeClassWithMockableFields();

        MockCandidate mockCandidate1 = new MockCandidate(SomeMockImplementation.class, null, fieldInstance);
        MockCandidate mockCandidate2 = new MockCandidate(AnotherMockImplementation.class, null, fieldInstance);

        Assert.assertTrue(mockCandidate1.isMockable());
        Assert.assertFalse(mockCandidate1.shouldBeIgnored());
        Assert.assertTrue(mockCandidate2.isMockable());
        Assert.assertFalse(mockCandidate2.shouldBeIgnored());

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        MockCandidate filteredMockCandidate = mockCandidateFilter.filterCandidate(mocks, mockCandidate1, fieldInstance);

        Assert.assertNull(filteredMockCandidate);
    }

    @Test
    public void testInjectMockCandidateWhenMultipleCandidatesByName() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = SomeClassWithMockableFields.class;
        Set<Object> mocks = new HashSet<Object>(Arrays.asList(new SomeMockImplementation(), new AnotherMockImplementation()));
        Object fieldInstance = new SomeClassWithMockableFields();

        MockCandidate mockCandidate1 = new MockCandidate("fieldName", SomeMockImplementation.class, null, fieldInstance);
        MockCandidate mockCandidate2 = new MockCandidate("fieldName", AnotherMockImplementation.class, null, fieldInstance);

        Assert.assertTrue(mockCandidate1.isMockable());
        Assert.assertFalse(mockCandidate1.shouldBeIgnored());
        Assert.assertTrue(mockCandidate2.isMockable());
        Assert.assertFalse(mockCandidate2.shouldBeIgnored());

        MockCandidate filteredMockCandidate = mockCandidateFilter.filterCandidate(mocks, mockCandidate1, fieldInstance);

        Assert.assertNull(filteredMockCandidate);
    }

    @Test
    public void testInjectMockCandidateWhenOneMockCandidate() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = SomeClassWithMockableFields.class;
        Set<Object> mocks = new HashSet<Object>(Arrays.asList(new SomeMockImplementation()));
        Object fieldInstance = new SomeClassWithMockableFields();

        MockCandidate mockCandidate = new MockCandidate(SomeMockImplementation.class, null, fieldInstance);

        Assert.assertTrue(mockCandidate.isMockable());
        Assert.assertFalse(mockCandidate.shouldBeIgnored());

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        MockCandidate filteredMockCandidate = mockCandidateFilter.filterCandidate(mocks, mockCandidate, fieldInstance);

        Assert.assertNotNull(filteredMockCandidate);
        Assert.assertEquals(SomeMockImplementation.class, filteredMockCandidate.getType());
        Assert.assertEquals("fieldName", filteredMockCandidate.getName());
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetMockByPropertySetterFails() throws InstantiationException, IllegalAccessException {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = SomeClassWithMockableFields.class;
        Set<Object> mocks = new HashSet<Object>(Arrays.asList(new SomeMockImplementation()));
        Object fieldInstance = new SomeClassWithMockableFields();

        Field mockField = new FieldUtils().getFieldByName(awaitingInjectionClazz, "mockField", true);

        MockCandidate mockCandidate = new MockCandidate(SomeMockImplementation.class, null, fieldInstance);

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        Whitebox.setInternalState(fieldInstance, mockField.getName(), mockCandidate);
    }

    @Test
    public void testSetMockByFieldInjectionSucceeds() throws InstantiationException, IllegalAccessException {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = SomeClassWithMockableFields.class;
        Set<Object> mocks = new HashSet<Object>(Arrays.asList(new SomeMockImplementation()));
        Object fieldInstance = new SomeClassWithMockableFields();

        Field mockField = new FieldUtils().getFieldByName(awaitingInjectionClazz, "mockField", true);

        MockCandidate mockCandidate = new MockCandidate(SomeMockImplementation.class, null, fieldInstance);

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        Whitebox.setInternalState(fieldInstance, mockField, mockCandidate.getMockObject());
    }
}

class SomeClass {
    @InjectMocks
    private SomeClassWithMockableFields someClassWithMockableFields;
}

class SomeClassWithMockableFields {
    private SomeMockImplementation mockField;
}

class SomeMockImplementation {
}

class AnotherMockImplementation {
}