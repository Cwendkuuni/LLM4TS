import org.junit.Before;
import org.junit.Test;
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
		mockUtil = mock(MockUtil.class);
		filter = new NameBasedCandidateFilter(next, mockUtil);
	}

	@Test
	public void filterCandidate_WithMatchingFieldName_ReturnsFilteredCandidates() {
		// Arrange
		List<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		Object mock3 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);
		Field field = SomeClass.class.getDeclaredField("fieldName");
		Object fieldInstance = new SomeClass();

		when(mockUtil.getMockName(mock1)).thenReturn("fieldName");
		when(mockUtil.getMockName(mock2)).thenReturn("fieldName");
		when(mockUtil.getMockName(mock3)).thenReturn("wrongName");

		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Assert
		verify(next).filterCandidate(anyList(), eq(field), eq(fieldInstance));
		assertEquals(2, ((List<Object>) result).size());
		assertTrue(((List<Object>) result).contains(mock1));
		assertTrue(((List<Object>) result).contains(mock2));
		assertFalse(((List<Object>) result).contains(mock3));
	}

	@Test
	public void filterCandidate_WithNoMatchingFieldName_ReturnsOriginalCandidates() {
		// Arrange
		List<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = SomeClass.class.getDeclaredField("fieldName");
		Object fieldInstance = new SomeClass();

		when(mockUtil.getMockName(mock1)).thenReturn("wrongName");
		when(mockUtil.getMockName(mock2)).thenReturn("wrongName");

		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Assert
		verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
		assertEquals(2, ((List<Object>) result).size());
		assertTrue(((List<Object>) result).contains(mock1));
		assertTrue(((List<Object>) result).contains(mock2));
	}

	@Test
	public void filterCandidate_WithMultipleCandidatesAndMatchingFieldName_ReturnsFilteredCandidates() {
		// Arrange
		List<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		Object mock3 = mock(Object.class);
		Object mock4 = mock(Object.class);
		Object mock5 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);
		mocks.add(mock4);
		mocks.add(mock5);
		Field field = SomeClass.class.getDeclaredField("fieldName");
		Object fieldInstance = new SomeClass();

		when(mockUtil.getMockName(mock1)).thenReturn("wrongName");
		when(mockUtil.getMockName(mock2)).thenReturn("wrongName");
		when(mockUtil.getMockName(mock3)).thenReturn("fieldName");
		when(mockUtil.getMockName(mock4)).thenReturn("wrongName");
		when(mockUtil.getMockName(mock5)).thenReturn("fieldName");

		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Assert
		verify(next).filterCandidate(anyList(), eq(field), eq(fieldInstance));
		assertEquals(2, ((List<Object>) result).size());
		assertTrue(((List<Object>) result).contains(mock3));
		assertTrue(((List<Object>) result).contains(mock5));
		assertFalse(((List<Object>) result).contains(mock1));
		assertFalse(((List<Object>) result).contains(mock2));
		assertFalse(((List<Object>) result).contains(mock4));
	}
}