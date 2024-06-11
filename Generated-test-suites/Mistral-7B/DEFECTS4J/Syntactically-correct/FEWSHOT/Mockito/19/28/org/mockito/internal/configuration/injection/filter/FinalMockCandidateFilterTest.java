package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.InjectingFixture;
import org.mockito.internal.util.reflection.Injector;
import org.mockito.internal.util.reflection.OngoingInjecter;
import org.mockito.internal.util.reflection.Stubber;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private Stubber stubber;
    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;
    private Reporter reporter;
    private InjectingFixture fixture;
    private Injector injector;
    private FinalMockCandidateFilter filter;

    @Before
    public void setup() {
        stubber = mock(Stubber.class);
        propertySetter = mock(BeanPropertySetter.class);
        fieldSetter = mock(FieldSetter.class);
        reporter = mock(Reporter.class);
        fixture = mock(InjectingFixture.class);
        injector = mock(Injector.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWhenSingleMock() {
        // given
        Object mock = mock(MockitoConfiguration.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter expectedInjecter = mock(OngoingInjecter.class);

        when(propertySetter.set(fieldInstance, field)).thenReturn(true);

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(Arrays.asList(mock), field, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNotNull(actualInjecter.thenInject());
        verify(reporter).cannotInjectDependency(field, mock, isNull());
        verify(propertySetter).set(fieldInstance, field);
        verify(fieldSetter, never()).set(fieldInstance, field);
        verify(injector, never()).inject(fieldInstance);
    }

    @Test
    public void testFilterCandidateWhenMultipleMocksAndSuccessPropertySetter() {
        // given
        Object mock1 = mock(MockitoConfiguration.class);
        Object mock2 = mock(MockitoConfiguration.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter expectedInjecter = mock(OngoingInjecter.class);

        when(propertySetter.set(fieldInstance, field)).thenReturn(true);

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNotNull(actualInjecter.thenInject());
        verify(reporter, never()).cannotInjectDependency(field, mock1, any(RuntimeException.class));
        verify(reporter, never()).cannotInjectDependency(field, mock2, any(RuntimeException.class));
        verify(propertySetter).set(fieldInstance, field);
        verify(fieldSetter, never()).set(fieldInstance, field);
        verify(injector, never()).inject(fieldInstance);
    }

    @Test
    public void testFilterCandidateWhenMultipleMocksAndFailurePropertySetter() {
        // given
        Object mock1 = mock(MockitoConfiguration.class);
        Object mock2 = mock(MockitoConfiguration.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter expectedInjecter = mock(OngoingInjecter.class);

        when(propertySetter.set(fieldInstance, field)).thenThrow(new RuntimeException("Property setter failed"));

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
        verify(reporter).cannotInjectDependency(field, mock1, isNull());
        verify(reporter, never()).cannotInjectDependency(field, mock2, any(RuntimeException.class));
        verify(propertySetter, times(1)).set(fieldInstance, field);
        verify(fieldSetter, times(1)).set(fieldInstance, field);
        verify(injector, never()).inject(fieldInstance);
    }

    @Test
    public void testFilterCandidateWhenMultipleMocksAndSuccessFieldSetter() {
        // given
        Object mock1 = mock(MockitoConfiguration.class);
        Object mock2 = mock(MockitoConfiguration.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter expectedInjecter = mock(OngoingInjecter.class);

        when(fieldSetter.set(fieldInstance, field)).thenReturn(true);

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
        verify(reporter, never()).cannotInjectDependency(field, mock1, any(RuntimeException.class));
        verify(reporter, never()).cannotInjectDependency(field, mock2, any(RuntimeException.class));
        verify(propertySetter, never()).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        verify(injector, never()).inject(fieldInstance);
    }

    @Test
    public void testFilterCandidateWhenMultipleMocksAndFailureFieldSetter() {
        // given
        Object mock1 = mock(MockitoConfiguration.class);
        Object mock2 = mock(MockitoConfiguration.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter expectedInjecter = mock(OngoingInjecter.class);

        when(fieldSetter.set(fieldInstance, field)).thenThrow(new RuntimeException("Field setter failed"));

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
        verify(reporter).cannotInjectDependency(field, mock1, isNull());
        verify(reporter, never()).cannotInjectDependency(field, mock2, any(RuntimeException.class));
        verify(propertySetter, times(1)).set(fieldInstance, field);
        verify(fieldSetter, times(1)).set(fieldInstance, field);
        verify(injector, never()).inject(fieldInstance);
    }

    @Test
    public void testFilterCandidateWhenEmptyMockList() {
        // given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
        verify(reporter, never()).cannotInjectDependency(field, any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWhenNullMockList() {
        // given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(null, field, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
        verify(reporter, never()).cannotInjectDependency(field, any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWhenNullField() {
        // given
        Object mocks = mock(Collection.class);
        Object fieldInstance = mock(Object.class);

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, null, fieldInstance);

        // then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
        verify(reporter, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWhenNullFieldInstance() {
        // given
        Collection<Object> mocks = Arrays.asList(mock(MockitoConfiguration.class), mock(MockitoConfiguration.class));
        Field field = mock(Field.class);

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, null);

        // then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
        verify(reporter, never()).cannotInjectDependency(field, any(Object.class), any(RuntimeException.class));
    }
}