package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(TypeBasedCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_CollectionIsEmpty() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.spy(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = Mockito.spy(Field.class);
        field.setType(Integer.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_OneMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        Field field = Mockito.spy(Field.class);
        field.setType(Integer.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidate_MultipleMatchingMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(Long.class));
        Field field = Mockito.spy(Field.class);
        field.setType(Number.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0) instanceof Integer ? mocks.get(0) : mocks.get(1), result);
    }

    @Test
    public void testFilterCandidate_FieldAccessible() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(Long.class));
        Field field = Mockito.spy(Field.class);
        field.setAccessible(true);
        field.setType(Number.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0) instanceof Integer ? mocks.get(0) : mocks.get(1), result);
    }

    @Test
    public void testFilterCandidate_FieldFinal() throws NoSuchFieldException {
        Class<Object> clazz = Object.class;
        Field field = clazz.getField("field");
        Modifier finalModifier = field.getModifiers();
        field.setAccessible(true);
        field.setModifiers(finalModifier | Modifier.FINAL);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(Long.class));
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0) instanceof Integer ? mocks.get(0) : mocks.get(1), result);
    }

    @Test
    public void testFilterCandidate_NextFilter() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        Field field = Mockito.spy(Field.class);
        field.setType(Number.class);
        Object fieldInstance = mock(Object.class);
        Object nextResult = mock(OngoingInjecter.class);

        when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(nextResult);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextResult, result);
    }
}