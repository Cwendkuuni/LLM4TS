package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private static final Class<Object> TEST_CLASS = Object.class;
    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMockType_ThenMockIsReturned() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(TEST_CLASS);
        Object mock2 = mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.spy(new FieldStub());
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mock1, result.getMock());
        verifyZeroInteractions(mock2);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsNotAssignableFromMockType_ThenNextFilterIsCalled() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(TEST_CLASS);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.spy(new FieldStub());
        Object fieldInstance = new Object();

        when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                        Object mock3 = invocation.getArgumentAt(0, 0);
                        return new OngoingInjecter(mock3);
                    }
                });

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mock2, result.getMock());
        verifyZeroInteractions(mock1);
    }

    private static class FieldStub {
        public Class<?> getType() {
            return TEST_CLASS;
        }
    }
}