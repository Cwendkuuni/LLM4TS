
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.metadb.iam.UserEntity;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() {

    }

    @Test
    public void testGetCandidates() {
        UserTaskInstanceCandidateEntity mockEntity = Mockito.mock(UserTaskInstanceCandidateEntity.class);
        Mockito.when(mockEntity.getRoleId()).thenReturn(11L);
        Mockito.when(mockEntity.getUserId()).thenReturn(12L);
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(Mockito.anyLong()))
            .thenReturn(Arrays.asList(mockEntity));

        HashSet<User> result = (HashSet<User>) approvalPermissionService.getCandidates(1L, true);
        assertEquals(1, result.size());
    }

    @Test 
    public void testGetCandidateRoles() {
        UserTaskInstanceCandidateEntity mockEntity = Mockito.mock(UserTaskInstanceCandidateEntity.class);
        Mockito.when(mockEntity.getRoleId()).thenReturn(11L);
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(Mockito.anyLong()))
            .thenReturn(Arrays.asList(mockEntity));
        
        assertEquals(1, approvalPermissionService.getCandidateRoles(1L, true).size());
    }

    @Test
    public void testIsApprovable() {
        Mockito.when(userTaskCandidateRepository.findByCandidateUserId(Mockito.anyLong()))
            .thenReturn(Collections.singletonList(Mockito.mock(UserTaskInstanceCandidateEntity.class)));

        assertTrue(approvalPermissionService.isApprovable(1L));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        UserTaskInstanceCandidateEntity mockEntity = Mockito.mock(UserTaskInstanceCandidateEntity.class);
        Mockito.when(mockEntity.getApprovalInstanceId()).thenReturn(11L);
        Mockito.when(mockEntity.getRoleId()).thenReturn(10L);
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(Mockito.any()))
            .thenReturn(Arrays.asList(mockEntity));
        
        assertEquals(1, approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(1L, 2L)).size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        UserTaskInstanceCandidateEntity mockEntity = Mockito.mock(UserTaskInstanceCandidateEntity.class);
        Mockito.when(mockEntity.getResourceRoleIdentifier()).thenReturn("ROLE_1");
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(Mockito.any()))
            .thenReturn(Arrays.asList(mockEntity));

        UserEntity mockUserEntity = Mockito.mock(UserEntity.class);
        Mockito.when(mockUserEntity.getId()).thenReturn(1L);
        Mockito.when(userTaskCandidateRepository.findByResourceIdsAndResourceRoleIdsIn(Mockito.any()))
            .thenReturn(Arrays.asList(mockUserEntity));
        
        assertEquals(1, approvalPermissionService.getCandidatesByFlowInstanceIds(Arrays.asList(1L, 2L)).size());
    }

}
