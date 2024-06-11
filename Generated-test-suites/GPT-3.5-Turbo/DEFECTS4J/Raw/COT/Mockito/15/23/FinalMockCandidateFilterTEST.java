
import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

  @Test
  public void testFilterCandidate_whenSizeIsOne_thenInject() {
    // Given
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    mocks.add(new Object());
    Field field = null;
    Object fieldInstance = new Object();

    // When
    FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    Assert.assertTrue(ongoingInjecter.thenInject());
  }

  @Test
  public void testFilterCandidate_whenSizeIsNotOne_thenDoNotInject() {
    // Given
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    mocks.add(new Object());
    mocks.add(new Object());
    Field field = null;
    Object fieldInstance = new Object();

    // When
    FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    Assert.assertFalse(ongoingInjecter.thenInject());
  }

  @Test(expected = MockitoException.class)
  public void testFilterCandidate_whenExceptionThrown_thenPropagateException() {
    // Given
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    mocks.add(new Object());
    Field field = null;
    Object fieldInstance = new Object();

    // When
    FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
    ongoingInjecter.thenInject();

    // Then
    // MockitoException is expected to be thrown
  }
}
