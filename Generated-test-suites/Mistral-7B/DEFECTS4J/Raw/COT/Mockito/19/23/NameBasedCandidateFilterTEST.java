import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.apis.annotation.ParamType;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter nextSpy;
    private NameBasedCandidateFilter filter;
    private Object fieldInstance;
    private List<Object> mockList;
    private Collection<Object> mocks;
    private Field field;

    @Before
    public void setup() throws Exception {
        nextSpy = spy(MockitoMockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextSpy);
        fieldInstance = mock(Object.class);
        mockList = new ArrayList<>();
        mockList.add(mock(Object.class));
        mockList.add(mock(Object.class));
        mocks = mock(Collection.class);
        when(mocks.size()).thenReturn(2);
        when(mocks.toArray()).thenReturn(mockList.toArray(new Object[0]));
        field = Whitebox.getInternalState(filter, "next");
        Whitebox.setInternalState(filter, "next", nextSpy);
        mockStatic(MockUtil.class);
        when(MockUtil.getMockName(mockList.get(0))).thenReturn("testMock1");
        when(MockUtil.getMockName(mockList.get(1))).thenReturn("testMock2");
        whenNew(NameBasedCandidateFilter.class, new Class[]{MockCandidateFilter.class}).withArguments(nextSpy).thenReturn(filter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_MatchingName() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testFilterCandidate_MultipleMocks_NoMatchingName() {
        when(MockUtil.getMockName(mockList.get(0))).thenReturn("testMock3");
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testFilterCandidate_SingleMock_NoMatchingName() {
        Collection<Object> singleMocks = mock(Collection.class);
        when(singleMocks.size()).thenReturn(1);
        OngoingInjecter result = filter.filterCandidate(singleMocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMock_MatchingName() {
        Collection<Object> singleMocks = mock(Collection.class);
        when(singleMocks.size()).thenReturn(1);
        when(MockUtil.getMockName(singleMocks.get(0))).thenReturn("testMock1");
        OngoingInjecter result = filter.filterCandidate(singleMocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullMocks() {
        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullField() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullFieldInstance() {
        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }
}

class MockitoMockCandidateFilter implements MockCandidateFilter {
    // Empty implementation
}