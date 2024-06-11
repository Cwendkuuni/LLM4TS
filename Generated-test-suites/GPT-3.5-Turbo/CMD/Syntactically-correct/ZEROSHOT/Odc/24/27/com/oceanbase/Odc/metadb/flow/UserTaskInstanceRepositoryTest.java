import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setUp() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("12345");
        userTaskInstanceEntity.setStatus("COMPLETED");
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId("operator123");
        userTaskInstanceEntity.setComment("Test comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(60);
        userTaskInstanceEntity.setExternalFlowInstanceId("ext-12345");
    }

    @Test
    public void testFindById() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("12345", result.get().getUserTaskId());
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETED)).thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.COMPLETED);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("operator123", result.get(0).getOperatorId());
    }

    @Test
    public void testUpdate() {
        Mockito.when(userTaskInstanceRepository.update(userTaskInstanceEntity)).thenReturn(1);
        int result = userTaskInstanceService.update(userTaskInstanceEntity);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(userTaskInstanceRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int result = userTaskInstanceService.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activity123", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activity123", 1L);
        assertTrue(result.isPresent());
        assertEquals("Test comment", result.get().getComment());
    }

    @Test
    public void testFindConfirmById() {
        Mockito.when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(true);
        Boolean result = userTaskInstanceService.findConfirmById(1L);
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.COMPLETED))
                .thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.COMPLETED);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ext-12345", result.get(0).getExternalFlowInstanceId());
    }
}