package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate_SingleMock_FieldSetterSuccess() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock = mock(MockitoTestClass.class);
        mocks.add(mock);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = spy(mock);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // When
        final Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        };

        final Stubber stubber = mock(Stubber.class);
        when(mock).thenAnswer(answer);
        when(field).thenReturn(stubber);
        stubber.doAnswer(answer).when(mock).methodCall();

        // Then
        filterResult.thenInject();
        verify(mock).methodCall();
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterFailure() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock = mock(MockitoTestClass.class);
        mocks.add(mock);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = spy(mock);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // When & Then
        Assert.assertThrows(MockitoException.class, filterResult::thenInject);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldAccessSuccess() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock = mock(MockitoTestClass.class);
        mocks.add(mock);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = spy(mock);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // When
        final Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        };

        final Stubber stubber = mock(Stubber.class);
        when(mock).thenAnswer(answer);
        when(field).thenReturn(stubber);
        stubber.doAnswer(answer).when(mock).setAccessible(true);
        stubber.doAnswer(answer).when(mock).getDeclaredField(field.getName());
        stubber.doAnswer(answer).when(fieldInstance).set(mock, null);

        // Then
        filterResult.thenInject();
        verify(mock).setAccessible(true);
        verify(mock).getDeclaredField(field.getName());
        verify(mock).set(fieldInstance, null);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldAccessFailure() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock = mock(MockitoTestClass.class);
        mocks.add(mock);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = spy(mock);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // When & Then
        Assert.assertThrows(MockitoException.class, filterResult::thenInject);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock1 = mock(MockitoTestClass.class);
        final Object mock2 = mock(MockitoTestClass.class);
        mocks.add(mock1);
        mocks.add(mock2);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = mock(MockitoTestClass.class);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        final OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        Assert.assertFalse(filterResult.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock1 = mock(MockitoTestClass.class);
        final Object mock2 = mock(MockitoTestClass.class);
        mocks.add(mock1);
        mocks.add(mock2);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = mock(MockitoTestClass.class);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        final Object matchingMock = mocks.iterator().next();
        final OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // When & Then
        final Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        };

        final Stubber stubber = mock(Stubber.class);
        when(matchingMock).thenAnswer(answer);
        when(field).thenReturn(stubber);
        stubber.doAnswer(answer).when(matchingMock).methodCall();

        filterResult.thenInject();
        verify(matchingMock).methodCall();
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessSuccess() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock1 = mock(MockitoTestClass.class);
        final Object mock2 = mock(MockitoTestClass.class);
        mocks.add(mock1);
        mocks.add(mock2);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = mock(MockitoTestClass.class);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        final Object matchingMock = mocks.iterator().next();
        final OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // When
        final Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        };

        final Stubber stubber = mock(Stubber.class);
        when(matchingMock).thenAnswer(answer);
        when(field).thenReturn(stubber);
        stubber.doAnswer(answer).when(matchingMock).setAccessible(true);
        stubber.doAnswer(answer).when(matchingMock).getDeclaredField(field.getName());
        stubber.doAnswer(answer).when(fieldInstance).set(matchingMock, null);

        // Then
        filterResult.thenInject();
        verify(matchingMock).setAccessible(true);
        verify(matchingMock).getDeclaredField(field.getName());
        verify(matchingMock).set(fieldInstance, null);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessFailure() {
        // Given
        final Collection<Object> mocks = new ArrayList<>();
        final Object mock1 = mock(MockitoTestClass.class);
        final Object mock2 = mock(MockitoTestClass.class);
        mocks.add(mock1);
        mocks.add(mock2);

        final Field field = FieldUtils.getField(MockitoTestClass.class, "testField");
        final Object fieldInstance = mock(MockitoTestClass.class);

        final MockCandidateFilter filter = new FinalMockCandidateFilter();

        // When & Then
        Assert.assertThrows(MockitoException.class, filter::filterCandidate);
    }

    private static class MockitoTestClass {
        private int testField;

        public void methodCall() {
        }
    }
}