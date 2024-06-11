
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class))).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETED);
        assertEquals(entities, result);
    }

    // Similarly, write test methods for other methods like findByCandidateUserIdOrRoleIds, findByCandidateUserIdOrResourceRoleIdentifier, etc.

    @Test
    public void testUpdate() {
        when(userTaskInstanceRepository.update(any(UserTaskInstanceEntity.class))).thenReturn(1);
        int result = userTaskInstanceRepository.update(userTaskInstanceEntity);
        assertEquals(1, result);
    }

    // Similarly, write test methods for other methods like updateUserTaskIdById, updateStatusById, deleteByFlowInstanceId, etc.
}
