package com.oceanbase.odc.metadb.flow;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("1");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("comment");
        entity.setExpireIntervalSeconds(1);
        entity.setExternalFlowInstanceId("1");
        // we simply assume there will be one line of record that affected (updated)
        when(userTaskInstanceRepository.update(any(UserTaskInstanceEntity.class))).thenReturn(1);

        int updatedLines = userTaskInstanceRepository.update(entity);

        assert(updatedLines == 1);
    }

    // other methods should be tested similarly...
}