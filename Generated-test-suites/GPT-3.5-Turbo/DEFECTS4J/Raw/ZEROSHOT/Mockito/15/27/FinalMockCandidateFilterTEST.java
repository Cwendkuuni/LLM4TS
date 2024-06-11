package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Test case where mocks has only one element
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertTrue(injecter.thenInject());
        
        // Test case where mocks has more than one element
        mocks.add(new Object());

        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertFalse(injecter.thenInject());
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + fieldName);
        }
    }
}