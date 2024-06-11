
package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(approvalInstanceId);
        entity.setUserId(1L);
        entity.setRoleId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEnabled(true);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.singletonList(entity));
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(1L), enabled)).thenReturn(Collections.singletonList(userEntity));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(1, result.size());
        assertEquals(userEntity.getId(), result.iterator().next().getId());
    }

    //Similar test methods for the rest of the methods in ApprovalPermissionService
    //@Test
    //public void testGetCandidateRoles() { ... }

    //@Test
    //public void testIsApprovable() { ... }
    
    //@Test
    //public void testGetApprovableApprovalInstances() { ... }

    //@Test
    //public void testSetCandidateResourceRole() { ... }

    //@Test
    //public void testListApprovableExternalInstances() { ... }

    //@Test
    //public void testGetCandidatesByFlowInstanceIds() { ... }

    //Continue to write similar test code
}
