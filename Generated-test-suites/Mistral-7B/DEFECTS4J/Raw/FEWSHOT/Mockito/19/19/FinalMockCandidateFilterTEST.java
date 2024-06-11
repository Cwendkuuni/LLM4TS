package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitosis.MockitoTestBase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest extends MockitoTestBase {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private FinalMockCandidateFilter filter;
    private Reporter reporter;

    @Before
    public void setup() {
        mocks = mock(Collection.class);
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        filter = new FinalMockCandidateFilter();
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidateWhenOneMock() {
        when(mocks.iterator().next()).thenReturn(mock1);
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedObject = injecter.thenInject();
        verify(mock1).getClass();
        verify(fieldInstance).getClass();
        verify(field).set(mock1, any());
        assertEquals(mock1, injectedObject);
    }

    @Test
    public void testFilterCandidateWhenMultipleMocks() {
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        when(mocks.iterator().next()).thenReturn(mock1);
        when(mocks.iterator().next()).thenReturn(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verifyNoInteractions(mock2);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateThrowsExceptionWhenCannotInject() {
        when(mocks.iterator().next()).thenReturn(mock1);
        when(field.set(mock1, any())).thenThrow(new RuntimeException());

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(reporter).cannotInjectDependency(field, mock1, any(RuntimeException.class));
        throw new RuntimeException();
    }

    @Test
    public void testFilterCandidateReturnsNullInjecterWhenMultipleMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(injecter.thenInject());
    }
}