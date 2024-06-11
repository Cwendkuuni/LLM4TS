package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> taskList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVAL_PENDING);
        assertNotNull(taskList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> taskList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(taskList);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> taskList = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role1"));
        assertNotNull(taskList);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> taskList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("role1"));
        assertNotNull(taskList);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> taskList = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(taskList);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> taskList = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(taskList);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("12345");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("Approved");
        entity.setExpireIntervalSeconds(60);
        entity.setExternalFlowInstanceId("abc123");

        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
    }

    // Additional test cases can be added for the remaining methods in UserTaskInstanceRepository

}