package org.mockito.internal.configuration.injection;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Before
    public void setup() {
        List<Object> mocks = Collections.singletonList(mock(Object.class));
        this.field = Mockito.spy(Mockito.mock(Field.class));
        this.fieldInstance = mock(Object.class);
        this.mock = mock(Object.class);

        filter = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);
    }

    private OngoingInjecter filter;
    private Object fieldInstance;
    private Field field;
    private Object mock;

    @Test
    public void testSingleMock() {
        filter.thenInject();
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        assertEquals(mock, fieldInstance);
    }

    @Test
    public void testExceptionThrownWhenInjectingSingleMock() {
        Mockito.doThrow(new RuntimeException("Test exception")).when(fieldSetter).set(Mockito.any());

        filter.thenInject();
        verify(filter).thenInject();
        verify(fieldSetter).set(Mockito.any());
        verify(fieldSetter, times(0)).set(eq(mock));
    }

    @Test
    public void testNoMock() {
        filter.thenInject();
        // No-op
        verify(filter, times(1)).thenInject();
    }

    @Test
    public void testMultipleMocks() {
        List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        filter = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);
        assertFalse(filter.thenInject());
        verify(filter, times(1)).thenInject();
    }

    @Test(expected = MockitoException.class)
    public void testExceptionThrownWhenMultipleMocks() {
        when(field.getType()).thenReturn(mock(Exception.class));
        filter = new FinalMockCandidateFilter().filterCandidate(Collections.singletonList(mock(Object.class)), field, fieldInstance);
        filter.thenInject();
        verify(filter, times(1)).thenInject();
    }
}