import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        String mockName1 = "mock1";
        String mockName2 = "mock2";
        String fieldName = "fieldName";
        Object fieldInstance = new Object();

        Object mock1 = mock(Object.class, Mockito.withMockName(mockName1));
        Object mock2 = mock(Object.class, Mockito.withMockName(mockName2));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.spy(Field.class);
        field.setName(fieldName);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock1, result); // since mock1's name matches the field name
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        String mockName = "mockName";
        String fieldName = "fieldName";
        Object fieldInstance = new Object();

        Object mock = mock(Object.class, Mockito.withMockName(mockName));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = Mockito.spy(Field.class);
        field.setName(fieldName);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result); // since mock's name matches the field name
    }
}