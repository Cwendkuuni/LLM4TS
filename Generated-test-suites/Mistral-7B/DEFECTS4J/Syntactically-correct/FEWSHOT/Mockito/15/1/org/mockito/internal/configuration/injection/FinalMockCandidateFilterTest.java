package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.Injector;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(SomeClass.class);
        mocks.add(mock);
        Field field = SomeClass.class.getField("fieldToInject");
        Object fieldInstance = spy(new SomeClass());

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) injecter;
        Assert.assertTrue(ongoingInjecter.thenInject());
        Object injectedValue = field.get(fieldInstance);
        Assert.assertEquals(mock, injectedValue);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(SomeClass.class);
        Object mock2 = mock(SomeClass.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = SomeClass.class.getField("fieldToInject");
        Object fieldInstance = new SomeClass();

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) injecter;
        Assert.assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(SomeClass.class);
        Field field = SomeClass.class.getField("fieldToInject");
        Object fieldInstance = new SomeClass();

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then - should throw an exception
        injecter.thenInject();
    }

    @Test
    public void testFilterCandidateWithPropertySetter() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(SomeClass.class);
        mocks.add(mock);
        Field field = SomeClass.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new SomeClass();

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        OngoingInjecter ongoingInjecter = (OngoingInjecter) injecter;
        Stubber stubber = mock(Stubber.class);
        Answer answer = mock(Answer.class);
        when(stubber.answers(answer)).thenReturn(answer);
        when(mock.answer(answer)).then(stubber);

        ongoingInjecter.thenInject();

        verify(mock).answer(answer);
        verify(field).set(fieldInstance, mock);
    }

    @Test
    public void testFilterCandidateWithFieldAccess() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(SomeClass.class);
        mocks.add(mock);
        Field field = SomeClass.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new SomeClass();

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        OngoingInjecter ongoingInjecter = (OngoingInjecter) injecter;
        FieldUtils.makeAccessible(field);
        ongoingInjecter.thenInject();

        Assert.assertNotSame(fieldInstance, field.get(fieldInstance));
        Assert.assertEquals(mock, field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        // Given
        Field field = SomeClass.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new SomeClass();

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) injecter;
        Assert.assertFalse(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullField() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(SomeClass.class);
        mocks.add(mock);
        Object fieldInstance = new SomeClass();

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        Assert.assertNotNull(injecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) injecter;
        Assert.assertFalse(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(SomeClass.class);
        mocks.add(mock);
        Field field = SomeClass.class.getDeclaredField("fieldToInject");

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);

        // Then
        Assert.assertNotNull(injecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) injecter;
        Assert.assertFalse(ongoingInjecter.thenInject());
    }
}

class SomeClass {
    private SomeDependency fieldToInject;

    public void someMethod(InvocationOnMock invocation) {
        // Test implementation
    }
}