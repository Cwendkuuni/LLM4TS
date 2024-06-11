package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    
    private MockCandidateFilter next;
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;
    
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        next = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testFilterCandidateOnlyOneMock() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        
        Field field = new Field();
        Object fieldInstance = new Object();
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testFilterCandidateNoMatchingName() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        
        Field field = new Field();
        field.setName("field1");
        Object fieldInstance = new Object();
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testFilterCandidateMatchingName() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Object mock2 = new Object();
        mocks.add(mock2);
        
        Field field = new Field();
        field.setName(mockUtil.getMockName(mock1).toString());
        Object fieldInstance = new Object();
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotEquals(null, result);
    }
    
    @Test
    public void testFilterCandidateMultipleMatchingNames() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Object mock2 = new Object();
        mocks.add(mock2);
        
        Field field = new Field();
        field.setName(mockUtil.getMockName(mock1).toString());
        Object fieldInstance = new Object();
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotEquals(null, result);
    }
    
    @Test
    public void testFilterCandidateNoMockName() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Object mock2 = new Object();
        mocks.add(mock2);
        
        Field field = new Field();
        field.setName(null);
        Object fieldInstance = new Object();
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testFilterCandidateNullField() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Object mock2 = new Object();
        mocks.add(mock2);
        
        Field field = null;
        Object fieldInstance = new Object();
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testFilterCandidateNullFieldInstance() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Object mock2 = new Object();
        mocks.add(mock2);
        
        Field field = new Field();
        field.setName("field1");
        Object fieldInstance = null;
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testFilterCandidateMatchingNameInNextFilter() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Object mock2 = new Object();
        mocks.add(mock2);
        
        Field field = new Field();
        field.setName("field1");
        Object fieldInstance = new Object();
        
        OngoingInjecter result = new OngoingInjecter();
        
        MockCandidateFilter nextFilter = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return result;
            }
        };
        
        filter = new NameBasedCandidateFilter(nextFilter);
        
        OngoingInjecter output = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(result, output);
    }
}