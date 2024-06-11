package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.Injectable;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;
    private MockCandidateFilter filter;

    @Rule
    public MockitoJUnit MockitoJUnitRule = new MockitoJUnit();

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        propertySetter = mock(BeanPropertySetter.class);
        fieldSetter = mock(FieldSetter.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        // given
        Object mock = mock(Injectable.class);
        Collection<Object> mocks = Collections.singletonList(mock);
        Object fieldInstance = mock(Object.class);
        Field field = Whitebox.getInternalState(fieldSetter, "field");

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verify(propertySetter).set(fieldInstance, field, mock);
        verifyZeroInteractions(fieldSetter);
        verify(reporter).cannotInjectDependency(isNull(), isNull(), isNull());
        assertEquals(mock, actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // given
        Object mock1 = mock(Injectable.class);
        Object mock2 = mock(Injectable.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Object fieldInstance = mock(Object.class);
        Field field = Whitebox.getInternalState(fieldSetter, "field");

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verifyZeroInteractions(reporter);
        assertNull(actualInjecter.thenInject());
    }

    @Test(expected = AssertionError.class)
    public void testFilterCandidateWithNullCollection() {
        // given
        Object fieldInstance = mock(Object.class);
        Field field = Whitebox.getInternalState(fieldSetter, "field");

        // when
        filter.filterCandidate(null, field, fieldInstance);

        // then
        // an AssertionError is thrown because the Collection is null
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullField() {
        // given
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Object fieldInstance = mock(Object.class);

        // when
        filter.filterCandidate(mocks, null, fieldInstance);

        // then
        // an IllegalArgumentException is thrown because the Field is null
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        // given
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = Whitebox.getInternalState(fieldSetter, "field");

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, null);

        // then
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verifyZeroInteractions(reporter);
        assertNull(actualInjecter.thenInject());
    }

    private static Object spyIfInstanceOf(Object object, Class<?> clazz) {
        return clazz.isInstance(object) ? spy(object) : object;
    }

    private static boolean isNull(Object object) {
        return object == null;
    }

    private static OngoingInjecter assertEquals(OngoingInjecter expected, OngoingInjecter actual) {
        assertEquals(expected.getClass(), actual.getClass());
        return expected;
    }
}