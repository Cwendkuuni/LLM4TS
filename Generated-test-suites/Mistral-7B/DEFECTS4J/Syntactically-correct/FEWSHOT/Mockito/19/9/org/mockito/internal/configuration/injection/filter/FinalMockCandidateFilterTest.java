package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockingConfiguration;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setVal;
import static org.mockito.internal.util.reflection.Reflections.setValue;
import static org.mockito.mockitoSession.MockitoSession.mockitoSession;
import static org.mockito.stubbing.OngoingStubbing.thenAnswer;
import static org.mockito.stubbing.OngoingStubbing.thenThrow;
import static org.mockito.stubbing.OngoingStubbing.thenReturn;
import static org.mockito.verification.VerificationModeFactory.times;
import static org.mockito.verification.VerificationModeFactory.verify;

public class FinalMockCandidateFilterTest {
    private final Reporter reporter = mock(Reporter.class);
    private FinalMockCandidateFilter filter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        mocks = Collections.singletonList(mock(Object.class));
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object mock = mocks.iterator().next();
        assert mock instanceof OngoingInjecter : "filterCandidate should return an OngoingInjecter";
        OngoingInjecter actualInjecter = (OngoingInjecter) injecter;

        Object result = actualInjecter.thenInject();
        assertEquals(mock, result);

        verify(reporter, times(0)).cannotInjectDependency(any(), any(), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());

        verify(reporter, times(0)).cannotInjectDependency(any(), any(), any(RuntimeException.class));
    }

    @Test
    public void testPropertySetterInjection() {
        Object mock = mock(Object.class);
        BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);

        setValue(propertySetter, "valueToSet");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();
        assertEquals(mock, result);

        verify(reporter, times(0)).cannotInjectDependency(any(), any(), any(RuntimeException.class));

        Object fieldValue = getFieldValue(mock, field);
        assertEquals("valueToSet", fieldValue);
    }

    @Test
    public void testFieldAccessInjection() {
        Object mock = mock(Object.class);

        setVal(fieldInstance, field, mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();
        assertEquals(mock, result);

        verify(reporter, times(0)).cannotInjectDependency(any(), any(), any(RuntimeException.class));

        Object fieldValue = getFieldValue(fieldInstance, field);
        assertEquals(mock, fieldValue);
    }

    @Test
    public void testCannotInjectDependency() {
        when(reporter).thenAnswer(invocation -> {
            Throwable cause = invocation.getArgument(2);
            throw new RuntimeException(cause.getMessage(), cause);
        });

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            fail("Expected Reporter to be called with error message");
        } catch (RuntimeException e) {
            // Expected
        }

        verify(reporter, times(1)).cannotInjectDependency(any(), any(), any(RuntimeException.class));
    }
}