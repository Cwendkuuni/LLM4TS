package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.Collections3.newArrayList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.MockCandidateFilter;
import org.mockito.MockCandidateFilter.OngoingInjecter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;

        Collection<Object> mocks = newArrayList(new String(), new Integer(1), new StringBuffer(""));

        Field field = getField(StringBuffer.class, "value");

        Object fieldInstance = new StringBuffer("test");

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);

        // Test with a String type
        assertEquals(fieldInstance, result.getFieldInstance());
        assertEquals(String.class, result.getCandidate().getClass());

        // Test with a StringBuffer type (passes the test)
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(fieldInstance, result.getFieldInstance());
        assertEquals(StringBuffer.class, result.getCandidate().getClass());

        // Test with an Integer type (nextMock should be called)
        reset(nextMock);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
        assertEquals(fieldInstance, result.getFieldInstance());
        assertEquals(Integer.class, result.getCandidate().getClass());
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error getting field", e);
        }
    }
}