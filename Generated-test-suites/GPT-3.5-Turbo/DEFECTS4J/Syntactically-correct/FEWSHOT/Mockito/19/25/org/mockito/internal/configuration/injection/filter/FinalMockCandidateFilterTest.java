package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Test mocks size is 1
        {
            List<Object> mocks = new ArrayList<Object>();
            mocks.add("mock1");

            Field field = null;
            Object fieldInstance = null;

            OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
            
            // Test thenInject when matching mock is found
            {
                Object result = ongoingInjecter.thenInject();
                assertEquals("mock1", result);
            }
            
            // Test thenInject when matching mock is not found
            {
                mocks.remove("mock1");
                Object result = ongoingInjecter.thenInject();
                assertNull(result);
            }
        }
        
        // Test mocks size is not 1
        {
            Collection<Object> mocks = new ArrayList<Object>();
            mocks.add("mock1");
            mocks.add("mock2");

            Field field = null;
            Object fieldInstance = null;

            OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
            
            // Test thenInject when mocks size is not 1
            {
                Object result = ongoingInjecter.thenInject();
                assertNull(result);
            }
        }
    }
}