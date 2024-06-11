package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class NameBasedCandidateFilterTest {
	
	@Test
	public void testFilterCandidate_singleMock() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);
		assertEquals(1, injecter.getMocks().size());
		assertEquals(mock, injecter.getMocks().get(0));
		assertEquals(field, injecter.getField());
		assertEquals(fieldInstance, injecter.getFieldInstance());
	}
	
	@Test
	public void testFilterCandidate_noMatches() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
		assertEquals(0, injecter.getMocks().size());
		assertEquals(field, injecter.getField());
		assertEquals(fieldInstance, injecter.getFieldInstance());
	}
	
	@Test
	public void testFilterCandidate_multipleMocks_sameName() {
		List<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);
		
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(2, injecter.getMocks().size());
		assertTrue(injecter.getMocks().contains(mock1));
		assertTrue(injecter.getMocks().contains(mock2));
		assertEquals(field, injecter.getField());
		assertEquals(fieldInstance, injecter.getFieldInstance());
	}
	
	@Test
	public void testFilterCandidate_multipleMocks_differentName() {
		List<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);
		
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, differentField, fieldInstance);
		assertEquals(0, injecter.getMocks().size());
		assertEquals(differentField, injecter.getField());
		assertEquals(fieldInstance, injecter.getFieldInstance());
	}
	
}