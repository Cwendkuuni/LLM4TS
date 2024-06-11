import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

public class NameBasedCandidateFilterTest {

	private MockCandidateFilter next;
	private NameBasedCandidateFilter filter;
	private MockUtil mockUtil;
	
	@Before
	public void setUp() {
		next = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(next);
		mockUtil = mock(MockUtil.class);
	}
	
	@Test
	public void testFilterCandidate_NoMocks() {
		Collection<Object> mocks = new ArrayList<Object>();
		Field field = createMockField("fieldName");
		Object fieldInstance = new Object();
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertEquals(next, result);
		verify(next).filterCandidate(mocks, field, fieldInstance);
	}
	
	@Test
	public void testFilterCandidate_NoMockNameMatches() {
		Collection<Object> mocks = createMockCollection(2);
		Field field = createMockField("fieldName");
		Object fieldInstance = new Object();
		mockGetMockName(null);
		mockGetMockName("otherName");
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertEquals(next, result);
		verify(next).filterCandidate(mocks, field, fieldInstance);
		verify(mockUtil, times(2)).getMockName(anyObject());
	}
	
	@Test
	public void testFilterCandidate_MockNameMatches() {
		Collection<Object> mocks = createMockCollection(2);
		Field field = createMockField("fieldName");
		Object fieldInstance = new Object();
		mockGetMockName("fieldName");
		mockGetMockName("otherName");
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertFilteredCandidate(result, field, fieldInstance);
		verify(mockUtil, times(2)).getMockName(anyObject());
	}
	
	@Test
	public void testFilterCandidate_MultipleMockNameMatches() {
		Collection<Object> mocks = createMockCollection(3);
		Field field = createMockField("fieldName");
		Object fieldInstance = new Object();
		mockGetMockName("fieldName");
		mockGetMockName("otherName");
		mockGetMockName("fieldName");
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertFilteredCandidate(result, field, fieldInstance);
		verify(mockUtil, times(3)).getMockName(anyObject());
	}
	
	@Test
	public void testFilterCandidate_ConflictingMockNameMatches() {
		Collection<Object> mocks = createMockCollection(3);
		Field field = createMockField("fieldName");
		Object fieldInstance = new Object();
		mockGetMockName("otherName");
		mockGetMockName("fieldName");
		mockGetMockName("fieldName");
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertFilteredCandidate(result, field, fieldInstance);
		verify(mockUtil, times(3)).getMockName(anyObject());
	}
	
	private Collection<Object> createMockCollection(int size) {
		Collection<Object> mocks = new ArrayList<Object>();
		for (int i = 0; i < size; i++) {
			mocks.add(createMockObject());
		}
		return mocks;
	}
	
	private Field createMockField(String fieldName) {
		Field field = mock(Field.class);
		when(field.getName()).thenReturn(fieldName);
		return field;
	}
	
	private Object createMockObject() {
		return new Object();
	}
	
	private void mockGetMockName(String mockName) {
		when(mockUtil.getMockName(anyObject())).thenReturn(mockName);
	}
	
	private void assertFilteredCandidate(OngoingInjecter result, Field field, Object fieldInstance) {
		assertTrue(result instanceof OngoingInjecter);
		verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
	}

}