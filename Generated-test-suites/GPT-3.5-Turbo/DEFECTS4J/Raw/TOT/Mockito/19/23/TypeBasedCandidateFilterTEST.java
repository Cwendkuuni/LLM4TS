import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter typeBasedCandidateFilter;
    private MockCandidateFilter mockCandidateFilter;
    private List<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };

        typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        mocks = new ArrayList<>();
        field = getField();
        fieldInstance = new Object();
    }

    @Test
    public void filterCandidate_WhenMockMatchesFieldType_ReturnsOngoingInjecter() {
        // Arrange
        Object mock = new FooMock();
        mocks.add(mock);
        
        // Act
        TypeBasedCandidateFilter.OngoingInjecter ongoingInjecter = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        assertEquals(mockCandidateFilter, ongoingInjecter.getMockCandidateFilter());
        assertTrue(ongoingInjecter.getMockCandidates().contains(mock));
        assertEquals(field, ongoingInjecter.getField());
        assertEquals(fieldInstance, ongoingInjecter.getFieldInstance());
    }

    @Test
    public void filterCandidate_WhenMockDoesNotMatchFieldType_ReturnsOngoingInjecterWithoutMatchingMocks() {
        // Arrange
        Object mock = new BarMock();
        mocks.add(mock);

        // Act
        TypeBasedCandidateFilter.OngoingInjecter ongoingInjecter = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(mockCandidateFilter, ongoingInjecter.getMockCandidateFilter());
        assertTrue(ongoingInjecter.getMockCandidates().isEmpty());
        assertEquals(field, ongoingInjecter.getField());
        assertEquals(fieldInstance, ongoingInjecter.getFieldInstance());
    }
    
    private Field getField() {
        try {
            return TypeBasedCandidateFilter.class.getDeclaredField("next");
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
    
    private static class FooMock {
    }
    
    private static class BarMock {
    }

}