package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field();
        Object fieldInstance = new Object();

        // Test case 1: mocks size is 1
        mocks.add(mock(Object.class));
        OngoingInjecter ongoingInjecter1 = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter1);

        // Test case 2: mocks size is not 1
        mocks.clear();
        OngoingInjecter ongoingInjecter2 = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNull(ongoingInjecter2);
    }

    @Test
    public void testThenInject() {
        Field field = new Field();
        Object fieldInstance = new Object();
        Object matchingMock = mock(Object.class);

        // Test case 1: BeanPropertySetter.set() returns true
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(fieldInstance, field)).thenReturn(true);
        OngoingInjecter ongoingInjecter1 = new FinalMockCandidateFilter().new OngoingInjecter(matchingMock, beanPropertySetter);
        Object result1 = ongoingInjecter1.thenInject();
        assertEquals(matchingMock, result1);
        verify(beanPropertySetter).set(fieldInstance, field);

        // Test case 2: BeanPropertySetter.set() returns false
        when(beanPropertySetter.set(fieldInstance, field)).thenReturn(false);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        OngoingInjecter ongoingInjecter2 = new FinalMockCandidateFilter().new OngoingInjecter(matchingMock, beanPropertySetter, fieldSetter);
        Object result2 = ongoingInjecter2.thenInject();
        assertEquals(matchingMock, result2);
        verify(beanPropertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);

        // Test case 3: RuntimeException is thrown
        doThrow(new RuntimeException()).when(beanPropertySetter).set(fieldInstance, field);
        Reporter reporter = mock(Reporter.class);
        OngoingInjecter ongoingInjecter3 = new FinalMockCandidateFilter().new OngoingInjecter(matchingMock, beanPropertySetter, fieldSetter, reporter);
        Object result3 = ongoingInjecter3.thenInject();
        assertEquals(matchingMock, result3);
        verify(beanPropertySetter).set(fieldInstance, field);
        verify(reporter).cannotInjectDependency(field, matchingMock, e);
    }
}