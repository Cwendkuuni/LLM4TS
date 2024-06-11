package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtilTest;
import org.mockito.internal.util.naming.DefaultMockNameGenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
        DefaultMockNameGenerator nameGenerator = new DefaultMockNameGenerator();
        String mockName1 = nameGenerator.generateMockName("TestClass1");
        String mockName2 = nameGenerator.generateMockName("TestClass2");
        nextFilter = new StubFilter();
        filter = new NameBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("TestClass1", mockName1));

        Field field = new FieldTestClass().getClass().getDeclaredField("testField");

        Object fieldInstance = new FieldTestClass();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(mockName1, result.getMock().getName());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("TestClass1", mockName1));
        mocks.add(createMock("TestClass1", mockName2));

        Field field = new FieldTestClass().getClass().getDeclaredField("testField");

        Object fieldInstance = new FieldTestClass();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(mockName1, result.getMock().getName());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidateWithConflictingNameFields() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("TestClass1", mockName1));
        mocks.add(createMock("TestClass1", mockName2));

        Field field = new FieldTestClass().getClass().getDeclaredField("testField1");

        Object fieldInstance = new FieldTestClass();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(mockName2, result.getMock().getName());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();

        Field field = new FieldTestClass().getClass().getDeclaredField("testField");

        Object fieldInstance = new FieldTestClass();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        Collection<Object> mocks = null;

        Field field = new FieldTestClass().getClass().getDeclaredField("testField");

        Object fieldInstance = new FieldTestClass();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("TestClass1", mockName1));

        Field field = null;

        Object fieldInstance = new FieldTestClass();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    private Object createMock(String className, String name) {
        MockCandidate mock = mockUtil.createMock(className);
        mockUtil.injectName(mock, name);
        return mock;
    }

    private static class FieldTestClass {
        private String testField;
        private String testField1;
    }

    private static class StubFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks,
                Field field, Object fieldInstance) {
            return null;
        }
    }
}