package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"javax.management.*"})
public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(mockCandidateFilter).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                this.invocation = invocation;
                return null;
            }
        }).using(Mockito.eq(MockCandidateFilter.class), Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));

        Mockito.verify(mockCandidateFilter).filterCandidate(Mockito.isNull(), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        verify(mockCandidateFilter).filterCandidate(Mockito.isNull(), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        assertEquals(null, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(mockCandidateFilter).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                this.invocation = invocation;
                return null;
            }
        }).using(Mockito.eq(MockCandidateFilter.class), Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));

        Mockito.verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        assertEquals(null, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterCandidateCalled() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);

        PowerMockito.mockStatic(Mockito.class);
        when(Mockito.mockStatic(Mockito.class).thenReturn(mock(MockingContext.class))).thenReturn(PowerMockito.mock(MockingContext.class));

        when(mockCandidateFilter.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(mock(OngoingInjecter.class));

        Object filterCandidate = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        assertEquals(mock(OngoingInjecter.class), filterCandidate);
    }

}