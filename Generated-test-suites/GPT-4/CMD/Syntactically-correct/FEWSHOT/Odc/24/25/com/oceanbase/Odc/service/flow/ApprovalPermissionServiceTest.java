
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserService userService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testGetCandidates() {
        //Initialize test data
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        //Mock dependencies
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByRoleIdsAndEnabled(Mockito.any(), Mockito.any()))
            .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUserIdsAndEnabled(Mockito.any(), Mockito.any()))
            .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUserIdsOrRoleIds(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(new ArrayList<>());

        //Call test method
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        //Asserts
        assertEquals(0, result.size());
    }

    @Test
    public void testGetCandidateRoles() {
        //Initialize test data
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
    
        //Mock dependencies
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(new ArrayList<>());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(Mockito.any(), Mockito.any()))
            .thenReturn(new ArrayList<>());
    
        //Call test method
        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
    
        //Asserts
        assertEquals(0, result.size());
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        //Initialize test data
        long userId = 123L;

        //Mock dependencies
        Mockito.when(userService.getUserRoleIds(userId)).thenReturn(null);
        Mockito.when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new HashSet<>(Arrays.asList("Test Role")));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());

        //Call test method
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();

        //Asserts
        assertEquals(0, result.size());
    }

    @Test
    public void testGetApprovableApprovalInstances_byUserId() {
        //Initialize test data
        long userId = 123L;

        //Mock dependencies
        Mockito.when(userService.getUserRoleIds(userId)).thenReturn(null);
        Mockito.when(userService.getUserRoleIds(userId)).thenReturn(new HashSet<>(Arrays.asList(1L, 2L, 3L)));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());

        //Call test method
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);

        //Asserts
        assertEquals(0, result.size());
    }

    @Test
    public void testIsApprovable() {
        //Initialize test data
        Long approvalInstanceId = 1L;

        //Mock dependencies
        Mockito.when(userTaskInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //Call the test method
        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

        //Asserts
        assertFalse(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        //Initialize test data
        Long approvalInstanceId = 1L;
        Set<Long> flowInstanceIds = new HashSet<>(Arrays.asList(approvalInstanceId));

        //Mock dependencies
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(Mockito.any()))
            .thenReturn(new ArrayList<>());
        Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Mockito.any()))
            .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUserIds(Mockito.any()))
            .thenReturn(new ArrayList<>());

        //Call the test method
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

        //Asserts
        assertEquals(0, result.size());
    }
}
