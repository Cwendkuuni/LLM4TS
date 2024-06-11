import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.reflect.Field;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidate_withEmptyMocksList() {
		List<Object> emptyMocks = new ArrayList<Object>();
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);

		OngoingInjecter result = filter.filterCandidate(emptyMocks, null, null);

		assertNull(result);
	}

	@Test
	public void testFilterCandidate_withOneMockAndDifferentFieldName() {
		Object mock = new Object();
		Collection<Object> mocks = Collections.singletonList(mock);
		Field field = getField("field", Object.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, null);

		assertNull(result);
	}

	@Test
	public void testFilterCandidate_withOneMockAndMatchingFieldName() {
		Object mock = new Object();
		Collection<Object> mocks = Collections.singletonList(mock);
		Field field = getField("field", Object.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, null);

		assertNotNull(result);
		assertSame(mock, result.get(field));
	}

	@Test
	public void testFilterCandidate_withMultipleMocksAndNoMatchingFieldName() {
		Object mock1 = new Object();
		Object mock2 = new Object();
		Collection<Object> mocks = new ArrayList<Object>();
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = getField("field", Object.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, null);

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testFilterCandidate_withMultipleMocksAndMatchingFieldName() {
		Object mock1 = new Object();
		Object mock2 = new Object();
		Collection<Object> mocks = new ArrayList<Object>();
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = getField("field", Object.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, null);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue(result.contains(mock1));
		assertTrue(result.contains(mock2));
	}

	private Field getField(String fieldName, Class<?> fieldType) {
		try {
			return getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			fail("Field not found: " + fieldName);
			return null;
		}
	}
}