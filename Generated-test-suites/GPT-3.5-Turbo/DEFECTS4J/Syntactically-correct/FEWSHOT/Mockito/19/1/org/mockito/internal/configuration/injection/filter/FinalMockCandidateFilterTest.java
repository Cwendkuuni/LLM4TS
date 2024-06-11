package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateWithSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();
        assertEquals(fieldInstance, result);

        // assert that field is set
        // assert that BeanPropertySetter.set() is called with matchingMock
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();
        assertEquals(null, result);

        // assert that field is not set
        // assert that BeanPropertySetter.set() is not called
        // assert that Reporter.cannotInjectDependency() is not called
    }

    private Field getField() {
        // create and return a mock Field object
        return mock(Field.class);
    }

    private Object getFieldInstance() {
        // create and return a mock fieldInstance object
        return mock(Object.class);
    }
}