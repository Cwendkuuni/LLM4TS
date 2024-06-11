package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.JUnit;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public JUnit.Rules defaultRules = JUnit.rules();

    private Reporter reporter;
    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        propertySetter = new BeanPropertySetter(null, null);
        fieldSetter = new FieldSetter(null, null);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Reporter reporterSpy = mock(Reporter.class);
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Object field = mock(Field.class);

        Collection<Object> mocks = Arrays.asList(mock);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, actualInjecter.thenInject());
        assertFalse(reporterSpy.wasReportedError());
    }

    @Test
    public void testFilterCandidateWithOneFieldSetterSuccess() {
        Reporter reporterSpy = mock(Reporter.class);
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Object field = mock(Field.class);

        Collection<Object> mocks = Arrays.asList(mock);

        BeanPropertySetter propertySetterSpy = mock(BeanPropertySetter.class);
        when(propertySetterSpy.set(fieldInstance, field)).thenReturn(true);
        when(propertySetter.set(fieldInstance, field)).thenReturn(propertySetterSpy);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, actualInjecter.thenInject());
        assertFalse(reporterSpy.wasReportedError());
    }

    @Test
    public void testFilterCandidateWithOneFieldSetterFailure() {
        Reporter reporterSpy = mock(Reporter.class);
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Object field = mock(Field.class);

        Collection<Object> mocks = Arrays.asList(mock);

        BeanPropertySetter propertySetterSpy = mock(BeanPropertySetter.class);
        when(propertySetterSpy.set(fieldInstance, field)).thenThrow(new RuntimeException("Test Exception"));
        when(propertySetter.set(fieldInstance, field)).thenReturn(propertySetterSpy);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(actualInjecter.thenInject());
        assertTrue(reporterSpy.wasReportedError());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Reporter reporterSpy = mock(Reporter.class);
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Object field = mock(Field.class);

        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(actualInjecter.thenInject());
    }
}