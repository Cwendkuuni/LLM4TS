package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockInjectingField;
import org.mockito.internal.configuration.injection.MockInjectingValue;
import org.mockito.internal.configuration.injection.MockitoInjector;
import org.mockito.internal.configuration.injection.MockitoOngoingInjecter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class FinalMockCandidateFilterTest {

    private MockitoConfiguration mockitoConfiguration;
    private FinalMockCandidateFilter finalMockCandidateFilter;
    private Reporter reporter;

    @Before
    public void setUp() {
        mockitoConfiguration = new MockitoConfiguration();
        finalMockCandidateFilter = new FinalMockCandidateFilter();
        reporter = mockitoConfiguration.getReporter();
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySetterSuccess() {
        // Given
        Object mock = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(mock, "someField");

        MockInjectingField mockField = new MockInjectingField(field);
        mockField.setValue(mockitoConfiguration, mock);
        setInternalState(mock, "someField", mockField);

        // When
        Object candidateFilter = finalMockCandidateFilter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        // Then
        assertEquals(mock, candidateFilter);
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySetterFailure() {
        // Given
        Object mock = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(mock, "someField");

        MockInjectingField mockField = new MockInjectingField(field);
        mockField.setValue(mockitoConfiguration, mock);
        setInternalState(mock, "someField", mockField);

        // When
        try {
            finalMockCandidateFilter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);
            fail("Expected Reporter.cannotInjectDependency to be called");
        } catch (Throwable e) {
            // Then
            assertEquals("org.mockito.exceptions.base.MockitoFailureReason: Cannot inject", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterSuccess() {
        // Given
        Object mock = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(mock, "someField");

        MockitoOngoingInjecter mockOngoingInjecter = new MockitoOngoingInjecter();
        setInternalState(mock, "someField", mockOngoingInjecter);

        // When
        Object candidateFilter = finalMockCandidateFilter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        // Then
        assertEquals(mock, candidateFilter);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterFailure() {
        // Given
        Object mock = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(mock, "someField");

        MockitoOngoingInjecter mockOngoingInjecter = new MockitoOngoingInjecter();
        setInternalState(mock, "someField", mockOngoingInjecter);

        // When
        try {
            finalMockCandidateFilter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);
            fail("Expected Reporter.cannotInjectDependency to be called");
        } catch (Throwable e) {
            // Then
            assertEquals("org.mockito.exceptions.base.MockitoFailureReason: Cannot inject", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch() {
        // Given
        Object mock1 = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object mock2 = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(mock1, "someField");

        // When
        Object candidateFilter = finalMockCandidateFilter.filterCandidate(List.of(mock1, mock2), field, fieldInstance);

        // Then
        assertNull(candidateFilter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FirstMatch() {
        // Given
        Object mock1 = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object mock2 = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(mock1, "someField");

        MockInjectingValue mock1FieldValue = new MockInjectingValue(mockitoConfiguration, mock1);
        setInternalState(mock1, "someField", mock1FieldValue);

        // When
        Object candidateFilter = finalMockCandidateFilter.filterCandidate(List.of(mock1, mock2), field, fieldInstance);

        // Then
        assertEquals(mock1, candidateFilter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_SecondMatch() {
        // Given
        Object mock1 = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object mock2 = new MockitoInjector(mockitoConfiguration).getInstance(MockitoInjector.class);
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(mock2, "someField");

        MockInjectingValue mock2FieldValue = new MockInjectingValue(mockitoConfiguration, mock2);
        setInternalState(mock2, "someField", mock2FieldValue);

        // When
        Object candidateFilter = finalMockCandidateFilter.filterCandidate(List.of(mock1, mock2), field, fieldInstance);

        // Then
        assertNull(candidateFilter);
    }
}