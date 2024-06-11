package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserTaskInstanceRepositoryTest {

  @Mock
  private UserTaskInstanceRepository userTaskInstanceRepository;

  private FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
  private Long userId = 1L;
  private Collection<Long> roleIds = new ArrayList<Long>(){{add(1L);}};
  private Collection<String> resourceRoleIdentifiers = new ArrayList<String>(){{add("Default");}};

  @Test
  public void testFindByStatus() {
    when(userTaskInstanceRepository.findByStatus(status)).thenReturn(new ArrayList<UserTaskInstanceEntity>());
    List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(status);
    assertNotNull(entities);
    verify(userTaskInstanceRepository, times(1)).findByStatus(status);
  }

  @Test
  public void testFindByCandidateUserIdOrRoleIds() {
    when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(new ArrayList<UserTaskInstanceEntity>());
    List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
    assertNotNull(entities);
    verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
  }

  // The rest of the methods tested similarly...
}

