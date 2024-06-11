import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

  private MockCandidateFilter next;
  private NameBasedCandidateFilter filter;
  private MockUtil mockUtil;

  @Before
  public void setUp() {
    next = mock(MockCandidateFilter.class);
    filter = new NameBasedCandidateFilter(next);
    mockUtil = new MockUtil();
  }

  @Test
  public void testFilterCandidate_withMatchingNames_shouldReturnFilteredCandidates() throws Exception {
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    Object mock3 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    mocks.add(mock3);

    Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
    field.setAccessible(true);
    Object fieldInstance = field.get(filter);

    List<Object> filteredMocks = new ArrayList<>();
    filteredMocks.add(mock1);
    filteredMocks.add(mock2);

    when(mockUtil.getMockName(mock1)).thenReturn("mock1");
    when(mockUtil.getMockName(mock2)).thenReturn("mock2");
    when(mockUtil.getMockName(mock3)).thenReturn("mock3");
    when(next.filterCandidate(filteredMocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    verify(next).filterCandidate(filteredMocks, field, fieldInstance);
    assertSame(result, next.filterCandidate(filteredMocks, field, fieldInstance));
  }

  @Test
  public void testFilterCandidate_withNoMatchingNames_shouldReturnUnfilteredCandidates() throws Exception {
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    Object mock3 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    mocks.add(mock3);

    Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
    field.setAccessible(true);
    Object fieldInstance = field.get(filter);

    when(mockUtil.getMockName(mock1)).thenReturn("mock1");
    when(mockUtil.getMockName(mock2)).thenReturn("mock2");
    when(mockUtil.getMockName(mock3)).thenReturn("mock3");
    when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    verify(next).filterCandidate(mocks, field, fieldInstance);
    assertSame(result, next.filterCandidate(mocks, field, fieldInstance));
  }

  @Test
  public void testFilterCandidate_withSingleMock_shouldReturnUnfilteredCandidates() throws Exception {
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    mocks.add(mock1);

    Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
    field.setAccessible(true);
    Object fieldInstance = field.get(filter);

    when(mockUtil.getMockName(mock1)).thenReturn("mock1");
    when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    verify(next).filterCandidate(mocks, field, fieldInstance);
    assertSame(result, next.filterCandidate(mocks, field, fieldInstance));
  }
}