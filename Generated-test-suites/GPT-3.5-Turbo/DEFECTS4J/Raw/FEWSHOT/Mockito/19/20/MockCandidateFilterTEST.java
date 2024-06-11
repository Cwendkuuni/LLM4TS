package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {

    private class MockCandidateFilterImpl implements MockCandidateFilter {

        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return new OngoingInjecter() {
                @Override
                public OngoingInjecter assignableFrom(Class<?>... classes) {
                    return this;
                }

                @Override
                public void thenIgnoreForConstructors() {
                }

                @Override
                public void thenInject(Object... instances) {
                }

                @Override
                public void thenInject(Object instance) {
                }
            };
        }
    }

    @Test
    public void testFilterCandidate() throws NoSuchFieldException, IllegalAccessException {
        MockCandidateFilter filter = new MockCandidateFilterImpl();
        Collection<Object> mocks = null;
        Field fieldToBeInjected = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

}