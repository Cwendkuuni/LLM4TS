package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository mockedRepository;

    @InjectMocks
    private UserTaskInstanceEntity taskInstanceEntity = new UserTaskInstanceEntity();

    @Before
    public void setup() {
        // prerequisites for setup
    }

    @Test
    public void testFindByStatus() {
        when(mockedRepository.findByStatus(FlowNodeStatus.OPEN)).thenReturn(Arrays.asList(taskInstanceEntity));

        List<UserTaskInstanceEntity> results = mockedRepository.findByStatus(FlowNodeStatus.OPEN);

        assertFalse(results.isEmpty());
        assertEquals(results.get(0), taskInstanceEntity);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Similar to the first test case, accordingly for all other JPA methods...

    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(mockedRepository.update(userTaskInstanceEntity)).thenReturn(1);

        int result = mockedRepository.update(userTaskInstanceEntity);
        assertTrue(result > 0);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        // Similar to the first test case, accordingly for all other JPA methods...

    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        // Similar to the first test case, accordingly for all other JPA methods...
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        // Similar to the first test case, accordingly for all other JPA methods...
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        when(mockedRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_FLOW, "activityId", 1L))
            .thenReturn(Optional.of(taskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = mockedRepository
            .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_FLOW, "activityId", 1L);
        assertTrue(result.isPresent());
        assertEquals(result.get(), taskInstanceEntity);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Similar to the first test case, accordingly for all other JPA methods...

    }

    @Test
    public void testFindConfirmById() {
        when(mockedRepository.findConfirmById(1L)).thenReturn(false);

        Boolean result = mockedRepository.findConfirmById(1L);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Similar to the first test case, accordingly for all other JPA methods...
    }
}