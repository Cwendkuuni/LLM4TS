package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.Collections3.newArrayList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injectingbyname.OngoingStubbing;
import org.mockito.internal.configuration.injectingbyname.OngoingStubbingBase;
import org.mockito.internal.configuration.injectingbytype.TypeMatches;
import org.mockito.internal.util.collections.Collections3;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_emptyCollection() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Collections3.emptyList();
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new Object();

        OngoingStubbing result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_noTypeMatch() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = newArrayList(new IntTypeMock(), new StringTypeMock());
        Field field = new Field("testField", Integer.class, null, null);
        Object fieldInstance = new Object();

        OngoingStubbing result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_singleTypeMatch() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = newArrayList(new StringTypeMock(), new StringTypeMock());
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new Object();

        OngoingStubbing result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertNotNull(result.mock);
        assertNotNull(result.field);
        assertEquals(String.class, result.field.getType());
    }

    @Test
    public void testFilterCandidate_nextFilter() {
        TypeBasedCandidateFilter filter1 = new TypeBasedCandidateFilter(null);
        List<Object> mocks = newArrayList(new StringTypeMock(), new IntTypeMock());
        Field field = new Field("testField", Integer.class, null, null);
        Object fieldInstance = new Object();

        TypeBasedCandidateFilter filter2 = mock(TypeBasedCandidateFilter.class);
        when(filter2.filterCandidate(Collections3.list(new StringTypeMock()), field, fieldInstance))
                .thenReturn(new OngoingStubbingBase(new StringTypeMock(), field));

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(filter2);

        OngoingStubbing result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertNotNull(result.mock);
        assertNotNull(result.field);
        assertEquals(String.class, result.field.getType());

        verify(filter2).filterCandidate(Collections3.list(new StringTypeMock()), field, fieldInstance);
    }
}

class IntTypeMock implements OngoingStubbingBase<Integer> {
}

class StringTypeMock implements OngoingStubbing<String> {
}

class Field {
    String name;
    Class<?> type;
    Object annnotations;

    Field(String name, Class<?> type, Object annnotations, Object declaredAnnotations) {
        this.name = name;
        this.type = type;
        this.annotations = annnotations;
    }
}