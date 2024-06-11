import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new MockCandidateFilterImpl();
    }

    @Test
    public void shouldFilterCandidateWhenMocksNotEmptyAndFieldToBeInjectedNonNull() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = getField("testField");
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertEquals(mocks, ongoingInjecter.getMocks());
        assertEquals(fieldToBeInjected, ongoingInjecter.getFieldToBeInjected());
        assertEquals(fieldInstance, ongoingInjecter.getFieldInstance());
    }

    @Test
    public void shouldFilterCandidateWhenMocksNotEmptyAndFieldToBeInjectedNull() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertEquals(mocks, ongoingInjecter.getMocks());
        assertEquals(fieldToBeInjected, ongoingInjecter.getFieldToBeInjected());
        assertEquals(fieldInstance, ongoingInjecter.getFieldInstance());
    }
    
    @Test
    public void shouldFilterCandidateWhenMocksEmptyAndFieldToBeInjectedNonNull() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = getField("testField");
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertEquals(mocks, ongoingInjecter.getMocks());
        assertEquals(fieldToBeInjected, ongoingInjecter.getFieldToBeInjected());
        assertEquals(fieldInstance, ongoingInjecter.getFieldInstance());
    }
    
    @Test
    public void shouldFilterCandidateWhenMocksEmptyAndFieldToBeInjectedNull() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertEquals(mocks, ongoingInjecter.getMocks());
        assertEquals(fieldToBeInjected, ongoingInjecter.getFieldToBeInjected());
        assertEquals(fieldInstance, ongoingInjecter.getFieldInstance());
    }

    // Helper method to create a Field object
    private Field getField(String fieldName) {
        try {
            return MockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + fieldName, e);
        }
    }
}