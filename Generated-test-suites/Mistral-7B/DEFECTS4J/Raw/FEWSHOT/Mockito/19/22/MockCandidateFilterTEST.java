package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.mockito.internal.configuration.injection.FieldInjectionContext;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ArrayUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MockCandidateFilterTest {

    @Test
    public void testNullInput() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        assertNull(filter.filterCandidate(null, null, null));
    }

    @Test
    public void testEmptyCollection() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        assertNull(filter.filterCandidate(Collections.emptyList(), null, null));
    }

    @Test
    public void testFilterCandidate_NoAnnotation() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));

        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));

        MockAnnotationProcessor mockAnnotationProcessor = Mockito.spy(new MockAnnotationProcessor());
        mockAnnotationProcessor.processInjectionPoint(new FieldInjectionContext(fieldToBeInjected, fieldInstance));

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_AnnotatedWithMock() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));

        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));

        MockAnnotationProcessor mockAnnotationProcessor = Mockito.spy(new MockAnnotationProcessor());
        mockAnnotationProcessor.processInjectionPoint(new FieldInjectionContext(fieldToBeInjected, fieldInstance));

        Mockito.when(fieldToBeInjected.getAnnotation(Mockito.Mock.class)).thenReturn(new Mockito.Mock());

        Object candidate = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(fieldInstance, candidate);
    }

    @Test
    public void testFilterCandidate_AnnotatedWithSpy() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));

        Object fieldInstance = Mockito.spy(Mockito.mock(Object.class));
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));

        MockAnnotationProcessor mockAnnotationProcessor = Mockito.spy(new MockAnnotationProcessor());
        mockAnnotationProcessor.processInjectionPoint(new FieldInjectionContext(fieldToBeInjected, fieldInstance));

        Mockito.when(fieldToBeInjected.getAnnotation(Mockito.Spy.class)).thenReturn(new Mockito.Spy());

        Object candidate = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(fieldInstance, candidate);
    }

    @Test
    public void testFilterCandidate_AnnotatedWithStub() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));

        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));

        MockAnnotationProcessor mockAnnotationProcessor = Mockito.spy(new MockAnnotationProcessor());
        mockAnnotationProcessor.processInjectionPoint(new FieldInjectionContext(fieldToBeInjected, fieldInstance));

        Mockito.when(fieldToBeInjected.getAnnotation(Mockito.Stub.class)).thenReturn(new Mockito.Stub());

        Object candidate = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(candidate);
    }

    @Test
    public void testFilterCandidate_AnnotatedWithArguments() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));

        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));

        MockAnnotationProcessor mockAnnotationProcessor = Mockito.spy(new MockAnnotationProcessor());
        mockAnnotationProcessor.processInjectionPoint(new FieldInjectionContext(fieldToBeInjected, fieldInstance));

        Mockito.when(fieldToBeInjected.getAnnotation(Mockito.Mock.class)).thenReturn(new Mockito.Mock(Mockito.captor(ArrayUtil.arrayOf(new Class[]{Integer.class, String.class}))));

        Object[] arguments = new Object[]{1, "hello"};
        Object candidate = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance, arguments);
        assertEquals(fieldInstance, candidate);
    }

    @Test
    public void testFilterCandidate_AnnotatedWithArgumentsAndAnnotation() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));

        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));

        MockAnnotationProcessor mockAnnotationProcessor = Mockito.spy(new MockAnnotationProcessor());
        mockAnnotationProcessor.processInjectionPoint(new FieldInjectionContext(fieldToBeInjected, fieldInstance));

        Mockito.when(fieldToBeInjected.getAnnotation(Mockito.Mock.class)).thenReturn(new Mockito.Mock(Mockito.captor(ArrayUtil.arrayOf(new Class[]{Integer.class, String.class}))));

        Object[] arguments = new Object[]{1, "hello"};
        Mockito.when(fieldToBeInjected.getAnnotation(Mockito.Mock.class)).thenAnswer(i -> i.getArguments()[0]);

        Object candidate = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance, arguments);
        assertEquals(fieldInstance, candidate);
    }
}