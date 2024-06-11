package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.CollectionUtils.*;
import static org.mockito.junit.MockitoJUnit.mock;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {
    private MockCandidateFilter next;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate_allMocksMatchType_returnNextFilter() {
        // given
        Collection<Object> mocks = newArrayList(mock(ClassA.class), mock(ClassB.class), mock(ClassC.class));
        Field field = new FieldStub();
        Object fieldInstance = new ObjectStub();

        // when
        OngoingInjecter result = new TypeBasedCandidateFilter(next).filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_noMocksMatchType_returnEmptyCollection() {
        // given
        Collection<Object> mocks = newArrayList(mock(ClassD.class), mock(ClassE.class));
        Field field = new FieldStub();
        Object fieldInstance = new ObjectStub();

        // when
        Collection<Object> result = new TypeBasedCandidateFilter(next).filterCandidate(mocks, field, fieldInstance).getMocks();

        // then
        assertEmpty(result);
    }

    @Test
    public void testFilterCandidate_someMocksMatchType_returnMatchedMocksNextFilter() {
        // given
        Collection<Object> mocks = newArrayList(mock(ClassA.class), mock(ClassB.class), mock(ClassC.class), mock(ClassD.class), mock(ClassE.class));
        Field field = new FieldStub();
        Object fieldInstance = new ObjectStub();

        // when
        Collection<Object> result = new TypeBasedCandidateFilter(next).filterCandidate(mocks, field, fieldInstance).getMocks();

        // then
        assertEquals(3, result.size());
        assertTrue(result.contains(mock(ClassA.class)));
        assertTrue(result.contains(mock(ClassB.class)));
        assertTrue(result.contains(mock(ClassC.class)));
    }
}

class FieldStub {
    // empty class
}

class ObjectStub {
    // empty class
}

class ClassA {
    // empty class
}

class ClassB {
    // empty class
}

class ClassC {
    // empty class
}

class ClassD {
    // empty class
}

class ClassE {
    // empty class
}