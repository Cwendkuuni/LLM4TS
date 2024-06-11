import org.junit.*;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

  private NameBasedCandidateFilter filter;
  private MockCandidateFilter next;
  private MockUtil mockUtil;

  @Before
  public void setUp() {
    next = mock(MockCandidateFilter.class);
    filter = new NameBasedCandidateFilter(next);
    mockUtil = new MockUtil();
  }

  @Test
  public void testFilterCandidate_withMatchingFieldNames() {
    // Setup
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    Object mock3 = mock(Object.class);
    Object mock4 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    mocks.add(mock3);
    mocks.add(mock4);
    Field field = mock(Field.class);
    Object fieldInstance = new Object();

    // Stubbing
    when(field.getName()).thenReturn("mock1");

    // Execution
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Verification
    verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    List<Object> expectedMatches = new ArrayList<>();
    expectedMatches.add(mock1);
    assertEquals(expectedMatches, result.getMocks());
  }

  @Test
  public void testFilterCandidate_withNoMatchingFieldNames() {
    // Setup
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    Object mock3 = mock(Object.class);
    Object mock4 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    mocks.add(mock3);
    mocks.add(mock4);
    Field field = mock(Field.class);
    Object fieldInstance = new Object();

    // Stubbing
    when(field.getName()).thenReturn("mock5");

    // Execution
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Verification
    verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    assertEquals(mocks, result.getMocks());
  }

  @Test
  public void testFilterCandidate_withMultipleMatchingFieldNames() {
    // Setup
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    Object mock3 = mock(Object.class);
    Object mock4 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    mocks.add(mock3);
    mocks.add(mock4);
    Field field = mock(Field.class);
    Object fieldInstance = new Object();

    // Stubbing
    when(field.getName()).thenReturn("mock2");

    // Execution
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Verification
    verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    List<Object> expectedMatches = new ArrayList<>();
    expectedMatches.add(mock2);
    assertEquals(expectedMatches, result.getMocks());
  }

  @Test
  public void testFilterCandidate_withEmptyMocks() {
    // Setup
    Collection<Object> mocks = new ArrayList<>();
    Field field = mock(Field.class);
    Object fieldInstance = new Object();

    // Execution
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Verification
    verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    assertEquals(mocks, result.getMocks());
  }

  @Test
  public void testFilterCandidate_withNullField() {
    // Setup
    Collection<Object> mocks = new ArrayList<>();
    Field field = null;
    Object fieldInstance = new Object();

    // Execution
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Verification
    verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    assertEquals(mocks, result.getMocks());
  }

  @Test
  public void testFilterCandidate_withNullFieldInstance() {
    // Setup
    Collection<Object> mocks = new ArrayList<>();
    Field field = mock(Field.class);
    Object fieldInstance = null;

    // Execution
    OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

    // Verification
    verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    assertEquals(mocks, result.getMocks());
  }
}