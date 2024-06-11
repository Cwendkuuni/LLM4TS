package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApprovalPermissionServiceTest {

  @Autowired
  private ApprovalPermissionService approvalPermissionService;

  // Assuming there is actual data in the database
  @Test
  public void testGetCandidates() {
    Set<User> result = approvalPermissionService.getCandidates(1L, true);
    assertNotNull(result);
    assertFalse(result.isEmpty());
    for(User user: result) {
       assertNotNull(user);
    }
  }

  @Test
  public void testGetInstanceId2CandidateRoleIds() {   
    Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));
    assertTrue(result.containsKey(1L));
    assertTrue(result.containsKey(2L));
    assertNotNull(result.get(1L));
    assertNotNull(result.get(2L));
  }

  @Test
  public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {   
    Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(new HashSet<>(Arrays.asList(1L, 2L)));
    assertTrue(result.containsKey(1L));
    assertTrue(result.containsKey(2L));
    assertNotNull(result.get(1L));
    assertNotNull(result.get(2L));
  }

  @Test
  public void testGetCandidateUserIds() {   
    Set<Long> result = approvalPermissionService.getCandidateUserIds(new HashSet<>(Arrays.asList(1L, 2L)));
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  public void testGetCandidateRoles() {
    List<Role> result = approvalPermissionService.getCandidateRoles(1L, true);
    assertNotNull(result);
    assertFalse(result.isEmpty());
    for(Role role : result) {
       assertNotNull(role);
    }
  }
  
  @Test
  public void testIsApprovable() {
    boolean result = approvalPermissionService.isApprovable(1L);
    assertTrue(result);
  }

  @Test
  public void testGetApprovableApprovalInstances() {
    List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
    assertNotNull(result);
    assertFalse(result.isEmpty());
    for(UserTaskInstanceEntity entity: result) {
       assertNotNull(entity);
       assertEquals(entity.getStatus(), FlowNodeStatus.EXECUTING);
    }
  }

  @Test
  public void testGetCandidateResourceRole() {
    approvalPermissionService.setCandidateResourceRole(1L, "ROLE");
    List<UserTaskInstanceCandidateEntity> entities = userTaskCandidateRepository.findByApprovalInstanceId(1L);
    assertEquals(1, entities.size());
    assertEquals(1L, entities.get(0).getApprovalInstanceId().longValue());
    assertEquals("ROLE", entities.get(0).getResourceRoleIdentifier());
  }
  
}