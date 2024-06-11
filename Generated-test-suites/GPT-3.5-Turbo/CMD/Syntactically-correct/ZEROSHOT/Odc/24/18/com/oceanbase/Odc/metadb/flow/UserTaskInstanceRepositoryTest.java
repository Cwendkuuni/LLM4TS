import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    private UserTaskInstanceEntity userTaskInstance1;
    private UserTaskInstanceEntity userTaskInstance2;
    private UserTaskInstanceEntity userTaskInstance3;

    @Before
    public void setUp() {
        userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setId(1L);
        userTaskInstance1.setFlowInstanceId(101L);
        userTaskInstance1.setStatus(FlowNodeStatus.PENDING);

        userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setId(2L);
        userTaskInstance2.setFlowInstanceId(102L);
        userTaskInstance2.setStatus(FlowNodeStatus.APPROVED);

        userTaskInstance3 = new UserTaskInstanceEntity();
        userTaskInstance3.setId(3L);
        userTaskInstance3.setFlowInstanceId(103L);
        userTaskInstance3.setStatus(FlowNodeStatus.REJECTED);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstance1, userTaskInstance2);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceService.findByStatus(FlowNodeStatus.PENDING);

        assertNotNull(actualList);
        assertEquals(2, actualList.size());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstance1, userTaskInstance3);
        Long userId = 123L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceService.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertNotNull(actualList);
        assertEquals(2, actualList.size());
        assertEquals(expectedList, actualList);
    }

    // Add more test methods for other repository methods

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity updatedUserTaskInstance = new UserTaskInstanceEntity();
        updatedUserTaskInstance.setId(1L);
        updatedUserTaskInstance.setUserTaskId("UT-001");

        when(userTaskInstanceRepository.update(updatedUserTaskInstance)).thenReturn(1);

        int result = userTaskInstanceService.update(updatedUserTaskInstance);

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).update(updatedUserTaskInstance);
    }

}