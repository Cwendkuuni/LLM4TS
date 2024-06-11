package org.mockito.internal.configuration.injection.filter;

import org.mockito.internal.util.MockUtil;
import org.mockito.Mockito;
import org.mockito.mock.MockName;
import org.mockito.mock.SimpleMockName;
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        // Create mocks
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Object mock3 = Mockito.mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2, mock3);

        // Create NameBasedCandidateFilter
        MockCandidateFilter next = Mockito.mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        // Create field and fieldInstance
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        field.setAccessible(true);
        Object fieldInstance = field.get(filter);

        // Set up expectations
        MockUtil mockUtil = new MockUtil();
        SimpleMockName mockName1 = new SimpleMockName("mock1");
        SimpleMockName mockName2 = new SimpleMockName("mock2");
        SimpleMockName mockName3 = new SimpleMockName("mock3");

        Mockito.when(mockUtil.getMockName(mock1)).thenReturn(mockName1);
        Mockito.when(mockUtil.getMockName(mock2)).thenReturn(mockName2);
        Mockito.when(mockUtil.getMockName(mock3)).thenReturn(mockName3);

        // Test when mocks size > 1 and field name matches mock name
        Assert.assertEquals(mocks, filter.filterCandidate(mocks, field, fieldInstance));

        // Test when mocks size > 1 and field name does not match mock name
        Collection<Object> expectedMocks = Arrays.asList(mock1);
        Mockito.when(next.filterCandidate(Mockito.eq(expectedMocks), Mockito.eq(field), Mockito.eq(fieldInstance))).thenReturn(expectedMocks);
        Assert.assertEquals(expectedMocks, filter.filterCandidate(mocks, field, fieldInstance));

        // Test when mocks size = 1
        expectedMocks = Arrays.asList(mock1, mock2, mock3);
        Mockito.when(next.filterCandidate(Mockito.eq(expectedMocks), Mockito.eq(field), Mockito.eq(fieldInstance))).thenReturn(expectedMocks);
        Assert.assertEquals(expectedMocks, filter.filterCandidate(mocks, field, fieldInstance));
    }
}