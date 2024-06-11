package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.mockitoSession.MockitoSession.mockingSession;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.mockito.internal.configuration.injection.MockingContext;
import org.mockito.internal.util.reflection.Whitebox;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_returnsNull_whenFieldIsNull() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = new Object();

        MockCandidateFilter filter = new MockAnnotationProcessor.MockCandidateFilter();
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_returnsNull_whenFieldIsAnnotatedWithDontMock() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = createFieldWithAnnotation(DontMock.class);
        Object fieldInstance = new Object();

        MockCandidateFilter filter = new MockAnnotationProcessor.MockCandidateFilter();
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_returnsMock_whenFieldIsAnnotatedWithMock() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = createFieldWithAnnotation(Mock.class);
        Object fieldInstance = new Object();

        MockCandidateFilter filter = new MockAnnotationProcessor.MockCandidateFilter();
        MockingContext mockingContext = mockingSession();
        Object mock = mockingContext.spy(fieldInstance.getClass());
        mocks.add(mock);

        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock, filteredMock);
    }

    @Test
    public void testFilterCandidate_returnsMock_whenFieldIsAnnotatedWithSpy() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = createFieldWithAnnotation(Spy.class);
        Object fieldInstance = new Object();

        MockCandidateFilter filter = new MockAnnotationProcessor.MockCandidateFilter();
        MockingContext mockingContext = mockingSession();
        Object mock = mockingContext.spy(fieldInstance.getClass());
        mocks.add(mock);

        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock, filteredMock);
    }

    @Test
    public void testFilterCandidate_returnsMock_whenFieldIsAnnotatedWithMockAnnotatedWithMock() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = createFieldWithAnnotation(MockAnnotatedWithMock.class);
        Object fieldInstance = new Object();

        MockCandidateFilter filter = new MockAnnotationProcessor.MockCandidateFilter();
        MockingContext mockingContext = mockingSession();
        Object mock = mockingContext.spy(fieldInstance.getClass());
        mocks.add(mock);

        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock, filteredMock);
    }

    private Field createFieldWithAnnotation(Class<? extends Annotation> annotationClass) {
        Class<?> clazz = Whitebox.getInternalState(this, "getClass");
        try {
            Field field = clazz.getDeclaredField("testField");
            field.setAccessible(true);
            field.annotate(annotationClass);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}