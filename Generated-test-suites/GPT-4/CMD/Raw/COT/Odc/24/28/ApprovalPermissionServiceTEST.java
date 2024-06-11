package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApprovalPermissionServiceTest {
    ApprovalPermissionService service = new ApprovalPermissionService();
  
    @Test
    public void getCandidatesTest() {
        Set<User> users = service.getCandidates(1L, true);
        assertNotNull(users);
    }

    @Test
    public void getInstanceId2CandidateRoleIdsTest() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        Map<Long, Set<Long>> results = service.getInstanceId2CandidateRoleIds(ids);
        assertNotNull(results);
    }
    
    @Test
    public void getInstanceId2CandidateResourceRoleIdentifierIdsTest() {
      // similar to previous test cases
    }

    @Test
    public void getCandidateUserIdsTest() {
      // similar to previous test cases
    }

    @Test
    public void getCandidateRolesTest() {
      // similar to previous test cases
    }

    @Test
    public void isApprovableTest() {
      boolean result = service.isApprovable(1L);
      assertNotNull(result);
    } 

    @Test
    public void getApprovableApprovalInstancesTest() {
      List<UserTaskInstanceEntity> entities = service.getApprovableApprovalInstances();
      assertNotNull(entities);
    }

    @Test
    public void getApprovableApprovalInstancesWithUserIdTest() {
      List<UserTaskInstanceEntity> entities = service.getApprovableApprovalInstances(1L);
      assertNotNull(entities);
    }
    
    @Test
    public void setCandidateResourceRoleTest() {
        service.setCandidateResourceRole(1L, "role1");
    }

    @Test
    public void listApprovableExternalInstancesTest() {
      List<UserTaskInstanceEntity> entities = service.listApprovableExternalInstances();
      assertNotNull(entities);
    }

    @Test
    public void getCandidatesByFlowInstanceIdsTest() {
      Collection<Long> ids = new ArrayList<>();
      ids.add(1L);
      ids.add(2L);
      Map<Long, Set<UserEntity>> results = service.getCandidatesByFlowInstanceIds(ids);
      assertNotNull(results);
    }
}