package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter nextMock = Mockito.mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<Object>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Mockito.mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = Mockito.mock(OngoingInjecter.class);

        Mockito.when(field.getType().isAssignableFrom(mock1.getClass())).thenReturn(true);
        Mockito.when(field.getType().isAssignableFrom(mock2.getClass())).thenReturn(false);

        Mockito.when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(nextMock).filterCandidate(Mockito.eq(Collections.singletonList(mock1)), Mockito.eq(field), Mockito.eq(fieldInstance));
        Mockito.verify(nextMock, Mockito.never()).filterCandidate(Mockito.eq(Collections.singletonList(mock2)), Mockito.eq(field), Mockito.eq(fieldInstance));

        assertEquals(ongoingInjecter, result);
    }
}