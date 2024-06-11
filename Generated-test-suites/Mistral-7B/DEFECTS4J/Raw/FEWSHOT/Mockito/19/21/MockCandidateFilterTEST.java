package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;
import org.mockito.internal.util.reflection.FieldAccessUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullFilter_ReturnsNull() {
        MockCandidateFilter filter = null;
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToMock");
        Object fieldInstance = new TestClass();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNull_ReturnsNull() {
        MockCandidateFilter filter = new TestFilterThatReturnsNull();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToMock");
        Object fieldInstance = new TestClass();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsMock_ReturnsMock() {
        MockCandidateFilter filter = new TestFilterThatReturnsMock();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(new TestMock());
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToMock");
        Object fieldInstance = new TestClass();

        MockitoMocked mock = (MockitoMocked) filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Assert.assertNotNull(mock);
        Assert.assertTrue(mock.isMock());
    }

    @Test
    public void testFilterCandidate_FilterReturnsMultipleMocks_ReturnsFirstMatchingMock() {
        MockCandidateFilter filter = new TestFilterThatReturnsMock();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(new TestMockWithSameNameAsFieldInstance());
        mocks.add(new TestMock());
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToMock");
        Object fieldInstance = new TestClassWithSameNameAsMock();

        MockitoMocked mock = (MockitoMocked) filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Assert.assertNotNull(mock);
        Assert.assertEquals(TestMockWithSameNameAsFieldInstance.class, mock.getMockType());
    }

    @Test
    public void testFilterCandidate_FilterReturnsMultipleMocks_ReturnsNullIfNoneMatch() {
        MockCandidateFilter filter = new TestFilterThatReturnsMock();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(new TestMock());
        mocks.add(new TestDifferentMock());
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToMock");
        Object fieldInstance = new TestClass();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNullIfFieldInstanceIsMock() {
        MockCandidateFilter filter = new TestFilterThatReturnsMock();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(new TestClassWithMockFieldInstance());
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToMock");
        Object fieldInstance = mock(TestClassWithMockFieldInstance.class);

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNullIfFieldIsNotAnnotated() {
        MockCandidateFilter filter = new TestFilterThatAnnotated();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "notAnnotatedField");
        Object fieldInstance = new TestClass();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNullIfMockIsAnnotatedButFieldIsNot() {
        MockCandidateFilter filter = new TestFilterThatAnnotated();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(mock(TestClassWithAnnotation.class));
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "notAnnotatedField");
        Object fieldInstance = new TestClass();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNullIfFieldIsAnnotatedButMockIsNot() {
        MockCandidateFilter filter = new TestFilterThatAnnotated();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToBeAnnotatedButMockIsNot");
        Object fieldInstance = new TestClass();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNullIfFieldIsAnnotatedWithDifferentAnnotation() {
        MockCandidateFilter filter = new TestFilterThatAnnotated();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(mock(TestClassWithDifferentAnnotation.class));
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToBeAnnotatedWithDifferentAnnotation");
        Object fieldInstance = new TestClass();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsSameInstanceIfFieldIsAnnotatedAndMockIsAnnotatedWithSameAnnotation() {
        MockCandidateFilter filter = new TestFilterThatAnnotated();
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(mock(TestClassWithAnnotation.class));
        Field fieldToBeInjected = FieldAccessUtil.getField(TestClass.class, "fieldToBeAnnotatedAndMockIsAnnotated");
        Object fieldInstance = mock(TestClassWithAnnotation.class);

        MockitoMocked mock = (MockitoMocked) filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Assert.assertNotNull(mock);
        Assert.assertEquals(fieldInstance, mock.getMock());
    }

    private static class TestClass {
        private static String fieldToMock;
        private static String notAnnotatedField;
        private static String fieldToBeAnnotatedButMockIsNot;
        private static String fieldToBeAnnotatedAndMockIsAnnotated;
        private static String fieldToBeAnnotatedWithDifferentAnnotation;

        private TestClassWithMockFieldInstance mockFieldInstance = mock(TestClassWithMockFieldInstance.class);
        private TestClassWithAnnotation annotatedMock = mock(TestClassWithAnnotation.class);
        private TestClassWithDifferentAnnotation differentAnnotationMock = mock(TestClassWithDifferentAnnotation.class);

        static class TestMock implements MockitoMocked {
            @Override
            public Class<?> getMockType() {
                return TestMock.class;
            }

            @Override
            public boolean isMock() {
                return true;
            }
        }

        static class TestMockWithSameNameAsFieldInstance extends TestMock {
            @Override
            public String toString() {
                return "TestMockWithSameNameAsFieldInstance";
            }
        }

        static class TestMockWithSameNameAsFieldInstanceAndAnnotation extends TestMock {
            @Override
            public String toString() {
                return "TestMockWithSameNameAsFieldInstanceAndAnnotation";
            }
        }

        static class TestClassWithMockFieldInstance {
            public TestClassWithMockFieldInstance mockField = mock(TestClassWithMockFieldInstance.class);
        }

        static class TestClassWithAnnotation {
            @MockAnnotation
            private String fieldAnnotation;
        }

        static class TestClassWithDifferentAnnotation {
            @DifferentAnnotation
            private String fieldAnnotation;
        }

        static class TestClassWithDifferentAnnotationAndMock {
            @DifferentAnnotation
            private String fieldAnnotation;
        }
    }

    static abstract class TestFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return null;
        }
    }

    static class TestFilterThatReturnsNull extends TestFilter {
    }

    static class TestFilterThatReturnsMock extends TestFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            if (mocks.contains(fieldInstance)) {
                return new OngoingMockInjecter(fieldToBeInjected, fieldInstance);
            }
            return null;
        }
    }

    static class TestFilterThatAnnotated extends TestFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            if (fieldToBeInjected.isAnnotationPresent(MockAnnotation.class) && mocks.contains(fieldInstance)) {
                return new OngoingMockInjecter(fieldToBeInjected, fieldInstance);
            }
            return null;
        }
    }
}