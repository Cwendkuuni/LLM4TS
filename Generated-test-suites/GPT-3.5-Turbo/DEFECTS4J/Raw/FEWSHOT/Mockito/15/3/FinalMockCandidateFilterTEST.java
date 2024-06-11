package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import java.util.Collection;
import java.lang.reflect.Field;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");

        Field field = new Field("field", String.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());

        //Verify that the mock is injected into the field
        assertEquals("mock1", fieldInstance.field);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        Field field = new Field("field", String.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());

        //Verify that the field is not modified
        assertNull(fieldInstance.field);
    }

    @Test
    public void testFilterCandidate_exception() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");

        Field field = new Field("field", Mock.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        //Verify that an exception is thrown
        try {
            injecter.thenInject();
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in field", e.getMessage());
        }
    }
}