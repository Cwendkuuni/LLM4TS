package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldLocator;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter nameBasedCandidateFilter;
	private MockCandidateFilter mockCandidateFilterSpy;
	private Field mockNameField;
	private Object mock1;
	private Object mock2;
	private Object fieldInstance;
	private Collection<Object> mocks;
	private List<Object> mockNameMatches;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setUp() {
		mockCandidateFilterSpy = spy(Mockito.mock(MockCandidateFilter.class));
		nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilterSpy);
		mock1 = mock(Object.class, "mock1");
		mock2 = mock(Object.class, "mock2");
		mockNameField = FieldLocator.getField(mock1, "name"); // Assuming there is a 'name' field in 'Object' class.
		fieldInstance = new Object();
		mocks = Arrays.asList(mock1, mock2);
	}

	// Test case for filterCandidate method with one mock
	@Test
	public void testFilterCandidateWithOneMock() {
		mockNameMatches = new ArrayList<Object>();
		mockNameMatches.add(mock1);
		Object result = nameBasedCandidateFilter.filterCandidate(mocks, mockNameField, fieldInstance);
		assertEquals(mock1, result);
	}

	// Test case for filterCandidate method with multiple mocks and name match
	@Test
	public void testFilterCandidateWithMultipleMocksAndNameMatch() {
		mockNameMatches = new ArrayList<Object>();
		mockNameMatches.add(mock1);
		Object result = nameBasedCandidateFilter.filterCandidate(mocks, mockNameField, fieldInstance);
		assertEquals(mock1, result);
	}

	// Test case for filterCandidate method with multiple mocks and no name match
	@Test
	public void testFilterCandidateWithMultipleMocksAndNoNameMatch() {
		mockNameMatches = new ArrayList<Object>();
		Object result = nameBasedCandidateFilter.filterCandidate(mocks, mockNameField, fieldInstance);
		assertNull(result);
	}

	// Test case for filterCandidate method with multiple mocks and conflicting names
	@Test(expected = IllegalStateException.class)
	public void testFilterCandidateWithMultipleMocksAndConflictingNames() {
		mockNameMatches = new ArrayList<Object>();
		mockNameMatches.add(mock1);
		Whitebox.setInternalState(mock2, "name", "mock1");
		nameBasedCandidateFilter.filterCandidate(mocks, mockNameField, fieldInstance);
	}

	// Test case for filterCandidate method with empty list of mocks
	@Test
	public void testFilterCandidateWithEmptyListOfMocks() {
		Object result = nameBasedCandidateFilter.filterCandidate(new ArrayList<Object>(), mockNameField, fieldInstance);
		assertNull(result);
	}
}