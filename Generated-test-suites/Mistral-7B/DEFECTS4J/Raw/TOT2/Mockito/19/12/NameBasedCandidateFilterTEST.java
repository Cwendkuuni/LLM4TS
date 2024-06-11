import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NameBasedCandidateFilterTest {
    NameBasedCandidateFilter filter;
    MockCandidateFilter nextMock;
    MockUtil mockUtil;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextMock);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithSameName_ReturnsCorrectMock() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = Mockito.field(SomeClass.class, "fieldName");
        Object fieldInstance = new Object();

        String mockName1 = mockUtil.getMockName(mock1);
        String mockName2 = mockUtil.getMockName(mock2);

        assertEquals(mock1, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NoConflictingNames_ReturnsNextFilter() {
        Object mock = Mockito.mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock);
        Field field = Mockito.field(SomeClass.class, "fieldName");
        Object fieldInstance = new Object();

        Mockito.when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.anyObject())).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return Mockito.mock(OngoingInjecter.class);
            }
        });

        assertEquals(Mockito.mock(OngoingInjecter.class), filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFields() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.field(SomeClass.class, "fieldName");
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_EmptyMocksCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.field(SomeClass.class, "fieldName");
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithSameNameDifferentTypes() {
        Object mock1 = Mockito.mock(Object1.class);
        Object mock2 = Mockito.mock(Object2.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = Mockito.field(SomeClass.class, "fieldName");
        Object fieldInstance = new Object();

        String mockName1 = mockUtil.getMockName(mock1);
        String mockName2 = mockUtil.getMockName(mock2);

        assertEquals(mock1, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_TwoFieldsWithSameNameDifferentTypesAndDifferentMocks() {
        Object mock1 = Mockito.mock(Object1.class);
        Object mock2 = Mockito.mock(Object2.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field1 = Mockito.field(SomeClass.class, "fieldName1");
        Field field2 = Mockito.field(SomeClass.class, "fieldName1");
        Object fieldInstance = new Object();

        String mockName1 = mockUtil.getMockName(mock1);
        String mockName2 = mockUtil.getMockName(mock2);

        assertEquals(mock1, filter.filterCandidate(mocks, field1, fieldInstance));
    }
}