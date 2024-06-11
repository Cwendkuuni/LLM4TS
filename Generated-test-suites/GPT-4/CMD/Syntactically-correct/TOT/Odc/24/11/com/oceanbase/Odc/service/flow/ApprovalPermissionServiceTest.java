package com.oceanbase.odc.service.flow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        boolean enabled = true;
        
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setUserId(1L);
        entity1.setRoleId(1L);
        entity1.setApprovalInstanceId(approvalInstanceId);
        
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setUserId(2L);
        entity2.setRoleId(2L);
        entity2.setApprovalInstanceId(approvalInstanceId);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Arrays.asList(entity1, entity2));
        
        // Since the UserRepository, RoleRepository are not implemented here, the returned value will be empty.
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(0, result.size());
    }
}