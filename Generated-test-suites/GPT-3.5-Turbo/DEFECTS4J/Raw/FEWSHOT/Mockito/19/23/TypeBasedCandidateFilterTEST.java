package org.mockito.internal.configuration.injection.filter;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {
  
  private TypeBasedCandidateFilter filter;
  private MockCandidateFilter nextMock;
  
  @Before
  public void setup() {
    nextMock = mock(MockCandidateFilter.class);
    filter = new TypeBasedCandidateFilter(nextMock);
  }
  
  @Test
  public void testFilterCandidate_whenNoMockMatchesWithType_thenShouldReturnOngoingInjecterWithEmptyMocks() {
    // Arrange
    Collection<Object> mocks = new ArrayList<Object>();
    Field field = getField();
    Object fieldInstance = new Object();
    
    // Act
    filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    verify(nextMock).filterCandidate(new ArrayList<Object>(), field, fieldInstance);
  }
  
  @Test
  public void testFilterCandidate_whenOneMockMatchesWithType_thenShouldReturnOngoingInjecterWithMatchingMock() {
    // Arrange
    Collection<Object> mocks = createMockCollection(1);
    Field field = getField();
    Object fieldInstance = new Object();
    
    // Act
    filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    verify(nextMock).filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance);
  }
  
  @Test
  public void testFilterCandidate_whenMultipleMocksMatchWithType_thenShouldReturnOngoingInjecterWithAllMatchingMocks() {
    // Arrange
    Collection<Object> mocks = createMockCollection(3);
    Field field = getField();
    Object fieldInstance = new Object();
    
    // Act
    filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    verify(nextMock).filterCandidate(mocks, field, fieldInstance);
  }
  
  // Helper method to create a mock collection with the given size
  private Collection<Object> createMockCollection(int size) {
    Collection<Object> mocks = new ArrayList<Object>();
    for (int i = 0; i < size; i++) {
      mocks.add(mock(Object.class));
    }
    return mocks;
  }
  
  // Helper method to get a sample Field object
  private Field getField() {
    try {
      return TypeBasedCandidateFilter.class.getDeclaredField("next");
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return null;
  }
}