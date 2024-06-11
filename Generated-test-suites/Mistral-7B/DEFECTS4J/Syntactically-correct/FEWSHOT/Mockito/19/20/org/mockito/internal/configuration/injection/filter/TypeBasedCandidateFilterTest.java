package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.CollectionUtils;
import org.mockito.internal.util.reflect.FieldAccessibleObject;
import org.mockito.internal.util.reflect.ReflectUtils;
import org.mockito.internal.util.reflection.UsesReflection;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    @UsesReflection
    public void testFilterCandidate_EmptyMocks_ReturnsNull() {
        Collection<Object> mocks = CollectionUtils.emptyList();
        Field field = ReflectUtils.field(Object.class, "fieldName");
        Object fieldInstance = ReflectUtils.newInstance(Object.class);

        assertNotNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    @UsesReflection
    public void testFilterCandidate_NoTypeMatch_ReturnsNullFromNext() {
        Collection<Object> mocks = newArrayList(new Object[] { Mockito.mock(String.class), Mockito.mock(List.class) });
        Field field = ReflectUtils.field(Object.class, "fieldName");
        Object fieldInstance = ReflectUtils.newInstance(Object.class);

        Method nextFilterCandidateMethod = ReflectUtils.method(nextMock.getClass(), "filterCandidate", Collection.class, Field.class, Object.class);

        try {
            FieldAccessibleObject nextFilterCandidate = new FieldAccessibleObject(nextFilterCandidateMethod, filter, "next");
            nextFilterCandidate.set(nextFilterCandidate, Mockito.mock(OngoingInjecter.class));
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    @UsesReflection
    public void testFilterCandidate_TypeMatch_ReturnsOngoingInjecter() {
        Collection<Object> mocks = newArrayList(new Object[] { Mockito.mock(Object.class), Mockito.mock(Object.class), Mockito.mock(Object.class) });
        Field field = ReflectUtils.field(Object.class, "fieldName");
        Object fieldInstance = ReflectUtils.newInstance(Object.class);

        Object matchingMock = Mockito.mock(field.getType());
        mocks.add(matchingMock);

        OngoingInjecter ongoingInjecter = Mockito.mock(OngoingInjecter.class);

        try {
            Method nextFilterCandidateMethod = ReflectUtils.method(nextMock.getClass(), "filterCandidate", Collection.class, Field.class, Object.class);
            FieldAccessibleObject nextFilterCandidate = new FieldAccessibleObject(nextFilterCandidateMethod, filter, "next");
            nextFilterCandidate.set(nextFilterCandidate, ongoingInjecter);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(ongoingInjecter, result);
    }
}