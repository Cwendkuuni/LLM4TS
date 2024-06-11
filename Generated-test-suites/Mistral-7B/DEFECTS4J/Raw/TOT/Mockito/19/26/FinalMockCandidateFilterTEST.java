package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.RepresentationError;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.internal.util.reflection.ProxyFactory;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Object mock3;
    private Field field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mock3 = mock(Object.class);
        field = MockUtil.getField(FinalMockCandidateFilter.class, "fieldInstance");
        try {
            FieldSetter.setField(field, filter, fieldInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSingleMock() {
        List<Object> mocks = Arrays.asList(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        assertEquals(mock1, result);
    }

    @Test
    public void testMultipleMocks() {
        List<Object> mocks = Arrays.asList(mock1, mock2, mock3);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
    }

    @Test(expected = RepresentationError.class)
    public void testThrowException() {
        List<Object> mocks = Arrays.asList(mock1);

        final Object exceptionMock = mock(Exception.class);

        Field field = MockUtil.getField(Exception.class, "message");
        try {
            FieldSetter.setField(field, exceptionMock, "Test Exception");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        ProxyFactory.setField(field, filter, exceptionMock);

        filter.filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
    }
}