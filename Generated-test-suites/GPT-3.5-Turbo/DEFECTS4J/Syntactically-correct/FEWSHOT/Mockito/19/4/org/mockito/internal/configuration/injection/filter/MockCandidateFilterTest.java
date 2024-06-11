package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                assertNotNull(mocks);
                assertNotNull(fieldToBeInjected);
                assertNotNull(fieldInstance);

                assertEquals(ArrayList.class, mocks.getClass());
                assertEquals(Field.class, fieldToBeInjected.getClass());
                assertEquals(Object.class, fieldInstance.getClass());

                OngoingInjecter ongoingInjecter = new OngoingInjecter() {
                    @Override
                    public void thenInject() {
                        assertTrue(true);
                    }
                };

                return ongoingInjecter;
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(ongoingInjecter);
        ongoingInjecter.thenInject();
    }
}