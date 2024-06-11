package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        List<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();

        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(null);

        Field field = null;

        // Test when mocks is empty
        Collection<Object> result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test when mocks contain an object of the same type as field
        Object mock = new Object();
        mocks.add(mock);
        result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(mock));

        // Test when mocks contain an object that is a subclass of the field type
        class SubClass extends Object {}
        SubClass subMock = new SubClass();
        mocks.add(subMock);
        result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(mock));
        assertTrue(result.contains(subMock));

        // Test when mocks contain an object that implements an interface implemented by the field type
        class InterfaceImpl implements Runnable {}
        InterfaceImpl interfaceMock = new InterfaceImpl();
        mocks.add(interfaceMock);
        result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(mock));
        assertTrue(result.contains(subMock));
        assertTrue(result.contains(interfaceMock));
    }

}