package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    
    @Test
    public void testFilterCandidate_withOneMock_shouldInjectMock() throws Exception {
        // Arrange
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new SomeClass();
        
        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(ongoingInjecter).thenInject();
        verify(new FieldSetter(fieldInstance, field)).set(mocks.iterator().next());
    }
    
    @Test
    public void testFilterCandidate_withMultipleMocks_shouldNotInjectMock() throws Exception {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new SomeClass();
        
        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        assertFalse(ongoingInjecter.thenInject());
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withException_shouldThrowMockitoException() throws Exception {
        // Arrange
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new SomeClass();
        FieldSetter fieldSetter = mock(FieldSetter.class);
        doThrow(Exception.class).when(fieldSetter).set(mocks.iterator().next());
        
        // Act
        filter.filterCandidate(mocks, field, fieldInstance);
    }
    
    private class SomeClass {
        private Object someField;
    }
}