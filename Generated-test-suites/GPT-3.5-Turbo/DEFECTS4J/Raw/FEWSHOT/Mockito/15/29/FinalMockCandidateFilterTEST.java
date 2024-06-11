package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
  
    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }
  
    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);
      
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
      
        assertEquals(ongoingInjecter.getClass(), OngoingInjecter.class);
        assertTrue(ongoingInjecter.thenInject());
        verify(mockitoException.getName()).equals("Problems injecting dependency in " + field.getName());
    }
  
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
      
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
      
        assertEquals(ongoingInjecter.getClass(), OngoingInjecter.class);
        assertFalse(ongoingInjecter.thenInject());
    }
  
    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() {
        Object mock = mock(Object.class);
        mocks.add(mock);
        doThrow(MockitoException.class).when(fieldInstance).set(any());
      
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
      
        ongoingInjecter.thenInject();
    }
}