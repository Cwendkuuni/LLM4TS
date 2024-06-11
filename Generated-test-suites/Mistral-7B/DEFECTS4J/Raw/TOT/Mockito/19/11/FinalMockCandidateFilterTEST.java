package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Test class for {@link FinalMockCandidateFilter}
 */
public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object fieldInstance;
    private Field field;

    @Rule
    public MockitoJUnit MockitoJUnitRule = new MockitoJUnit();

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
    }

    @Test
    public void testSingleMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        // When
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object result = filterResult.thenInject();
        assertNull(result); // The filter simply returns the mock, but we're not interested in its return value here
        verifyInjected(mock1, fieldInstance, field);
    }

    @Test
    public void testMultipleMocks() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        // When
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(filterResult.thenInject()); // The filter returns an empty injecter in this scenario
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionDuringBeanPropertySetter() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        // When
        when(field.set(fieldInstance, anyObject())).thenThrow(new RuntimeException("Test exception"));

        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        filterResult.thenInject(); // This call should trigger the exception
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionDuringFieldSetter() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        // When
        when(new BeanPropertySetter(fieldInstance, field).canSet()).thenReturn(false);
        when(new BeanPropertySetter(fieldInstance, field).set(anyObject())).thenThrow(new RuntimeException("Test exception"));
        when(new FieldSetter(fieldInstance, field).canSet()).thenReturn(true);

        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        filterResult.thenInject(); // This call should trigger the exception
    }

    private void verifyInjected(Object mock, Object fieldInstance, Field field) {
        try {
            new BeanPropertySetter(fieldInstance, field).set(mock);
            new FieldSetter(fieldInstance, field).set(mock);
        } catch (RuntimeException ignored) {
            // We don't care about the exception here. Just check that the field was set by either method.
        }

        Object value = fieldInstance.getClass().getField("injectedValue").get(fieldInstance);
        assertEquals(mock, value);
    }
}