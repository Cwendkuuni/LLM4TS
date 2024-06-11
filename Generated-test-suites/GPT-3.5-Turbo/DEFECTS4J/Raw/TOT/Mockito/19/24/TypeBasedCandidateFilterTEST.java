package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter typeBasedCandidateFilter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_WithMatchingTypes_InvokesNextFilter() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = mock(MyClass.class);

        when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_WithNoMatchingTypes_DoesNotInvokeNextFilter() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = mock(String.class);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verifyZeroInteractions(next);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_WithNullMocks_ThrowsNullPointerException() throws Exception {
        Collection<Object> mocks = null;
        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = mock(MyClass.class);

        try {
            typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testFilterCandidate_WithNullField_ThrowsNullPointerException() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = mock(MyClass.class);

        try {
            typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testFilterCandidate_WithNullFieldInstance_ThrowsNullPointerException() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = null;

        try {
            typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

}

class MyClass {
    private String field;
}