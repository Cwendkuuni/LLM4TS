package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.Answer;
import org.mockito.stubs.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        Reporter reporter = mock(Reporter.class);
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object() {});
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        when(field.getName()).thenReturn("testField");
        when(fieldInstance.getClass()).thenReturn(Object.class);

        result.thenInject();

        verify(fieldInstance).getClass();
        verify(field).set(mock, any());
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Reporter reporter = mock(Reporter.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(reporter);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        Reporter reporter = mock(Reporter.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        when(field.getName()).thenReturn("testField");
        when(fieldInstance.getClass()).thenReturn(Object.class);

        doThrow(new RuntimeException("Test exception")).when(mock).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Test exception");
            }
        });

        result.thenInject();

        verify(reporter).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        Reporter reporter = mock(Reporter.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(null, field, fieldInstance);

        result.thenInject();

        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testFilterCandidateWithNullField() {
        Reporter reporter = mock(Reporter.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(new ArrayList<>(), null, fieldInstance);

        result.thenInject();

        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(reporter);
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        Reporter reporter = mock(Reporter.class);
        Field field = mock(Field.class);
        Collection<Object> mocks = new ArrayList<>();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        result.thenInject();

        verifyZeroInteractions(reporter);
    }
}