package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private final MockUtil mockUtil = new MockUtil();
    private final MockCandidateFilter nextMock = new MockCandidateFilter() {
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null;
        }
    };
    private NameBasedCandidateFilter candidateFilter;

    public NameBasedCandidateFilterTest() {
        candidateFilter = new NameBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_withMatchingName() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mockUtil.createMock("field1", Object.class);
        Object mock2 = mockUtil.createMock("field2", Object.class);
        Object mock3 = mockUtil.createMock("field3", Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = getField("field2");
        Object fieldInstance = null;

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_withNoMatchingName() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mockUtil.createMock("field1", Object.class);
        Object mock2 = mockUtil.createMock("field2", Object.class);
        Object mock3 = mockUtil.createMock("field3", Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = getField("field4");
        Object fieldInstance = null;

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_withSameNameButDifferentType() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mockUtil.createMock("field1", Object.class);
        Object mock2 = mockUtil.createMock("field2", String.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getField("field2");
        Object fieldInstance = null;

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    private Field getField(String name) {
        try {
            return NameBasedCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}