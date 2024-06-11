package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.MockitoClassUtil;
import org.mockito.mock.MockFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Reporter reporter;
    private MockFactory mockFactory;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setUp() {
        reporter = mock(Reporter.class);
        mockFactory = mock(MockFactory.class);
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = MockitoClassUtil.getDeclaredField(FieldType.FIELD, FieldSetter.class, fieldInstance, "fieldToInject");
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(field.getType()).thenReturn(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock).setState(MockitoClassUtil.getMockState(mock, mockFactory));
        verify(mock).__Mockito_fieldToInject__ = fieldInstance;
        verifyZeroInteractions(reporter);

        assertEquals(mock, result.thenInject());
    }

    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidateWithSingleMock_PropertySetterFailure() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(field.getType()).thenReturn(String.class);

        try {
            filter.filterCandidate(mocks, field, fieldInstance).thenInject();
        } catch (RuntimeException e) {
            throw e.getCause();
        }

        verify(mock).setState(MockitoClassUtil.getMockState(mock, mockFactory));
        verify(reporter).cannotInjectDependency(field, mock, e);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verifyZeroInteractions(mock1);
        verifyZeroInteractions(mock2);
        verifyZeroInteractions(reporter);

        assertNull(result.thenInject());
    }
}