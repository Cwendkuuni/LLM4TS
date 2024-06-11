import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

  @Test
  public void testFilterCandidate_TypeMatches_ReturnsNextFilter() {
    // Arrange
    MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
    TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);
    List<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    mocks.add(mock1);
    Object mock2 = mock(Object.class);
    mocks.add(mock2);
    Field field = getField("fieldInstance", TypeBasedCandidateFilter.class);
    Object fieldInstance = new Object();

    // Act
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Assert
    verify(nextFilter).filterCandidate(argThat(c -> c.contains(mock1) && c.contains(mock2)), field, fieldInstance);
    assertEquals(nextFilter, result);
  }

  @Test
  public void testFilterCandidate_TypeDoesNotMatch_ReturnsNull() {
    // Arrange
    MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
    TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);
    List<Object> mocks = new ArrayList<>();
    Object mock = mock(String.class);
    mocks.add(mock);
    Field field = getField("fieldInstance", TypeBasedCandidateFilter.class);
    Object fieldInstance = new Object();

    // Act
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Assert
    verifyZeroInteractions(nextFilter);
    assertNull(result);
  }

  @Test
  public void testFilterCandidate_EmptyMocksCollection_ReturnsNull() {
    // Arrange
    MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
    TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);
    List<Object> mocks = new ArrayList<>();
    Field field = getField("fieldInstance", TypeBasedCandidateFilter.class);
    Object fieldInstance = new Object();

    // Act
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Assert
    verifyZeroInteractions(nextFilter);
    assertNull(result);
  }

  private Field getField(String fieldName, Class<?> clazz) {
    Field[] declaredFields = clazz.getDeclaredFields();
    for (Field field : declaredFields) {
      if (field.getName().equals(fieldName)) {
        field.setAccessible(true);
        return field;
      }
    }
    return null;
  }
}