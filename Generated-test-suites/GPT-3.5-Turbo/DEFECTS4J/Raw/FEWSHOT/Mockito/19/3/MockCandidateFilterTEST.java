package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                assertNotNull(mocks);
                assertNotNull(fieldToBeInjected);
                assertNotNull(fieldInstance);
                return new OngoingInjecter() {
                    public OngoingInjecter annotatedWith(Class<?> annotationType) {
                        assertNotNull(annotationType);
                        return this;
                    }
                    public void inject() {
                        // Do nothing
                    }
                };
            }
        };

        Field field = getFieldToBeInjected();
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(Collections.emptyList(), field, fieldInstance);
        assertNotNull(ongoingInjecter);

        ongoingInjecter.annotatedWith(Test.class);
        ongoingInjecter.inject();
    }

    private Field getFieldToBeInjected() {
        // TODO: Create a Field object to be injected
        return null;
    }
}