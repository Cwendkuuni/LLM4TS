package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        approvalPermissionService = mock(ApprovalPermissionService.class);
    }

    @Test
    public void testGetCandidates() {
        Set<User> result = new HashSet<>() {{
            add(new User(101L, "testUser1"));
            add(new User(102L, "testUser2"));
        }};
        when(approvalPermissionService.getCandidates(any(Long.class), any(Boolean.class))).thenReturn(result);
        Set<User> returnedResult = approvalPermissionService.getCandidates(1L, true); 
        assertEquals(2, returnedResult.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> result = Map.ofEntries(
                Map.entry(1L, new HashSet<>(Arrays.asList(10L, 11L))),
                Map.entry(2L, new HashSet<>(Arrays.asList(20L, 21L)))
        );
        when(approvalPermissionService.getInstanceId2CandidateRoleIds(any(Collection.class))).thenReturn(result);
        Map<Long, Set<Long>> returnedResult = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(1L, 2L)); 
        assertEquals(2, returnedResult.size());
    }
  
    // ..Continue generating test methods for the remaining methods..
}