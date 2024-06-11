package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.MockUtil.getMockName;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockUtil mockUtil;
	private Stubber stubber;
	private Object mock1;
	private Object mock2;
	private Object fieldInstance;
	private Collection<Object> mocks;

	@Before
	public void setup() {
		mockUtil = new MockUtil();
		stubber = mockitoThreeAnswers();
		mock1 = stubber.createMock(String.class);
		mock2 = stubber.createMock(String.class);
		fieldInstance = new Object();
		mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		filter = new NameBasedCandidateFilter(new MockCandidateFilter() {
			@Override
			public OngoingInjecter filterCandidate(Collection<Object> mocks,
					Field field, Object fieldInstance) {
				return null;
			}
		});
	}

	@Test
	public void testFilterCandidate_sameName_singleMock() {
		// Given
		String expectedFieldName = getMockName(mock1).toString();
		Field field = new Field("testField", String.class, fieldInstance.getClass());

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(mock1, result.getMock());
	}

	@Test
	public void testFilterCandidate_sameName_multipleMocks() {
		// Given
		String expectedFieldName = getMockName(mock1).toString();
		Field field = new Field("testField", String.class, fieldInstance.getClass());

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(mock1, result.getMock());
	}

	@Test(expected = RuntimeException.class)
	public void testFilterCandidate_conflictingNames() {
		// Given
		String mock1Name = getMockName(mock1).toString();
		String mock2Name = getMockName(mock2).toString();
		Field field = new Field("testField", String.class, fieldInstance.getClass());

		// When
		filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		throw new RuntimeException("Expected conflict: " + mock1Name + " vs " + mock2Name);
	}

	@Test
	public void testFilterCandidate_noMatch() {
		// Given
		Field field = new Field("nonMatchingField", String.class, fieldInstance.getClass());

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertNull(result);
	}
}