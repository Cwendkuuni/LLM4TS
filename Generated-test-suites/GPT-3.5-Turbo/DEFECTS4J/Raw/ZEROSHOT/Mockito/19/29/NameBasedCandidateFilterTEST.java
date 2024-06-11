import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {
	
	@Test
	public void testFilterCandidate() {
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		List<Object> mocks = new ArrayList<Object>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
		
		filter.filterCandidate(mocks, field, fieldInstance);
		
		verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
	}
	
	@Test
	public void testFilterCandidateWithNameMatch() {
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		List<Object> mocks = new ArrayList<Object>();
		Object mock = mock(Object.class);
		mocks.add(mock);
		Field field = mock(Field.class);
		when(field.getName()).thenReturn("mock");
		Object fieldInstance = mock(Object.class);
		
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertNotEquals(null, result);
		verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
	}
	
	@Test
	public void testFilterCandidateWithMultipleMocksAndMatch() {
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		List<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = mock(Field.class);
		when(field.getName()).thenReturn("mock");
		Object fieldInstance = mock(Object.class);
		
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertNotEquals(null, result);
		verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
	}
	
	@Test
	public void testFilterCandidateWithMultipleMocksAndNoMatch() {
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		List<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = mock(Field.class);
		when(field.getName()).thenReturn("test");
		Object fieldInstance = mock(Object.class);
		
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertNotEquals(null, result);
		verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
	}
	
	@Test
	public void testFilterCandidateWithConflictingNaming() {
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		List<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = mock(Field.class);
		when(field.getName()).thenReturn("mock1");
		Object fieldInstance = mock(Object.class);
		
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertNotEquals(null, result);
		verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
	}
}