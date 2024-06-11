package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    private List<UserTaskInstanceEntity> userTaskInstanceEntities;

    @Before
    public void setUp(){
        userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntities.add(userTaskInstanceEntity);
    }

    @Test
    public void whenFindByStatus_thenUserTaskInstanceEntityShouldBeReturned() {
        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.any(FlowNodeStatus.class))).thenReturn(userTaskInstanceEntities);
        assertTrue(userTaskInstanceService.findByStatus(FlowNodeStatus.NEW).equals(userTaskInstanceEntities));
    }

    @Test
    public void whenFindByCandidateUserIdOrRoleIds_thenUserTaskInstanceEntityShouldBeReturned() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.anyList())).thenReturn(userTaskInstanceEntities);
        assertTrue(userTaskInstanceService.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>()).equals(userTaskInstanceEntities));
    }

    // Apply similar methods for other methods present in UserTaskInstanceRepository
}
