package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.collections.CollectionUtils;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws NoSuchFieldException {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Class<? extends Object> mockClass1 = MockitoMockito.class;
        Class<? extends Object> mockClass2 = Integer.class;
        Class<? extends Object> mockClass3 = String.class;

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        List<Object> mocks = newArrayList(new Object[] { new MockitoMockito(), 42, "test" });

        List<Object> mockTypeMatches = new ArrayList<Object>();
        for (Object mock : mocks) {
            if (field.getType().isAssignableFrom(mock.getClass())) {
                mockTypeMatches.add(mock);
            }
        }

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);

        if (field.getType().isAssignableFrom(mockClass1.getDeclaredConstructors()[0].getParameterTypes()[0].getClass())) {
            assertEquals(mockClass1, result.getClass());
        } else if (field.getType().isAssignableFrom(mockClass2.getDeclaredConstructors()[0].getParameterTypes()[0].getClass())) {
            assertEquals(mockClass2, result.getClass());
        } else if (field.getType().isAssignableFrom(mockClass3.getDeclaredConstructors()[0].getParameterTypes()[0].getClass())) {
            assertEquals(mockClass3, result.getClass());
        }

        verify(nextMock).filterCandidate(mockTypeMatches, field, fieldInstance);
    }
}