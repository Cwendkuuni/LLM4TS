package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.InjectionPoint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Reporter reporter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        reporter = mock(Reporter.class);
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        // Given
        Object mock = mock(Object.class);

        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(injecter);
        verify(mock).equals(fieldInstance);
        verify(reporter).cannotInjectDependency(field, mock, Mockito.isNull());
    }

    @Test
    public void testFilterCandidateWithSinglePropertySetterSuccess() {
        // Given
        Object mock = mock(Object.class);

        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);

        // When
        Object result = propertySetter.set(mock, "value");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock, result);
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithSingleFieldSetterSuccess() {
        // Given
        Object mock = mock(Object.class);

        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);

        // When
        Object result = fieldSetter.set(mock, "value");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock, result);
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndPropertySetterFailure() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(injecter);
        verify(mock1).equals(fieldInstance);
        verify(mock2).equals(fieldInstance);
        verify(reporter).cannotInjectDependency(field, mock1, Mockito.isNull());
        verify(reporter).cannotInjectDependency(field, mock2, Mockito.isNull());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndFieldSetterFailure() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(injecter);
        verify(mock1).equals(fieldInstance);
        verify(mock2).equals(fieldInstance);
        verify(reporter).cannotInjectDependency(field, mock1, Mockito.isNull());
        verify(reporter).cannotInjectDependency(field, mock2, Mockito.isNull());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndBothPropertyAndFieldSetterFailure() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(injecter);
        verify(mock1).equals(fieldInstance);
        verify(mock2).equals(fieldInstance);
        verify(reporter).cannotInjectDependency(field, mock1, Mockito.isNull());
        verify(reporter).cannotInjectDependency(field, mock2, Mockito.isNull());
    }

    @Test
    public void testFilterCandidateWithNoMocks() {
        // Given
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);

        // Then
        assertNull(injecter);
    }
}