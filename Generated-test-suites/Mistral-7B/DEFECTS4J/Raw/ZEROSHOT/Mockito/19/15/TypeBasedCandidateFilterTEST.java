package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_FieldTypeIsAssignableFromMockedObjectType_ReturnFilteredMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(Number.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.mock(Field.class);

        Object fieldInstance = new Object() {};

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, result.getMock());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());

        Mockito.verify(nextMock).filterCandidate(Mockito.eq(new ArrayList<>()), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_FieldTypeIsNotAssignableFromMockedObjectType_NextFilterShouldBeCalled() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(Number.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.mock(Field.class);

        Object fieldInstance = new Object() {};

        Mockito.when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.eq(field), Mockito.eq(fieldInstance)))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocation) {
                        Object mock3 = Mockito.mock(List.class);
                        return new OngoingInjecter(mock3, field, fieldInstance);
                    }
                });

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);

        Mockito.verify(nextMock).filterCandidate(Mockito.eq(new ArrayList<>()), Mockito.eq(field), Mockito.eq(fieldInstance));
    }
}