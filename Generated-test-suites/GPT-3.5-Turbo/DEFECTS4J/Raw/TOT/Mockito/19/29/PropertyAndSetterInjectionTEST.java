import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    @Test
    public void processInjection_ShouldInjectMocks_WhenSetterAndFieldInjectionAvailable() {
        // Arrange
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        Field injectMocksField = null;
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        boolean expected = true;

        when(mockFilter.filterCandidate(any(), any(), any())).thenReturn(mock(Object.class));
        strategy.setMockCandidateFilter(mockFilter);

        // Act
        boolean actual = strategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertEquals(expected, actual);
        verify(mockFilter, times(2)).filterCandidate(any(), any(), any());
    }

    @Test
    public void processInjection_ShouldNotInjectMocks_WhenSetterAndFieldInjectionNotAvailable() {
        // Arrange
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        Field injectMocksField = null;
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        boolean expected = false;

        when(mockFilter.filterCandidate(any(), any(), any())).thenReturn(null);
        strategy.setMockCandidateFilter(mockFilter);

        // Act
        boolean actual = strategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertEquals(expected, actual);
        verify(mockFilter, times(2)).filterCandidate(any(), any(), any());
    }

    @Test
    public void initializeInjectMocksField_ShouldThrowException_WhenFieldInitializationFails() {
        // Arrange
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        FieldInjectMocksStub injectMocksField = new FieldInjectMocksStub();
        Object injectMocksFieldOwner = null;

        // Act
        try {
            strategy.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
            fail("MockitoException should have been thrown");
        } catch (MockitoException e) {
            // Assert
            assertTrue(true);
        }
    }

    @Test
    public void injectMockCandidates_ShouldInjectMocks_WhenMockCandidatesAvailable() {
        // Arrange
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        FieldInjectMocksStub injectMocksField = new FieldInjectMocksStub();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Object());
        Object instance = new Object();
        boolean expected = true;

        // Act
        boolean actual = strategy.injectMockCandidates(injectMocksField.getClass(), mockCandidates, instance);

        // Assert
        assertEquals(expected, actual);
        assertEquals(0, mockCandidates.size());
    }

    @Test
    public void injectMockCandidates_ShouldNotInjectMocks_WhenMockCandidatesNotAvailable() {
        // Arrange
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        FieldInjectMocksStub injectMocksField = new FieldInjectMocksStub();
        Set<Object> mockCandidates = new HashSet<>();
        Object instance = new Object();
        boolean expected = false;

        // Act
        boolean actual = strategy.injectMockCandidates(injectMocksField.getClass(), mockCandidates, instance);

        // Assert
        assertEquals(expected, actual);
        assertEquals(0, mockCandidates.size());
    }

    @Test
    public void injectMockCandidatesOnFields_ShouldInjectMocks_WhenMockCandidatesAvailable() throws NoSuchFieldException {
        // Arrange
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        Set<Object> mocks = new HashSet<>();
        Object mock = mock(Object.class);
        Field field = FieldInjectMocksStub.class.getDeclaredField("field1");
        Object instance = new FieldInjectMocksStub();
        boolean injectionOccurred = false;

        mocks.add(mock);

        when(mockFilter.filterCandidate(any(), any(), any())).thenReturn(mock);
        strategy.setMockCandidateFilter(mockFilter);

        // Act
        boolean actual = strategy.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, new ArrayList<Field>() {{
            add(field);
        }});

        // Assert
        assertTrue(actual);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void injectMockCandidatesOnFields_ShouldNotInjectMocks_WhenMockCandidatesNotAvailable() throws NoSuchFieldException {
        // Arrange
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        Set<Object> mocks = new HashSet<>();
        Field field = FieldInjectMocksStub.class.getDeclaredField("field1");
        Object instance = new FieldInjectMocksStub();
        boolean injectionOccurred = false;

        when(mockFilter.filterCandidate(any(), any(), any())).thenReturn(null);
        strategy.setMockCandidateFilter(mockFilter);

        // Act
        boolean actual = strategy.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, new ArrayList<Field>() {{
            add(field);
        }});

        // Assert
        assertFalse(actual);
        assertTrue(mocks.isEmpty());
    }

    private class FieldInjectMocksStub {
        private Object field1;
        private Object field2;
    }

}