package com.oceanbase.odc.service.flow;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        approvalPermissionService = new ApprovalPermissionService();

        // Mock entities
        long approvalInstanceId = 12345L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setUserId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setRoleId(2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(entity1, entity2);

        UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        approvalPermissionService.setUserTaskCandidateRepository(userTaskCandidateRepository);

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(2, candidates.size());
    }

    // Add test methods for other ApprovalPermissionService methods

}