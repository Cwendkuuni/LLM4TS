
package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Collection;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // create mocks
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Object matchingMock = Mockito.mock(Object.class);

        // set up expectations
        Mockito.when(mocks.size()).thenReturn(1);
        Mockito.when(mocks.iterator().next()).thenReturn(matchingMock);
        Mockito.when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenReturn(true);

        // test the method
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // assert the result
        assertNotNull(result);
        assertEquals(matchingMock, result);
        Mockito.verify(new BeanPropertySetter(fieldInstance, field)).set(matchingMock);
        Mockito.verifyZeroInteractions(new FieldSetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new Reporter());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // create mocks
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        // set up expectations
        Mockito.when(mocks.size()).thenReturn(2);

        // test the method
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // assert the result
        assertNull(result);
        Mockito.verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new FieldSetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new Reporter());
    }
}
