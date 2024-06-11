package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Exceptions;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.mock.MockComponent;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private FinalMockCandidateFilter filter;
    private MockMaker mockMaker;
    private Field field;

    @Before
    public void setUp() {
        mockMaker = mock(MockMaker.class);
        filter = new FinalMockCandidateFilter();
        field = Mockito.mock(Field.class);
    }

    @Test
    public void shouldReturnOngoingInjecterWhenCollectionSizeIsOne() {
        // Given
        Object mock = mock(MockComponent.class, mockMaker);
        Collection<Object> mocks = Collections.singletonList(mock);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, new Object());

        // Then
        assertNotNull(injecter);
    }

    @Test
    public void shouldTryToInjectUsingBeanPropertySetterWhenCollectionSizeIsOneAndPropertySetterWorks() {
        // Given
        Object mock = mock(MockComponent.class, mockMaker);
        Collection<Object> mocks = Collections.singletonList(mock);

        doNothing().when(mock).setFieldValue(any(Field.class), any());
        doNothing().when(mock).setPropertyValue(any(), any());

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, new Object());

        // Then
        verify(mock).setPropertyValue(any(), any());
    }

    @Test(expected = RuntimeException.class)
    public void shouldTryToInjectUsingFieldSetterWhenCollectionSizeIsOneAndPropertySetterFails() {
        // Given
        Object mock = mock(MockComponent.class, mockMaker);
        Collection<Object> mocks = Collections.singletonList(mock);

        doThrow(new RuntimeException("Test exception")).when(mock).setPropertyValue(any(), any());
        doNothing().when(mock).setFieldValue(any(Field.class), any());

        // When
        filter.filterCandidate(mocks, field, new Object()).thenInject();

        // Then
        throw new AssertionError("Method should have thrown an exception");
    }

    @Test
    public void shouldReturnNullWhenCollectionSizeIsMoreThanOne() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(MockComponent.class), mock(MockComponent.class));

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, new Object());

        // Then
        assertNull(injecter);
    }

    @Test
    public void shouldThrowExceptionWhenCandidateCouldNotBeInjected() {
        // Given
        Object mock = mock(MockComponent.class, mockMaker);
        Collection<Object> mocks = Arrays.asList(mock, mock(MockComponent.class));

        doThrow(new RuntimeException("Test exception")).when(mock).setPropertyValue(any(), any());
        doThrow(new RuntimeException("Test exception")).when(mock).setFieldValue(any(Field.class), any());

        // When & Then
        try {
            filter.filterCandidate(mocks, field, new Object()).thenInject();
        } catch (RuntimeException e) {
            Exceptions.printStackTrace(e);
        }

        verify(mock).setPropertyValue(any(), any());
        verify(mock).setFieldValue(any(Field.class), any());
    }
}