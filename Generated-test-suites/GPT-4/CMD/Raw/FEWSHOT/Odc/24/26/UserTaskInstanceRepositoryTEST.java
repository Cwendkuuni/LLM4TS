package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {
    
    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Mock
    TestEntityManager entityManager;

    @Test
    public void testFindConfirmById() {
        Long id = 123L;
        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        assertTrue(confirm);
    }

    @Test
    public void testFindByIdInstanceTypeAndActivityId() {

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        Long id = 123L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        String activityId = "testActivityId";
        Long flowInstanceId = 3425L;
        
        userTaskInstanceEntity.setId(id);
        userTaskInstanceEntity.setFlowNodeType(instanceType);
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        entityManager.persist(userTaskInstanceEntity);
        entityManager.flush();

        Optional<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
            instanceType, activityId, flowInstanceId
        );

        assertTrue(found.isPresent());
        assertEquals(found.get().getId(), id);
    }
    
    // Similarly, write tests for other repository methods
}