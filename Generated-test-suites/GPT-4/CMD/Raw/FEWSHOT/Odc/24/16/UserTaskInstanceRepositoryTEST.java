
package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import java.util.Arrays;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

  @InjectMocks
  UserTaskInstanceRepository repository;
  
  @Mock
  JdbcTemplate template;

  @Test
  public void testFindByStatus() {
    repository.findByStatus(FlowNodeStatus.SUCCESS);
    verify(template, times(1)).findByStatus(any(FlowNodeStatus.class));
  }
  
  @Test
  public void testFindByCandidateUserIdOrRoleIds() {
    repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
    verify(template, times(1)).findByCandidateUserIdOrRoleIds(any(Long.class), any(Collection.class));
  }

  @Test
  public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
    repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("resourceRole1", "resourceRole2"));
    verify(template, times(1)).findByCandidateUserIdOrResourceRoleIdentifier(any(Long.class), any(Collection.class));
  }

  @Test
  public void testUpdate() {
    UserTaskInstanceEntity entity = mock(UserTaskInstanceEntity.class);
    repository.update(entity);
    verify(template, times(1)).update(any(UserTaskInstanceEntity.class));
  }

  @Test 
  public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
    repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.SUCCESS);
    verify(template, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(any(Collection.class), any(FlowNodeStatus.class));
  }
  
  //Continue writing other test methods following the pattern above.
}
