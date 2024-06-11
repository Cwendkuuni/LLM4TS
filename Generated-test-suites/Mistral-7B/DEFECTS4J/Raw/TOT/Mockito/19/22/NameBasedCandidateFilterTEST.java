import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        filter = new NameBasedCandidateFilter(null);
        mockUtil = MockitoAnnotations.spy(new MockUtil());
    }

    @Test
    public void testFilterCandidate_singleMockWithMatchingName() {
        // given
        Object mock = mock(Object.class);
        List<Object> mocks = Arrays.asList(mock);
        Field field = new Field("testField", Object.class, null);
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        verify(mockUtil).getMockName(mock);
    }

    @Test
    public void testFilterCandidate_singleMockWithMismatchingName() {
        // given
        Object mock = mock(Object.class);
        List<Object> mocks = Arrays.asList(mock);
        Field field = new Field("wrongName", Object.class, null);
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertNotNull(result.getNext());
        verify(mockUtil, Mockito.never()).getMockName(mock);
    }

    @Test
    public void testFilterCandidate_multipleMocksWithMatchingName() {
        // given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        List<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = new Field("testField", Object.class, null);
        Object fieldInstance = new Object();

        // when
        when(mockUtil.getMockName(mock1)).thenReturn("testName");
        when(mockUtil.getMockName(mock2)).thenReturn("testName");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(mock1, result.getMock());
        verify(mockUtil).getMockName(mock1);
    }

    @Test
    public void testFilterCandidate_multipleMocksWithDifferentNames() {
        // given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        List<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = new Field("testField", Object.class, null);
        Object fieldInstance = new Object();

        // when
        when(mockUtil.getMockName(mock1)).thenReturn("testName1");
        when(mockUtil.getMockName(mock2)).thenReturn("testName2");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertNotNull(result.getNext());
        verify(mockUtil).getMockName(mock2);
    }

    @Test
    public void testFilterCandidate_conflictingFields() {
        // given
        Object mock = mock(Object.class);
        List<Object> mocks = Arrays.asList(mock);
        Field field1 = new Field("testField1", Object.class, null);
        Field field2 = new Field("testField1", Object.class, null);
        Object fieldInstance = new Object();

        // when
        when(mockUtil.getMockName(mock)).thenReturn("testName");

        OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        verify(mockUtil).getMockName(mock);

        // when
        result = filter.filterCandidate(mocks, field2, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        verify(mockUtil).getMockName(mock);
    }

    @Test
    public void testFilterCandidate_emptyMockCollection() {
        // given
        List<Object> mocks = Collections.emptyList();
        Field field = new Field("testField", Object.class, null);
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNull(result);
    }
}