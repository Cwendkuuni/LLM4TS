
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Autowired
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        assertNotNull(userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        assertNotNull(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        assertNotNull(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2")));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        assertNotNull(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2")));
    }

    @Test
    public void testFindByRoleId() {
        assertNotNull(userTaskInstanceRepository.findByRoleId(1L));
    }

    @Test
    public void testFindByCandidateUserId() {
        assertNotNull(userTaskInstanceRepository.findByCandidateUserId(1L));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("task1");
        entity.setStatus(FlowNodeStatus.ACTIVE);
        entity.setApproved(true);
        entity.setOperatorId("user1");
        entity.setComment("Test comment");
        entity.setExpireIntervalSeconds(60);
        entity.setExternalFlowInstanceId("externalId");

        assertEquals(1, userTaskInstanceRepository.update(entity));
    }

    @Test
    public void testUpdateUserTaskIdById() {
        assertEquals(1, userTaskInstanceRepository.updateUserTaskIdById(1L, "task1"));
    }

    @Test
    public void testUpdateStatusById() {
        assertEquals(1, userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.ACTIVE));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        assertEquals(1, userTaskInstanceRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        assertNotNull(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TYPE1, "activity1", 1L));
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        assertNotNull(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TYPE1, "name1", 1L));
    }

    @Test
    public void testFindConfirmById() {
        assertNotNull(userTaskInstanceRepository.findConfirmById(1L));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        assertNotNull(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.ACTIVE));
    }

}
