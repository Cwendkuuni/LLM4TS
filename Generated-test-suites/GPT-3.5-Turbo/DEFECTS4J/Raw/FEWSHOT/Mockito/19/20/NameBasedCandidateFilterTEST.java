package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    @Test
    public void testConstructor() {
        MockCandidateFilter next = new MockCandidateFilterImpl();
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        assertEquals(next, filter.next);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndMatchingMockNames() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilterImpl();
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<Object>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new Object();
        
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        Field nextField = result.getClass().getDeclaredField("next");
        nextField.setAccessible(true);
        assertEquals(next, nextField.get(result));
        Field mocksField = result.getClass().getDeclaredField("mocks");
        mocksField.setAccessible(true);
        List<Object> filteredMocks = (List<Object>)mocksField.get(result);
        assertEquals(2, filteredMocks.size());
        assertTrue(filteredMocks.contains(mock1));
        assertTrue(filteredMocks.contains(mock2));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndNoMatchingMockNames() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilterImpl();
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<Object>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new Object();
        
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        Field nextField = result.getClass().getDeclaredField("next");
        nextField.setAccessible(true);
        assertEquals(next, nextField.get(result));
        Field mocksField = result.getClass().getDeclaredField("mocks");
        mocksField.setAccessible(true);
        List<Object> filteredMocks = (List<Object>)mocksField.get(result);
        assertEquals(0, filteredMocks.size());
    }

    @Test
    public void testFilterCandidateWithSingleMockAndMatchingMockName() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilterImpl();
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<Object>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        field.setAccessible(true);
        MockUtil mockUtil = (MockUtil)field.get(filter);
        mockUtil.getMockName(mock1).thenReturn("mock1");
        Object fieldInstance = new Object();
        
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        Field nextField = result.getClass().getDeclaredField("next");
        nextField.setAccessible(true);
        assertEquals(next, nextField.get(result));
        Field mocksField = result.getClass().getDeclaredField("mocks");
        mocksField.setAccessible(true);
        List<Object> filteredMocks = (List<Object>)mocksField.get(result);
        assertEquals(1, filteredMocks.size());
        assertTrue(filteredMocks.contains(mock1));
    }

    @Test
    public void testFilterCandidateWithNoMatchingMockNames() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilterImpl();
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<Object>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        field.setAccessible(true);
        MockUtil mockUtil = (MockUtil)field.get(filter);
        mockUtil.getMockName(mock1).thenReturn("mock2");
        Object fieldInstance = new Object();
        
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        Field nextField = result.getClass().getDeclaredField("next");
        nextField.setAccessible(true);
        assertEquals(next, nextField.get(result));
        Field mocksField = result.getClass().getDeclaredField("mocks");
        mocksField.setAccessible(true);
        List<Object> filteredMocks = (List<Object>)mocksField.get(result);
        assertEquals(0, filteredMocks.size());
    }

    @Test
    public void testFilterCandidateWithNullField() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilterImpl();
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<Object>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Field field = null;
        Object fieldInstance = new Object();
        
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        Field nextField = result.getClass().getDeclaredField("next");
        nextField.setAccessible(true);
        assertEquals(next, nextField.get(result));
        Field mocksField = result.getClass().getDeclaredField("mocks");
        mocksField.setAccessible(true);
        List<Object> filteredMocks = (List<Object>)mocksField.get(result);
        assertEquals(1, filteredMocks.size());
        assertTrue(filteredMocks.contains(mock1));
    }

    private class MockCandidateFilterImpl implements MockCandidateFilter {
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null;
        }
    }
}