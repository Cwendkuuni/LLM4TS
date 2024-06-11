package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_withOneMock() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        // Verify that the matchingMock is set
        Object matchingMock = ongoingInjecter.thenInject();
        assertNotNull(matchingMock);

        // Verify that the BeanPropertySetter is called
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(matchingMock)).thenReturn(true);

        fieldInstance = mock(Object.class);

        ongoingInjecter.thenInject();
        verify(beanPropertySetter).set(matchingMock);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        Collection<Object> mocks = mock(Collection.class, withSize(2));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        // Verify that no matchingMock is set
        Object matchingMock = ongoingInjecter.thenInject();
        assertNull(matchingMock);
    }
}